package com.dumptruckman.dircbot.commands;

import com.dumptruckman.dircbot.Command;
import com.dumptruckman.dircbot.CommandContext;
import com.dumptruckman.dircbot.CommandException;
import com.dumptruckman.dircbot.DircBot;
import org.jetbrains.annotations.NotNull;

public class KillCommand extends Command {

    public KillCommand(DircBot bot, String channel, String sender, String login, String hostname, CommandContext context) throws CommandException {
        super(bot, channel, sender, login, hostname, context);
    }

    @Override
    protected void runCommand(@NotNull CommandContext commandContext) {
        if (getBot().isAdministrator(getLogin(), getHostname())) {
            replyToSender("Shutting down...");
            getBot().kill();
        }
    }
}
