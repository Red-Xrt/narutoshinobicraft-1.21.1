package narutoshinobicraft.client.fx.speck.register;

import com.mojang.serialization.MapCodec;
import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.client.fx.speck.SpeckOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.joml.Vector3f;

@SuppressWarnings("null")
public class SpeckTypeRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_REGISTER =
        DeferredRegister.create(Registries.PARTICLE_TYPE, NarutoShinobiCraft.MODID);

    public static final DeferredHolder<ParticleType<?>, ParticleType<SpeckOptions>> GENERIC =
        PARTICLE_REGISTER.register("generic_jutsu", () ->
            new ParticleType<SpeckOptions>(false) {
                @Override
                public MapCodec<SpeckOptions> codec() {
                    return SpeckOptions.CODEC;
                }

                @Override
                public StreamCodec<RegistryFriendlyByteBuf, SpeckOptions> streamCodec() {
                    return StreamCodec.of(
                        (buf, options) -> {},
                        buf -> new SpeckOptions(
                            this,
                            new Vector3f(1.0f, 1.0f, 1.0f),
                            1.0f,
                            0.0f,
                            0,
                            null,
                            java.util.List.of(),
                            false
                        )
                    );
                }
            }
        );

    public static final DeferredHolder<ParticleType<?>, ParticleType<SpeckOptions>> SMOKE =
        PARTICLE_REGISTER.register("smoke", () -> new ParticleType<SpeckOptions>(false) {
            @Override
            public MapCodec<SpeckOptions> codec() {
                return SpeckOptions.CODEC;
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, SpeckOptions> streamCodec() {
                return StreamCodec.of(
                    (buf, options) -> {},
                    buf -> new SpeckOptions(
                        this,
                        new Vector3f(1.0f, 1.0f, 1.0f),
                        1.0f,
                        0.0f,
                        0,
                        null,
                        java.util.List.of(),
                        false
                    )
                );
            }
        });
}
