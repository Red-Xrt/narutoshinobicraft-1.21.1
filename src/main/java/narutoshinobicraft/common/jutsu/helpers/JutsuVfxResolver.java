package narutoshinobicraft.common.jutsu.helpers;

import java.util.Optional;

import narutoshinobicraft.common.data.component.JutsuStackOps;
import narutoshinobicraft.common.registry.JutsuRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("null")
public final class JutsuVfxResolver {
    private JutsuVfxResolver() {}

    public static Optional<ResourceLocation> fromJutsuId(ResourceLocation jutsuId) {
        return JutsuRegistry.findEntry(jutsuId).flatMap(entry -> entry.definition().vfxId());
    }

    public static Optional<ResourceLocation> fromStack(ItemStack stack) {
        ResourceLocation jutsuId = JutsuStackOps.getCurrentJutsuId(stack);
        return jutsuId == null ? Optional.empty() : fromJutsuId(jutsuId);
    }
}
