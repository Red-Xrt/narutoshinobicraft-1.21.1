package narutoshinobicraft.client.fx.anchor;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public sealed interface FxAnchor permits FxAnchor.OnBody, FxAnchor.InFlight {
    static OnBody on(LivingEntity entity) {
        return new OnBody(entity);
    }

    static InFlight inFlight(Entity entity, Vec3 motion) {
        return new InFlight(entity, motion);
    }

    record OnBody(LivingEntity entity) implements FxAnchor {}

    record InFlight(Entity entity, Vec3 motion) implements FxAnchor {}
}
