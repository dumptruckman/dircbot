package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NoticeEvent extends SourcedEvent {

    private final String target, notice;

    public NoticeEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
        super(bot, sourceNick, sourceLogin, sourceHostname);
        this.target = target;
        this.notice = notice;
    }

    public String getTarget() {
        return target;
    }

    public String getNotice() {
        return notice;
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
