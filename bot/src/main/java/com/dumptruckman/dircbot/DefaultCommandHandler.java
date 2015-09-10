package com.dumptruckman.dircbot;

import com.dumptruckman.dircbot.command.Command;
import com.dumptruckman.dircbot.command.CommandHandler;
import com.dumptruckman.dircbot.command.CommandInfo;
import org.jetbrains.annotations.NotNull;

class DefaultCommandHandler extends CommandHandler {

    DefaultCommandHandler(@NotNull DircBot bot) {
        super(bot);
    }

    @Override
    protected void registerBuiltInCommand(Class<? extends Command> commandClass) {
        super.registerBuiltInCommand(commandClass);
    }
}
