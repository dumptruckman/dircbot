package com.dumptruckman.dircbot.commands;

import com.dumptruckman.dircbot.Command;
import com.dumptruckman.dircbot.CommandContext;
import com.dumptruckman.dircbot.CommandException;
import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.util.DiceRolls;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class RollCommand extends Command {

    private static final Pattern DICE_PATTERN = Pattern.compile("([\\d()+\\-/^*d]+#)*[\\d()+\\-/^*d]*d[\\d()+\\-/^*d]+\\s.*");
    static final DecimalFormat FORMAT = new DecimalFormat() {{setDecimalSeparatorAlwaysShown(false);}};

    public static boolean isDice(@NotNull String message) {
        return DICE_PATTERN.matcher(message).matches();
    }

    public RollCommand(DircBot bot, String channel, String sender, String login, String hostname, CommandContext context) throws CommandException {
        super(bot, channel, sender, login, hostname, context);
    }

    @Override
    protected void runCommand(@NotNull CommandContext commandContext) {

        try {
            String diceString;
            if (!commandContext.getCommand().equalsIgnoreCase("roll")) {
                diceString = commandContext.getCommand() + " " + commandContext.getOriginalArgs();
            } else if (commandContext.argsLength() > 0) {
                diceString = commandContext.getOriginalArgs();
            } else {
                diceString = "1d20";
            }

            StringBuilder resultBuffer = new StringBuilder().append(getSender()).append(", ");

            String[] separateRolls = diceString.split(";");

            DiceRolls rolls = new DiceRolls();

            for (int i = 0; i < separateRolls.length; i++) {
                if (i > 0) {
                    resultBuffer.append("; ");
                }

                String separateRoll = separateRolls[i].trim();
                String[] rollDetails = separateRoll.split(" ", 2);
                if (rollDetails.length > 1) {
                    resultBuffer.append(rollDetails[1]).append(": ");
                } else {
                    resultBuffer.append(rollDetails[0]).append(": ");
                }

                String[] multiRoll = rollDetails[0].split("#", 2);
                if (multiRoll.length > 1) {
                    try {
                        int numRolls = Integer.parseInt(multiRoll[0]);
                        if (numRolls < 1) {
                            numRolls = 1;
                        }
                        for (int j = 0; j < numRolls; j++) {
                            if (j > 0) {
                                resultBuffer.append(", ");
                            }
                            resultBuffer = rollAndAppendResults(multiRoll[1], resultBuffer, rolls);
                        }
                    } catch (NumberFormatException e) {
                        resultBuffer = rollAndAppendResults(multiRoll[1], resultBuffer, rolls);
                    }
                } else {
                    resultBuffer = rollAndAppendResults(multiRoll[0], resultBuffer, rolls);
                }
            }

            reply(resultBuffer.toString());
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != null) {
                reply(e.getMessage());
            } else {
                reply("Sorry, I don't understand that input.");
            }
        }
    }

    private StringBuilder rollAndAppendResults(@NotNull String rollString, @NotNull StringBuilder buffer, @NotNull DiceRolls rolls) {
        double rollResult = getBot().getDiceEvaluator().evaluate(rollString, rolls);
        buffer.append(FORMAT.format(rollResult)).append(" ").append(rolls.getRollStrings());
        rolls.clearRollStrings();
        return buffer;
    }
}
