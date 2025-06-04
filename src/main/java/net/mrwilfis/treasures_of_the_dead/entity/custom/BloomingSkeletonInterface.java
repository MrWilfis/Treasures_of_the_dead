package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public interface BloomingSkeletonInterface {

    int getTickCount();

    default void CreateBloomingParticles(Level level, RandomSource random, Vec3 position) {
        if (getTickCount() % 2 == 0) {

            double xOffset = random.nextDouble() * 0.5 - 0.25; // Случайный смещение по X
            double yOffset = random.nextDouble() * 0.5 - 0.25; // Случайный смещение по Y
            double zOffset = random.nextDouble() * 0.5 - 0.25; // Случайный смещение по Z

            double xSpeed = (random.nextDouble() * 0.5) - 0.25; // Случайная скорость по X от -0.2 до 0.2
            double ySpeed = (random.nextDouble() * 0.5); // Случайная скорость по Y от -0.2 до 0.2
            double zSpeed = (random.nextDouble() * 0.5) - 0.25; // Случайная скорость по Z от -0.2 до 0.2
            // Добавляем частицы

            if (random.nextBoolean()) {

                level.addParticle(ParticleTypes.CHERRY_LEAVES, // Тип частиц
                        position.x + xOffset, // Позиция X
                        position.y + yOffset + 1.6, // Позиция Y
                        position.z + zOffset, // Позиция Z
                        xSpeed, ySpeed + 0.1, zSpeed); // Скорость (0, 0, 0)
            } else {
                level.addParticle(ParticleTypes.SPORE_BLOSSOM_AIR, // Тип частиц
                        position.x + xOffset, // Позиция X
                        position.y + yOffset + 1.6, // Позиция Y
                        position.z + zOffset, // Позиция Z
                        xSpeed, ySpeed + 0.1, zSpeed); // Скорость (0, 0, 0)
            }
        }
    }

}
