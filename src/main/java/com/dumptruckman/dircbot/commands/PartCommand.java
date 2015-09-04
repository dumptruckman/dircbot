package com.dumptruckman.dircbot.commands;

import com.dumptruckman.dircbot.Command;
import com.dumptruckman.dircbot.CommandContext;
import com.dumptruckman.dircbot.CommandException;
import com.dumptruckman.dircbot.DircBot;
import org.jetbrains.annotations.NotNull;

public class PartCommand extends Command {

    public PartCommand(DircBot bot, String channel, String sender, String login, String hostname, CommandContext context) throws CommandException {
        super(bot, channel, sender, login, hostname, context);
    }

    @Override
    protected void runCommand(@NotNull CommandContext commandContext) {
        try {
            String channel = commandContext.getString(0);
            if (getBot().isAdministrator(getLogin(), getHostname())) {
                getBot().partChannel(channel);
            }
        } catch (IndexOutOfBoundsException e) {
            replyToSender("You must specify a channel to part!");
        }
    }
}
