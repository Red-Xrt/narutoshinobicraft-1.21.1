package narutoshinobicraft.client.fx.speck.runtime.look;

import com.mojang.serialization.MapCodec;
import narutoshinobicraft.client.fx.speck.FxSpeck;
import net.minecraft.util.Mth;

public class SmokeFade implements SpeckLook {
    public static final MapCodec<SmokeFade> CODEC = MapCodec.unit(SmokeFade::new);

    public SmokeFade() {}

    @Override
    public void tick(FxSpeck speck) {
        float f = (float) speck.getAge() / (float) speck.getLifetime();
        float targetScale = Mth.clamp(f * 32.0F, 0.0F, 1.0F);
        speck.setSpeckScale(speck.getInitialScale() * targetScale);

        float targetAlpha = speck.getInitialAlpha() * (1.0F - f * f * 0.5F);
        speck.setAlpha(targetAlpha);
    }

    @Override
    public MapCodec<? extends SpeckLook> codec() {
        return CODEC;
    }
}
