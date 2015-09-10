package com.dumptruckman.dircbot.successdice;

import com.dumptruckman.dircbot.command.CommandContext;
import com.dumptruckman.dircbot.command.CommandException;
import com.dumptruckman.dircbot.command.CommandInfo;
import com.dumptruckman.dircbot.mathdice.MathDicePlugin;
import com.dumptruckman.dircbot.plugin.PluginCommand;
import org.jetbrains.annotations.NotNull;

@CommandInfo(aliases = "chance")
public class ChanceCommand extends PluginCommand<MathDicePlugin> {

    public ChanceCommand(MathDicePlugin plugin, String channel, String sender, String login, String hostname, CommandContext context) throws CommandException {
        super(plugin, channel, sender, login, hostname, context);
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
                    resultBuffer.append("chance: ");
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
        int rollResult = getPlugin().getDiceCache().getRoll(10);
        buffer.append(com.dumptruckman.dircbot.mathdice.RollCommand.FORMAT.format(rollResult)).append(" - ");
        if (rollResult == 10) {
            buffer.append("1 Success");
        } else {
            buffer.append("No Success");
        }
        return buffer;
    }
}
