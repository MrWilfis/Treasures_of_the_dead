package net.mrwilfis.treasures_of_the_dead;

import java.util.Set;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = Treasures_of_the_dead.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    private static final ModConfigSpec.IntValue RANDOM_ADVENTURE_ITEM_DISTANCE_IN_CHUNKS;
    private static final ModConfigSpec.ConfigValue<String> CAPTAIN_NAMES_LANG;
    private static final ModConfigSpec.DoubleValue BLUNDER_BOMB_AOE_MAX_DAMAGE;
    private static final ModConfigSpec.DoubleValue BLUNDER_BOMB_ON_ENTITY_HIT_DAMAGE;
    private static final ModConfigSpec.DoubleValue BLUNDER_BOMB_PLAYER_GET_DAMAGE_MULTIPLIER;
    private static final ModConfigSpec.DoubleValue PIRATE_SKELETON_HEALTH;
    private static final ModConfigSpec.DoubleValue PIRATE_SKELETON_DAMAGE;
    private static final ModConfigSpec.DoubleValue CAPTAIN_SKELETON_HEALTH;
    private static final ModConfigSpec.DoubleValue CAPTAIN_SKELETON_DAMAGE;
    private static final ModConfigSpec.DoubleValue BLOOMING_SKELETON_HEALTH;
    private static final ModConfigSpec.DoubleValue BLOOMING_SKELETON_DAMAGE;
    private static final ModConfigSpec.DoubleValue CAPTAIN_BLOOMING_SKELETON_HEALTH;
    private static final ModConfigSpec.DoubleValue CAPTAIN_BLOOMING_SKELETON_DAMAGE;
    private static final ModConfigSpec.DoubleValue SHADOW_SKELETON_HEALTH;
    private static final ModConfigSpec.DoubleValue SHADOW_SKELETON_DAMAGE;
    private static final ModConfigSpec.DoubleValue CAPTAIN_SHADOW_SKELETON_HEALTH;
    private static final ModConfigSpec.DoubleValue CAPTAIN_SHADOW_SKELETON_DAMAGE;
    private static final ModConfigSpec.DoubleValue GOLDEN_SKELETON_HEALTH;
    private static final ModConfigSpec.DoubleValue GOLDEN_SKELETON_DAMAGE;
    private static final ModConfigSpec.DoubleValue CAPTAIN_GOLDEN_SKELETON_HEALTH;
    private static final ModConfigSpec.DoubleValue CAPTAIN_GOLDEN_SKELETON_DAMAGE;

    static {
        BUILDER.push("Treasures of the dead - Common Config!");

        RANDOM_ADVENTURE_ITEM_DISTANCE_IN_CHUNKS = BUILDER
                .comment("For big adventures you can set this parameter however you want.")
                .defineInRange("randomAdventureItemDistanceInChunks", 16, 6, 128);
        CAPTAIN_NAMES_LANG = BUILDER
                .comment("(For servers) Use the ready-made captain names for \"en_us\" or \"ru_ru\". OR, write \"custom\" and create your own captain names")
                .define("captain_names_lang", "en_us");
        BLUNDER_BOMB_AOE_MAX_DAMAGE = BUILDER
                .comment("The damage depends on the distance from the center of the bomb explosion to the entity. The maximum damage is achieved in the very center.")
                .defineInRange("blunderBombAOEMaxDamage", 15.0, 0.0, 1024);
        BLUNDER_BOMB_ON_ENTITY_HIT_DAMAGE = BUILDER
                .comment("Damage when the entity is hit directly. This is NOT additional damage! Please note that the highest damage is selected and dealt between AOE and on hit (by minecraft)")
                .defineInRange("blunderBombOnEntityHitDamage", 15.0, 0.0, 1024);
        BLUNDER_BOMB_PLAYER_GET_DAMAGE_MULTIPLIER = BUILDER
                .comment("Player Damage Modifier. Affects on all levels of difficulty")
                .defineInRange("blunderBombPlayerGetDamageMultiplier", 0.5, 0.0, 1024);

        BUILDER.push("Mobs stats");
        PIRATE_SKELETON_HEALTH = BUILDER.defineInRange("pirateSkeletonHealth", 26.0, 1.0, 1024);
        PIRATE_SKELETON_DAMAGE = BUILDER.defineInRange("pirateSkeletonDamage", 3.0, 0.0, 1024);
        CAPTAIN_SKELETON_HEALTH = BUILDER.defineInRange("captainSkeletonHealth", 70.0, 1.0, 1024);
        CAPTAIN_SKELETON_DAMAGE = BUILDER.defineInRange("captainSkeletonDamage", 4.0, 0.0, 1024);
        BLOOMING_SKELETON_HEALTH = BUILDER.defineInRange("bloomingSkeletonHealth", 26.0, 1.0, 1024);
        BLOOMING_SKELETON_DAMAGE = BUILDER.defineInRange("bloomingSkeletonDamage", 3.0, 0.0, 1024);
        CAPTAIN_BLOOMING_SKELETON_HEALTH = BUILDER.defineInRange("captainBloomingSkeletonHealth", 70.0, 1.0, 1024);
        CAPTAIN_BLOOMING_SKELETON_DAMAGE = BUILDER.defineInRange("captainBloomingSkeletonDamage", 4.0, 0.0, 1024);
        SHADOW_SKELETON_HEALTH = BUILDER.defineInRange("shadowSkeletonHealth", 22.0, 1.0, 1024);
        SHADOW_SKELETON_DAMAGE = BUILDER.defineInRange("shadowSkeletonDamage", 4.5, 0.0, 1024);
        CAPTAIN_SHADOW_SKELETON_HEALTH = BUILDER.defineInRange("captainShadowSkeletonHealth", 60.0, 1.0, 1024);
        CAPTAIN_SHADOW_SKELETON_DAMAGE = BUILDER.defineInRange("captainShadowSkeletonDamage", 5.5, 0.0, 1024);
        GOLDEN_SKELETON_HEALTH = BUILDER.defineInRange("goldenSkeletonHealth", 26.0, 1.0, 1024);
        GOLDEN_SKELETON_DAMAGE = BUILDER.defineInRange("goldenSkeletonDamage", 3.0, 0.0, 1024);
        CAPTAIN_GOLDEN_SKELETON_HEALTH = BUILDER.defineInRange("captainGoldenSkeletonHealth", 70.0, 1.0, 1024);
        CAPTAIN_GOLDEN_SKELETON_DAMAGE = BUILDER.defineInRange("captainGoldenSkeletonDamage", 4.0, 0.0, 1024);

        BUILDER.pop(2);
        SPEC = BUILDER.build();
    }


    //static final ModConfigSpec SPEC = BUILDER.build();

    public static Set<Item> items;

    public static int randomAdventureItemDistanceInChunks;
    public static String captainNamesLang;
    public static double blunderBombAOEMaxDamage;
    public static double blunderBombOnEntityHitDamage;
    public static double blunderBombPlayerGetDamageMultiplier;
    public static double pirateSkeletonHealth;
    public static double pirateSkeletonDamage;
    public static double captainSkeletonHealth;
    public static double captainSkeletonDamage;
    public static double bloomingSkeletonHealth;
    public static double bloomingSkeletonDamage;
    public static double captainBloomingSkeletonHealth;
    public static double captainBloomingSkeletonDamage;
    public static double shadowSkeletonHealth;
    public static double shadowSkeletonDamage;
    public static double captainShadowSkeletonHealth;
    public static double captainShadowSkeletonDamage;
    public static double goldenSkeletonHealth;
    public static double goldenSkeletonDamage;
    public static double captainGoldenSkeletonHealth;
    public static double captainGoldenSkeletonDamage;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {

        randomAdventureItemDistanceInChunks = RANDOM_ADVENTURE_ITEM_DISTANCE_IN_CHUNKS.get();
        captainNamesLang = CAPTAIN_NAMES_LANG.get();
        blunderBombAOEMaxDamage = BLUNDER_BOMB_AOE_MAX_DAMAGE.get();
        blunderBombOnEntityHitDamage = BLUNDER_BOMB_ON_ENTITY_HIT_DAMAGE.get();
        blunderBombPlayerGetDamageMultiplier = BLUNDER_BOMB_PLAYER_GET_DAMAGE_MULTIPLIER.get();
        pirateSkeletonHealth = PIRATE_SKELETON_HEALTH.get();
        pirateSkeletonDamage = PIRATE_SKELETON_DAMAGE.get();
        captainSkeletonHealth = CAPTAIN_SKELETON_HEALTH.get();
        captainSkeletonDamage = CAPTAIN_SKELETON_DAMAGE.get();
        bloomingSkeletonHealth = BLOOMING_SKELETON_HEALTH.get();
        bloomingSkeletonDamage = BLOOMING_SKELETON_DAMAGE.get();
        captainBloomingSkeletonHealth = CAPTAIN_BLOOMING_SKELETON_HEALTH.get();
        captainBloomingSkeletonDamage = CAPTAIN_BLOOMING_SKELETON_DAMAGE.get();
        shadowSkeletonHealth = SHADOW_SKELETON_HEALTH.get();
        shadowSkeletonDamage = SHADOW_SKELETON_DAMAGE.get();
        captainShadowSkeletonHealth = CAPTAIN_SHADOW_SKELETON_HEALTH.get();
        captainShadowSkeletonDamage = CAPTAIN_SHADOW_SKELETON_DAMAGE.get();
        goldenSkeletonHealth = GOLDEN_SKELETON_HEALTH.get();
        goldenSkeletonDamage = GOLDEN_SKELETON_DAMAGE.get();
        captainGoldenSkeletonHealth = CAPTAIN_GOLDEN_SKELETON_HEALTH.get();
        captainGoldenSkeletonDamage = CAPTAIN_GOLDEN_SKELETON_DAMAGE.get();
    }
}
