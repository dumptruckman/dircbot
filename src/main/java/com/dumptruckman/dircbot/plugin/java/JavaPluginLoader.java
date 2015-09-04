package com.dumptruckman.dircbot.plugin.java;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.Event;
import com.dumptruckman.dircbot.event.EventException;
import com.dumptruckman.dircbot.event.EventHandler;
import com.dumptruckman.dircbot.event.Listener;
import com.dumptruckman.dircbot.event.bot.PluginDisableEvent;
import com.dumptruckman.dircbot.event.bot.PluginEnableEvent;
import com.dumptruckman.dircbot.plugin.EventExecutor;
import com.dumptruckman.dircbot.plugin.InvalidDescriptionException;
import com.dumptruckman.dircbot.plugin.InvalidPluginException;
import com.dumptruckman.dircbot.plugin.Plugin;
import com.dumptruckman.dircbot.plugin.PluginDescriptionFile;
import com.dumptruckman.dircbot.plugin.PluginLoader;
import com.dumptruckman.dircbot.plugin.RegisteredListener;
import com.dumptruckman.dircbot.plugin.UnknownDependencyException;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * Represents a Java plugin loader, allowing plugins in the form of .jar
 */
public final class JavaPluginLoader implements PluginLoader {

    final DircBot bot;
    private final Pattern[] fileFilters = new Pattern[] { Pattern.compile("\\.jar$"), };
    private final Map<String, Class<?>> classes = new HashMap<>();
    private final Map<String, PluginClassLoader> loaders = new LinkedHashMap<>();

    /**
     * This class was not meant to be constructed explicitly
     */
    @Deprecated
    public JavaPluginLoader(@NotNull DircBot bot) {
        this.bot = bot;
    }

    public Plugin loadPlugin(@NotNull final File file) throws InvalidPluginException {
        if (!file.exists()) {
            throw new InvalidPluginException(new FileNotFoundException(file.getPath() + " does not exist"));
        }

        final PluginDescriptionFile description;
        try {
            description = getPluginDescription(file);
        } catch (InvalidDescriptionException ex) {
            throw new InvalidPluginException(ex);
        }

        final File parentFile = file.getParentFile();
        final File dataFolder = new File(parentFile, description.getName());
        @SuppressWarnings("deprecation")
        final File oldDataFolder = new File(parentFile, description.getRawName());

        // Found old data folder
        if (dataFolder.equals(oldDataFolder)) {
            // They are equal -- nothing needs to be done!
        } else if (dataFolder.isDirectory() && oldDataFolder.isDirectory()) {
            bot.log(String.format(
                    "[WARNING] While loading %s (%s) found old-data folder: `%s' next to the new one `%s'",
                    description.getFullName(),
                    file,
                    oldDataFolder,
                    dataFolder
            ));
        } else if (oldDataFolder.isDirectory() && !dataFolder.exists()) {
            if (!oldDataFolder.renameTo(dataFolder)) {
                throw new InvalidPluginException("Unable to rename old data folder: `" + oldDataFolder + "' to: `" + dataFolder + "'");
            }
            bot.log(String.format(
                    "While loading %s (%s) renamed data folder: `%s' to `%s'",
                    description.getFullName(),
                    file,
                    oldDataFolder,
                    dataFolder
            ));
        }

        if (dataFolder.exists() && !dataFolder.isDirectory()) {
            throw new InvalidPluginException(String.format(
                "Projected datafolder: `%s' for %s (%s) exists and is not a directory",
                dataFolder,
                description.getFullName(),
                file
            ));
        }

        for (final String pluginName : description.getDepend()) {
            if (loaders == null) {
                throw new UnknownDependencyException(pluginName);
            }
            PluginClassLoader current = loaders.get(pluginName);

            if (current == null) {
                throw new UnknownDependencyException(pluginName);
            }
        }

        final PluginClassLoader loader;
        try {
            loader = new PluginClassLoader(this, getClass().getClassLoader(), description, dataFolder, file);
        } catch (InvalidPluginException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new InvalidPluginException(ex);
        }

        loaders.put(description.getName(), loader);

        return loader.plugin;
    }

    public PluginDescriptionFile getPluginDescription(@NotNull File file) throws InvalidDescriptionException {

        JarFile jar = null;
        InputStream stream = null;

        try {
            jar = new JarFile(file);
            JarEntry entry = jar.getJarEntry("plugin.yml");

            if (entry == null) {
                throw new InvalidDescriptionException(new FileNotFoundException("Jar does not contain plugin.yml"));
            }

            stream = jar.getInputStream(entry);

            return new PluginDescriptionFile(stream);

        } catch (IOException ex) {
            throw new InvalidDescriptionException(ex);
        } catch (YAMLException ex) {
            throw new InvalidDescriptionException(ex);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public Pattern[] getPluginFileFilters() {
        return fileFilters.clone();
    }

    Class<?> getClassByName(final String name) {
        Class<?> cachedClass = classes.get(name);

        if (cachedClass != null) {
            return cachedClass;
        } else {
            for (String current : loaders.keySet()) {
                PluginClassLoader loader = loaders.get(current);

                try {
                    cachedClass = loader.findClass(name, false);
                } catch (ClassNotFoundException cnfe) {}
                if (cachedClass != null) {
                    return cachedClass;
                }
            }
        }
        return null;
    }

    void setClass(final String name, final Class<?> clazz) {
        if (!classes.containsKey(name)) {
            classes.put(name, clazz);

            /*
            if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
                Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
                ConfigurationSerialization.registerClass(serializable);
            }
            */
        }
    }

    private void removeClass(String name) {
        Class<?> clazz = classes.remove(name);

        /*
        try {
            if ((clazz != null) && (ConfigurationSerializable.class.isAssignableFrom(clazz))) {
                Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
                ConfigurationSerialization.unregisterClass(serializable);
            }
        } catch (NullPointerException ex) {
            // Boggle!
            // (Native methods throwing NPEs is not fun when you can't stop it before-hand)
        }
        */
    }

    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(@NotNull Listener listener, @NotNull final Plugin plugin) {

        //boolean useTimings = server.getPluginManager().useTimings();
        Map<Class<? extends Event>, Set<RegisteredListener>> ret = new HashMap<>();
        Set<Method> methods;
        try {
            Method[] publicMethods = listener.getClass().getMethods();
            methods = new HashSet<>(publicMethods.length, Float.MAX_VALUE);
            for (Method method : publicMethods) {
                methods.add(method);
            }
            for (Method method : listener.getClass().getDeclaredMethods()) {
                methods.add(method);
            }
        } catch (NoClassDefFoundError e) {
            plugin.log("[SEVERE] Plugin " + plugin.getDescription().getFullName() + " has failed to register events for " + listener.getClass() + " because " + e.getMessage() + " does not exist.");
            return ret;
        }

        for (final Method method : methods) {
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) continue;
            final Class<?> checkClass;
            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                plugin.log("[SEVERE] " + plugin.getDescription().getFullName() + " attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + listener.getClass());
                continue;
            }
            final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
            method.setAccessible(true);
            Set<RegisteredListener> eventSet = ret.get(eventClass);
            if (eventSet == null) {
                eventSet = new HashSet<>();
                ret.put(eventClass, eventSet);
            }

            EventExecutor executor = new EventExecutor() {
                public void execute(Listener listener, Event event) throws EventException {
                    try {
                        if (!eventClass.isAssignableFrom(event.getClass())) {
                            return;
                        }
                        method.invoke(listener, event);
                    } catch (InvocationTargetException ex) {
                        throw new EventException(ex.getCause());
                    } catch (Throwable t) {
                        throw new EventException(t);
                    }
                }
            };
            //if (useTimings) {
            //    eventSet.add(new TimedRegisteredListener(listener, executor, eh.priority(), plugin, eh.ignoreCancelled()));
            //} else {
            eventSet.add(new RegisteredListener(listener, executor, eh.priority(), plugin));
            //}
        }
        return ret;
    }

    public void enablePlugin(final Plugin plugin) {
        if (plugin instanceof JavaPlugin) {
            throw new RuntimeException("Plugin is not associated with this PluginLoader");
        }

        if (!plugin.isEnabled()) {
            plugin.log("Enabling " + plugin.getDescription().getFullName());

            JavaPlugin jPlugin = (JavaPlugin) plugin;

            String pluginName = jPlugin.getDescription().getName();

            if (!loaders.containsKey(pluginName)) {
                loaders.put(pluginName, (PluginClassLoader) jPlugin.getClassLoader());
            }

            try {
                jPlugin.setEnabled(true);
            } catch (Throwable ex) {
                bot.log("[SEVERE] Error occurred while enabling " + plugin.getDescription().getFullName() + " (Is it up to date?)");
                ex.printStackTrace();
            }

            // Perhaps abort here, rather than continue going, but as it stands,
            // an abort is not possible the way it's currently written
            bot.getPluginManager().callEvent(new PluginEnableEvent(bot, plugin));
        }
    }

    public void disablePlugin(Plugin plugin) {
        if (plugin instanceof JavaPlugin) {
            throw new RuntimeException("Plugin is not associated with this PluginLoader");
        }

        if (plugin.isEnabled()) {
            String message = String.format("Disabling %s", plugin.getDescription().getFullName());
            plugin.log(message);

            bot.getPluginManager().callEvent(new PluginDisableEvent(bot, plugin));

            JavaPlugin jPlugin = (JavaPlugin) plugin;
            ClassLoader cloader = jPlugin.getClassLoader();

            try {
                jPlugin.setEnabled(false);
            } catch (Throwable ex) {
                bot.log("[SEVERE] Error occurred while disabling " + plugin.getDescription().getFullName() + " (Is it up to date?)");
                ex.printStackTrace();
            }

            loaders.remove(jPlugin.getDescription().getName());

            if (cloader instanceof PluginClassLoader) {
                PluginClassLoader loader = (PluginClassLoader) cloader;
                Set<String> names = loader.getClasses();

                for (String name : names) {
                    removeClass(name);
                }
            }
        }
    }
}
