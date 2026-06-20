package narutoshinobicraft.common.jutsu.api;

import com.mojang.serialization.MapCodec;
import narutoshinobicraft.common.data.component.JutsuContext;
import net.minecraft.world.item.ItemStack;

public interface JutsuAction {
    static boolean requireServerContext(JutsuContext context) {
        return context != null
            && context.level() != null
            && !context.level().isClientSide()
            && context.player() != null;
    }

    boolean execute(JutsuContext context);

    default MapCodec<? extends JutsuAction> codec() {
        throw new UnsupportedOperationException("Java-only action (not serializable from JSON)");
    }

    default boolean isActivated(ItemStack stack) {
        return false;
    }

    default float getPower(ItemStack stack) {
        return 0.0f;
    }
}
