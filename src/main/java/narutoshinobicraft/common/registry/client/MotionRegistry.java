package narutoshinobicraft.common.registry.client;

import java.util.HashMap;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import narutoshinobicraft.client.particle.api.ParticleMotion;
import narutoshinobicraft.client.particle.motions.LinearMotion;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("null")
public class MotionRegistry {
    private static final Map<ResourceLocation, MapCodec<? extends ParticleMotion>> BY_NAME = new HashMap<>();
    private static final Map<MapCodec<? extends ParticleMotion>, ResourceLocation> BY_CODEC = new HashMap<>();

    static {
        register("narutoshinobicraft:linear", LinearMotion.CODEC);
        //You can add more motion in here!
    }

    private static void register(String name, MapCodec<? extends ParticleMotion> codec) {
        ResourceLocation id = ResourceLocation.parse(name);
        BY_NAME.put(id, codec);
        BY_CODEC.put(codec, id);
    }
    
    public static final Codec<ParticleMotion> DISPATCH_CODEC = ResourceLocation.CODEC.dispatch(
        "type", 
        motion -> {
            ResourceLocation id = BY_CODEC.get(motion.codec());
            if (id == null) {
                throw new IllegalStateException("Does not have Motion Type for class -> " + motion.getClass().getName());
            }
            return id;
        },
        id -> {
            MapCodec<? extends ParticleMotion> codec = BY_NAME.get(id);
            if (codec == null) throw new IllegalArgumentException("Motion Type with ID not found " + id);
            return codec;
        }
    );
}
