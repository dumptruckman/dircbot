package com.dumptruckman.dircbot.mathdice;

import com.dumptruckman.dircbot.command.CommandContext;
import com.dumptruckman.dircbot.command.CommandException;
import com.dumptruckman.dircbot.command.IllegalCommandAccessException;
import com.dumptruckman.dircbot.event.EventHandler;
import com.dumptruckman.dircbot.event.Listener;
import com.dumptruckman.dircbot.event.bot.BotPreConnectEvent;
import com.dumptruckman.dircbot.event.irc.MessageEvent;
import com.dumptruckman.dircbot.event.irc.PrivateMessageEvent;
import com.dumptruckman.dircbot.plugin.java.JavaPlugin;

public class MathDicePlugin extends JavaPlugin implements Listener {

    private final DiceEvaluator diceEvaluator = new DiceEvaluator(this);
    private final DiceCache diceCache = new DiceCache(this);
    private boolean freeRoll = true;

    @Override
    public void onEnable() {
        getBot().getCommandHandler().registerCommand(this, RollCommand.class);
        getBot().getPluginManager().registerEvents(this, this);
    }

    public DiceEvaluator getDiceEvaluator() {
        return diceEvaluator;
    }

    public DiceCache getDiceCache() {
        return diceCache;
    }

    @EventHandler
    private void onBotPreConnect(BotPreConnectEvent event) {
        this.freeRoll = !event.getStartupContext().hasFlag('R');
    }

    @EventHandler
    private void onPrivateMessage(PrivateMessageEvent event) {
        if (freeRoll && RollCommand.isDice(event.getMessage())) {
            try {
                (new RollCommand(this, null, event.getSourceNick(), event.getSourceLogin(), event.getSourceHostname(), new CommandContext(event.getMessage()))).runCommand();
            } catch (CommandException e) {
                if (e.getCause() != null) {
                    e.printStackTrace();
                }
                if (!(e instanceof IllegalCommandAccessException)) {
                    getBot().sendMessage(event.getSourceNick(), e.getMessage());
                }
            }
        }
    }

    @EventHandler
    private void onMessage(MessageEvent event) {
        System.out.println("freeroll? " + freeRoll + " message: " + event.getMessage() + " isDice? " + RollCommand.isDice(event.getMessage()));

        if (freeRoll && RollCommand.isDice(event.getMessage())) {
            try {
                (new RollCommand(this, event.getChannel(), event.getSourceNick(), event.getSourceLogin(), event.getSourceHostname(), new CommandContext(event.getMessage()))).runCommand();
            } catch (CommandException e) {
                if (e.getCause() != null) {
                    e.printStackTrace();
                }
                if (!(e instanceof IllegalCommandAccessException)) {
                    getBot().sendMessage(event.getChannel(), e.getMessage());
                }
            }
        }
    }
}
