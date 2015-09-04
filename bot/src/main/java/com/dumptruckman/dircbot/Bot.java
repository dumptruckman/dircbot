package com.dumptruckman.dircbot;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public enum Bot {
    ;

    private static DircBot instance = null;

    static void setInstance(@NotNull DircBot bot) {
        instance = bot;
    }

    public static Logger getLogger() {
        if (instance == null) {
            throw new IllegalStateException("Bot instance has not been initialized");
        }
        return instance.getLogger();
    }
}
