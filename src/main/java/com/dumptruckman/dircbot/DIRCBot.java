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
import com.dumptruckman.dircbot.event.irc.ActionEvent;
import com.dumptruckman.dircbot.event.irc.ChannelInfoEvent;
import com.dumptruckman.dircbot.event.irc.ConnectEvent;
import com.dumptruckman.dircbot.event.irc.DccChatRequestEvent;
import com.dumptruckman.dircbot.event.irc.DccSendRequestEvent;
import com.dumptruckman.dircbot.event.irc.DeOpEvent;
import com.dumptruckman.dircbot.event.irc.DeVoiceEvent;
import com.dumptruckman.dircbot.event.irc.DisconnectEvent;
import com.dumptruckman.dircbot.event.Events;
import com.dumptruckman.dircbot.event.irc.FileTransferFinishedEvent;
import com.dumptruckman.dircbot.event.irc.FingerEvent;
import com.dumptruckman.dircbot.event.irc.IncomingChatRequestEvent;
import com.dumptruckman.dircbot.event.irc.IncomingFileTransferEvent;
import com.dumptruckman.dircbot.event.irc.InviteEvent;
import com.dumptruckman.dircbot.event.irc.JoinEvent;
import com.dumptruckman.dircbot.event.irc.KickEvent;
import com.dumptruckman.dircbot.event.irc.MessageEvent;
import com.dumptruckman.dircbot.event.irc.ModeEvent;
import com.dumptruckman.dircbot.event.irc.NickChangeEvent;
import com.dumptruckman.dircbot.event.irc.NoticeEvent;
import com.dumptruckman.dircbot.event.irc.OpEvent;
import com.dumptruckman.dircbot.event.irc.PartEvent;
import com.dumptruckman.dircbot.event.irc.PingEvent;
import com.dumptruckman.dircbot.event.irc.PrivateMessageEvent;
import com.dumptruckman.dircbot.event.irc.QuitEvent;
import com.dumptruckman.dircbot.event.irc.RemoveChannelBanEvent;
import com.dumptruckman.dircbot.event.irc.RemoveChannelKeyEvent;
import com.dumptruckman.dircbot.event.irc.RemoveChannelLimitEvent;
import com.dumptruckman.dircbot.event.irc.RemoveInviteOnlyEvent;
import com.dumptruckman.dircbot.event.irc.RemoveModeratedEvent;
import com.dumptruckman.dircbot.event.irc.RemoveNoExternalMessagesEvent;
import com.dumptruckman.dircbot.event.irc.RemovePrivateEvent;
import com.dumptruckman.dircbot.event.irc.RemoveSecretEvent;
import com.dumptruckman.dircbot.event.irc.RemoveTopicProtectionEvent;
import com.dumptruckman.dircbot.event.irc.ServerPingEvent;
import com.dumptruckman.dircbot.event.irc.ServerResponseEvent;
import com.dumptruckman.dircbot.event.irc.SetChannelBanEvent;
import com.dumptruckman.dircbot.event.irc.SetChannelKeyEvent;
import com.dumptruckman.dircbot.event.irc.SetChannelLimitEvent;
import com.dumptruckman.dircbot.event.irc.SetInviteOnlyEvent;
import com.dumptruckman.dircbot.event.irc.SetModeratedEvent;
import com.dumptruckman.dircbot.event.irc.SetNoExternalMessagesEvent;
import com.dumptruckman.dircbot.event.irc.SetPrivateEvent;
import com.dumptruckman.dircbot.event.irc.SetSecretEvent;
import com.dumptruckman.dircbot.event.irc.SetTopicProtectionEvent;
import com.dumptruckman.dircbot.event.irc.TimeEvent;
import com.dumptruckman.dircbot.event.irc.TopicEvent;
import com.dumptruckman.dircbot.event.irc.UnknownEvent;
import com.dumptruckman.dircbot.event.irc.UserListEvent;
import com.dumptruckman.dircbot.event.irc.UserModeEvent;
import com.dumptruckman.dircbot.event.irc.VersionEvent;
import com.dumptruckman.dircbot.event.irc.VoiceEvent;
import com.dumptruckman.dircbot.plugin.PluginManager;
import com.dumptruckman.dircbot.util.DiceCache;
import com.dumptruckman.dircbot.util.DiceEvaluator;
import com.dumptruckman.dircbot.util.LoggerOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jibble.pircbot.DccChat;
import org.jibble.pircbot.DccFileTransfer;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class DircBot extends PircBot {

    private final Logger logger = Logger.getLogger("DircBot");

    private final DiceCache diceCache = new DiceCache(this);
    private final DiceEvaluator diceEvaluator = new DiceEvaluator(this);

    private String password = "";
    private final Set<String> administrators = new HashSet<>();

    private final Map<String, Boolean> nicksToCheckIdentification = new HashMap<>();
    private final Set<String> registeredUsers = new HashSet<>();

    private boolean kill = false;
    private boolean freeJoin = false;
    private boolean freeRoll = true;
    private boolean successDiceMode = false;

    private PluginManager pluginManager;

    public static void main(String[] args) throws Exception {
        // CraftBukkit start
        System.setOut(new PrintStream(new LoggerOutputStream(this.getLogger().getLogger(), Level.INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(this.getLogger().getLogger(), Level.SEVERE), true));
        // CraftBukkit end
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
        String[] argsNew = new String[args.length + 1];
        argsNew[0] = "dircbot";
        System.arraycopy(args, 0, argsNew, 1, args.length);
        CommandContext startupContext = new CommandContext(argsNew,
                new HashSet<Character>() {{
                    add('n'); // nickname
                    add('c'); // channels, comma separated
                    add('p'); // bot administrator password
                }});

        DircBot bot = new DircBot();
        Bot.setInstance(bot);
        bot.setVersion("1.0-SNAPSHOT");
        Bot.getLogger().info("TEST!!!!");

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

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public Logger getLogger() {
        return logger;
    }

    @Override
    protected void onPrivateMessage(String sender, String login, String hostname, String message) {
        Events.callEvent(new PrivateMessageEvent(this, sender, login, hostname, message));
        if (sender.equalsIgnoreCase("NickServ")) {
            onNickServMessage(message);
        }
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
        Events.callEvent(new MessageEvent(this, sender, login, hostname, channel, message));
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
        Events.callEvent(new PartEvent(this, sender, login, hostname, channel));
        if (!isNickInChannels(sender)) {
            if (isAdministrator(login, hostname)) {
                removeAdministrator(login, hostname);
            }
            removeRegisteredUser(login, hostname);
        }
    }

    @Override
    protected void onDisconnect() {
        DisconnectEvent event = new DisconnectEvent(this, kill);
        Events.callEvent(event);
        if (event.isTerminatingProcess()) {
            System.exit(0);
        }
    }

    @Override
    protected void onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel) {
        Events.callEvent(new InviteEvent(this, sourceNick, sourceLogin, sourceHostname, channel, targetNick));
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
        if (administrators.remove(adminName)) {
            log("Removed administrator: " + adminName);
        }
    }

    private void addAdministrator(@NotNull String login, @NotNull String hostname) {
        String adminName = login + "@" + hostname;
        if (administrators.add(adminName)) {
            log("Added administrator: " + adminName);
        }
    }

    public boolean isNickIdentified(@NotNull String nick, @NotNull String login, @NotNull String hostname) {
        if (registeredUsers.contains(login + "@" + hostname)) {
            return true;
        }
        nicksToCheckIdentification.put(nick, false);
        sendMessage("NickServ", "status " + nick);
        try {
            Thread.sleep(300); // TODO figure out a better method...
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            if (nicksToCheckIdentification.get(nick)) {
                addRegisteredUser(login, hostname);
                return true;
            }
            return false;
        } finally {
            nicksToCheckIdentification.remove(nick);
        }
    }

    private void onNickServMessage(@NotNull String message) {
        if (message.startsWith("(notice) STATUS") && message.length() > 17) {
            String[] status = message.substring(16).split(" ");
            if (nicksToCheckIdentification.containsKey(status[0]) && status[1].equals("3")) {
                nicksToCheckIdentification.put(status[0], true);
                log("Verified " + status[0] + "'s identity.");
            }
        }
    }

    private void addRegisteredUser(@NotNull String login, @NotNull String hostname) {
        String userName = login + "@" + hostname;
        if (registeredUsers.add(userName)) {
            log("Added registered user: " + userName);
        }
    }

    private void removeRegisteredUser(@NotNull String login, @NotNull String hostname) {
        String userName = login + "@" + hostname;
        if (registeredUsers.remove(userName)) {
            log("Removed registered user: " + userName);
        }
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

    @Override
    protected void onConnect() {
        Events.callEvent(new ConnectEvent(this));
    }

    @Override
    protected void onServerResponse(int code, String response) {
        Events.callEvent(new ServerResponseEvent(this, code, response));
    }

    @Override
    protected void onUserList(String channel, User[] users) {
        Events.callEvent(new UserListEvent(this, channel, users));
    }

    @Override
    protected void onAction(String sender, String login, String hostname, String target, String action) {
        Events.callEvent(new ActionEvent(this, sender, login, hostname, target, action));
    }

    @Override
    protected void onNotice(String sourceNick, String sourceLogin, String sourceHostname, String target, String notice) {
        Events.callEvent(new NoticeEvent(this, sourceNick, sourceLogin, sourceHostname, target, notice));
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        Events.callEvent(new JoinEvent(this, sender, login, hostname, channel));
    }

    @Override
    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        Events.callEvent(new NickChangeEvent(this, oldNick, login, hostname, newNick));
    }

    @Override
    protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
        Events.callEvent(new KickEvent(this, kickerNick, kickerLogin, kickerHostname, channel, recipientNick, reason));
    }

    @Override
    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        Events.callEvent(new QuitEvent(this, sourceNick, sourceLogin, sourceHostname, reason));
    }

    @Override
    protected void onTopic(String channel, String topic, String setBy, long date, boolean changed) {
        Events.callEvent(new TopicEvent(this, channel, topic, setBy, date, changed));
    }

    @Override
    protected void onChannelInfo(String channel, int userCount, String topic) {
        Events.callEvent(new ChannelInfoEvent(this, channel, topic, userCount));
    }

    @Override
    protected void onMode(String channel, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
        Events.callEvent(new ModeEvent(this, sourceNick, sourceLogin, sourceHostname, channel, mode));
    }

    @Override
    protected void onUserMode(String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String mode) {
        Events.callEvent(new UserModeEvent(this, sourceNick, sourceLogin, sourceHostname, targetNick, mode));
    }

    @Override
    protected void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        Events.callEvent(new OpEvent(this, sourceNick, sourceLogin, sourceHostname, channel, recipient));
    }

    @Override
    protected void onDeop(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        Events.callEvent(new DeOpEvent(this, sourceNick, sourceLogin, sourceHostname, channel, recipient));
    }

    @Override
    protected void onVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        Events.callEvent(new VoiceEvent(this, sourceNick, sourceLogin, sourceHostname, channel, recipient));
    }

    @Override
    protected void onDeVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
        Events.callEvent(new DeVoiceEvent(this, sourceNick, sourceLogin, sourceHostname, channel, recipient));
    }

    @Override
    protected void onSetChannelKey(String channel, String sourceNick, String sourceLogin, String sourceHostname, String key) {
        Events.callEvent(new SetChannelKeyEvent(this, sourceNick, sourceLogin, sourceHostname, channel, key));
    }

    @Override
    protected void onRemoveChannelKey(String channel, String sourceNick, String sourceLogin, String sourceHostname, String key) {
        Events.callEvent(new RemoveChannelKeyEvent(this, sourceNick, sourceLogin, sourceHostname, channel, key));
    }

    @Override
    protected void onSetChannelLimit(String channel, String sourceNick, String sourceLogin, String sourceHostname, int limit) {
        Events.callEvent(new SetChannelLimitEvent(this, sourceNick, sourceLogin, sourceHostname, channel, limit));
    }

    @Override
    protected void onRemoveChannelLimit(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        Events.callEvent(new RemoveChannelLimitEvent(this, sourceNick, sourceLogin, sourceHostname, channel));
    }

    @Override
    protected void onSetChannelBan(String channel, String sourceNick, String sourceLogin, String sourceHostname, String hostmask) {
        Events.callEvent(new SetChannelBanEvent(this, sourceNick, sourceLogin, sourceHostname, channel, hostmask));
    }

    @Override
    protected void onRemoveChannelBan(String channel, String sourceNick, String sourceLogin, String sourceHostname, String hostmask) {
        Events.callEvent(new RemoveChannelBanEvent(this, sourceNick, sourceLogin, sourceHostname, channel, hostmask));
    }

    @Override
    protected void onSetTopicProtection(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        Events.callEvent(new SetTopicProtectionEvent(this, sourceNick, sourceLogin, sourceHostname, channel));
    }

    @Override
    protected void onRemoveTopicProtection(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        Events.callEvent(new RemoveTopicProtectionEvent(this, sourceNick, sourceLogin, sourceHostname, channel));
    }

    @Override
    protected void onSetNoExternalMessages(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        Events.callEvent(new SetNoExternalMessagesEvent(this, sourceNick, sourceLogin, sourceHostname, channel));
    }

    @Override
    protected void onRemoveNoExternalMessages(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        Events.callEvent(new RemoveNoExternalMessagesEvent(this, sourceNick, sourceLogin, sourceHostname, channel));
    }

    @Override
    protected void onSetInviteOnly(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        Events.callEvent(new SetInviteOnlyEvent(this, sourceNick, sourceLogin, sourceHostname, channel));
    }

    @Override
    protected void onRemoveInviteOnly(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        Events.callEvent(new RemoveInviteOnlyEvent(this, sourceNick, sourceLogin, sourceHostname, channel));
    }

    @Override
    protected void onSetModerated(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        Events.callEvent(new SetModeratedEvent(this, sourceNick, sourceLogin, sourceHostname, channel));
    }

    @Override
    protected void onRemoveModerated(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        Events.callEvent(new RemoveModeratedEvent(this, sourceNick, sourceLogin, sourceHostname, channel));
    }

    @Override
    protected void onSetPrivate(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        Events.callEvent(new SetPrivateEvent(this, sourceNick, sourceLogin, sourceHostname, channel));
    }

    @Override
    protected void onRemovePrivate(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        Events.callEvent(new RemovePrivateEvent(this, sourceNick, sourceLogin, sourceHostname, channel));
    }

    @Override
    protected void onSetSecret(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        Events.callEvent(new SetSecretEvent(this, sourceNick, sourceLogin, sourceHostname, channel));
    }

    @Override
    protected void onRemoveSecret(String channel, String sourceNick, String sourceLogin, String sourceHostname) {
        Events.callEvent(new RemoveSecretEvent(this, sourceNick, sourceLogin, sourceHostname, channel));
    }

    @Override
    protected void onDccSendRequest(String sourceNick, String sourceLogin, String sourceHostname, String filename, long address, int port, int size) {
        Events.callEvent(new DccSendRequestEvent(this, sourceNick, sourceLogin, sourceHostname, filename, address, port, size));
    }

    @Override
    protected void onDccChatRequest(String sourceNick, String sourceLogin, String sourceHostname, long address, int port) {
        Events.callEvent(new DccChatRequestEvent(this, sourceNick, sourceLogin, sourceHostname, address, port));
    }

    @Override
    protected void onIncomingFileTransfer(DccFileTransfer transfer) {
        Events.callEvent(new IncomingFileTransferEvent(this, transfer));
    }

    @Override
    protected void onFileTransferFinished(DccFileTransfer transfer, Exception e) {
        Events.callEvent(new FileTransferFinishedEvent(this, transfer, e));
    }

    @Override
    protected void onIncomingChatRequest(DccChat chat) {
        Events.callEvent(new IncomingChatRequestEvent(this, chat));
    }

    @Override
    protected void onVersion(String sourceNick, String sourceLogin, String sourceHostname, String target) {
        Events.callEvent(new VersionEvent(this, sourceNick, sourceLogin, sourceHostname, target));
    }

    @Override
    protected void onPing(String sourceNick, String sourceLogin, String sourceHostname, String target, String pingValue) {
        Events.callEvent(new PingEvent(this, sourceNick, sourceLogin, sourceHostname, target, pingValue));
    }

    @Override
    protected void onServerPing(String response) {
        Events.callEvent(new ServerPingEvent(this, response));
    }

    @Override
    protected void onTime(String sourceNick, String sourceLogin, String sourceHostname, String target) {
        Events.callEvent(new TimeEvent(this, sourceNick, sourceLogin, sourceHostname, target));
    }

    @Override
    protected void onFinger(String sourceNick, String sourceLogin, String sourceHostname, String target) {
        Events.callEvent(new FingerEvent(this, sourceNick, sourceLogin, sourceHostname, target));
    }

    @Override
    protected void onUnknown(String line) {
        Events.callEvent(new UnknownEvent(this, line));
    }
}
