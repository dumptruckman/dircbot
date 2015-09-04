package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TopicEvent extends IrcEvent {

    private final String channel, topic, setBy;
    private final long date;
    private final boolean changed;

    public TopicEvent(@NotNull DircBot bot, String channel, String topic, String setBy, long date, boolean changed) {
        super(bot);
        this.channel = channel;
        this.topic = topic;
        this.setBy = setBy;
        this.date = date;
        this.changed = changed;
    }

    public String getChannel() {
        return channel;
    }

    public String getTopic() {
        return topic;
    }

    public String getSetBy() {
        return setBy;
    }

    public long getDate() {
        return date;
    }

    public boolean isChanged() {
        return changed;
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
