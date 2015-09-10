package com.dumptruckman.dircbot.event;

import com.dumptruckman.dircbot.DircBot;
import org.jetbrains.annotations.NotNull;

public abstract class Event {

    private final DircBot bot;
    private String name;

    protected Event(@NotNull final DircBot bot) {
        this.bot = bot;
    }

    @NotNull
    protected DircBot getBot() {
        return bot;
    }

    public abstract HandlerList getHandlers();

    /**
     * Convenience method for providing a user-friendly identifier. By
     * default, it is the event's class's {@linkplain Class#getSimpleName()
     * simple name}.
     *
     * @return name of this event
     */
    public String getEventName() {
        if (name == null) {
            name = getClass().getSimpleName();
        }
        return name;
    }
}
