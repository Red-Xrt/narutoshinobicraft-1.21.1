package narutoshinobicraft.common.commands.subcommands;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import narutoshinobicraft.common.commands.interfaces.ICommands;
import narutoshinobicraft.common.data.attachments.PlayersChakra;
import narutoshinobicraft.common.network.payloads.SyncChakraPayload;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import narutoshinobicraft.common.registry.AttachmentRegistry;

@SuppressWarnings("null")
public class SetCharka implements ICommands {
    @Override
    public String getName(){
        return "setcharka";
    }
    @Override
    public ArgumentBuilder<CommandSourceStack, ?> build() {
        return Commands.literal(getName())
            .then(Commands.argument("target", EntityArgument.player())
                .then(Commands.argument("amount", IntegerArgumentType.integer(1, 9999999))
                    .executes(context -> {
                        return executesLogic(context);
                    })
                )
        );
    }

    private int executesLogic(CommandContext<CommandSourceStack> context) throws CommandSyntaxException{
        CommandSourceStack source = context.getSource();
        ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "target");
        int amount = IntegerArgumentType.getInteger(context, "amount");

        PlayersChakra newChakra = targetPlayer.getData(AttachmentRegistry.PLAYER_CHAKRA);
        newChakra.setCharkaMax(amount);
        newChakra.setCurrentChakra(amount);
        targetPlayer.setData(AttachmentRegistry.PLAYER_CHAKRA, newChakra);
        
        PacketDistributor.sendToPlayer(targetPlayer, new SyncChakraPayload(newChakra));
        source.sendSuccess(() -> Component.literal("Gived " + amount + " Chakra to " + targetPlayer.getScoreboardName()), true);
        
        return 1;
    }
}
