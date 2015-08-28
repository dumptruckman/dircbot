package com.dumptruckman.dircbot;

import com.dumptruckman.dircbot.commands.AlternateRollCommand;
import com.dumptruckman.dircbot.commands.ChanceCommand;
import com.dumptruckman.dircbot.commands.HelpCommand;
import com.dumptruckman.dircbot.commands.JoinCommand;
import com.dumptruckman.dircbot.commands.KillCommand;
import com.dumptruckman.dircbot.commands.LmgtfyCommand;
import com.dumptruckman.dircbot.commands.LoginCommand;
import com.dumptruckman.dircbot.commands.PartCommand;
import com.dumptruckman.dircbot.commands.RollCommand;
import com.dumptruckman.dircbot.commands.SuccessCommand;
import com.dumptruckman.dircbot.util.DiceCache;
import com.dumptruckman.dircbot.util.DiceEvaluator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import java.util.HashSet;
import java.util.Set;

public class DIRCBot extends PircBot {

    private final DiceCache diceCache = new DiceCache(this);
    private final DiceEvaluator diceEvaluator = new DiceEvaluator(this);

    private String password = "";
    private final Set<String> administrators = new HashSet<>();

    private boolean kill = false;
    private boolean freeJoin = false;
    private boolean freeRoll = true;
    private boolean successDiceMode = false;

    public static void main(String[] args) throws Exception {
        String[] argsNew = new String[args.length + 1];
        argsNew[0] = "dircbot";
        System.arraycopy(args, 0, argsNew, 1, args.length);
        CommandContext startupContext = new CommandContext(argsNew,
                new HashSet<Character>() {{
                    add('n'); // nickname
                    add('c'); // channels, comma separated
                    add('p'); // bot administrator password
                }});

        DIRCBot bot = new DIRCBot();
        bot.setVersion("1.0-SNAPSHOT");

        try {
            String nick = startupContext.getFlag('n', "DircBot");
            bot.setName(nick);
            bot.setLogin(nick.length() >= 9 ? nick.substring(0, 8) : nick);
            bot.setVerbose(startupContext.hasFlag('v'));
            bot.password = startupContext.getFlag('p', "");
            bot.freeJoin = startupContext.hasFlag('j');
            bot.freeRoll = !startupContext.hasFlag('R');
            bot.successDiceMode = startupContext.hasFlag('s');

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
        try {
            if (message.startsWith("!") && message.length() > 1) {
                processCommand(null, sender, login, hostname, message.substring(1));
            } else {
                processCommand(null, sender, login, hostname, message);
            }
        } catch (CommandException e) {
            if (!message.startsWith("!")) {
                message = "!" + message;
            }
            onMessage(null, sender, login, hostname, message);
        }
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        try {
            if (message.startsWith("!") && message.length() > 1) {
                processCommand(channel, sender, login, hostname, message.substring(1));
            } else if (freeRoll && RollCommand.isDice(message)) {
                ((Command) new RollCommand(this, channel, sender, login, hostname, new CommandContext(message))).runCommand();
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

    private void processCommand(@Nullable String channel, String sender, String login, String hostname, String message) throws CommandException {
        CommandContext context = new CommandContext(message);
        switch (context.getCommand()) {
            case "lmgtfy":
            case "google":
                ((Command) new LmgtfyCommand(this, channel, sender, login, hostname, context)).runCommand();
                break;
            case "roll":
                if (successDiceMode) {
                    ((Command) new AlternateRollCommand(this, channel, sender, login, hostname, context, 10)).runCommand();
                } else {
                    ((Command) new RollCommand(this, channel, sender, login, hostname, context)).runCommand();
                }
                break;
            case "roll9":
                if (successDiceMode) {
                    ((Command) new AlternateRollCommand(this, channel, sender, login, hostname, context, 9)).runCommand();
                }
                break;
            case "roll8":
                if (successDiceMode) {
                    ((Command) new AlternateRollCommand(this, channel, sender, login, hostname, context, 8)).runCommand();
                }
                break;
            case "success":
                if (successDiceMode) {
                    ((Command) new SuccessCommand(this, channel, sender, login, hostname, context)).runCommand();
                }
                break;
            case "chance":
                if (successDiceMode) {
                    ((Command) new ChanceCommand(this, channel, sender, login, hostname, context)).runCommand();
                }
                break;
            case "kill":
                if (channel == null) {
                    ((Command) new KillCommand(this, null, sender, login, hostname, context)).runCommand();
                }
                break;
            case "help":
                if (channel == null) {
                    ((Command) new HelpCommand(this, null, sender, login, hostname, context)).runCommand();
                }
                break;
            case "login":
                if (channel == null) {
                    ((Command) new LoginCommand(this, null, sender, login, hostname, context)).runCommand();
                }
                break;
            case "join":
                if (channel == null) {
                    ((Command) new JoinCommand(this, null, sender, login, hostname, context)).runCommand();
                }
                break;
            case "part":
                if (channel == null) {
                    ((Command) new PartCommand(this, null, sender, login, hostname, context)).runCommand();
                }
                break;
            default:
                if (channel == null) {
                    throw new CommandException("I don't know of a command named '" + context.getCommand() + "'.");
                }
                break;
        }
    }

    public DiceCache getDiceCache() {
        return diceCache;
    }

    public DiceEvaluator getDiceEvaluator() {
        return diceEvaluator;
    }

    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        if (isAdministrator(login, hostname)) {
            if (!isNickInChannels(sender)) {
                removeAdministrator(login, hostname);
            }
        }
    }

    @Override
    protected void onDisconnect() {
        if (kill) {
            System.exit(0);
        }
    }

    @Override
    protected void onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel) {
        try {
            ((Command) new JoinCommand(this, null, sourceNick, sourceLogin, sourceHostname, new CommandContext("join " + channel))).runCommand();
        } catch (CommandException ignore) { }
    }

    public void loginAdministrator(@NotNull String nick, @NotNull String login, @NotNull String hostname, @NotNull String password, @Nullable String channelToJoin) {
        if (isAdministrator(login, hostname)) {
            throw new IllegalStateException("You are already logged in!");
        }
        if (!password.equals(this.password)) {
            throw new IllegalArgumentException("Invalid password!");
        }
        if (channelToJoin == null && !isNickInChannels(nick)) {
            throw new IllegalStateException("You must be in a channel with me to login. You can specify a channel for me to join as part of this command.");
        } else if (channelToJoin != null && !isNickInChannel(nick, channelToJoin)) {
            throw new IllegalStateException("You must be in a channel with me to login. You aren't in the channel specified.");
        }
        if (channelToJoin != null) {
            joinChannel(channelToJoin);
            if (!isInChannel(channelToJoin)) {
                throw new IllegalStateException("I couldn't join that channel!");
            }
        }
        addAdministrator(login, hostname);
    }

    private boolean isNickInChannels(@NotNull String nick) {
        for (String channel : getChannels()) {
            if (isNickInChannel(nick, channel)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNickInChannel(@NotNull String nick, @NotNull String channel) {
        for (User user : getUsers(channel)) {
            if (user.getNick().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInChannel(@NotNull String channel) {
        for (String c : getChannels()) {
            if (c.equals(channel)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdministrator(@NotNull String login, @NotNull String hostname) {
        return administrators.contains(login + "@" + hostname);
    }

    private void removeAdministrator(@NotNull String login, @NotNull String hostname) {
        String adminName = login + "@" + hostname;
        administrators.remove(adminName);
        log("Removed administrator: " + adminName);
    }

    private void addAdministrator(@NotNull String login, @NotNull String hostname) {
        String adminName = login + "@" + hostname;
        administrators.add(adminName);
        log("Added administrator: " + adminName);
    }

    public void kill() {
        kill = true;
        disconnect();
    }

    public boolean isFreeJoinEnabled() {
        return freeJoin;
    }

    public boolean isSuccessDiceModeEnabled() {
        return successDiceMode;
    }

    public boolean isFreeRollEnabled() {
        return freeRoll;
    }
}
