package net.mrwilfis.treasures_of_the_dead;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = Treasures_of_the_dead.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    private static final ForgeConfigSpec.IntValue RANDOM_ADVENTURE_ITEM_DISTANCE_IN_CHUNKS;
    private static final ForgeConfigSpec.ConfigValue<String> CAPTAIN_NAMES_LANG;
    private static final ForgeConfigSpec.ConfigValue<Double> PIRATE_SKELETON_HEALTH;
    private static final ForgeConfigSpec.ConfigValue<Double> PIRATE_SKELETON_DAMAGE;
    private static final ForgeConfigSpec.ConfigValue<Double> CAPTAIN_SKELETON_HEALTH;
    private static final ForgeConfigSpec.ConfigValue<Double> CAPTAIN_SKELETON_DAMAGE;

    static {
        BUILDER.push("Treasures of the dead - Common Config!");

        RANDOM_ADVENTURE_ITEM_DISTANCE_IN_CHUNKS = BUILDER
                .comment("For big adventures you can set this parameter to your distance of view. (if the value of this parameter is higher than your distance of view, than buried treasures and skeletons will spawning in bedrock)")
                .defineInRange("randomAdventureItemDistanceInChunks", 8, 6, 128);
        CAPTAIN_NAMES_LANG = BUILDER
                .comment("(For servers) For now you can use only ru_ru or en_us")
                .define("captain_names_lang", "en_us");

        BUILDER.comment("ALL BELOW IS NOT WORKING FOR NOW!");
        PIRATE_SKELETON_HEALTH = BUILDER.define("pirateSkeletonHealth", 26.0);
        PIRATE_SKELETON_DAMAGE = BUILDER.define("pirateSkeletonDamage", 3.0);
        CAPTAIN_SKELETON_HEALTH = BUILDER.define("captainSkeletonHealth", 70.0);
        CAPTAIN_SKELETON_DAMAGE = BUILDER.define("captainSkeletonDamage", 4.0);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static Set<Item> items;

    public static int randomAdventureItemDistanceInChunks;
    public static String captainNamesLang;
    public static double pirateSkeletonHealth;
    public static double pirateSkeletonDamage;
    public static double captainSkeletonHealth;
    public static double captainSkeletonDamage;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
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
