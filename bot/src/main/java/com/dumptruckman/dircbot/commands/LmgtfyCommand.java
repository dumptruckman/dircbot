package com.dumptruckman.dircbot.commands;

import com.dumptruckman.dircbot.command.Command;
import com.dumptruckman.dircbot.command.CommandContext;
import com.dumptruckman.dircbot.command.CommandException;
import com.dumptruckman.dircbot.DircBot;
import org.jetbrains.annotations.NotNull;

public class LmgtfyCommand extends Command {

    public LmgtfyCommand(DircBot bot, String channel, String sender, String login, String hostname, CommandContext context) throws CommandException {
        super(bot, channel, sender, login, hostname, context);
    }

    @Override
    protected void runCommand(@NotNull CommandContext commandContext) {
        if (commandContext.argsLength() > 0) {
            StringBuilder searchTerms = new StringBuilder();
            if (commandContext.getCommand().equalsIgnoreCase("lmgtfy")) {
                searchTerms.append("http://lmgtfy.com/?q=");
            } else {
                searchTerms.append("https://www.google.com/search?q=");
            }
            searchTerms.append(commandContext.getString(0));
            for (int i = 1; i < commandContext.argsLength(); i++) {
                searchTerms.append("+").append(commandContext.getString(i));
            }
            reply(searchTerms.toString());
        } else {
            reply(getSender() + ", you should probably specify some search terms...");
        }
    }
}
