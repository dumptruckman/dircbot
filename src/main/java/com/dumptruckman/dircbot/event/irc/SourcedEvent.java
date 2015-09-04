package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import org.jetbrains.annotations.NotNull;

public abstract class SourcedEvent extends IrcEvent {

    private final String sourceNick, sourceLogin, sourceHostname;

    protected SourcedEvent(@NotNull DircBot bot, String sourceNick, String sourceLogin, String sourceHostname) {
        super(bot);
        this.sourceNick = sourceNick;
        this.sourceLogin = sourceLogin;
        this.sourceHostname = sourceHostname;
    }

    public String getSourceNick() {
        return sourceNick;
    }

    public String getSourceLogin() {
        return sourceLogin;
    }

    public String getSourceHostname() {
        return sourceHostname;
    }
}
