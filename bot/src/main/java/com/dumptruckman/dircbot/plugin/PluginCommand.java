package com.dumptruckman.dircbot.plugin;

import com.dumptruckman.dircbot.command.Command;
import com.dumptruckman.dircbot.command.CommandContext;
import com.dumptruckman.dircbot.command.CommandException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PluginCommand<P extends Plugin> extends Command {

    private final P plugin;

    public PluginCommand(@NotNull P plugin, @Nullable String channel, @NotNull String sender, @NotNull String login, @NotNull String hostname, @NotNull CommandContext commandContext) throws CommandException {
        super(plugin.getBot(), channel, sender, login, hostname, commandContext);
        this.plugin = plugin;
    }

    public P getPlugin() {
        return plugin;
    }
}
