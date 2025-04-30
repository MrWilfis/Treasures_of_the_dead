package net.mrwilfis.treasures_of_the_dead.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.*;
import net.mrwilfis.treasures_of_the_dead.entity.custom.chestVariants.TreasureChestEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.powderKegsVariants.PowderKegEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.DisgracedSkullEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.FoulSkullEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.HatefulSkullEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.VillainousSkullEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Treasures_of_the_dead.MOD_ID);

    //SKELETONS
    public static final RegistryObject<EntityType<TOTDSkeletonEntity>> TOTD_SKELETON =
            ENTITY_TYPES.register("totd_skeleton",
                    () -> EntityType.Builder.of(TOTDSkeletonEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F)
                            .build(new ResourceLocation(Treasures_of_the_dead.MOD_ID, "totd_skeleton").toString()));
    public static final RegistryObject<EntityType<CaptainSkeletonEntity>> CAPTAIN_SKELETON =
            ENTITY_TYPES.register("captain_skeleton",
                    () -> EntityType.Builder.of(CaptainSkeletonEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F)
                            .build(new ResourceLocation(Treasures_of_the_dead.MOD_ID, "captain_skeleton").toString()));
    public static final RegistryObject<EntityType<BloomingSkeletonEntity>> BLOOMING_SKELETON =
            ENTITY_TYPES.register("blooming_skeleton",
                    () -> EntityType.Builder.of(BloomingSkeletonEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F)
                            .build(new ResourceLocation(Treasures_of_the_dead.MOD_ID, "blooming_skeleton").toString()));
    public static final RegistryObject<EntityType<CaptainBloomingSkeletonEntity>> CAPTAIN_BLOOMING_SKELETON =
            ENTITY_TYPES.register("captain_blooming_skeleton",
                    () -> EntityType.Builder.of(CaptainBloomingSkeletonEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F)
                            .build(new ResourceLocation(Treasures_of_the_dead.MOD_ID, "captain_blooming_skeleton").toString()));
    public static final RegistryObject<EntityType<ShadowSkeletonEntity>> SHADOW_SKELETON =
            ENTITY_TYPES.register("shadow_skeleton",
                    () -> EntityType.Builder.of(ShadowSkeletonEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F)
                            .build(new ResourceLocation(Treasures_of_the_dead.MOD_ID, "shadow_skeleton").toString()));
    public static final RegistryObject<EntityType<CaptainShadowSkeletonEntity>> CAPTAIN_SHADOW_SKELETON =
            ENTITY_TYPES.register("captain_shadow_skeleton",
                    () -> EntityType.Builder.of(CaptainShadowSkeletonEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F)
                            .build(new ResourceLocation(Treasures_of_the_dead.MOD_ID, "captain_shadow_skeleton").toString()));

    //TROPHY SKULLS
    public static final RegistryObject<EntityType<HatefulSkullEntity>> HATEFUL_SKULL =
            ENTITY_TYPES.register("hateful_skull",
                    () -> EntityType.Builder.of(HatefulSkullEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(new ResourceLocation(Treasures_of_the_dead.MOD_ID, "totd_skeleton").toString()));
    public static final RegistryObject<EntityType<FoulSkullEntity>> FOUL_SKULL =
            ENTITY_TYPES.register("foul_skull",
                    () -> EntityType.Builder.of(FoulSkullEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(new ResourceLocation(Treasures_of_the_dead.MOD_ID, "totd_skeleton").toString()));
    public static final RegistryObject<EntityType<DisgracedSkullEntity>> DISGRACED_SKULL =
            ENTITY_TYPES.register("disgraced_skull",
                    () -> EntityType.Builder.of(DisgracedSkullEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(new ResourceLocation(Treasures_of_the_dead.MOD_ID, "totd_skeleton").toString()));
    public static final RegistryObject<EntityType<VillainousSkullEntity>> VILLAINOUS_SKULL =
            ENTITY_TYPES.register("villainous_skull",
                    () -> EntityType.Builder.of(VillainousSkullEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build(new ResourceLocation(Treasures_of_the_dead.MOD_ID, "totd_skeleton").toString()));

    //TREASURE CHESTS
    public static final RegistryObject<EntityType<TreasureChestEntity>> TREASURE_CHEST =
            ENTITY_TYPES.register("treasure_chest",
                    () -> EntityType.Builder.of(TreasureChestEntity::new, MobCategory.MISC)
                            .sized(0.7F, 0.7F)
                            .build(new ResourceLocation(Treasures_of_the_dead.MOD_ID, "totd_skeleton").toString()));

    //POWDER KEGS
    public static final RegistryObject<EntityType<PowderKegEntity>> POWDER_KEG =
            ENTITY_TYPES.register("powder_keg",
                    () -> EntityType.Builder.of(PowderKegEntity::new, MobCategory.MISC)
                            .sized(0.7F, 0.625F)
                            .build(new ResourceLocation(Treasures_of_the_dead.MOD_ID, "powder_keg").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
