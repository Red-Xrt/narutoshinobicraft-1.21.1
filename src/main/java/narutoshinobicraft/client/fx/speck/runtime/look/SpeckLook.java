package narutoshinobicraft.client.fx.speck.runtime.look;

import com.mojang.serialization.MapCodec;
import narutoshinobicraft.client.fx.speck.FxSpeck;

public interface SpeckLook {
    void tick(FxSpeck speck);

    MapCodec<? extends SpeckLook> codec();
}
