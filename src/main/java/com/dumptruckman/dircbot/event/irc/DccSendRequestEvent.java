package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DccSendRequestEvent extends SourcedEvent {

    private final String filename;
    private final long address;
    private final int port, size;

    public DccSendRequestEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String filename, long address, int port, int size) {
        super(bot, sourceNick, sourceLogin, sourceHostname);
        this.filename = filename;
        this.address = address;
        this.port = port;
        this.size = size;
    }

    public String getFilename() {
        return filename;
    }

    public long getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public int getSize() {
        return size;
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
