package narutoshinobicraft.client.fx.speck.runtime.motion;

import com.mojang.serialization.MapCodec;
import narutoshinobicraft.client.fx.speck.FxSpeck;

public interface SpeckMotion {
    void apply(FxSpeck speck);

    MapCodec<? extends SpeckMotion> codec();
}
