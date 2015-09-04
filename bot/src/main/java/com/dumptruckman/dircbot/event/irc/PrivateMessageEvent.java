package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PrivateMessageEvent extends SourcedEvent {

    private final String message;

    public PrivateMessageEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String message) {
        super(bot, sourceNick, sourceLogin, sourceHostname);
        this.message = message;
    }

    public String getMessage() {
        return message;
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
