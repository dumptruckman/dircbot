package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SetChannelKeyEvent extends ChannelEvent {

    private final String key;

    public SetChannelKeyEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String channel, String key) {
        super(bot, sourceNick, sourceLogin, sourceHostname, channel);
        this.key = key;
    }

    public String getKey() {
        return key;
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
