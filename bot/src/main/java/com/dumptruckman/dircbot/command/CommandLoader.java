package com.dumptruckman.dircbot.command;

import com.dumptruckman.dircbot.DircBot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CommandLoader {

    <C extends Command> C createCommandInstance(@NotNull Class<C> commandClass, @NotNull DircBot bot, @Nullable String channel, @NotNull String sender, @NotNull String login, @NotNull String hostname, @NotNull CommandContext commandContext) throws CommandException;
}
