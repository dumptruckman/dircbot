package com.dumptruckman.dircbot.commands;

import com.dumptruckman.dircbot.Command;
import com.dumptruckman.dircbot.CommandContext;
import com.dumptruckman.dircbot.CommandException;
import com.dumptruckman.dircbot.DircBot;
import org.jetbrains.annotations.NotNull;

public class JoinCommand extends Command {

    public JoinCommand(DircBot bot, String channel, String sender, String login, String hostname, CommandContext context) throws CommandException {
        super(bot, channel, sender, login, hostname, context);
    }

    @Override
    protected void runCommand(@NotNull CommandContext commandContext) {
        try {
            String channel = commandContext.getString(0);
            if (getBot().isFreeJoinEnabled()) {
                getBot().joinChannel(channel);
            } else if (getBot().isAdministrator(getLogin(), getHostname())) {
                getBot().joinChannel(channel);
            } else {
                replyToSender("You are not authorized to invite this bot into a channel.");
            }
        } catch (IndexOutOfBoundsException e) {
            replyToSender("You must specify a channel to join!");
        }
    }
}
