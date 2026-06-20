package narutoshinobicraft.common.jutsu.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@SuppressWarnings("null")
public record ScrollPreset(ResourceLocation itemId, List<ResourceLocation> jutsuIds) {
    public static final Codec<ScrollPreset> CODEC = RecordCodecBuilder.create(inst -> inst.group(
        ResourceLocation.CODEC.fieldOf("item").forGetter(ScrollPreset::itemId),
        ResourceLocation.CODEC.listOf().fieldOf("jutsu_ids").forGetter(ScrollPreset::jutsuIds)
    ).apply(inst, ScrollPreset::new));

    public ScrollPreset {
        jutsuIds = List.copyOf(jutsuIds);
    }
}
