package com.dumptruckman.dircbot.commands;

import com.dumptruckman.dircbot.Command;
import com.dumptruckman.dircbot.CommandContext;
import com.dumptruckman.dircbot.CommandException;
import com.dumptruckman.dircbot.DIRCBot;
import com.dumptruckman.dircbot.util.DiceRolls;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class SuccessCommand extends Command {

    static int countSuccesses(double roll) {
        return ((int) (Math.floor((roll - 8) / 3))) + 1;
    }

    public SuccessCommand(DIRCBot bot, String channel, String sender, String login, String hostname, CommandContext context) throws CommandException {
        super(bot, channel, sender, login, hostname, context);
    }

    @Override
    protected void runCommand(@NotNull CommandContext commandContext) {
        try {
            int result = commandContext.getInteger(0);
            int successes = countSuccesses(result);
            if (successes < 1) {
                reply(result + " - No Success");
            } else if (successes == 1) {
                reply(result + " - 1 Success");
            } else {
                reply(result + " - " + successes + " Successes");
            }
        } catch (NumberFormatException e) {
            reply("You must specify a result number to see how many successes that is.");
        } catch (IndexOutOfBoundsException e) {
            reply("You must specify a result number to see how many successes that is.");
        }
    }
}
