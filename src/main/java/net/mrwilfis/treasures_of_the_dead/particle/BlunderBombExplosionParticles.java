package net.mrwilfis.treasures_of_the_dead.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlunderBombExplosionParticles extends TextureSheetParticle {

    public boolean noFading;

    public BlunderBombExplosionParticles(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        Random rand = new Random();
        float randomValue = rand.nextFloat();
        this.noFading = randomValue > 0.5f;
        //this.noFading = true;


//        this.friction = 0.65f;
//        this.lifetime = 15;
//        this.gravity = 3.5f;
        this.quadSize = 0.025f;
        this.setSpriteFromAge(spriteSet);

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;

        if (this.noFading) {
            // Незатухающие частицы - летят дальше, быстрее падают
            this.gravity = 4.5f;
            this.friction = 0.75f;
            this.lifetime = 80 + rand.nextInt(0, 20); // Дольше живут
        } else {
            // Затухающие частицы
            this.gravity = 3.5f;
            this.friction = 0.65f;
            this.lifetime = 80;
        }

        float spreed1 = rand.nextFloat(0.95f, 1.05f);
        float spreed2 = rand.nextFloat(0.75f, 0.85f);
        double speedModifier = this.noFading ? spreed1 : spreed2;

        this.xd += xSpeed * speedModifier;
        this.yd += ySpeed * speedModifier;
        this.zd += zSpeed * speedModifier;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.noFading) {

        } else {
            if (this.age > 4) {
                this.friction = 0.95f; // Сильное замедление
                this.gravity = 0.1f; // Почти не падаем
            }
            if (this.onGround) {
                this.remove();
            }
        }
    }

    @Override
    protected int getLightColor(float partialTick) {
        if (this.noFading) {
            return 0xF000F0;
        } else {

            int fadeStartAge = 6;
            int fadeDuration = 12;

            if (this.age < fadeStartAge) {
                return 0xF000F0;
            } else if (this.age < fadeStartAge + fadeDuration) {
                float fadeProgress = (this.age - fadeStartAge + partialTick) / fadeDuration; // от 0 до 1
                fadeProgress = Math.min(fadeProgress, 1.0f);
                int lightLevel = 15 - (int)(15 * fadeProgress);
                return lightLevel << 20 | lightLevel << 4;
            } else {
                return 0;
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel,
                                       double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new BlunderBombExplosionParticles(clientLevel, pX, pY, pZ, this.spriteSet, pXSpeed, pYSpeed, pZSpeed);
        }
    }
}
