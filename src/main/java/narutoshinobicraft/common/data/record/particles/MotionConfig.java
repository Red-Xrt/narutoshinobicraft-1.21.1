package narutoshinobicraft.common.data.record.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("null")
public record MotionConfig(
    ResourceLocation typeId,
    float speed,
    float radius,
    int targetId
) 
{
    public static final MapCodec<MotionConfig> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
       ResourceLocation.CODEC.fieldOf("type").forGetter(MotionConfig::typeId),
       Codec.FLOAT.optionalFieldOf("speed", 0.0f).forGetter(MotionConfig::speed),
       Codec.FLOAT.optionalFieldOf("radius", 0.0f).forGetter(MotionConfig::radius),
       Codec.INT.optionalFieldOf("target_id", -1).forGetter(MotionConfig::targetId) 
    ).apply(instance, MotionConfig::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MotionConfig> STREAM_CODEC = StreamCodec.composite(
        ResourceLocation.STREAM_CODEC, MotionConfig::typeId,
        ByteBufCodecs.FLOAT, MotionConfig::speed,
        ByteBufCodecs.FLOAT, MotionConfig::radius,
        ByteBufCodecs.INT, MotionConfig::targetId,
        MotionConfig::new
    );
}
