package com.dumptruckman.dircbot;

import com.dumptruckman.dircbot.commands.AlternateRollCommand;
import com.dumptruckman.dircbot.commands.ChanceCommand;
import com.dumptruckman.dircbot.commands.RollCommand;
import com.dumptruckman.dircbot.commands.SuccessCommand;
import com.dumptruckman.dircbot.util.DiceCache;
import com.dumptruckman.dircbot.util.DiceEvaluator;
import org.jetbrains.annotations.NotNull;
import org.jibble.pircbot.PircBot;

import java.util.HashSet;

public class DIRCBot extends PircBot {

    private final DiceCache diceCache = new DiceCache(this);
    private final DiceEvaluator diceEvaluator = new DiceEvaluator(this);

    private String password;

    public static void main(String[] args) throws Exception {
        String[] argsNew = new String[args.length + 1];
        argsNew[0] = "dircbot";
        System.arraycopy(args, 0, argsNew, 1, args.length);
        CommandContext startupContext = new CommandContext(argsNew,
                new HashSet<Character>() {{
                    add('n');
                    add('c');
                }});

        DIRCBot bot = new DIRCBot();
        bot.setVersion("dev-SNAPSHOT");

        try {
            bot.setName(startupContext.getFlag('n', "DircBot"));
            bot.setVerbose(startupContext.hasFlag('v'));
            bot.password = startupContext.getFlag('p', null);

            bot.connect(startupContext.getString(0));
            if (startupContext.hasFlag('c')) {
                String[] channels = startupContext.getFlag('c').split(",");
                for (String channel : channels) {
                    bot.joinChannel(channel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        onMessage(null, sender, login, hostname, message);
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.startsWith("!") && message.length() > 1) {
            processCommand(channel, sender, login, hostname, message.substring(1));
        } else if (message.startsWith(".") && message.length() > 1) {
            processAlternateCommand(channel, sender, login, hostname, message.substring(1));
        } else if (RollCommand.isDice(message)) {
            try {
                ((Command) new RollCommand(this, channel, sender, login, hostname, new CommandContext(message))).runCommand();
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

    private void processAlternateCommand(String channel, String sender, String login, String hostname, String message) {
        try {
            CommandContext context = new CommandContext(message);
            switch (context.getCommand()) {
                case "roll":
                    ((Command) new AlternateRollCommand(this, channel, sender, login, hostname, context, 10)).runCommand();
                    break;
                case "roll9":
                    ((Command) new AlternateRollCommand(this, channel, sender, login, hostname, context, 9)).runCommand();
                    break;
                case "roll8":
                    ((Command) new AlternateRollCommand(this, channel, sender, login, hostname, context, 8)).runCommand();
                    break;
                case "success":
                    ((Command) new SuccessCommand(this, channel, sender, login, hostname, context)).runCommand();
                    break;
                case "chance":
                    ((Command) new ChanceCommand(this, channel, sender, login, hostname, context)).runCommand();
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

    public boolean isCorrectPassword(@NotNull String password) {
        return this.password.equals(password);
    }
}
