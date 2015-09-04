package com.dumptruckman.dircbot.plugin;

import com.dumptruckman.dircbot.event.Event;
import com.dumptruckman.dircbot.event.EventException;
import com.dumptruckman.dircbot.event.Listener;

/**
 * Interface which defines the class for event call backs to plugins
 */
public interface EventExecutor {
    void execute(Listener listener, Event event) throws EventException;
}
