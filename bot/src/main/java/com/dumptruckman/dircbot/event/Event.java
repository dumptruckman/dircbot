package com.dumptruckman.dircbot.event;

import com.dumptruckman.dircbot.DircBot;
import org.jetbrains.annotations.NotNull;

public abstract class Event {

    private final DircBot bot;

    protected Event(@NotNull final DircBot bot) {
        this.bot = bot;
    }

    @NotNull
    protected DircBot getBot() {
        return bot;
    }

    public abstract HandlerList getHandlers();
}
