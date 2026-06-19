package narutoshinobicraft.common.registry;

import java.util.List;
import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.jutsu.JutsuBootstrap;
import narutoshinobicraft.common.jutsu.api.JutsuScrollStacks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("null")
public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NarutoShinobiCraft.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB =
        CREATIVE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.narutoshinobicraft.main"))
            .icon(() -> new ItemStack(ItemRegistry.TEST_VFX.get()))
            .displayItems((params, output) -> {
                output.accept(JutsuScrollStacks.create(
                    ItemRegistry.TEST_VFX.get(),
                    List.of(JutsuBootstrap.JUTSU_ONE, JutsuBootstrap.JUTSU_TWO)
                ));
            })
            .build());
}
