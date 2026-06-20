package narutoshinobicraft.common.jutsu.projectile.behavior;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.jutsu.projectile.JutsuProjectile;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("null")
public final class SpiralBehavior implements ProjectileBehavior {
    public static final MapCodec<SpiralBehavior> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.DOUBLE.optionalFieldOf("radius", 0.3D).forGetter(b -> b.radius),
        Codec.DOUBLE.optionalFieldOf("speed", 0.5D).forGetter(b -> b.speed)
    ).apply(inst, SpiralBehavior::new));

    private final double radius;
    private final double speed;

    public SpiralBehavior(double radius, double speed) {
        this.radius = radius;
        this.speed = speed;
    }

    @Override
    public void tick(JutsuProjectile projectile) {
        Vec3 velocity = projectile.getDeltaMovement();
        if (velocity.lengthSqr() < 1.0E-6D) {
            return;
        }
        Vec3 heading = velocity.normalize();
        Vec3 reference = Math.abs(heading.y) > 0.99D ? new Vec3(1.0D, 0.0D, 0.0D) : new Vec3(0.0D, 1.0D, 0.0D);
        Vec3 right = heading.cross(reference).normalize();
        Vec3 up = right.cross(heading).normalize();

        int age = projectile.getProjectileAge();
        Vec3 delta = offsetAt(age, right, up).subtract(offsetAt(age - 1, right, up));
        projectile.setPos(projectile.getX() + delta.x, projectile.getY() + delta.y, projectile.getZ() + delta.z);
    }

    private Vec3 offsetAt(int age, Vec3 right, Vec3 up) {
        double angle = age * this.speed;
        return right.scale(Math.cos(angle) * this.radius).add(up.scale(Math.sin(angle) * this.radius));
    }

    @Override
    public MapCodec<? extends ProjectileBehavior> codec() {
        return CODEC;
    }
}

