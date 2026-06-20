package narutoshinobicraft.common.jutsu.projectile.motion;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import narutoshinobicraft.common.jutsu.projectile.JutsuProjectile;
import narutoshinobicraft.common.jutsu.projectile.motion.force.Force;
import narutoshinobicraft.common.jutsu.projectile.registry.ForceRegistry;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("null")
public final class PhysicsMotion implements EntityMotion {
    public static final MapCodec<PhysicsMotion> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
        Codec.DOUBLE.optionalFieldOf("initial_speed", 1.0D).forGetter(m -> m.initialSpeed),
        ForceRegistry.DISPATCH_CODEC.listOf().optionalFieldOf("forces", List.of()).forGetter(m -> m.forces)
    ).apply(inst, PhysicsMotion::new));

    private final double initialSpeed;
    private final List<Force> forces;

    public PhysicsMotion(double initialSpeed, List<Force> forces) {
        this.initialSpeed = initialSpeed;
        this.forces = List.copyOf(forces);
    }

    @Override
    public void start(JutsuProjectile projectile) {
        projectile.setDeltaMovement(projectile.getShootVector().scale(this.initialSpeed));
    }

    @Override
    public void tick(JutsuProjectile projectile) {
        Vec3 velocity = projectile.getDeltaMovement();
        for (Force force : this.forces) {
            velocity = force.apply(projectile, velocity);
        }
        projectile.setDeltaMovement(velocity);
        projectile.setPos(
            projectile.getX() + velocity.x,
            projectile.getY() + velocity.y,
            projectile.getZ() + velocity.z
        );
        projectile.alignRotationToMotion();
    }

    @Override
    public MapCodec<? extends EntityMotion> codec() {
        return CODEC;
    }
}

