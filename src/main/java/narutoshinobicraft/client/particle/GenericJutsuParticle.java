package narutoshinobicraft.client.particle;

import narutoshinobicraft.client.particle.api.ParticleMotion;
import narutoshinobicraft.common.data.record.particles.JutsuParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;

@SuppressWarnings("null")
public class GenericJutsuParticle extends TextureSheetParticle {
    private final ParticleMotion motion;

    protected GenericJutsuParticle(ClientLevel level, double x, double y, double z, JutsuParticleOptions options, SpriteSet spriteSet) {
        super(level, x, y, z);
        this.rCol = options.color().x();
        this.gCol = options.color().y();
        this.bCol = options.color().z();
        this.quadSize = options.scale();
        this.lifetime = options.lifetime();
        this.motion = options.motion();
        this.pickSprite(spriteSet);
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
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public double getMotionX() { return this.xd; }
    public double getMotionY() { return this.yd; }
    public double getMotionZ() { return this.zd; }
    public double getOldMotionX() { return this.xo; }
    public double getOldMotionY() { return this.yo; }
    public double getOldMotionZ() { return this.zo; }
    public int getAge() { return this.age; }
    public int getLifeTime() { return this.lifetime; }
    
    public void setMotion(double x, double y, double z) {
        this.xd = x;
        this.yd = y;
        this.zd = z;
    }

    public void setOldMotion(double x, double y, double z){
        this.xo = x;
        this.yo = y;
        this.zo = z;
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

    public boolean isParticleOnGround() { 
        return this.onGround; 
    }

    public void moveParticle(double dx, double dy, double dz) {
        this.move(dx, dy, dz);
    }

    public static class Provider implements ParticleProvider<JutsuParticleOptions> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(JutsuParticleOptions options, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            GenericJutsuParticle particle = new GenericJutsuParticle(level, x, y, z, options, this.spriteSet);
            particle.setMotion(dx, dy, dz);
            return particle;
        }
    }
}
