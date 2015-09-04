package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DisconnectEvent extends IrcEvent {

    private boolean terminatingProcess;

    public DisconnectEvent(@NotNull DircBot bot, boolean terminatingProcess) {
        super(bot);
        this.terminatingProcess = terminatingProcess;
    }

    public boolean isTerminatingProcess() {
        return terminatingProcess;
    }

    public void setTerminatingProcess(boolean terminatingProcess) {
        this.terminatingProcess = terminatingProcess;
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
