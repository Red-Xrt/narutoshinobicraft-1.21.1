package narutoshinobicraft.client.particle.registry;

import java.util.HashMap;
import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import narutoshinobicraft.client.particle.api.ParticleBehavior;
import narutoshinobicraft.client.particle.behavior.SmokeBehavior;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("null")
public class BehaviorRegistry {
    private static final Map<ResourceLocation, MapCodec<? extends ParticleBehavior>> BY_NAME = new HashMap<>();
    private static final Map<MapCodec<? extends ParticleBehavior>, ResourceLocation> BY_CODEC = new HashMap<>();

    static {
        register("narutoshinobicraft:smoke_visuals", SmokeBehavior.CODEC);
    }

    private static void register(String name, MapCodec<? extends ParticleBehavior> codec) {
        ResourceLocation id = ResourceLocation.parse(name);
        BY_NAME.put(id, codec);
        BY_CODEC.put(codec, id);
    }

    public static final Codec<ParticleBehavior> DISPATCH_CODEC = ResourceLocation.CODEC.dispatch(
        "type", 
        behavior -> {
            ResourceLocation id = BY_CODEC.get(behavior.codec());
            if (id == null) throw new IllegalStateException("LỖI: Chưa đăng ký Behavior Type -> " + behavior.getClass().getName());
            return id;
        },
        id -> {
            MapCodec<? extends ParticleBehavior> codec = BY_NAME.get(id);
            if (codec == null) throw new IllegalArgumentException("LỖI JSON: Không tìm thấy Behavior Type -> " + id);
            return codec;
        }
    );
}
