package narutoshinobicraft.common.jutsu.helpers;

import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import narutoshinobicraft.common.data.attachments.PlayersChakra;
import narutoshinobicraft.common.data.component.JutsuStackState;

public class JutsuChargeModifiers {
    private JutsuChargeModifiers() {}

    public static float cooldownModifier(double modifier) {
        return (float) (1.0d / (0.5d + 0.02d * modifier));
    }

    public static float chakraModifier(double chakraCurrent, double chakraMax) {
        double level = Math.sqrt(Math.max(chakraCurrent, chakraMax));
        return cooldownModifier(level);
    }

    public static float chakraModifier(PlayersChakra chakra) {
        if (chakra == null) {
            return 0.0f;
        }
        return chakraModifier(chakra.getCurrentChakra(), chakra.getCharkaMax());
    }

    public static float jutsuXpModifier(int currentXp, int requiredXp, boolean creativeLike) {
        int requirement = Math.max(0, requiredXp);
        int progress = creativeLike ? requirement : currentXp;
        if (progress <= 0) {
            return 0.0f;
        }
        if (requirement <= 0) {
            return 1.0f;
        }
        return (float) requirement / (float) progress;
    }

    public static float jutsuXpModifier(JutsuStackState state, ResourceLocation jutsuId, int requiredXp, boolean creativeLike) {
        if (state == null || jutsuId == null) {
            return 0.0f;
        }
        Map<ResourceLocation, Integer> xpMap = state.xp();
        int currentXp = xpMap != null ? xpMap.getOrDefault(jutsuId, 0) : 0;
        return jutsuXpModifier(currentXp, requiredXp, creativeLike);
    }

    public static float combinedModifier(PlayersChakra chakra, JutsuStackState state, ResourceLocation jutsuId, int requiredXp, boolean creativeLike) {
        return chakraModifier(chakra) * jutsuXpModifier(state, jutsuId, requiredXp, creativeLike);
    }
}
