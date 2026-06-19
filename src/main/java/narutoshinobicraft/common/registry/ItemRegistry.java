package narutoshinobicraft.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.jutsu.JutsuScrollItem;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, NarutoShinobiCraft.MODID);

    // Generic jutsu scroll. There is only ONE scroll class: to add a new scroll you register another
    // entry here with its own id + texture and fill it with jutsu ids (see CreativeTabRegistry).
    public static final DeferredHolder<Item, JutsuScrollItem> TEST_VFX = ITEMS.register("test_vfx", 
        () -> new JutsuScrollItem(JutsuScrollItem.defaultProperties())
    );
}
