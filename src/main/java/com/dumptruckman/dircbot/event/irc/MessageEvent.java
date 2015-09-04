package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MessageEvent extends ChannelEvent {

    private final String message;

    public MessageEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String channel, String message) {
        super(bot, sourceNick, sourceLogin, sourceHostname, channel);
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
