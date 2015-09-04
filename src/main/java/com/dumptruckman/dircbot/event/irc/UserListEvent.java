package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jibble.pircbot.User;

public class UserListEvent extends IrcEvent {

    private final String channel;
    private final User[] users;

    public UserListEvent(@NotNull DircBot bot, String channel, User[] users) {
        super(bot);
        this.channel = channel;
        this.users = users;
    }

    public String getChannel() {
        return channel;
    }

    public User[] getUsers() {
        return users;
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
