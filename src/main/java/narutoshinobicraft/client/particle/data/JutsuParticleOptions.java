package narutoshinobicraft.client.particle.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.client.particle.api.ParticleBehavior;
import narutoshinobicraft.client.particle.api.ParticleMotion;
import narutoshinobicraft.client.particle.registry.BehaviorRegistry;
import narutoshinobicraft.client.particle.registry.MotionRegistry;
import narutoshinobicraft.client.particle.registry.ParticleRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import java.util.Optional;
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
    ParticleType<?> type,
    Vector3f color,
    float alpha,
    float scale,
    int lifetime,
    ParticleMotion motion,
    List<ParticleBehavior> behaviors,
    boolean isAnimated
) implements ParticleOptions {    
    public static final MapCodec<JutsuParticleOptions> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            BuiltInRegistries.PARTICLE_TYPE.byNameCodec().optionalFieldOf("type").forGetter(opt -> Optional.of(opt.getType())),
            Codec.INT.listOf().optionalFieldOf("color", List.of(255, 255, 255))
                .xmap(
                    ints -> new Vector3f(ints.get(0) / 255.0f, ints.get(1) / 255.0f, ints.get(2) / 255.0f),
                    vec -> List.of((int)(vec.x() * 255), (int)(vec.y() * 255), (int)(vec.z() * 255))
                ).forGetter(JutsuParticleOptions::color),
            // Base opacity (0..1). The original mod's chakra aura leaned on a very low alpha so many
            // overlapping particles read as a soft glow instead of solid blobs. This is the knob for it.
            Codec.FLOAT.optionalFieldOf("alpha", 1.0f).forGetter(JutsuParticleOptions::alpha),
            Codec.FLOAT.optionalFieldOf("scale", 1.0f).forGetter(JutsuParticleOptions::scale),
            Codec.INT.optionalFieldOf("lifetime", 20).forGetter(JutsuParticleOptions::lifetime),

            MotionRegistry.DISPATCH_CODEC.optionalFieldOf("motion").forGetter(opt -> Optional.ofNullable(opt.motion())),
            BehaviorRegistry.DISPATCH_CODEC.listOf().optionalFieldOf("behaviors", List.of()).forGetter(JutsuParticleOptions::behaviors),
            Codec.BOOL.optionalFieldOf("is_animated", false).forGetter(JutsuParticleOptions::isAnimated)
        ).apply(inst, (optType, color, alpha, scale, lifetime, optMotion, behaviors, isAnimated) ->
            new JutsuParticleOptions(
                optType.orElseGet(ParticleRegistry.GENERIC_JUTSU_PARTICLE::get),
                color, alpha, scale, lifetime, optMotion.orElse(null), behaviors, isAnimated
            )
        ));

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }
}
