package com.dumptruckman.dircbot.plugin;

import com.dumptruckman.dircbot.DircBot;
import pluginbase.logging.LoggablePlugin;
import pluginbase.logging.PluginLogger;

public interface Plugin extends LoggablePlugin {

    DircBot getBot();

    boolean isEnabled();

    PluginLoader getPluginLoader();

    void log(String log);

    PluginDescriptionFile getDescription();

    PluginLogger getLogger();

    /**
     * Called when this plugin is disabled
     */
    void onDisable();

    /**
     * Called after a plugin is loaded but before it has been enabled.
     * <p>
     * When mulitple plugins are loaded, the onLoad() for all plugins is
     * called before any onEnable() is called.
     */
    void onLoad();

    /**
     * Called when this plugin is enabled
     */
    void onEnable();
}
