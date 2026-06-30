package narutoshinobicraft.common.jutsu.helpers;

import narutoshinobicraft.common.data.attachments.PlayersChakra;
import narutoshinobicraft.common.jutsu.api.JutsuDefinition;

public class JutsuPowerCalculator {
    public static final int DEFAULT_MAX_USE_DURATION = 72000;

    private JutsuPowerCalculator() {}

    public static float calculatePower(int timeLeft, float basePower, float powerupDelay, float modifier, float maxPower) {
        return calculatePower(
            timeLeft,
            DEFAULT_MAX_USE_DURATION,
            DEFAULT_MAX_USE_DURATION,
            basePower,
            powerupDelay,
            modifier,
            maxPower
        );
    }

    public static float calculatePower(
        int timeLeft,
        int useDuration,
        int maxChargeTicks,
        float basePower,
        float powerupDelay,
        float modifier,
        float maxPower
    ) {
        float f = powerupDelay * modifier;
        if (f <= 0.0f) {
            return 0.0f;
        }
        int elapsed = Math.max(0, useDuration - timeLeft);
        int chargeTicks = Math.min(elapsed, Math.max(0, maxChargeTicks));
        float rawPower = basePower + (float) chargeTicks / f;
        float cap = Math.max(0.0f, maxPower);
        return Math.min(rawPower, cap);
    }

    public static float calculatePower(JutsuPowerContext context) {
        if (context == null) {
            return 0.0f;
        }
        return calculatePower(
            context.timeLeft(),
            context.useDuration(),
            context.maxChargeTicks(),
            context.basePower(),
            context.powerupDelay(),
            context.modifier(),
            context.maxPower()
        );
    }

    public static float resolveMaxPower(double chakraAmount, double chakraUsage) {
        if (chakraUsage <= 0.0d || chakraAmount <= 0.0d) {
            return 0.0f;
        }
        return (float) (chakraAmount / chakraUsage * 0.9999d);
    }

    public static float resolveMaxPower(double chakraAmount, double chakraUsage, float hardCap) {
        float chakraBased = resolveMaxPower(chakraAmount, chakraUsage);
        if (hardCap <= 0.0f) {
            return chakraBased;
        }
        return Math.min(chakraBased, hardCap);
    }

    public static float resolveMaxPower(PlayersChakra chakra, JutsuDefinition definition) {
        if (chakra == null || definition == null) {
            return 0.0f;
        }
        return resolveMaxPower(chakra.getCurrentChakra(), definition.chakraCost(), definition.maxPowerCap());
    }
}
