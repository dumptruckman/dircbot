package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class UserModeEvent extends SourcedEvent {

    private final String targetNick, mode;

    public UserModeEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String targetNick, String mode) {
        super(bot, sourceNick, sourceLogin, sourceHostname);
        this.targetNick = targetNick;
        this.mode = mode;
    }

    public String getTargetNick() {
        return targetNick;
    }

    public String getMode() {
        return mode;
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
