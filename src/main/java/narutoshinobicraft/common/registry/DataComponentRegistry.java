package narutoshinobicraft.common.registry;

import com.mojang.serialization.Codec;
import java.util.List;
import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.jutsu.stack.JutsuStackState;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("null")
public class DataComponentRegistry {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, NarutoShinobiCraft.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<JutsuStackState>> JUTSU_STACK_STATE =
        DATA_COMPONENT_TYPES.register("jutsu_stack_state", () -> DataComponentType.<JutsuStackState>builder()
        .persistent(JutsuStackState.CODEC)
        .networkSynchronized(ByteBufCodecs.fromCodec(JutsuStackState.CODEC))
        .build()
    );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ResourceLocation>>> JUTSU_SKILL_IDS =
        DATA_COMPONENT_TYPES.register("jutsu_skill_ids", () -> DataComponentType.<List<ResourceLocation>>builder()
        .persistent(Codec.list(ResourceLocation.CODEC))
        .networkSynchronized(ByteBufCodecs.fromCodec(Codec.list(ResourceLocation.CODEC)))
        .build()
    );
}
