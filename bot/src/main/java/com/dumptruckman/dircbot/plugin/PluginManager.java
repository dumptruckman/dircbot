package com.dumptruckman.dircbot.plugin;

import com.dumptruckman.dircbot.event.Event;
import com.dumptruckman.dircbot.event.Events;
import com.dumptruckman.dircbot.event.Listener;
import org.jetbrains.annotations.NotNull;

public interface PluginManager {

    void registerEvents(@NotNull Plugin owner, @NotNull Listener listener);

    default void callEvent(@NotNull Event event) {
        Events.callEvent(event);
    }
}
