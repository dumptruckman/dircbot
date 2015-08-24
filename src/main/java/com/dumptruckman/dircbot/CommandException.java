package com.dumptruckman.dircbot;

import org.jetbrains.annotations.NotNull;

/**
 * Used when commands throw exceptions.
 */
public class CommandException extends Exception {

    public CommandException(@NotNull final String message) {
        super(message);
    }

    public CommandException(@NotNull final String message, @NotNull final Throwable throwable) {
        super(message, throwable);
    }

    public CommandException(@NotNull final String message, @NotNull final Exception cause) {
        super(message, cause);
    }

    public CommandException(@NotNull final Exception e) {
        super(e);
    }
}
