package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ModeEvent extends ChannelEvent {

    private final String mode;

    public ModeEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String channel, String mode) {
        super(bot, sourceNick, sourceLogin, sourceHostname, channel);
        this.mode = mode;
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
