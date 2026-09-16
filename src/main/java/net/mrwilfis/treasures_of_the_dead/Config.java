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
    private static final ModConfigSpec.IntValue BLUNDER_BOMB_COOLDOWN_TICKS;
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

    private static final ModConfigSpec.DoubleValue IRON_DAGGER_DAMAGE_MODIFIER;
    private static final ModConfigSpec.DoubleValue IRON_DAGGER_BREAK_CHANCE;
    private static final ModConfigSpec.IntValue IRON_DAGGER_MAX_STACK_SIZE;
    private static final ModConfigSpec.IntValue IRON_DAGGER_MAX_CHARGE_TIME_TICKS;
    private static final ModConfigSpec.IntValue IRON_DAGGER_COOLDOWN_TICKS;
    private static final ModConfigSpec.DoubleValue IRON_DAGGER_MIN_SPEED;
    private static final ModConfigSpec.DoubleValue IRON_DAGGER_MAX_SPEED;

    private static final ModConfigSpec.DoubleValue GOLDEN_DAGGER_DAMAGE_MODIFIER;
    private static final ModConfigSpec.DoubleValue GOLDEN_DAGGER_BREAK_CHANCE;
    private static final ModConfigSpec.IntValue GOLDEN_DAGGER_MAX_STACK_SIZE;
    private static final ModConfigSpec.IntValue GOLDEN_DAGGER_MAX_CHARGE_TIME_TICKS;
    private static final ModConfigSpec.IntValue GOLDEN_DAGGER_COOLDOWN_TICKS;
    private static final ModConfigSpec.DoubleValue GOLDEN_DAGGER_MIN_SPEED;
    private static final ModConfigSpec.DoubleValue GOLDEN_DAGGER_MAX_SPEED;

    private static final ModConfigSpec.DoubleValue DIAMOND_DAGGER_DAMAGE_MODIFIER;
    private static final ModConfigSpec.DoubleValue DIAMOND_DAGGER_BREAK_CHANCE;
    private static final ModConfigSpec.IntValue DIAMOND_DAGGER_MAX_STACK_SIZE;
    private static final ModConfigSpec.IntValue DIAMOND_DAGGER_MAX_CHARGE_TIME_TICKS;
    private static final ModConfigSpec.IntValue DIAMOND_DAGGER_COOLDOWN_TICKS;
    private static final ModConfigSpec.DoubleValue DIAMOND_DAGGER_MIN_SPEED;
    private static final ModConfigSpec.DoubleValue DIAMOND_DAGGER_MAX_SPEED;

    private static final ModConfigSpec.DoubleValue NETHERITE_DAGGER_DAMAGE_MODIFIER;
    private static final ModConfigSpec.DoubleValue NETHERITE_DAGGER_BREAK_CHANCE;
    private static final ModConfigSpec.IntValue NETHERITE_DAGGER_MAX_STACK_SIZE;
    private static final ModConfigSpec.IntValue NETHERITE_DAGGER_MAX_CHARGE_TIME_TICKS;
    private static final ModConfigSpec.IntValue NETHERITE_DAGGER_COOLDOWN_TICKS;
    private static final ModConfigSpec.DoubleValue NETHERITE_DAGGER_MIN_SPEED;
    private static final ModConfigSpec.DoubleValue NETHERITE_DAGGER_MAX_SPEED;

    private static final ModConfigSpec.IntValue CUTLASS_DASH_COOLDOWN_TICKS;
    private static final ModConfigSpec.IntValue CUTLASS_BLOCK_COOLDOWN_TICKS;
    private static final ModConfigSpec.DoubleValue CUTLASS_DASH_POWER;
    private static final ModConfigSpec.DoubleValue CUTLASS_VERTICAL_DASH_MULTIPLIER;
    private static final ModConfigSpec.DoubleValue CUTLASS_BLOCK_KNOCKBACK_DEFENDER_POWER;
    private static final ModConfigSpec.DoubleValue CUTLASS_BLOCK_KNOCKBACK_DEFENDER_VERTICAL_POWER;
    private static final ModConfigSpec.DoubleValue CUTLASS_BLOCK_KNOCKBACK_ATTACKER_POWER;
    private static final ModConfigSpec.DoubleValue CUTLASS_BLOCK_KNOCKBACK_ATTACKER_VERTICAL_POWER;

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
        BLUNDER_BOMB_COOLDOWN_TICKS = BUILDER.defineInRange("blunderBombCooldownTicks", 10, 0, 128);

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

        BUILDER.pop(1);

        BUILDER.push("Daggers characteristics");
        BUILDER.push("Iron Dagger");
        IRON_DAGGER_DAMAGE_MODIFIER = BUILDER.defineInRange("ironDaggerDamageModifier", 3.75, 0, 128);
        IRON_DAGGER_BREAK_CHANCE = BUILDER.defineInRange("ironDaggerBreakChance", 0.095, -1, 128);
        IRON_DAGGER_MAX_STACK_SIZE = BUILDER.defineInRange("ironDaggerMaxStackSize", 48, 1, 128);
        IRON_DAGGER_MAX_CHARGE_TIME_TICKS = BUILDER.defineInRange("ironDaggerMaxChargeTimeTicks", 15, 1, 128);
        IRON_DAGGER_COOLDOWN_TICKS = BUILDER.defineInRange("ironDaggerCooldownTicks", 5, 0, 128);
        IRON_DAGGER_MIN_SPEED = BUILDER.defineInRange("ironDaggerMinSpeed", 0.125, 0, 128);
        IRON_DAGGER_MAX_SPEED = BUILDER.defineInRange("ironDaggerMaxSpeed", 2.5, 0, 128);
        BUILDER.pop();

        BUILDER.push("Golden Dagger");
        GOLDEN_DAGGER_DAMAGE_MODIFIER = BUILDER.defineInRange("goldenDaggerDamageModifier", 2.6, 0, 128);
        GOLDEN_DAGGER_BREAK_CHANCE = BUILDER.defineInRange("goldenDaggerBreakChance", 0.12, -1, 128);
        GOLDEN_DAGGER_MAX_STACK_SIZE = BUILDER.defineInRange("goldenDaggerMaxStackSize", 64, 1, 128);
        GOLDEN_DAGGER_MAX_CHARGE_TIME_TICKS = BUILDER.defineInRange("goldenDaggerMaxChargeTimeTicks", 12, 1, 128);
        GOLDEN_DAGGER_COOLDOWN_TICKS = BUILDER.defineInRange("goldenDaggerCooldownTicks", 2, 0, 128);
        GOLDEN_DAGGER_MIN_SPEED = BUILDER.defineInRange("goldenDaggerMinSpeed", 0.25, 0, 128);
        GOLDEN_DAGGER_MAX_SPEED = BUILDER.defineInRange("goldenDaggerMaxSpeed", 3.0, 0, 128);
        BUILDER.pop();

        BUILDER.push("Diamond Dagger");
        DIAMOND_DAGGER_DAMAGE_MODIFIER = BUILDER.defineInRange("diamondDaggerDamageModifier", 8.75, 0, 128);
        DIAMOND_DAGGER_BREAK_CHANCE = BUILDER.defineInRange("diamondDaggerBreakChance", 0.03, -1, 128);
        DIAMOND_DAGGER_MAX_STACK_SIZE = BUILDER.defineInRange("diamondDaggerMaxStackSize", 24, 1, 128);
        DIAMOND_DAGGER_MAX_CHARGE_TIME_TICKS = BUILDER.defineInRange("diamondDaggerMaxChargeTimeTicks", 20, 1, 128);
        DIAMOND_DAGGER_COOLDOWN_TICKS = BUILDER.defineInRange("diamondDaggerCooldownTicks", 14, 0, 128);
        DIAMOND_DAGGER_MIN_SPEED = BUILDER.defineInRange("diamondDaggerMinSpeed", 0.125, 0, 128);
        DIAMOND_DAGGER_MAX_SPEED = BUILDER.defineInRange("diamondDaggerMaxSpeed", 2.5, 0, 128);
        BUILDER.pop();

        BUILDER.push("Netherite Dagger");
        NETHERITE_DAGGER_DAMAGE_MODIFIER = BUILDER.defineInRange("netheriteDaggerDamageModifier", 11.5, 0, 128);
        NETHERITE_DAGGER_BREAK_CHANCE = BUILDER.defineInRange("netheriteDaggerBreakChance", 0.02, -1, 128);
        NETHERITE_DAGGER_MAX_STACK_SIZE = BUILDER.defineInRange("netheriteDaggerMaxStackSize", 12, 1, 128);
        NETHERITE_DAGGER_MAX_CHARGE_TIME_TICKS = BUILDER.defineInRange("netheriteDaggerMaxChargeTimeTicks", 26, 1, 128);
        NETHERITE_DAGGER_COOLDOWN_TICKS = BUILDER.defineInRange("netheriteDaggerCooldownTicks", 20, 0, 128);
        NETHERITE_DAGGER_MIN_SPEED = BUILDER.defineInRange("netheriteDaggerMinSpeed", 0.125, 0, 128);
        NETHERITE_DAGGER_MAX_SPEED = BUILDER.defineInRange("netheriteDaggerMaxSpeed", 2.5, 0, 128);
        BUILDER.pop();
        BUILDER.pop();

        BUILDER.push("Cutlass settings");
        CUTLASS_DASH_COOLDOWN_TICKS = BUILDER.defineInRange("cutlassDashCooldownTicks", 30, 0, 128);
        CUTLASS_BLOCK_COOLDOWN_TICKS = BUILDER.defineInRange("cutlassBlockCooldownTicks", 70, 0, 128);
        CUTLASS_DASH_POWER = BUILDER.defineInRange("cutlassDashPower", 0.775, 0, 4);
        CUTLASS_VERTICAL_DASH_MULTIPLIER = BUILDER.defineInRange("cutlassDashMultiplier", 0.275, 0, 4);
        CUTLASS_BLOCK_KNOCKBACK_DEFENDER_POWER = BUILDER.defineInRange("cutlassBlockKnockbackDefenderPower", 1.0, 0, 4);
        CUTLASS_BLOCK_KNOCKBACK_DEFENDER_VERTICAL_POWER = BUILDER.defineInRange("cutlassBlockKnockbackDefenderVerticalPower", 0.375, 0, 4);
        CUTLASS_BLOCK_KNOCKBACK_ATTACKER_POWER = BUILDER.defineInRange("cutlassBlockKnockbackAttackerPower", 0.75, 0, 4);
        CUTLASS_BLOCK_KNOCKBACK_ATTACKER_VERTICAL_POWER = BUILDER.defineInRange("cutlassBlockKnockbackAttackerVerticalPower", 0.325, 0, 4);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }


    //static final ModConfigSpec SPEC = BUILDER.build();

    public static Set<Item> items;

    public static int randomAdventureItemDistanceInChunks;
    public static String captainNamesLang;
    public static double blunderBombAOEMaxDamage;
    public static double blunderBombOnEntityHitDamage;
    public static double blunderBombPlayerGetDamageMultiplier;
    public static int blunderBombCooldownTicks;
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

    public static double ironDaggerDamageModifier;
    public static double ironDaggerBreakChance;
    public static int ironDaggerMaxStackSize;
    public static int ironDaggerMaxChargeTimeTicks;
    public static int ironDaggerCooldownTicks;
    public static double ironDaggerMinSpeed;
    public static double ironDaggerMaxSpeed;

    public static double goldenDaggerDamageModifier;
    public static double goldenDaggerBreakChance;
    public static int goldenDaggerMaxStackSize;
    public static int goldenDaggerMaxChargeTimeTicks;
    public static int goldenDaggerCooldownTicks;
    public static double goldenDaggerMinSpeed;
    public static double goldenDaggerMaxSpeed;

    public static double diamondDaggerDamageModifier;
    public static double diamondDaggerBreakChance;
    public static int diamondDaggerMaxStackSize;
    public static int diamondDaggerMaxChargeTimeTicks;
    public static int diamondDaggerCooldownTicks;
    public static double diamondDaggerMinSpeed;
    public static double diamondDaggerMaxSpeed;

    public static double netheriteDaggerDamageModifier;
    public static double netheriteDaggerBreakChance;
    public static int netheriteDaggerMaxStackSize;
    public static int netheriteDaggerMaxChargeTimeTicks;
    public static int netheriteDaggerCooldownTicks;
    public static double netheriteDaggerMinSpeed;
    public static double netheriteDaggerMaxSpeed;

    public static int cutlassDashCooldownTicks;
    public static int cutlassBlockCooldownTicks;
    public static double cutlassDashPower;
    public static double cutlassVerticalDashMultiplier;
    public static double cutlassBlockKnockbackDefenderPower;
    public static double cutlassBlockKnockbackDefenderVerticalPower;
    public static double cutlassBlockKnockbackAttackerPower;
    public static double cutlassBlockKnockbackAttackerVerticalPower;

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
        blunderBombCooldownTicks = BLUNDER_BOMB_COOLDOWN_TICKS.get();
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

        ironDaggerDamageModifier = IRON_DAGGER_DAMAGE_MODIFIER.get();
        ironDaggerBreakChance = IRON_DAGGER_BREAK_CHANCE.get();
        ironDaggerMaxStackSize = IRON_DAGGER_MAX_STACK_SIZE.get();
        ironDaggerMaxChargeTimeTicks = IRON_DAGGER_MAX_CHARGE_TIME_TICKS.get();
        ironDaggerCooldownTicks = IRON_DAGGER_COOLDOWN_TICKS.get();
        ironDaggerMinSpeed = IRON_DAGGER_MIN_SPEED.get();
        ironDaggerMaxSpeed = IRON_DAGGER_MAX_SPEED.get();

        goldenDaggerDamageModifier = GOLDEN_DAGGER_DAMAGE_MODIFIER.get();
        goldenDaggerBreakChance = GOLDEN_DAGGER_BREAK_CHANCE.get();
        goldenDaggerMaxStackSize = GOLDEN_DAGGER_MAX_STACK_SIZE.get();
        goldenDaggerMaxChargeTimeTicks = GOLDEN_DAGGER_MAX_CHARGE_TIME_TICKS.get();
        goldenDaggerCooldownTicks = GOLDEN_DAGGER_COOLDOWN_TICKS.get();
        goldenDaggerMinSpeed = GOLDEN_DAGGER_MIN_SPEED.get();
        goldenDaggerMaxSpeed = GOLDEN_DAGGER_MAX_SPEED.get();

        diamondDaggerDamageModifier = DIAMOND_DAGGER_DAMAGE_MODIFIER.get();
        diamondDaggerBreakChance = DIAMOND_DAGGER_BREAK_CHANCE.get();
        diamondDaggerMaxStackSize = DIAMOND_DAGGER_MAX_STACK_SIZE.get();
        diamondDaggerMaxChargeTimeTicks = DIAMOND_DAGGER_MAX_CHARGE_TIME_TICKS.get();
        diamondDaggerCooldownTicks = DIAMOND_DAGGER_COOLDOWN_TICKS.get();
        diamondDaggerMinSpeed = DIAMOND_DAGGER_MIN_SPEED.get();
        diamondDaggerMaxSpeed = DIAMOND_DAGGER_MAX_SPEED.get();

        netheriteDaggerDamageModifier = NETHERITE_DAGGER_DAMAGE_MODIFIER.get();
        netheriteDaggerBreakChance = NETHERITE_DAGGER_BREAK_CHANCE.get();
        netheriteDaggerMaxStackSize = NETHERITE_DAGGER_MAX_STACK_SIZE.get();
        netheriteDaggerMaxChargeTimeTicks = NETHERITE_DAGGER_MAX_CHARGE_TIME_TICKS.get();
        netheriteDaggerCooldownTicks = NETHERITE_DAGGER_COOLDOWN_TICKS.get();
        netheriteDaggerMinSpeed = NETHERITE_DAGGER_MIN_SPEED.get();
        netheriteDaggerMaxSpeed = NETHERITE_DAGGER_MAX_SPEED.get();

        cutlassDashCooldownTicks = CUTLASS_DASH_COOLDOWN_TICKS.get();
        cutlassBlockCooldownTicks = CUTLASS_BLOCK_COOLDOWN_TICKS.get();
        cutlassDashPower = CUTLASS_DASH_POWER.get();
        cutlassVerticalDashMultiplier = CUTLASS_VERTICAL_DASH_MULTIPLIER.get();
        cutlassBlockKnockbackDefenderPower = CUTLASS_BLOCK_KNOCKBACK_DEFENDER_POWER.get();
        cutlassBlockKnockbackDefenderVerticalPower = CUTLASS_BLOCK_KNOCKBACK_DEFENDER_VERTICAL_POWER.get();
        cutlassBlockKnockbackAttackerPower = CUTLASS_BLOCK_KNOCKBACK_ATTACKER_POWER.get();
        cutlassBlockKnockbackAttackerVerticalPower = CUTLASS_BLOCK_KNOCKBACK_ATTACKER_VERTICAL_POWER.get();
    }
}
