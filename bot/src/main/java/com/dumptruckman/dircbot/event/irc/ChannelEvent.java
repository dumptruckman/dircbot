package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import org.jetbrains.annotations.NotNull;

public abstract class ChannelEvent extends SourcedEvent {

    private final String channel;

    protected ChannelEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String channel) {
        super(bot, sourceNick, sourceLogin, sourceHostname);
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }
}
