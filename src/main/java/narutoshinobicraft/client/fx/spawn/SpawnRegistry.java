package narutoshinobicraft.client.fx.spawn;

import com.mojang.serialization.Codec;
import narutoshinobicraft.common.registry.DispatchRegistry;

public final class SpawnRegistry {
    private static final DispatchRegistry<SpawnPattern> REGISTRY =
        new DispatchRegistry<>("fx_spawn", SpawnPattern::codec);

    static {
        REGISTRY.register("narutoshinobicraft:aura", AuraSpawn.CODEC);
        REGISTRY.register("narutoshinobicraft:burst", BurstSpawn.CODEC);
        REGISTRY.register("narutoshinobicraft:trail", TrailSpawn.CODEC);
    }

    public static final Codec<SpawnPattern> DISPATCH_CODEC = REGISTRY.dispatchCodec;

    private SpawnRegistry() {}
}
