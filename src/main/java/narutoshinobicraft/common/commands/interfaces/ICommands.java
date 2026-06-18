package narutoshinobicraft.common.commands.interfaces;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public interface ICommands {
    String getName();
    
    default int getPermissionLevel(){
        return 2;
    }

    ArgumentBuilder<CommandSourceStack, ?> build();
}
