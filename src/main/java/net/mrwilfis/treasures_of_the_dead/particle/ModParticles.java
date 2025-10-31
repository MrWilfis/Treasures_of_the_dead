package net.mrwilfis.treasures_of_the_dead.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Treasures_of_the_dead.MOD_ID);

    public static final Supplier<SimpleParticleType> BLUNDER_BOMB_EXPLOSION_PARTICLES =
            PARTICLE_TYPES.register("blunder_bomb_explosion_particles", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> RUSTED_GOLDEN_SKELETON_PARTICLES =
            PARTICLE_TYPES.register("rusted_golden_skeleton_particles", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
