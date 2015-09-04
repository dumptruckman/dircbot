package com.dumptruckman.dircbot.event.irc;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class IrcEvent extends Event {

    protected IrcEvent(@NotNull final DircBot bot) {
        super(bot);
    }
}
