package com.dumptruckman.dircbot.commands;

import com.dumptruckman.dircbot.Command;
import com.dumptruckman.dircbot.CommandContext;
import com.dumptruckman.dircbot.CommandException;
import com.dumptruckman.dircbot.DIRCBot;
import org.jetbrains.annotations.NotNull;

public class HelpCommand extends Command {

    public HelpCommand(DIRCBot bot, String channel, String sender, String login, String hostname, CommandContext context) throws CommandException {
        super(bot, channel, sender, login, hostname, context);
    }

    @Override
    protected void runCommand(@NotNull CommandContext commandContext) {
        final boolean loggedIn = getBot().isAdministrator(getLogin(), getHostname());
        replyToSender("COMMAND LIST");
        replyToSender("============");
        replyToSender("--> login <password> [channel] - logs you in as bot admin. specify channel to have me join that channel. you must be in a channel with me to be logged in.");
        replyToSender("--> join <channel> - makes me join a channel." + (getBot().isFreeJoinEnabled() ? "" : " (must be bot admin)"));
        if (loggedIn) {
            replyToSender("--> part <channel> - makes me part a channel. (must be bot admin)");
            replyToSender("--> kill - makes me disconnect from irc and terminate my java process.");
        }
        if (getBot().isSuccessDiceModeEnabled()) {
            replyToSender("--> roll [+bonuses] - rolls success die with bonuses. explodes on natural roll of 10.");
            replyToSender("--> roll9 [+bonuses] - rolls success die with bonuses. explodes on natural roll of 9.");
            replyToSender("--> roll8 [+bonuses] - rolls success die with bonuses. explodes on natural roll of 8.");
            replyToSender("--> chance - rolls a chance die.");
            replyToSender("--> success <result> - checks how many successes the given roll result is.");
        } else {
            replyToSender("--> roll [dice/math expression] - rolls some dice.");
        }
        if (getBot().isFreeRollEnabled()) {
            replyToSender("--> <dice expression> - rolls some dice.");
        }
        if (!loggedIn) {
            replyToSender("More commands may be available to a bot administrator.");
        }
    }
}
