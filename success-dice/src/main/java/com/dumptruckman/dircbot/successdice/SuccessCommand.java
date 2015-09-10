package com.dumptruckman.dircbot.successdice;

import com.dumptruckman.dircbot.command.CommandContext;
import com.dumptruckman.dircbot.command.CommandException;
import com.dumptruckman.dircbot.command.CommandInfo;
import com.dumptruckman.dircbot.mathdice.MathDicePlugin;
import com.dumptruckman.dircbot.plugin.PluginCommand;
import org.jetbrains.annotations.NotNull;

@CommandInfo(aliases = "success")
public class SuccessCommand extends PluginCommand<MathDicePlugin> {

    static int countSuccesses(double roll) {
        return ((int) (Math.floor((roll - 8) / 3))) + 1;
    }

    public SuccessCommand(MathDicePlugin plugin, String channel, String sender, String login, String hostname, CommandContext context) throws CommandException {
        super(plugin, channel, sender, login, hostname, context);
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
