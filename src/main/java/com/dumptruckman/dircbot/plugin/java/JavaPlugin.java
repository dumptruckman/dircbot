package com.dumptruckman.dircbot.plugin.java;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.plugin.Plugin;
import com.dumptruckman.dircbot.plugin.PluginDescriptionFile;
import com.dumptruckman.dircbot.plugin.PluginLoader;
import com.dumptruckman.dircbot.plugin.PluginLogger;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class JavaPlugin implements Plugin {

    private boolean enabled = false;
    private PluginLoader loader = null;
    private DircBot bot = null;
    private File file = null;
    private PluginDescriptionFile description = null;
    private File dataFolder = null;
    private ClassLoader classLoader = null;
    private boolean naggable = true;
    private File configFile = null;
    private PluginLogger logger = null;

    public JavaPlugin() {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        if (!(classLoader instanceof PluginClassLoader)) {
            throw new IllegalStateException("JavaPlugin requires " + PluginClassLoader.class.getName());
        }
        ((PluginClassLoader) classLoader).initialize(this);
    }

    final void init(PluginLoader loader, DircBot bot, PluginDescriptionFile description, File dataFolder, File file, ClassLoader classLoader) {
        this.loader = loader;
        this.bot = bot;
        this.file = file;
        this.description = description;
        this.dataFolder = dataFolder;
        this.classLoader = classLoader;
        this.configFile = new File(dataFolder, "config.yml");
        this.logger = new PluginLogger(this);
    }

    ClassLoader getClassLoader() {
        return classLoader;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public DircBot getBot() {
        return bot;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public PluginLoader getPluginLoader() {
        return loader;
    }

    @Override
    public void log(String log) {
        bot.log(log);
    }

    @Override
    public PluginDescriptionFile getDescription() {
        return description;
    }

    @Override
    public PluginLogger getLogger() {
        return logger;
    }
}
