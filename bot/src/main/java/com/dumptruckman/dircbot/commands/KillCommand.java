package com.dumptruckman.dircbot.commands;

import com.dumptruckman.dircbot.command.Command;
import com.dumptruckman.dircbot.command.CommandContext;
import com.dumptruckman.dircbot.command.CommandException;
import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.command.CommandInfo;
import com.dumptruckman.dircbot.command.CommandType;
import org.jetbrains.annotations.NotNull;

@CommandInfo(aliases = "kill", type = CommandType.PRIVATE, isAdminRequired = true)
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
