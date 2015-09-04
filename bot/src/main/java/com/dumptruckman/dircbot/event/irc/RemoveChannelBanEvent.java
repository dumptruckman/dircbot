package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RemoveChannelBanEvent extends ChannelEvent {

    private final String hostmask;

    public RemoveChannelBanEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String channel, String hostmask) {
        super(bot, sourceNick, sourceLogin, sourceHostname, channel);
        this.hostmask = hostmask;
    }

    public String getHostmask() {
        return hostmask;
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
