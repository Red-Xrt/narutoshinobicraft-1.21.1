package narutoshinobicraft.common.jutsu.helpers.power;

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

    public static float chargeModifier(double ninjaLevel, JutsuStackState state, ResourceLocation jutsuId, int requiredXp, boolean creativeLike) {
        return cooldownModifier(ninjaLevel) * jutsuXpModifier(state, jutsuId, requiredXp, creativeLike);
    }
}

