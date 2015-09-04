package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ActionEvent extends SourcedEvent {

    private final String target, action;

    public ActionEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String target, String action) {
        super(bot, sourceNick, sourceLogin, sourceHostname);
        this.target = target;
        this.action = action;
    }

    public String getTarget() {
        return target;
    }

    public String getAction() {
        return action;
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
