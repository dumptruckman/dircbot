package com.dumptruckman.dircbot.plugin;

import com.dumptruckman.dircbot.event.Event;
import com.dumptruckman.dircbot.event.Listener;

import java.util.Map;
import java.util.Set;

public interface PluginLoader {

    /**
     * Creates and returns registered listeners for the event classes used in
     * this listener
     *
     * @param listener The object that will handle the eventual call back
     * @param plugin The plugin to use when creating registered listeners
     * @return The registered listeners.
     */
    public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, Plugin plugin);
}
