package com.dumptruckman.dircbot.successdice;

import com.dumptruckman.dircbot.command.CommandInfo;
import com.dumptruckman.dircbot.mathdice.DiceCache;
import com.dumptruckman.dircbot.mathdice.DiceEvaluator;
import com.dumptruckman.dircbot.mathdice.MathDicePlugin;
import com.dumptruckman.dircbot.plugin.java.JavaPlugin;

public class SuccessDicePlugin extends JavaPlugin {

    private MathDicePlugin mathDicePlugin = null;

    @Override
    public void onEnable() {
        mathDicePlugin = (MathDicePlugin) getBot().getPluginManager().getPlugin("MathDice");

        getBot().getCommandHandler().registerCommand(this, RollCommand.class);
        getBot().getCommandHandler().registerCommand(this, ChanceCommand.class);
        getBot().getCommandHandler().registerCommand(this, SuccessCommand.class);
    }

    public DiceEvaluator getDiceEvaluator() {
        return mathDicePlugin.getDiceEvaluator();
    }

    public DiceCache getDiceCache() {
        return mathDicePlugin.getDiceCache();
    }
}
