package narutoshinobicraft.common.jutsu.helpers.client;

import narutoshinobicraft.NarutoShinobiCraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@SuppressWarnings("null")
public final class ChakraVisualResolver {

    public static final ResourceLocation DEFAULT_AURA =
        ResourceLocation.fromNamespaceAndPath(NarutoShinobiCraft.MODID, "chakra_blue");

    public static final ResourceLocation BIJUU_AURA =
        ResourceLocation.fromNamespaceAndPath(NarutoShinobiCraft.MODID, "chakra_red");

    private ChakraVisualResolver() {}

    public static ResourceLocation scrollAuraFor(LivingEntity entity) {
        if (entity instanceof Player player && usesBijuuChakra(player)) {
            return BIJUU_AURA;
        }
        return DEFAULT_AURA;
    }

    private static boolean usesBijuuChakra(Player player) {
        return false;
    }
}
