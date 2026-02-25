package net.mrwilfis.treasures_of_the_dead.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class GhostParticles extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final int fadeStartTick = 10;
    private final int fadeDuration = 20;

    // Базовый цвет #66fbbf в RGB
    private static final float BASE_R = 102f / 255f;   // 0.4
    private static final float BASE_G = 251f / 255f;  // 0.984
    private static final float BASE_B = 191f / 255f;  // 0.749

    protected GhostParticles(ClientLevel level, double x, double y, double z,
                            double xSpeed, double ySpeed, double zSpeed,
                            SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;

        // Базовые настройки
        this.gravity = 0.0F;
        this.friction = 0.85F;

        // Скорость - только вверх
        this.xd = 0;
        this.yd = 0.02;
        this.zd = 0;

        // Размер 0.1
        this.quadSize = 0.25F;

        // Время жизни 30 тиков
        this.lifetime = 30;

        // Случайный выбор текстуры
        RandomSource random = level.getRandom();
        int textureIndex = random.nextInt(10);
        this.setSprite(sprites.get(textureIndex, 10));

        // Генерация случайного оттенка на основе базового цвета
        float variation = 0.2f;
        float r = BASE_R + (random.nextFloat() - 0.5f) * variation;
        float g = BASE_G + (random.nextFloat() - 0.5f) * variation;
        float b = BASE_B + (random.nextFloat() - 0.5f) * variation;

        // Ограничиваем значения в пределах 0-1
        r = Mth.clamp(r, 0.0f, 1.0f);
        g = Mth.clamp(g, 0.0f, 1.0f);
        b = Mth.clamp(b, 0.0f, 1.0f);

        this.setColor(r, g, b);
        this.alpha = 1.0f;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age <= fadeStartTick) {
            // Первые 10 тиков - постоянное свечение
            this.alpha = 1.0f;
        } else {
            int fadeAge = this.age - fadeStartTick;
            float progress = (float) fadeAge / fadeDuration;

            // Уменьшение прозрачности
            this.alpha = Mth.clamp(1.0f - progress, 0.0f, 1.0f);

            // Ускорение движения вверх
            this.yd += 0.002;

            // Уменьшение размера
            this.quadSize = 0.25F * (1.0f - progress * 0.5f);
        }
    }

    @Override
    protected int getLightColor(float partialTick) {
        int fadeStartAge = 10;
        int fadeDuration = 20;

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

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new GhostParticles(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}
