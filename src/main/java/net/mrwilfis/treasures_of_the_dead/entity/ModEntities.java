package net.mrwilfis.treasures_of_the_dead.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.*;
import net.mrwilfis.treasures_of_the_dead.entity.custom.chestVariants.TreasureChestEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.powderKegsVariants.PowderKegEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.DisgracedSkullEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.FoulSkullEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.HatefulSkullEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.VillainousSkullEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Treasures_of_the_dead.MOD_ID);
    //SKELETONS
    public static final Supplier<EntityType<TOTDSkeletonEntity>> TOTD_SKELETON =
            ENTITY_TYPES.register("totd_skeleton",
                    () -> EntityType.Builder.of(TOTDSkeletonEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F)
                            .build("totd_skeleton"));
    public static final Supplier<EntityType<CaptainSkeletonEntity>> CAPTAIN_SKELETON =
            ENTITY_TYPES.register("captain_skeleton",
                    () -> EntityType.Builder.of(CaptainSkeletonEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F)
                            .build("captain_skeleton"));
    public static final Supplier<EntityType<BloomingSkeletonEntity>> BLOOMING_SKELETON =
            ENTITY_TYPES.register("blooming_skeleton",
                    () -> EntityType.Builder.of(BloomingSkeletonEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F)
                            .build("blooming_skeleton"));
    public static final Supplier<EntityType<CaptainBloomingSkeletonEntity>> CAPTAIN_BLOOMING_SKELETON =
            ENTITY_TYPES.register("captain_blooming_skeleton",
                    () -> EntityType.Builder.of(CaptainBloomingSkeletonEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F)
                            .build("captain_blooming_skeleton"));
    public static final Supplier<EntityType<ShadowSkeletonEntity>> SHADOW_SKELETON =
            ENTITY_TYPES.register("shadow_skeleton",
                    () -> EntityType.Builder.of(ShadowSkeletonEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F)
                            .build("shadow_skeleton"));
    public static final Supplier<EntityType<CaptainShadowSkeletonEntity>> CAPTAIN_SHADOW_SKELETON =
            ENTITY_TYPES.register("captain_shadow_skeleton",
                    () -> EntityType.Builder.of(CaptainShadowSkeletonEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F)
                            .build("captain_shadow_skeleton"));

    //TROPHY SKULLS
    public static final Supplier<EntityType<HatefulSkullEntity>> HATEFUL_SKULL =
            ENTITY_TYPES.register("hateful_skull",
                    () -> EntityType.Builder.of(HatefulSkullEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("hateful_skull"));
    public static final Supplier<EntityType<FoulSkullEntity>> FOUL_SKULL =
            ENTITY_TYPES.register("foul_skull",
                    () -> EntityType.Builder.of(FoulSkullEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("foul_skull"));
    public static final Supplier<EntityType<DisgracedSkullEntity>> DISGRACED_SKULL =
            ENTITY_TYPES.register("disgraced_skull",
                    () -> EntityType.Builder.of(DisgracedSkullEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("disgraced_skull"));
    public static final Supplier<EntityType<VillainousSkullEntity>> VILLAINOUS_SKULL =
            ENTITY_TYPES.register("villainous_skull",
                    () -> EntityType.Builder.of(VillainousSkullEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("villainous_skull"));

    //TREASURE CHESTS
    public static final Supplier<EntityType<TreasureChestEntity>> TREASURE_CHEST =
            ENTITY_TYPES.register("treasure_chest",
                    () -> EntityType.Builder.of(TreasureChestEntity::new, MobCategory.MISC)
                            .sized(0.7F, 0.7F)
                            .build("treasure_chest"));

    //POWDER KEGS
    public static final Supplier<EntityType<PowderKegEntity>> POWDER_KEG =
            ENTITY_TYPES.register("powder_keg",
                    () -> EntityType.Builder.of(PowderKegEntity::new, MobCategory.MISC)
                            .sized(0.7F, 0.625F)
                            .build("powder_keg"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
