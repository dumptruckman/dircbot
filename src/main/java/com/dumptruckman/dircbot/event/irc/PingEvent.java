package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PingEvent extends SourcedEvent {

    private final String target, pingValue;

    public PingEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String target, String pingValue) {
        super(bot, sourceNick, sourceLogin, sourceHostname);
        this.target = target;
        this.pingValue = pingValue;
    }

    public String getTarget() {
        return target;
    }

    public String getPingValue() {
        return pingValue;
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
