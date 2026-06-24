package narutoshinobicraft.client.fx.speck;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Optional;
import narutoshinobicraft.client.fx.speck.register.SpeckLookRegistry;
import narutoshinobicraft.client.fx.speck.register.SpeckMotionRegistry;
import narutoshinobicraft.client.fx.speck.register.SpeckTypeRegistry;
import narutoshinobicraft.client.fx.speck.runtime.look.SpeckLook;
import narutoshinobicraft.client.fx.speck.runtime.motion.SpeckMotion;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import org.joml.Vector3f;

@SuppressWarnings("null")
public record SpeckOptions(
    ParticleType<?> type,
    Vector3f color,
    float alpha,
    float scale,
    int lifetime,
    SpeckMotion motion,
    List<SpeckLook> looks,
    boolean isAnimated
) implements ParticleOptions {
    public static final MapCodec<SpeckOptions> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        BuiltInRegistries.PARTICLE_TYPE.byNameCodec().optionalFieldOf("type").forGetter(opt -> Optional.of(opt.getType())),
        Codec.INT.listOf().optionalFieldOf("color", List.of(255, 255, 255))
            .xmap(
                ints -> new Vector3f(
                    (ints.size() > 0 ? ints.get(0) : 255) / 255.0f,
                    (ints.size() > 1 ? ints.get(1) : 255) / 255.0f,
                    (ints.size() > 2 ? ints.get(2) : 255) / 255.0f
                ),
                vec -> List.of((int) (vec.x() * 255), (int) (vec.y() * 255), (int) (vec.z() * 255))
            ).forGetter(SpeckOptions::color),
        Codec.FLOAT.optionalFieldOf("alpha", 1.0f).forGetter(SpeckOptions::alpha),
        Codec.FLOAT.optionalFieldOf("scale", 1.0f).forGetter(SpeckOptions::scale),
        Codec.INT.optionalFieldOf("lifetime", 20).forGetter(SpeckOptions::lifetime),
        SpeckMotionRegistry.DISPATCH_CODEC.optionalFieldOf("motion").forGetter(opt -> Optional.ofNullable(opt.motion())),
        SpeckLookRegistry.DISPATCH_CODEC.listOf().optionalFieldOf("looks", List.of()).forGetter(SpeckOptions::looks),
        Codec.BOOL.optionalFieldOf("is_animated", false).forGetter(SpeckOptions::isAnimated)
    ).apply(inst, (optType, color, alpha, scale, lifetime, optMotion, looks, isAnimated) ->
        new SpeckOptions(
            optType.orElseGet(SpeckTypeRegistry.GENERIC::get),
            color, alpha, scale, lifetime, optMotion.orElse(null), looks, isAnimated
        )
    ));

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }
}
