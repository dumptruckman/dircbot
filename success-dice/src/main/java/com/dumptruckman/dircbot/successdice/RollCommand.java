package com.dumptruckman.dircbot.successdice;

import com.dumptruckman.dircbot.command.CommandContext;
import com.dumptruckman.dircbot.command.CommandException;
import com.dumptruckman.dircbot.command.CommandInfo;
import com.dumptruckman.dircbot.plugin.PluginCommand;
import org.jetbrains.annotations.NotNull;

@CommandInfo(aliases = {"roll", "roll9", "roll8"})
public class RollCommand extends PluginCommand<SuccessDicePlugin> {

    private final int explodeOn;

    public RollCommand(SuccessDicePlugin plugin, String channel, String sender, String login, String hostname, CommandContext context) throws CommandException {
        super(plugin, channel, sender, login, hostname, context);
        switch(context.getCommand()) {
            case "roll":
                this.explodeOn = 10;
                break;
            case "roll9":
                this.explodeOn = 9;
                break;
            case "roll8":
                this.explodeOn = 8;
                break;
            default:
                this.explodeOn = 10;
                break;
        }
    }

    @Override
    protected void runCommand(@NotNull CommandContext commandContext) {
        try {
            String diceString = commandContext.getOriginalArgs();
            StringBuilder resultBuffer = new StringBuilder().append(getSender()).append(", ");

            String[] separateRolls = diceString.split(";");

            for (int i = 0; i < separateRolls.length; i++) {
                if (i > 0) {
                    resultBuffer.append("; ");
                }

                String separateRoll = separateRolls[i].trim();
                String[] rollDetails = separateRoll.split(" ", 2);
                if (rollDetails.length > 1) {
                    resultBuffer.append(rollDetails[1]).append(": ");
                } else {
                    if (rollDetails[0].isEmpty()) {
                        resultBuffer.append("+0: ");
                    } else {
                        resultBuffer.append(rollDetails[0]).append(": ");
                    }
                }

                String[] multiRoll = rollDetails[0].split("#", 2);
                if (multiRoll.length > 1) {
                    try {
                        int numRolls = Integer.parseInt(multiRoll[0]);
                        if (numRolls < 1) {
                            numRolls = 1;
                        } else if (numRolls > 100) {
                            throw new IllegalArgumentException("You may not roll more than 100 dice!");
                        }
                        for (int j = 0; j < numRolls; j++) {
                            if (j > 0) {
                                resultBuffer.append(", ");
                            }
                            resultBuffer = rollAndAppendResults(multiRoll[1], resultBuffer);
                        }
                    } catch (NumberFormatException e) {
                        resultBuffer = rollAndAppendResults(multiRoll[1], resultBuffer);
                    }
                } else {
                    resultBuffer = rollAndAppendResults(multiRoll[0], resultBuffer);
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

    private StringBuilder rollAndAppendResults(@NotNull String rollString, @NotNull StringBuilder buffer) {
        double rollResult = getPlugin().getDiceCache().getRoll(10);
        int rollTotal = (int) rollResult;
        int explodeTimes = 0;
        if (rollResult >= explodeOn) {
            do {
                rollResult = getPlugin().getDiceCache().getRoll(10);
                rollTotal += rollResult;
                explodeTimes++;
            } while (rollResult >= explodeOn);
        }
        rollResult = getPlugin().getDiceEvaluator().evaluate(rollTotal + rollString);

        int successes = SuccessCommand.countSuccesses(rollResult);
        buffer.append(com.dumptruckman.dircbot.mathdice.RollCommand.FORMAT.format(rollResult)).append(" - ");
        if (successes < 1) {
            buffer.append("No Success");
        } else if (successes == 1) {
            buffer.append("1 Success");
        } else {
            buffer.append(successes).append(" Successes");
        }
        if (explodeTimes == 1) {
            buffer.append(" (Exploded 1 time!)");
        } else if (explodeTimes > 1) {
            buffer.append(" (Exploded ").append(explodeTimes).append(" times!)");
        }
        return buffer;
    }
}
