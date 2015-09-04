package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jibble.pircbot.DccFileTransfer;

public class FileTransferFinishedEvent extends IrcEvent {

    private final DccFileTransfer transfer;
    private final Exception exception;

    public FileTransferFinishedEvent(@NotNull DircBot bot, DccFileTransfer transfer, Exception exception) {
        super(bot);
        this.transfer = transfer;
        this.exception = exception;
    }

    public DccFileTransfer getTransfer() {
        return transfer;
    }

    public Exception getException() {
        return exception;
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
