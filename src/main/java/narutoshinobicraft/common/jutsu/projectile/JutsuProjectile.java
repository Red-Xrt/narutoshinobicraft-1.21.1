package narutoshinobicraft.common.jutsu.projectile;

import java.util.UUID;

import javax.annotation.Nullable;

import narutoshinobicraft.common.jutsu.effect.api.EffectContext;
import narutoshinobicraft.common.jutsu.effect.api.JutsuEffect;
import narutoshinobicraft.common.jutsu.projectile.behavior.api.ProjectileBehavior;
import narutoshinobicraft.common.registry.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.syncher.SynchedEntityData;

@SuppressWarnings("null")
public class JutsuProjectile extends Entity implements ItemSupplier {
    @Nullable private ProjectileConfig config;
    @Nullable private UUID ownerUuid;
    @Nullable private Entity cachedOwner;
    private float power = 1.0f;
    private int projectileAge;
    private float scale = 1.0f;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private float lerpYRot;
    private float lerpXRot;
    private int lerpSteps;

    public JutsuProjectile(EntityType<? extends JutsuProjectile> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public static JutsuProjectile spawn(Level level, LivingEntity caster, ProjectileConfig config, float power) {
        JutsuProjectile projectile = new JutsuProjectile(EntityRegistry.JUTSU_PROJECTILE.get(), level);
        projectile.config = config;
        projectile.setOwner(caster);
        projectile.power = power;
        Vec3 eye = caster.getEyePosition();
        Vec3 look = caster.getLookAngle();
        double forward = caster.getBbWidth() * 0.5D + 0.4D;
        Vec3 start = eye.add(look.scale(forward));
        projectile.setPos(start.x, start.y, start.z);
        projectile.setYRot(caster.getYRot());
        projectile.setXRot(caster.getXRot());
        projectile.yRotO = projectile.getYRot();
        projectile.xRotO = projectile.getXRot();
        projectile.refreshDimensions();
        config.motion().start(projectile);
        for (ProjectileBehavior behavior : config.behaviors()) {
            behavior.start(projectile);
        }
        level.addFreshEntity(projectile);
        return projectile;
    }

    public Vec3 getShootVector() {
        return this.getLookAngle();
    }

    public int getProjectileAge() {
        return this.projectileAge;
    }

    public int getLifetimeTicks() {
        return this.config != null ? this.config.lifetime() : 100;
    }

    public float getPower() {
        return this.power;
    }

    public void setProjectileScale(float newScale) {
        float clamped = Math.max(0.05f, newScale);
        if (clamped != this.scale) {
            this.scale = clamped;
            this.refreshDimensions();
        }
    }

    public void alignRotationToMotion() {
        Vec3 v = this.getDeltaMovement();
        if (v.lengthSqr() < 1.0E-6D) {
            return;
        }
        double horizontal = Math.sqrt(v.x * v.x + v.z * v.z);
        this.setYRot((float) (Mth.atan2(v.x, v.z) * (180D / Math.PI)));
        this.setXRot((float) (Mth.atan2(v.y, horizontal) * (180D / Math.PI)));
    }

    public void setOwner(@Nullable Entity owner) {
        this.ownerUuid = owner == null ? null : owner.getUUID();
        this.cachedOwner = owner;
    }

    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        }
        if (this.ownerUuid != null && this.level() instanceof ServerLevel serverLevel) {
            this.cachedOwner = serverLevel.getEntity(this.ownerUuid);
            return this.cachedOwner;
        }
        return null;
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYRot = yRot;
        this.lerpXRot = xRot;
        this.lerpSteps = steps;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            if (this.lerpSteps > 0) {
                this.lerpPositionAndRotationStep(this.lerpSteps, this.lerpX, this.lerpY, this.lerpZ, this.lerpYRot, this.lerpXRot);
                this.lerpSteps--;
            }
            return;
        }
        if (this.config == null) {
            this.discard();
            return;
        }

        Vec3 from = this.position();
        this.config.motion().tick(this);
        for (ProjectileBehavior behavior : this.config.behaviors()) {
            behavior.tick(this);
        }
        Vec3 to = this.position();

        HitResult hit = resolveHit(from, to);
        if (hit.getType() != HitResult.Type.MISS) {
            onImpact(hit);
            if (this.isRemoved()) {
                return;
            }
        }

        this.projectileAge++;
        if (this.projectileAge >= this.config.lifetime()) {
            this.discard();
        }
    }

    private HitResult resolveHit(Vec3 from, Vec3 to) {
        Vec3 end = to;
        HitResult blockHit = null;
        if (this.projectileAge >= 1) {
            HitResult clip = this.level().clip(
                new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (clip.getType() != HitResult.Type.MISS) {
                blockHit = clip;
                end = clip.getLocation();
            }
        }

        AABB sweep = this.getBoundingBox().expandTowards(to.subtract(from)).inflate(0.3D);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
            this.level(), this, from, end, sweep, this::canHitEntity);
        if (entityHit != null) {
            return entityHit;
        }
        if (blockHit != null) {
            return blockHit;
        }
        return BlockHitResult.miss(to, Direction.UP, BlockPos.containing(to));
    }

    protected boolean canHitEntity(Entity entity) {
        if (!entity.isAlive() || !entity.isPickable()) {
            return false;
        }
        Entity owner = this.getOwner();
        return owner == null || !owner.equals(entity);
    }

    private void onImpact(HitResult hit) {
        if (this.config == null) {
            this.discard();
            return;
        }
        Entity target = hit instanceof EntityHitResult entityHit ? entityHit.getEntity() : null;
        for (ProjectileBehavior behavior : this.config.behaviors()) {
            behavior.onHit(this, hit);
        }
        Entity owner = this.getOwner();
        LivingEntity caster = owner instanceof LivingEntity living ? living : null;
        EffectContext context = new EffectContext(this.level(), caster, this, target, hit.getLocation(), this.power);
        for (JutsuEffect effect : this.config.onHit()) {
            effect.apply(context);
        }
        this.discard();
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.FIRE_CHARGE);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        EntityDimensions base = this.getType().getDimensions();
        return base.scale(this.scale <= 0.0f ? 1.0f : this.scale);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.projectileAge = tag.getInt("Age");
        this.power = tag.getFloat("Power");
        this.scale = tag.contains("Scale") ? tag.getFloat("Scale") : 1.0f;
        if (tag.hasUUID("Owner")) {
            this.ownerUuid = tag.getUUID("Owner");
        }
        if (tag.contains("Config")) {
            ProjectileConfig.CODEC.parse(NbtOps.INSTANCE, tag.get("Config"))
                .resultOrPartial(error -> {})
                .ifPresent(parsed -> this.config = parsed);
        }
        this.refreshDimensions();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", this.projectileAge);
        tag.putFloat("Power", this.power);
        tag.putFloat("Scale", this.scale);
        if (this.ownerUuid != null) {
            tag.putUUID("Owner", this.ownerUuid);
        }
        if (this.config != null) {
            ProjectileConfig.CODEC.encodeStart(NbtOps.INSTANCE, this.config)
                .resultOrPartial(error -> {})
                .ifPresent(encoded -> tag.put("Config", encoded));
        }
    }
}

