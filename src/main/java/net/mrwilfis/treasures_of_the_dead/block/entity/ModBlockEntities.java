package net.mrwilfis.treasures_of_the_dead.block.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.block.ModBlocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Treasures_of_the_dead.MOD_ID);

public static final Supplier<BlockEntityType<SeaFortressCoreBlockEntity>> SEA_FORTRESS_CORE_BE =
        BLOCK_ENTITIES.register("sea_fortress_core_be", () -> BlockEntityType.Builder.of(
                SeaFortressCoreBlockEntity::new, ModBlocks.SEA_FORTRESS_CORE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
