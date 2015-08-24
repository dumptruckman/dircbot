package com.dumptruckman.dircbot;

import com.dumptruckman.dircbot.commands.RollCommand;
import com.dumptruckman.dircbot.util.DiceCache;
import com.dumptruckman.dircbot.util.DiceEvaluator;
import org.jetbrains.annotations.NotNull;
import org.jibble.pircbot.PircBot;

public class DIRCBot extends PircBot {

    private final DiceCache diceCache = new DiceCache(this);
    private final DiceEvaluator diceEvaluator = new DiceEvaluator(this);

    public DIRCBot(@NotNull String name) {
        setName(name);
    }

    public static void main(String[] args) throws Exception {

        DIRCBot bot = new DIRCBot("dtmbot");

        bot.setVerbose(true);

        bot.connect("irc.synirc.net");

        bot.joinChannel("#bottesting");
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        onMessage(null, sender, login, hostname, message);
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.startsWith("!") && message.length() > 1) {
            processCommand(channel, sender, login, hostname, message.substring(1));
        }
    }

    private void processCommand(String channel, String sender, String login, String hostname, String message) {
        try {
            CommandContext context = new CommandContext(message);
            switch (context.getCommand()) {
                case "roll":
                    ((Command) new RollCommand(this, channel, sender, login, hostname, context)).runCommand();
                    break;
                default:
                    throw new CommandException("I don't know of a command named '" + context.getCommand() + "'.");
            }
        } catch (CommandException e) {
            if (e.getCause() != null) {
                e.printStackTrace();
            }
            if (channel == null) {
                channel = sender;
            }
            sendMessage(channel, e.getMessage());
        }
    }

    public DiceCache getDiceCache() {
        return diceCache;
    }

    public DiceEvaluator getDiceEvaluator() {
        return diceEvaluator;
    }

    @Override
    protected void onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel) {
        if (targetNick.equals(getNick()) && sourceNick.equals("dumptruckman")) {
            joinChannel(channel);
        }
    }
}
