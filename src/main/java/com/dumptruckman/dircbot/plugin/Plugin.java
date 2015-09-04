package com.dumptruckman.dircbot.plugin;

import com.dumptruckman.dircbot.DircBot;

public interface Plugin {

    DircBot getBot();

    boolean isEnabled();

    PluginLoader getPluginLoader();

    void log(String log);

    PluginDescriptionFile getDescription();

    PluginLogger getLogger();
}
