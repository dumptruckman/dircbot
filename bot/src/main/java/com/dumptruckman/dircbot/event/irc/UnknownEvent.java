package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class UnknownEvent extends IrcEvent {

    private final String line;

    public UnknownEvent(@NotNull DircBot bot, String line) {
        super(bot);
        this.line = line;
    }

    public String getLine() {
        return line;
    }

    private static final HandlerList handlerList = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
