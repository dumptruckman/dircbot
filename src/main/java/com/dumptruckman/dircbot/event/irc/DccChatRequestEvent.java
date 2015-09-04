package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DccChatRequestEvent extends SourcedEvent {

    private final long address;
    private final int port;

    public DccChatRequestEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, long address, int port) {
        super(bot, sourceNick, sourceLogin, sourceHostname);
        this.address = address;
        this.port = port;
    }

    public long getAddress() {
        return address;
    }

    public int getPort() {
        return port;
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
