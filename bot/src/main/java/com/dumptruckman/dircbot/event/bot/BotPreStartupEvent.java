package com.dumptruckman.dircbot.event.bot;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.event.Event;
import com.dumptruckman.dircbot.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * This event is called before launch flags are evaluated and allows plugins to add additional value flags.
 */
public class BotPreStartupEvent extends Event {

    private final Set<Character> launchFlags;

    public BotPreStartupEvent(@NotNull DircBot bot, @NotNull Set<Character> launchFlags) {
        super(bot);
        this.launchFlags = launchFlags;
    }

    /**
     * Returns the set of value flags that will be evaluated for when parsing bot command line arguments.
     * Value flags are flags that take a string value such as "-p somepassword". Non-value flags should not be
     * added to this set.
     */
    public Set<Character> getLaunchFlags() {
        return launchFlags;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
