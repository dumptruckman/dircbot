package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class InviteEvent extends ChannelEvent {

    private final String targetNick;

    public InviteEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String channel, String targetNick) {
        super(bot, sourceNick, sourceLogin, sourceHostname, channel);
        this.targetNick = targetNick;
    }

    /**
     * This returns the target of the invite. This should be the bot!
     */
    public String getTargetNick() {
        return targetNick;
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
