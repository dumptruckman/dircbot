package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class KickEvent extends ChannelEvent {

    private final String targetNick, reason;

    public KickEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String channel, String targetNick, String reason) {
        super(bot, sourceNick, sourceLogin, sourceHostname, channel);
        this.targetNick = targetNick;
        this.reason = reason;
    }

    public String getTargetNick() {
        return targetNick;
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
