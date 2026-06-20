package narutoshinobicraft.common.jutsu.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import narutoshinobicraft.common.jutsu.api.JutsuScrollStacks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

@SuppressWarnings("null")
public final class ScrollPresetManager {
    private static final Map<ResourceLocation, ScrollPreset> PRESETS = new HashMap<>();

    private ScrollPresetManager() {}

    public static void replaceAll(Map<ResourceLocation, ScrollPreset> loaded) {
        PRESETS.clear();
        PRESETS.putAll(loaded);
    }

    public static Optional<ScrollPreset> get(ResourceLocation id) {
        return id == null ? Optional.empty() : Optional.ofNullable(PRESETS.get(id));
    }

    public static Map<ResourceLocation, ScrollPreset> view() {
        return Collections.unmodifiableMap(PRESETS);
    }

    public static ItemStack createStack(ResourceLocation presetId) {
        return get(presetId)
            .map(ScrollPresetManager::createStack)
            .orElse(ItemStack.EMPTY);
    }

    public static ItemStack createStack(ScrollPreset preset) {
        ItemLike item = resolveItem(preset.itemId());
        if (item == null || preset.jutsuIds().isEmpty()) {
            return ItemStack.EMPTY;
        }
        return JutsuScrollStacks.create(item, preset.jutsuIds());
    }

    public static boolean isKnownItem(ResourceLocation itemId) {
        return resolveItem(itemId) != null;
    }

    private static @Nullable ItemLike resolveItem(ResourceLocation itemId) {
        Item item = BuiltInRegistries.ITEM.get(itemId);
        return item == Items.AIR ? null : item;
    }
}
