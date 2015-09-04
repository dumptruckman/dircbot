package com.dumptruckman.dircbot.event.bot;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.Event;
import com.dumptruckman.dircbot.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public abstract class PluginEvent extends Event {

    @NotNull
    private final Plugin plugin;

    public PluginEvent(@NotNull DircBot bot, @NotNull final Plugin plugin) {
        super(bot);
        this.plugin = plugin;
    }

    /**
     * Gets the plugin involved in this event
     *
     * @return Plugin for this event
     */
    @NotNull
    public Plugin getPlugin() {
        return plugin;
    }
}
