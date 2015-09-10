package com.dumptruckman.dircbot.command;

import org.jetbrains.annotations.NotNull;

/**
 * Used when command does not exist.
 */
public class CommandNotFoundException extends CommandException {

    private final String command;

    public CommandNotFoundException(@NotNull final String command, @NotNull final String message) {
        super(message);
        this.command = command;
    }

    public CommandNotFoundException(@NotNull final String command, @NotNull final String message, @NotNull final Throwable throwable) {
        super(message, throwable);
        this.command = command;
    }

    public CommandNotFoundException(@NotNull final String command, @NotNull final String message, @NotNull final Exception cause) {
        super(message, cause);
        this.command = command;
    }

    public CommandNotFoundException(@NotNull final String command, @NotNull final Exception e) {
        super(e);
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
