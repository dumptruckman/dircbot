package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class RemoveTopicProtectionEvent extends ChannelEvent {

    public RemoveTopicProtectionEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String channel) {
        super(bot, sourceNick, sourceLogin, sourceHostname, channel);
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
