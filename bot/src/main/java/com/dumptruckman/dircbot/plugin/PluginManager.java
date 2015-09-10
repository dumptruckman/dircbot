package com.dumptruckman.dircbot.plugin;

import com.dumptruckman.dircbot.Bot;
import com.dumptruckman.dircbot.event.Event;
import com.dumptruckman.dircbot.event.Listener;
import org.jetbrains.annotations.NotNull;

public interface PluginManager {

    void registerEvents(@NotNull Plugin owner, @NotNull Listener listener);

    void callEvent(@NotNull Event event);

    /**
     * Checks if the given plugin is loaded and returns it when applicable
     * <p>
     * Please note that the name of the plugin is case-sensitive
     *
     * @param name Name of the plugin to check
     * @return Plugin if it exists, otherwise null
     */
    Plugin getPlugin(String name);

    /**
     * Gets a list of all currently loaded plugins
     *
     * @return Array of Plugins
     */
    Plugin[] getPlugins();

    /**
     * Checks if the given plugin is enabled or not
     * <p>
     * Please note that the name of the plugin is case-sensitive.
     *
     * @param name Name of the plugin to check
     * @return true if the plugin is enabled, otherwise false
     */
    boolean isPluginEnabled(String name);

    /**
     * Checks if the given plugin is enabled or not
     *
     * @param plugin Plugin to check
     * @return true if the plugin is enabled, otherwise false
     */
    boolean isPluginEnabled(Plugin plugin);
}
