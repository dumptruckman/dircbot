package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TimeEvent extends SourcedEvent {

    private final String target;

    public TimeEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String target) {
        super(bot, sourceNick, sourceLogin, sourceHostname);
        this.target = target;
    }

    public String getTarget() {
        return target;
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
