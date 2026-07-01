package narutoshinobicraft.common.jutsu.helpers.text;

import narutoshinobicraft.common.registry.JutsuRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("null")
public final class JutsuNames {
    private JutsuNames() {}

    public static Component display(ResourceLocation id) {
        if (id == null) {
            return Component.empty();
        }
        var entry = JutsuRegistry.getJutsu(id);
        if (entry != null && entry.definition().nameKey().isPresent()) {
            return Component.translatable(entry.definition().nameKey().get());
        }
        return Component.translatable("jutsu." + id.getNamespace() + "." + id.getPath());
    }
}
