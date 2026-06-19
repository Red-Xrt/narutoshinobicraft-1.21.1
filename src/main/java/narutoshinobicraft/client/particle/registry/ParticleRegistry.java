package narutoshinobicraft.client.particle.registry;

import org.joml.Vector3f;
import com.mojang.serialization.MapCodec;
import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.client.particle.data.JutsuParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("null")
public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_REGISTER = DeferredRegister.create(Registries.PARTICLE_TYPE, NarutoShinobiCraft.MODID);

    public static final DeferredHolder<ParticleType<?>, ParticleType<JutsuParticleOptions>> GENERIC_JUTSU_PARTICLE = 
        PARTICLE_REGISTER.register("generic_jutsu", () -> 
            new ParticleType<JutsuParticleOptions>(false) {
                @Override
                public MapCodec<JutsuParticleOptions> codec() {
                    return JutsuParticleOptions.CODEC;
                }

                @Override
                public StreamCodec<RegistryFriendlyByteBuf, JutsuParticleOptions> streamCodec() {
                    return StreamCodec.of(
                        (buf, options) -> { },
                        buf -> {
                            return new JutsuParticleOptions(
                                this,
                                new Vector3f(1.0f, 1.0f, 1.0f), 
                                1.0f, 
                                0.0f, 
                                0, 
                                null, 
                                java.util.List.of(), 
                                false
                            );
                        }
                    );
                }
            }
        );

    public static final DeferredHolder<ParticleType<?>, ParticleType<JutsuParticleOptions>> SMOKE = 
        PARTICLE_REGISTER.register("smoke", () -> new ParticleType<JutsuParticleOptions>(false) {
                @Override
                public MapCodec<JutsuParticleOptions> codec() {
                    return JutsuParticleOptions.CODEC;
                }

                @Override
                public StreamCodec<RegistryFriendlyByteBuf, JutsuParticleOptions> streamCodec() {
                    return StreamCodec.of(
                        (buf, options) -> { },
                        buf -> {
                            return new JutsuParticleOptions(
                                this,
                                new Vector3f(1.0f, 1.0f, 1.0f), 
                                1.0f, 
                                0.0f, 
                                0, 
                                null, 
                                java.util.List.of(), 
                                false
                            );
                        }
                    );
                }
         });
}
