package narutoshinobicraft.client.fx.speck;

import java.util.List;
import narutoshinobicraft.client.fx.speck.runtime.look.SpeckLook;
import narutoshinobicraft.client.fx.speck.runtime.motion.SpeckMotion;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

@SuppressWarnings("null")
public class FxSpeck extends TextureSheetParticle {
    private final SpeckMotion motion;
    private final List<SpeckLook> looks;
    private final float initialScale;
    private final float initialAlpha;
    private final SpriteSet sprites;
    private final boolean isAnimated;

    protected FxSpeck(ClientLevel level, double x, double y, double z, SpeckOptions options, SpriteSet spriteSet) {
        super(level, x, y, z);
        this.rCol = options.color().x();
        this.gCol = options.color().y();
        this.bCol = options.color().z();
        this.quadSize = options.scale();
        this.lifetime = options.lifetime();
        this.motion = options.motion();
        this.looks = options.looks();
        this.initialScale = options.scale();
        this.sprites = spriteSet;
        this.isAnimated = options.isAnimated();
        this.alpha = options.alpha();
        this.initialAlpha = this.alpha;
        this.gravity = 0.0F;
        this.friction = 1.0F;
        if (this.isAnimated) {
            this.setSpriteFromAge(spriteSet);
        } else {
            this.pickSprite(spriteSet);
        }
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        if (this.motion != null) {
            this.motion.apply(this);
        } else {
            this.move(this.xd, this.yd, this.zd);
        }

        for (SpeckLook look : this.looks) {
            look.tick(this);
        }

        if (this.isAnimated) {
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public double getMotionX() { return this.xd; }
    public double getMotionY() { return this.yd; }
    public double getMotionZ() { return this.zd; }
    public int getAge() { return this.age; }
    public int getLifetime() { return this.lifetime; }
    public float getInitialAlpha() { return this.initialAlpha; }
    public float getInitialScale() { return this.initialScale; }

    public void setSpeckScale(float targetScale) {
        this.quadSize = targetScale;
    }

    public void setAlpha(float newAlpha) {
        this.alpha = newAlpha;
    }

    public void setMotion(double x, double y, double z) {
        this.xd = x;
        this.yd = y;
        this.zd = z;
    }

    public void addMotion(double x, double y, double z) {
        this.xd += x;
        this.yd += y;
        this.zd += z;
    }

    public void multiplyMotion(double multiplierX, double multiplierY, double multiplierZ) {
        this.xd *= multiplierX;
        this.yd *= multiplierY;
        this.zd *= multiplierZ;
    }

    public boolean isSpeckOnGround() {
        return this.onGround;
    }

    public void moveParticle(double dx, double dy, double dz) {
        this.move(dx, dy, dz);
    }

    public static class Provider implements ParticleProvider<SpeckOptions> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SpeckOptions options, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            FxSpeck speck = new FxSpeck(level, x, y, z, options, this.spriteSet);
            speck.setMotion(dx, dy, dz);
            return speck;
        }
    }
}
