package narutoshinobicraft.common.data.record.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import narutoshinobicraft.common.registry.client.MotionRegistry;
import narutoshinobicraft.client.particle.api.ParticleMotion;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

@SuppressWarnings("null")
public record JutsuParticleOptions(
    Vector3f color,
    float scale,
    int lifetime,
    ParticleMotion motion 
) implements ParticleOptions {
    public static final Codec<Vector3f> vector3fCodec = ExtraCodecs.VECTOR3F;
    
    public static final MapCodec<JutsuParticleOptions> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        vector3fCodec.optionalFieldOf("color", new Vector3f(1.0F, 1.0F, 1.0F)).forGetter(JutsuParticleOptions::color),
        Codec.FLOAT.optionalFieldOf("scale", 1.0F).forGetter(JutsuParticleOptions::scale),
        Codec.INT.optionalFieldOf("lifetime", 20).forGetter(JutsuParticleOptions::lifetime),
        MotionRegistry.DISPATCH_CODEC.fieldOf("motion").forGetter(JutsuParticleOptions::motion)
    ).apply(inst, JutsuParticleOptions::new));

    @Override
    public ParticleType<?> getType() {
        return null; 
    }
}