package narutoshinobicraft.common.registry;

import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.jutsu.data.ScrollPresetManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("null")
public class CreativeTabRegistry {
    public static final ResourceLocation TEST_SCROLL_PRESET =
        ResourceLocation.fromNamespaceAndPath(NarutoShinobiCraft.MODID, "test_vfx");

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NarutoShinobiCraft.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB =
        CREATIVE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.narutoshinobicraft.main"))
            .icon(() -> new ItemStack(ItemRegistry.TEST_VFX.get()))
            .displayItems((params, output) -> {
                ItemStack scroll = ScrollPresetManager.createStack(TEST_SCROLL_PRESET);
                if (!scroll.isEmpty()) {
                    output.accept(scroll);
                }
            })
            .build());
}
