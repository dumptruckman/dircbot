package com.dumptruckman.dircbot.event.bot;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.command.CommandContext;
import com.dumptruckman.dircbot.event.Event;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * This event is called before the bot connects to any IRC servers and after standard launch flags have
 * been evaluated. This event gives plugins a chance to evaluate launch flags before connecting to IRC.
 */
public class BotPreConnectEvent extends Event {

    private final CommandContext startupContext;

    public BotPreConnectEvent(@NotNull DircBot bot, @NotNull CommandContext startupContext) {
        super(bot);
        this.startupContext = startupContext;
    }

    public CommandContext getStartupContext() {
        return startupContext;
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
