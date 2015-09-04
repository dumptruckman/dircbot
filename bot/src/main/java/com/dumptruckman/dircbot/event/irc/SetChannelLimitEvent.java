package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SetChannelLimitEvent extends ChannelEvent {

    private final int limit;

    public SetChannelLimitEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String channel, int limit) {
        super(bot, sourceNick, sourceLogin, sourceHostname, channel);
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
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
