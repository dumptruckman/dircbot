package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ChannelInfoEvent extends IrcEvent {

    private final String channel, topic;
    private final int userCount;

    public ChannelInfoEvent(@NotNull DircBot bot, String channel, String topic, int userCount) {
        super(bot);
        this.channel = channel;
        this.topic = topic;
        this.userCount = userCount;
    }

    public String getChannel() {
        return channel;
    }

    public String getTopic() {
        return topic;
    }

    public int getUserCount() {
        return userCount;
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
