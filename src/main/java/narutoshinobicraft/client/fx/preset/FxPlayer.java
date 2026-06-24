package narutoshinobicraft.client.fx.preset;

import narutoshinobicraft.client.fx.anchor.FxAnchor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

@SuppressWarnings("null")
public final class FxPlayer {
    private FxPlayer() {}

    public static void play(@Nullable ResourceLocation presetId, FxAnchor anchor) {
        if (presetId == null || anchor == null) {
            return;
        }
        Preset preset = PresetLibrary.get(presetId);
        if (preset == null) {
            return;
        }
        Level level = switch (anchor) {
            case FxAnchor.OnBody onBody -> onBody.entity().level();
            case FxAnchor.InFlight inFlight -> inFlight.entity().level();
        };
        if (level.isClientSide()) {
            preset.spawn().play(level, anchor, preset.speck());
        }
    }
}
