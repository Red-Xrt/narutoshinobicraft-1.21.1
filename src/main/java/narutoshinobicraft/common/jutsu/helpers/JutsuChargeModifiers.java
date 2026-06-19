package narutoshinobicraft.common.jutsu.helpers;

import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import narutoshinobicraft.common.data.component.JutsuStackState;

public class JutsuChargeModifiers {
    private JutsuChargeModifiers() {}

    public static float cooldownModifier(double modifier) {
        return (float) (1.0d / (0.5d + 0.02d * modifier));
    }

    public static float jutsuXpModifier(int currentXp, int requiredXp, boolean creativeLike) {
        int requirement = Math.max(0, requiredXp);
        // No mastery requirement => the jutsu is "free": it should charge at normal speed (1.0),
        // not stall at 0. This must be checked before the progress gate, otherwise requiredXp == 0
        // collapses progress to 0 and kills the power entirely.
        if (requirement <= 0) {
            return 1.0f;
        }
        int progress = creativeLike ? requirement : currentXp;
        if (progress <= 0) {
            return 0.0f;
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

    /**
     * Charge-speed multiplier. Keyed on ninja progression (same source as {@code JutsuCooldownHelper}
     * and the original mod), NOT chakra capacity: the stronger the ninja and the more mastered the
     * jutsu, the faster it charges.
     */
    public static float chargeModifier(double ninjaLevel, JutsuStackState state, ResourceLocation jutsuId, int requiredXp, boolean creativeLike) {
        return cooldownModifier(ninjaLevel) * jutsuXpModifier(state, jutsuId, requiredXp, creativeLike);
    }
}
