package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jibble.pircbot.DccFileTransfer;

public class IncomingFileTransferEvent extends IrcEvent {

    private final DccFileTransfer transfer;

    public IncomingFileTransferEvent(@NotNull DircBot bot, DccFileTransfer transfer) {
        super(bot);
        this.transfer = transfer;
    }

    public DccFileTransfer getTransfer() {
        return transfer;
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
