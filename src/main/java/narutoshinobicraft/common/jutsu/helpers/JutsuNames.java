package narutoshinobicraft.common.jutsu.helpers;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("null")
public final class JutsuNames {
    private JutsuNames() {}

    public static Component display(ResourceLocation id) {
        if (id == null) {
            return Component.empty();
        }
        String key = id.getPath().contains(".") ? id.getPath() : "entity." + id.getPath() + ".name";
        return Component.translatable(key);
    }
}
