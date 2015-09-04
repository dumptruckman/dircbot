package com.dumptruckman.dircbot.event.bot;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import com.dumptruckman.dircbot.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a plugin is enabled.
 */
public class PluginEnableEvent extends PluginEvent {

    public PluginEnableEvent(@NotNull DircBot bot, @NotNull final Plugin plugin) {
        super(bot, plugin);
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}