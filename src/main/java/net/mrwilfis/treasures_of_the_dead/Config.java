package net.mrwilfis.treasures_of_the_dead;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue RANDOM_ADVENTURE_ITEM_DISTANCE_IN_CHUNKS;
    private static final ModConfigSpec.ConfigValue<String> CAPTAIN_NAMES_LANG;
    private static final ModConfigSpec.ConfigValue<Double> PIRATE_SKELETON_HEALTH;
    private static final ModConfigSpec.ConfigValue<Double> PIRATE_SKELETON_DAMAGE;
    private static final ModConfigSpec.ConfigValue<Double> CAPTAIN_SKELETON_HEALTH;
    private static final ModConfigSpec.ConfigValue<Double> CAPTAIN_SKELETON_DAMAGE;

    static {
        BUILDER.push("Treasures of the dead - Common Config!");
        RANDOM_ADVENTURE_ITEM_DISTANCE_IN_CHUNKS = BUILDER.comment("For big adventures you can set this parameter to your distance of view. (if the value of this parameter is higher than your distance of view, than buried treasures and skeletons will spawning in bedrock)").defineInRange("randomAdventureItemDistanceInChunks", 8, 6, 128);
        CAPTAIN_NAMES_LANG = BUILDER.comment("(For servers) For now you can use only ru_ru or en_us").define("captain_names_lang", "en_us");

        BUILDER.comment("ALL BELOW IS NOT WORKING FOR NOW!");
        PIRATE_SKELETON_HEALTH = BUILDER.define("pirateSkeletonHealth", 26.0);
        PIRATE_SKELETON_DAMAGE = BUILDER.define("pirateSkeletonDamage", 3.0);
        CAPTAIN_SKELETON_HEALTH = BUILDER.define("captainSkeletonHealth", 60.0);
        CAPTAIN_SKELETON_DAMAGE = BUILDER.define("captainSkeletonDamage", 4.0);

        BUILDER.pop();

    }


    static final ModConfigSpec SPEC = BUILDER.build();

    public static Set<Item> items;

    public static int randomAdventureItemDistanceInChunks;
    public static String captainNamesLang;
    public static double pirateSkeletonHealth;
    public static double pirateSkeletonDamage;
    public static double captainSkeletonHealth;
    public static double captainSkeletonDamage;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {

        randomAdventureItemDistanceInChunks = RANDOM_ADVENTURE_ITEM_DISTANCE_IN_CHUNKS.get();
        captainNamesLang = CAPTAIN_NAMES_LANG.get();
        pirateSkeletonHealth = PIRATE_SKELETON_HEALTH.get();
        pirateSkeletonDamage = PIRATE_SKELETON_DAMAGE.get();
        captainSkeletonHealth = CAPTAIN_SKELETON_HEALTH.get();
        captainSkeletonDamage = CAPTAIN_SKELETON_DAMAGE.get();
    }
}
