package com.dumptruckman.dircbot.command;

import com.dumptruckman.dircbot.DircBot;
import com.dumptruckman.dircbot.command.CommandContext;
import com.dumptruckman.dircbot.command.CommandException;
import com.dumptruckman.dircbot.command.CommandLoader;
import com.dumptruckman.dircbot.plugin.Plugin;
import com.dumptruckman.dircbot.plugin.PluginCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class DefaultCommandLoader implements CommandLoader {

    private final Plugin plugin;

    public DefaultCommandLoader(@Nullable Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public <C extends Command> C createCommandInstance(@NotNull Class<C> commandClass, @NotNull DircBot bot, @Nullable String channel, @NotNull String sender, @NotNull String login, @NotNull String hostname, @NotNull CommandContext commandContext) throws CommandException {
        try {
            Constructor<C> constructor;
            if (PluginCommand.class.isAssignableFrom(commandClass) && plugin != null) {
                constructor = commandClass.getConstructor(plugin.getClass(), String.class, String.class, String.class, String.class, CommandContext.class);
                return constructor.newInstance(plugin, channel, sender, login, hostname, commandContext);
            } else {
                constructor = commandClass.getConstructor(DircBot.class, String.class, String.class, String.class, String.class, CommandContext.class);
                return constructor.newInstance(bot, channel, sender, login, hostname, commandContext);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            throw new CommandException("There was a problem running that command.", e);
        }
    }
}
