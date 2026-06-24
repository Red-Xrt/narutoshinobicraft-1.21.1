package narutoshinobicraft.client.fx.spawn;

import com.mojang.serialization.MapCodec;
import narutoshinobicraft.client.fx.anchor.FxAnchor;
import narutoshinobicraft.client.fx.speck.SpeckOptions;
import net.minecraft.world.level.Level;

public interface SpawnPattern {
    void play(Level level, FxAnchor anchor, SpeckOptions speck);

    MapCodec<? extends SpawnPattern> codec();
}
