package com.dumptruckman.dircbot.commands;

import com.dumptruckman.dircbot.command.Command;
import com.dumptruckman.dircbot.command.CommandContext;
import com.dumptruckman.dircbot.command.CommandException;
import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.command.CommandInfo;
import com.dumptruckman.dircbot.command.CommandType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandInfo(aliases = "login", type = CommandType.PRIVATE)
public class LoginCommand extends Command {

    public LoginCommand(DircBot bot, @Nullable String channel, String sender, String login, String hostname, CommandContext context) throws CommandException {
        super(bot, channel, sender, login, hostname, context);
    }

    @Override
    protected void runCommand(@NotNull CommandContext commandContext) {
        try {
            String password = commandContext.getString(0);
            String channel = commandContext.argsLength() > 1 ? commandContext.getString(1) : null;
            try {
                getBot().loginAdministrator(getSender(), getLogin(), getHostname(), password, channel);
                replyToSender("Thank you for logging in!");
            } catch (IllegalArgumentException e) {
                replyToSender(e.getMessage());
            } catch (IllegalStateException e) {
                replyToSender(e.getMessage());
            }
        } catch (IndexOutOfBoundsException e) {
            replyToSender("You must enter a password in order to login!");
        }
    }
}
