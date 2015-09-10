package com.dumptruckman.dircbot.commands;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.command.Command;
import com.dumptruckman.dircbot.command.CommandContext;
import com.dumptruckman.dircbot.command.CommandException;
import com.dumptruckman.dircbot.command.CommandInfo;
import com.dumptruckman.dircbot.command.CommandType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandInfo(aliases = "say", type = CommandType.PRIVATE, isAdminRequired = true)
public class SayCommand extends Command {

    public SayCommand(DircBot bot, @Nullable String channel, String sender, String login, String hostname, CommandContext context) throws CommandException {
        super(bot, channel, sender, login, hostname, context);
    }

    @Override
    protected void runCommand(@NotNull CommandContext commandContext) {
        try {
            String target = commandContext.getString(0);
            String message = commandContext.getString(1);
            getBot().sendMessage(target, message);
        } catch (IndexOutOfBoundsException e) {
            replyToSender("The format is: say <nick/#channel> \"message in quotes\"");
        }
    }
}
