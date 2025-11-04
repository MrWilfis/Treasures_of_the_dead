package net.mrwilfis.treasures_of_the_dead.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;

import java.util.function.Supplier;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Treasures_of_the_dead.MOD_ID);

    public static final Supplier<SimpleParticleType> BLUNDER_BOMB_EXPLOSION_PARTICLES =
            PARTICLE_TYPES.register("blunder_bomb_explosion_particles", () -> new SimpleParticleType(true));
    public static final Supplier<SimpleParticleType> RUSTED_GOLDEN_SKELETON_PARTICLES =
            PARTICLE_TYPES.register("rusted_golden_skeleton_particles", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
