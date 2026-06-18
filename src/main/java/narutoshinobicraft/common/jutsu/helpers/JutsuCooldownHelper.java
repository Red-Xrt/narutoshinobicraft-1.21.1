package narutoshinobicraft.common.jutsu.helpers;

import narutoshinobicraft.common.data.attachments.PlayerProcess;
import narutoshinobicraft.common.registry.AttachmentRegistry;
import net.minecraft.world.entity.player.Player;

@SuppressWarnings("null")
public final class JutsuCooldownHelper {
    private JutsuCooldownHelper() {}

    public static double modifiedCooldown(double cooldown, Player player) {
        if (player == null) {
            return cooldown;
        }
        PlayerProcess process = player.getData(AttachmentRegistry.PLAYER_PROCESS);
        return cooldown * JutsuChargeModifiers.cooldownModifier(process.getNinjaLevel());
    }
}
