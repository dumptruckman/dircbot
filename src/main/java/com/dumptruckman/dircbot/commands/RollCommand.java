package com.dumptruckman.dircbot.commands;

import com.dumptruckman.dircbot.Command;
import com.dumptruckman.dircbot.CommandContext;
import com.dumptruckman.dircbot.CommandException;
import com.dumptruckman.dircbot.DIRCBot;
import com.dumptruckman.dircbot.util.DiceRolls;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class RollCommand extends Command {

    private static final DecimalFormat FORMAT = new DecimalFormat() {{setDecimalSeparatorAlwaysShown(false);}};

    public RollCommand(DIRCBot bot, String channel, String sender, String login, String hostname, CommandContext context) throws CommandException {
        super(bot, channel, sender, login, hostname, context);
    }

    @Override
    protected void runCommand(@NotNull CommandContext commandContext) {
        String diceString;
        if (commandContext.argsLength() > 0) {
            diceString = commandContext.getString(0);
        } else {
            diceString = "1d20";
        }

        try {
            DiceRolls rolls = new DiceRolls();
            double rollResult = getBot().getDiceEvaluator().evaluate(diceString, rolls);
            reply(getSender() + ", " + diceString + ": " + FORMAT.format(rollResult) + " " + rolls.getRollStrings());
        } catch (IllegalArgumentException e) {
            reply(e.getMessage());
        }
    }
}
