package net.mrwilfis.treasures_of_the_dead.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.block.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Treasures_of_the_dead.MOD_ID);

//    public static final RegistryObject<BlockEntityType<DisgracedSkullEntity>> DISGRACED_SKULL_ENTITY =
//            BLOCK_ENTITIES.register("disgraced_skull_entity", () ->
//                    BlockEntityType.Builder.of(DisgracedSkullEntity::new,
//                            ModBlocks.DISGRACED_SKULL.get()).build(null));
//    public static final RegistryObject<BlockEntityType<RottenSkullEntity>> ROTTEN_SKULL_ENTITY =
//            BLOCK_ENTITIES.register("rotten_skull_entity", () ->
//                    BlockEntityType.Builder.of(RottenSkullEntity::new,
//                            ModBlocks.ROTTEN_SKULL.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
