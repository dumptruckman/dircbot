package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jibble.pircbot.DccChat;

public class IncomingChatRequestEvent extends IrcEvent {

    private final DccChat chat;

    public IncomingChatRequestEvent(@NotNull DircBot bot, DccChat chat) {
        super(bot);
        this.chat = chat;
    }

    public DccChat getChat() {
        return chat;
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
