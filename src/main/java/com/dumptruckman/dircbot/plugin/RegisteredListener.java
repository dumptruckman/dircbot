package com.dumptruckman.dircbot.plugin;

import com.dumptruckman.dircbot.event.Event;
import com.dumptruckman.dircbot.event.EventException;
import com.dumptruckman.dircbot.event.EventPriority;
import com.dumptruckman.dircbot.event.Listener;

/**
 * Stores relevant information for plugin listeners
 */
public class RegisteredListener {
    private final Listener listener;
    private final EventPriority priority;
    private final Plugin plugin;
    private final EventExecutor executor;

    public RegisteredListener(final Listener listener, final EventExecutor executor, final EventPriority priority, final Plugin plugin) {
        this.listener = listener;
        this.priority = priority;
        this.plugin = plugin;
        this.executor = executor;
    }

    /**
     * Gets the listener for this registration
     *
     * @return Registered Listener
     */
    public Listener getListener() {
        return listener;
    }

    /**
     * Gets the plugin for this registration
     *
     * @return Registered Plugin
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Gets the priority for this registration
     *
     * @return Registered Priority
     */
    public EventPriority getPriority() {
        return priority;
    }

    /**
     * Calls the event executor
     *
     * @param event The event
     * @throws EventException If an event handler throws an exception.
     */
    public void callEvent(final Event event) throws EventException {
        /*
        if (event instanceof Cancellable){
            if (((Cancellable) event).isCancelled() && isIgnoringCancelled()){
                return;
            }
        }
        */
        executor.execute(listener, event);
    }
}
