package narutoshinobicraft.common.commands;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import narutoshinobicraft.common.commands.interfaces.ICommands;
import narutoshinobicraft.common.commands.subcommands.SetBattleExp;
import narutoshinobicraft.common.commands.subcommands.SetCharka;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CommandsManager {
    private static final List<ICommands> SUB_COMMAND = new ArrayList<>();

    static{
        SUB_COMMAND.add(new SetCharka());
        SUB_COMMAND.add(new SetBattleExp());
    }


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> mainCommand = Commands.literal("narutomod").requires(source -> source.hasPermission(2));

        for (ICommands subCommand : SUB_COMMAND) {
            mainCommand.then(subCommand.build());
        }

        dispatcher.register(mainCommand);
    }
}
