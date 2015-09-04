package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class QuitEvent extends SourcedEvent {

    private final String reason;

    public QuitEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        super(bot, sourceNick, sourceLogin, sourceHostname);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
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
