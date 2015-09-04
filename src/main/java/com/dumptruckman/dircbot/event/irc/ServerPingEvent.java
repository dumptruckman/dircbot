package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ServerPingEvent extends IrcEvent {

    private final String response;

    public ServerPingEvent(@NotNull DircBot bot, String response) {
        super(bot);
        this.response = response;
    }

    public String getResponse() {
        return response;
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
