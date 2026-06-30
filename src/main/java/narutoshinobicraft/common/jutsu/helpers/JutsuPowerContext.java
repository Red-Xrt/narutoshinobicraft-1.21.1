package narutoshinobicraft.common.jutsu.helpers;

public record JutsuPowerContext(
    int timeLeft,
    int useDuration,
    int maxChargeTicks,
    float basePower,
    float powerupDelay,
    float modifier,
    float maxPower
) {
    public static JutsuPowerContext of(int timeLeft, float basePower, float powerupDelay, float modifier, float maxPower) {
        return new JutsuPowerContext(
            timeLeft,
            JutsuPowerCalculator.DEFAULT_MAX_USE_DURATION,
            JutsuPowerCalculator.DEFAULT_MAX_USE_DURATION,
            basePower,
            powerupDelay,
            modifier,
            maxPower
        );
    }

    public JutsuPowerContext {
        if (useDuration <= 0) {
            useDuration = JutsuPowerCalculator.DEFAULT_MAX_USE_DURATION;
        }
        if (maxChargeTicks <= 0) {
            maxChargeTicks = useDuration;
        }
    }
}
