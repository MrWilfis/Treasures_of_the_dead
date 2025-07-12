package net.mrwilfis.treasures_of_the_dead.event;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.*;
import net.mrwilfis.treasures_of_the_dead.entity.custom.chestVariants.TreasureChestEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.powderKegsVariants.PowderKegEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;


@EventBusSubscriber(modid = Treasures_of_the_dead.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.TOTD_SKELETON.get(), TOTDSkeletonEntity.setAttributes());
        event.put(ModEntities.CAPTAIN_SKELETON.get(), CaptainSkeletonEntity.setAttributes());
        event.put(ModEntities.BLOOMING_SKELETON.get(), BloomingSkeletonEntity.setAttributes());
        event.put(ModEntities.CAPTAIN_BLOOMING_SKELETON.get(), CaptainBloomingSkeletonEntity.setAttributes());
        event.put(ModEntities.SHADOW_SKELETON.get(), ShadowSkeletonEntity.setAttributes());
        event.put(ModEntities.CAPTAIN_SHADOW_SKELETON.get(), CaptainShadowSkeletonEntity.setAttributes());
        event.put(ModEntities.FOUL_SKULL.get(), AbstractSkullEntity.setAttributes());
        event.put(ModEntities.DISGRACED_SKULL.get(), AbstractSkullEntity.setAttributes());
        event.put(ModEntities.HATEFUL_SKULL.get(), AbstractSkullEntity.setAttributes());
        event.put(ModEntities.VILLAINOUS_SKULL.get(), AbstractSkullEntity.setAttributes());
        event.put(ModEntities.TREASURE_CHEST.get(), TreasureChestEntity.setAttributes());
        event.put(ModEntities.POWDER_KEG.get(), PowderKegEntity.setAttributes());
    }

    @SubscribeEvent
    public static void entitySpawnRestriction(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.TOTD_SKELETON.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.CAPTAIN_SKELETON.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.BLOOMING_SKELETON.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.CAPTAIN_BLOOMING_SKELETON.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
