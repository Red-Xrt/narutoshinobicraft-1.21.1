package narutoshinobicraft.common.jutsu.helpers;

import javax.annotation.Nullable;

import narutoshinobicraft.common.registry.AttachmentRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

@SuppressWarnings("null")
public class JutsuTarget {
    public static boolean canTarget(@Nullable Entity target){
        if (target == null || !target.isAlive()) {
            return false;
        }
        if (target instanceof Player player) {
            return !player.getData(AttachmentRegistry.PLAYER_VARIANTS).IsKamui();
        }
        return true;
    }
}

