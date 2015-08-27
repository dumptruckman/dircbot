package com.dumptruckman.dircbot;

import org.jetbrains.annotations.NotNull;
import org.jibble.pircbot.PircBot;

public abstract class Command {

    private final DIRCBot bot;
    private final String channel;
    private final String sender;
    private final String login;
    private final String hostname;
    private final CommandContext commandContext;

    public Command(DIRCBot bot, String channel, String sender, String login, String hostname, CommandContext commandContext) throws CommandException {
        this.bot = bot;
        this.channel = channel;
        this.sender = sender;
        this.login = login;
        this.hostname = hostname;
        this.commandContext = commandContext;
    }

    void runCommand() {
        runCommand(commandContext);
    }

    public DIRCBot getBot() {
        return bot;
    }

    public String getChannel() {
        return channel;
    }

    public String getSender() {
        return sender;
    }

    public String getLogin() {
        return login;
    }

    public String getHostname() {
        return hostname;
    }

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
