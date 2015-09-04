package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DeOpEvent extends ChannelEvent {

    private final String recipient;

    public DeOpEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String channel, String recipient) {
        super(bot, sourceNick, sourceLogin, sourceHostname, channel);
        this.recipient = recipient;
    }

    public String getRecipient() {
        return recipient;
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
