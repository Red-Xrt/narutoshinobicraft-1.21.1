package narutoshinobicraft.client.fx.speck.runtime.motion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import narutoshinobicraft.client.fx.speck.FxSpeck;

@SuppressWarnings("null")
public class LinearMotion implements SpeckMotion {
    public static final MapCodec<LinearMotion> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.DOUBLE.optionalFieldOf("y_accel", 0.0D).forGetter(m -> m.yAccel),
        Codec.DOUBLE.optionalFieldOf("drag", 0.98D).forGetter(m -> m.drag),
        Codec.DOUBLE.optionalFieldOf("ground_drag", 0.7D).forGetter(m -> m.groundDrag),
        Codec.BOOL.optionalFieldOf("bounce_on_ground", false).forGetter(m -> m.bounceOnGround)
    ).apply(inst, LinearMotion::new));

    private final double yAccel;
    private final double drag;
    private final double groundDrag;
    private final boolean bounceOnGround;

    public LinearMotion(double yAccel, double drag, double groundDrag, boolean bounceOnGround) {
        this.yAccel = yAccel;
        this.drag = drag;
        this.groundDrag = groundDrag;
        this.bounceOnGround = bounceOnGround;
    }

    @Override
    public void apply(FxSpeck speck) {
        speck.addMotion(0, this.yAccel, 0);
        speck.moveParticle(speck.getMotionX(), speck.getMotionY(), speck.getMotionZ());

        if (this.bounceOnGround && speck.getMotionY() == 0) {
            speck.multiplyMotion(1.1D, 1.0D, 1.1D);
        }

        speck.multiplyMotion(this.drag, this.drag, this.drag);

        if (speck.isSpeckOnGround()) {
            speck.multiplyMotion(this.groundDrag, 1.0D, this.groundDrag);
        }
    }

    @Override
    public MapCodec<? extends SpeckMotion> codec() {
        return CODEC;
    }
}
