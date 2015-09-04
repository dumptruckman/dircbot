package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class VersionEvent extends SourcedEvent {

    private final String targetNick;

    public VersionEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String targetNick) {
        super(bot, sourceNick, sourceLogin, sourceHostname);
        this.targetNick = targetNick;
    }

    public String getTargetNick() {
        return targetNick;
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
