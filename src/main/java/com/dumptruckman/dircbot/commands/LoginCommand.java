package com.dumptruckman.dircbot.commands;

import com.dumptruckman.dircbot.Command;
import com.dumptruckman.dircbot.CommandContext;
import com.dumptruckman.dircbot.CommandException;
import com.dumptruckman.dircbot.DIRCBot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LoginCommand extends Command {

    public LoginCommand(DIRCBot bot, @Nullable String channel, String sender, String login, String hostname, CommandContext context) throws CommandException {
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
