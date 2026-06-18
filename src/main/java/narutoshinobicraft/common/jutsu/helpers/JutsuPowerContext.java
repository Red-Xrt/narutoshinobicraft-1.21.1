package narutoshinobicraft.common.jutsu.helpers;

public record JutsuPowerContext(
    int timeLeft,
    int maxUseDuration,
    float basePower,
    float powerupDelay,
    float modifier,
    float maxPower
) {
    public static JutsuPowerContext of(int timeLeft, float basePower, float powerupDelay, float modifier, float maxPower) {
        return new JutsuPowerContext(
            timeLeft,
            JutsuPowerCalculator.DEFAULT_MAX_USE_DURATION,
            basePower,
            powerupDelay,
            modifier,
            maxPower
        );
    }

    public JutsuPowerContext {
        if (maxUseDuration <= 0) {
            maxUseDuration = JutsuPowerCalculator.DEFAULT_MAX_USE_DURATION;
        }
    }
}
