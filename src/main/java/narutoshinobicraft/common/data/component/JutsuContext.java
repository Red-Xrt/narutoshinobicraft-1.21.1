package narutoshinobicraft.common.data.component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public record JutsuContext(
    Level level,
    Player player,
    ItemStack stack,
    JutsuStackState stackState,
    float power,
    ResourceLocation currentJutsuId
){
    public static final JutsuContext EMPTY_CONTEXT = new JutsuContext(null, null, null, JutsuStackState.EMPTY_JUTSU_STATE, 0.0f, null);
}
