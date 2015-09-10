package com.dumptruckman.dircbot.command;

import com.dumptruckman.dircbot.DircBot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Command {

    private final DircBot bot;
    private final String channel;
    private final String sender;
    private final String login;
    private final String hostname;
    private final CommandContext commandContext;

    public Command(@NotNull DircBot bot, @Nullable String channel, @NotNull String sender, @NotNull String login, @NotNull String hostname, @NotNull CommandContext commandContext) throws CommandException {
        this.bot = bot;
        this.channel = channel;
        this.sender = sender;
        this.login = login;
        this.hostname = hostname;
        this.commandContext = commandContext;
    }

    public void runCommand() {
        runCommand(commandContext);
    }

    @NotNull
    public DircBot getBot() {
        return bot;
    }

    @Nullable
    public String getChannel() {
        return channel;
    }

    @NotNull
    public String getSender() {
        return sender;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    @NotNull
    public String getHostname() {
        return hostname;
    }

    @NotNull
    public CommandContext getCommandContext() {
        return commandContext;
    }

    protected abstract void runCommand(@NotNull final CommandContext commandContext);

    protected void reply(@NotNull String message) {
        bot.sendMessage(channel != null ? channel : sender, message);
    }

    protected void replyToSender(@NotNull String message) {
        bot.sendMessage(sender, message);
    }
}
