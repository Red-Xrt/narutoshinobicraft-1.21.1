package narutoshinobicraft.common.jutsu;

import java.util.Optional;
import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.jutsu.api.JutsuCategory;
import narutoshinobicraft.common.jutsu.api.JutsuDefinition;
import narutoshinobicraft.common.registry.JutsuRegistry;
import net.minecraft.resources.ResourceLocation;

/**
 * Code-side catalog of built-in jutsu (the "engine defaults").
 *
 * <p>This is where the Core layer wires {@link JutsuDefinition} metadata to a {@code JutsuAction}
 * (the Java logic). The actions below are intentionally VFX-only placeholders: they succeed so the
 * executor consumes chakra and fires the cast VFX, but carry no gameplay effect yet. When the
 * datapack loader / composable effect system lands, definitions can move to JSON and these become
 * the fallback set.
 */
public final class JutsuBootstrap {
    public static final ResourceLocation JUTSU_ONE =
        ResourceLocation.fromNamespaceAndPath(NarutoShinobiCraft.MODID, "test_jutsu_one");
    public static final ResourceLocation JUTSU_TWO =
        ResourceLocation.fromNamespaceAndPath(NarutoShinobiCraft.MODID, "test_jutsu_two");

    private static final ResourceLocation CAST_VFX =
        ResourceLocation.fromNamespaceAndPath(NarutoShinobiCraft.MODID, "chakra_charge");

    private JutsuBootstrap() {}

    public static void register() {
        // requiredXp = 0 so a fresh scroll is castable immediately (no grind needed to test).
        JutsuRegistry.register(JUTSU_ONE,
            new JutsuDefinition('D', 0, 50.0d, JutsuCategory.KATON, 1.0f, 50.0f, 5.0f, 20L, 0, Optional.of(CAST_VFX)),
            context -> true,
            JUTSU_RENDER);

        JutsuRegistry.register(JUTSU_TWO,
            new JutsuDefinition('C', 0, 80.0d, JutsuCategory.RAITON, 1.0f, 40.0f, 8.0f, 40L, 0, Optional.of(CAST_VFX)),
            context -> true,
            JUTSU_RENDER);
    }

    private static final narutoshinobicraft.common.jutsu.api.JutsuRender JUTSU_RENDER =
        new narutoshinobicraft.common.jutsu.api.JutsuRender() {};
}
