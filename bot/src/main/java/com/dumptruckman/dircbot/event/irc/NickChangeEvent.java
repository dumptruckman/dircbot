package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NickChangeEvent extends SourcedEvent {

    private final String newNick;

    public NickChangeEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String newNick) {
        super(bot, sourceNick, sourceLogin, sourceHostname);
        this.newNick = newNick;
    }

    public String getNewNick() {
        return newNick;
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
