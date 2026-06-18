package narutoshinobicraft.common.jutsu.api;

import java.util.List;
import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.data.component.JutsuStackOps;
import narutoshinobicraft.common.jutsu.JutsuScrollItem;
import narutoshinobicraft.common.jutsu.cast.JutsuCastValidator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

@SuppressWarnings("null")
public final class JutsuScrollStacks {
    private JutsuScrollStacks() {}

    public static ItemStack create(ItemLike scrollItem, List<ResourceLocation> jutsuIds) {
        return create(scrollItem, jutsuIds, true);
    }

    public static ItemStack create(ItemLike scrollItem, List<ResourceLocation> jutsuIds, boolean enableAll) {
        Item item = scrollItem.asItem();
        if (!(item instanceof JutsuScrollItem)) {
            throw new IllegalArgumentException("Item must be a JutsuScrollItem: " + item);
        }
        if (jutsuIds == null || jutsuIds.isEmpty()) {
            throw new IllegalArgumentException("Jutsu scroll requires at least one jutsu id");
        }

        ItemStack stack = new ItemStack(scrollItem);
        JutsuStackOps.setJutsuIds(stack, jutsuIds);
        for (ResourceLocation id : jutsuIds) {
            if (!JutsuCastValidator.isRegistered(id)) {
                NarutoShinobiCraft.LOGGER.warn("Scroll references unregistered jutsu [{}] — register it in JutsuRegistry first", id);
            }
            if (enableAll) {
                JutsuStackOps.enableJutsu(stack, id, true);
            }
        }
        return stack;
    }
}
