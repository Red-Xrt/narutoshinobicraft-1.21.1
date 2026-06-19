package narutoshinobicraft.common.data.record.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.registry.client.BehaviorRegistry;
import narutoshinobicraft.common.registry.client.MotionRegistry;
import narutoshinobicraft.common.registry.client.ParticleRegistry;
import narutoshinobicraft.client.particle.api.ParticleBehavior;
import narutoshinobicraft.client.particle.api.ParticleMotion;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import java.util.List;
import org.joml.Vector3f;
/**
* This particle uses the Local Render system (only loads JSON on the client side).
* Absolutely DO NOT call ServerLevel.sendParticles() or ServerLevel.addParticle() with this class.
* This will cause stealth errors in Multiplayer due to Dummy StreamCodec.
* ALWAYS spawn via HandlerJutsuCastSuccess (Client Side).
*/
@SuppressWarnings("null")
public record JutsuParticleOptions(
    Vector3f color,
    float scale,
    int lifetime,
    ParticleMotion motion,
    List<ParticleBehavior> behaviors,
    boolean isAnimated
) implements ParticleOptions {    
    public static final MapCodec<JutsuParticleOptions> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.INT.listOf().optionalFieldOf("color", List.of(255, 255, 255)).xmap(
            ints -> new Vector3f(ints.get(0) / 255.0f, ints.get(1) / 255.0f, ints.get(2) / 255.0f),
            vec -> List.of((int)(vec.x() * 255), (int)(vec.y() * 255), (int)(vec.z() * 255))
        ).forGetter(JutsuParticleOptions::color),
        Codec.FLOAT.optionalFieldOf("scale", 1.0F).forGetter(JutsuParticleOptions::scale),
        Codec.INT.optionalFieldOf("lifetime", 20).forGetter(JutsuParticleOptions::lifetime),
        MotionRegistry.DISPATCH_CODEC.fieldOf("motion").forGetter(JutsuParticleOptions::motion),
        BehaviorRegistry.DISPATCH_CODEC.listOf().optionalFieldOf("behaviors", List.of()).forGetter(JutsuParticleOptions::behaviors),
        Codec.BOOL.optionalFieldOf("is_animated", false).forGetter(JutsuParticleOptions::isAnimated)
    ).apply(inst, JutsuParticleOptions::new));

    @Override
    public ParticleType<?> getType() {
        return ParticleRegistry.GENERIC_JUTSU_PARTICLE.get(); 
    }
}