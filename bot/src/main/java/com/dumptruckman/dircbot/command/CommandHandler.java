package com.dumptruckman.dircbot.command;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.plugin.Plugin;
import com.dumptruckman.dircbot.plugin.PluginCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class CommandHandler {

    protected final DircBot bot;
    private final CommandLoader builtInCommandLoader = new DefaultCommandLoader(null);
    protected final Map<String, Class<? extends Command>> builtInCommandMap = new HashMap<>();
    protected final Map<String, Class<? extends Command>> commandAliasMap = new HashMap<>();
    protected final Map<Class<? extends Command>, CommandLoader> commandLoaderMap = new HashMap<>();
    protected final Map<Plugin, Set<Class<? extends Command>>> pluginCommandsMap = new HashMap<>();

    protected CommandHandler(@NotNull final DircBot bot) {
        this.bot = bot;
    }

    public void processCommand(@Nullable String channel, @NotNull String sender, @NotNull String login, @NotNull String hostname, @NotNull String commandString) throws CommandException {
        CommandContext context = new CommandContext(commandString);
        Class<? extends Command> commandClass = builtInCommandMap.get(context.getCommand());
        if (commandClass == null) {
            commandClass = commandAliasMap.get(context.getCommand());
        }
        if (commandClass != null) {
            CommandInfo commandInfo = commandClass.getAnnotation(CommandInfo.class);
            if (commandInfo.isAdminRequired() && !bot.isAdministrator(login, hostname)) {
                throw new IllegalCommandAccessException(context.getCommand(), "You are not authorized to use this command.");
            }
            CommandLoader commandLoader = commandLoaderMap.get(commandClass);
            Command command = commandLoader.createCommandInstance(commandClass, bot, channel, sender, login, hostname, context);
            command.runCommand(context);
        } else {
            throw new CommandNotFoundException(context.getCommand(), "I don't know of a command named '" + context.getCommand() + "'.");
        }
    }

    protected void registerBuiltInCommand(Class<? extends Command> commandClass) {
        CommandInfo commandInfo = commandClass.getAnnotation(CommandInfo.class);

        if (commandInfo == null) {
            throw new IllegalArgumentException("The class " + commandClass + " does not have the CommandInfo annotation.");
        }

        for (String alias : commandInfo.aliases()) {
            builtInCommandMap.put(alias, commandClass);
        }
        commandLoaderMap.put(commandClass, builtInCommandLoader);
    }

    public void registerCommand(@NotNull Plugin plugin, @NotNull Class<? extends PluginCommand> commandClass) {
        registerCommand(plugin, commandClass, new DefaultCommandLoader(plugin));
    }

    public void registerCommand(@NotNull Plugin plugin, @NotNull Class<? extends PluginCommand> commandClass, @NotNull CommandLoader commandLoader) {
        CommandInfo commandInfo = commandClass.getAnnotation(CommandInfo.class);

        if (commandInfo == null) {
            throw new IllegalArgumentException("The class " + commandClass + " does not have the CommandInfo annotation.");
        }

        String pluginName = getPluginName(plugin);
        for (String alias : commandInfo.aliases()) {
            commandAliasMap.put(alias, commandClass);
            commandAliasMap.put(pluginName + "." + alias, commandClass);
        }
        Set<Class<? extends Command>> pluginCommands = pluginCommandsMap.get(plugin);
        if (pluginCommands == null) {
            pluginCommands = new HashSet<>();
            pluginCommandsMap.put(plugin, pluginCommands);
        }
        pluginCommands.add(commandClass);

        commandLoaderMap.put(commandClass, commandLoader);
    }

    private String getPluginName(@NotNull Plugin plugin) {
        StringBuilder pluginName = new StringBuilder();
        for (String s : plugin.getName().split("\\s")) {
            pluginName.append(s.toLowerCase());
        }
        return pluginName.toString();
    }
}
