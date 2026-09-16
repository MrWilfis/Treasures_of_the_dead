package net.mrwilfis.treasures_of_the_dead.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.mrwilfis.treasures_of_the_dead.item.custom.daggers.DiamondDaggerItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.daggers.GoldenDaggerItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.daggers.IronDaggerItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.daggers.NetheriteDaggerItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;

import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;

import net.mrwilfis.treasures_of_the_dead.item.custom.*;
import net.mrwilfis.treasures_of_the_dead.item.custom.chestVariants.TreasureChestItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.guns.PistolItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.powderKegVariants.PowderKegItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.DisgracedSkullItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.FoulSkullItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.HatefulSkullItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.VillainousSkullItem;

import java.util.List;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Treasures_of_the_dead.MOD_ID);

    public static final ResourceLocation EMPTY_SLOT_CUTLASS =
            Treasures_of_the_dead.resource("item/empty_slot_cutlass");
    public static final ResourceLocation EMPTY_INGREDIENT_SLOT =
            ResourceLocation.fromNamespaceAndPath("minecraft", "item/empty_slot_diamond");

    public static final DeferredItem<Item> RUBY = ITEMS.register("ruby",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TREASURE_KEY = ITEMS.register("treasure_key",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<DoubloonPouchItem> DOUBLOON_POUCH = ITEMS.register("doubloon_pouch",
            () -> new DoubloonPouchItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DOUBLOON = ITEMS.register("doubloon",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DOUBLOON_PILE = ITEMS.register("doubloon_pile",
            () -> new Item(new Item.Properties()));

    //TROPHY SKULLS
    public static final DeferredItem<Item> FOUL_SKULL_ITEM = ITEMS.register("foul_skull",
            () -> new FoulSkullItem(new Item.Properties().stacksTo(4).rarity(Rarity.COMMON)));
    public static final DeferredItem<Item> DISGRACED_SKULL_ITEM = ITEMS.register("disgraced_skull",
            () -> new DisgracedSkullItem(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> HATEFUL_SKULL_ITEM = ITEMS.register("hateful_skull",
            () -> new HatefulSkullItem(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> VILLAINOUS_SKULL_ITEM = ITEMS.register("villainous_skull",
            () -> new VillainousSkullItem(new Item.Properties().stacksTo(4).rarity(Rarity.RARE)));
    //TREASURE CHESTS
    public static final DeferredItem<Item> TREASURE_CHEST_ITEM = ITEMS.register("treasure_chest",
            () -> new TreasureChestItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    //POWDER KEGS
    public static final DeferredItem<Item> POWDER_KEG_ITEM = ITEMS.register("powder_keg",
            () -> new PowderKegItem(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON)));

    public static final DeferredItem<ArmorItem> BICORN_TEST = ITEMS.register("bicorn_test",
            () -> new BicornArmorItem(ModArmorMaterials.BICORN, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(20))));

    //Pirate Clothes 1
    public static final DeferredItem<ArmorItem> GREEN_BANDANA = ITEMS.register("green_bandana",
            () -> new PirateClothes1ArmorItem(ModArmorMaterials.PIRATE_CLOTHES1, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(6))));
    public static final DeferredItem<ArmorItem> VEST = ITEMS.register("vest",
            () -> new PirateClothes1ArmorItem(ModArmorMaterials.PIRATE_CLOTHES1, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(6))));
    public static final DeferredItem<ArmorItem> PANTS = ITEMS.register("pants",
            () -> new PirateClothes1ArmorItem(ModArmorMaterials.PIRATE_CLOTHES1, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(6))));
    public static final DeferredItem<ArmorItem> BOOTS = ITEMS.register("boots",
            () -> new PirateClothes1ArmorItem(ModArmorMaterials.PIRATE_CLOTHES1, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(6))));

    //Pirate Clothes 2
    public static final DeferredItem<ArmorItem> RED_BANDANA = ITEMS.register("red_bandana",
            () -> new PirateClothes2ArmorItem(ModArmorMaterials.PIRATE_CLOTHES2, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(6))));
    public static final DeferredItem<ArmorItem> BLACK_VEST = ITEMS.register("black_vest",
            () -> new PirateClothes2ArmorItem(ModArmorMaterials.PIRATE_CLOTHES2, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(6))));
    public static final DeferredItem<ArmorItem> BLACK_PANTS = ITEMS.register("black_pants",
            () -> new PirateClothes2ArmorItem(ModArmorMaterials.PIRATE_CLOTHES2, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(6))));
    public static final DeferredItem<ArmorItem> BLACK_BOOTS = ITEMS.register("black_boots",
            () -> new PirateClothes2ArmorItem(ModArmorMaterials.PIRATE_CLOTHES2, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(6))));

    //Pirate Clothes 3
    public static final DeferredItem<ArmorItem> BLUE_BANDANA = ITEMS.register("blue_bandana",
            () -> new PirateClothes3ArmorItem(ModArmorMaterials.PIRATE_CLOTHES3, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(6))));
    public static final DeferredItem<ArmorItem> BLUE_VEST = ITEMS.register("blue_vest",
            () -> new PirateClothes3ArmorItem(ModArmorMaterials.PIRATE_CLOTHES3, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(6))));
    public static final DeferredItem<ArmorItem> BLUE_PANTS = ITEMS.register("blue_pants",
            () -> new PirateClothes3ArmorItem(ModArmorMaterials.PIRATE_CLOTHES3, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(6))));
    public static final DeferredItem<ArmorItem> BLUE_BOOTS = ITEMS.register("blue_boots",
            () -> new PirateClothes3ArmorItem(ModArmorMaterials.PIRATE_CLOTHES3, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(6))));

    //Captain Clothes 1
    public static final DeferredItem<ArmorItem> BICORN = ITEMS.register("bicorn",
            () -> new CaptainClothes1ArmorItem(ModArmorMaterials.CAPTAIN_CLOTHES1, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(20))));
    public static final DeferredItem<ArmorItem> CAPTAIN_JACKET = ITEMS.register("captain_jacket",
            () -> new CaptainClothes1ArmorItem(ModArmorMaterials.CAPTAIN_CLOTHES1, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(20))));
    public static final DeferredItem<ArmorItem> CAPTAIN_PANTS = ITEMS.register("captain_pants",
            () -> new CaptainClothes1ArmorItem(ModArmorMaterials.CAPTAIN_CLOTHES1, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(20))));

    //Captain Clothes 2
    public static final DeferredItem<ArmorItem> CAPTAIN_HAT = ITEMS.register("captain_hat",
            () -> new CaptainClothes2ArmorItem(ModArmorMaterials.CAPTAIN_CLOTHES1, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(20))));
    public static final DeferredItem<ArmorItem> CAPTAIN_CROP_VEST = ITEMS.register("captain_crop_vest",
            () -> new CaptainClothes2ArmorItem(ModArmorMaterials.CAPTAIN_CLOTHES1, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(20))));
    public static final DeferredItem<ArmorItem> CAPTAIN_SKIRT = ITEMS.register("captain_skirt",
            () -> new CaptainClothes2ArmorItem(ModArmorMaterials.CAPTAIN_CLOTHES2, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(20))));

    public static final DeferredItem<Item> TOTD_SKELETON_SPAWN_EGG = ITEMS.register("totd_skeleton_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.TOTD_SKELETON, 0xdcdcdc, 0x696969,  new Item.Properties()));
    public static final DeferredItem<Item> CAPTAIN_SKELETON_SPAWN_EGG = ITEMS.register("captain_skeleton_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.CAPTAIN_SKELETON, 0xdcdcdc, 0xb48820,  new Item.Properties()));
    public static final DeferredItem<Item> BLOOMING_SKELETON_SPAWN_EGG = ITEMS.register("blooming_skeleton_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.BLOOMING_SKELETON, 0xa1c99d, 0x696969,  new Item.Properties()));
    public static final DeferredItem<Item> CAPTAIN_BLOOMING_SKELETON_SPAWN_EGG = ITEMS.register("captain_blooming_skeleton_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.CAPTAIN_BLOOMING_SKELETON, 0xa1c99d, 0xb48820,  new Item.Properties()));
    public static final DeferredItem<Item> SHADOW_SKELETON_SPAWN_EGG = ITEMS.register("shadow_skeleton_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.SHADOW_SKELETON, 0x272530, 0x4b485c,  new Item.Properties()));
    public static final DeferredItem<Item> CAPTAIN_SHADOW_SKELETON_SPAWN_EGG = ITEMS.register("captain_shadow_skeleton_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.CAPTAIN_SHADOW_SKELETON, 0x272530, 0xb48820,  new Item.Properties()));
    public static final DeferredItem<Item> GOLDEN_SKELETON_SPAWN_EGG = ITEMS.register("golden_skeleton_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.GOLDEN_SKELETON, 0x5e4c14, 0xe0b73b,  new Item.Properties()));
    public static final DeferredItem<Item> CAPTAIN_GOLDEN_SKELETON_SPAWN_EGG = ITEMS.register("captain_golden_skeleton_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.CAPTAIN_GOLDEN_SKELETON, 0x5e4c14, 0xe48820,  new Item.Properties()));
    public static final DeferredItem<Item> GHOST_SPAWN_EGG = ITEMS.register("ghost_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.GHOST, 0x424242, 0xef2f2f2,  new Item.Properties()));



    public static final DeferredItem<Item> MESSAGE_IN_BOTTLE = ITEMS.register("message_in_bottle",
            () -> new RandomSpawningAdventureItem(new Item.Properties().stacksTo(1), "random_task"));
    public static final DeferredItem<Item> SKELETONS_ORDER = ITEMS.register("skeletons_order",
            () -> new RandomSpawningAdventureItem(new Item.Properties().stacksTo(1), "treasure_map"));
    public static final DeferredItem<Item> SKELETON_CREW_ASSIGNMENT = ITEMS.register("skeleton_crew_assignment",
            () -> new RandomSpawningAdventureItem(new Item.Properties().stacksTo(1), "skeleton_crew"));

    public static final DeferredItem<Item> PISTOL = ITEMS.register("pistol",
            () -> new PistolItem(new Item.Properties().durability(200), 1, 30));
    public static final DeferredItem<Item> CARTRIDGE = ITEMS.register("cartridge",
            () -> new CartridgeItem(new Item.Properties()));

    public static final DeferredItem<Item> BLUNDER_BOMB = ITEMS.register("blunder_bomb",
            () -> new BlunderBombItem(new Item.Properties()));
    public static final DeferredItem<Item> IRON_DAGGER = ITEMS.register("iron_dagger",
            () -> new IronDaggerItem((new Item.Properties()).attributes(IronDaggerItem.createAttributes(Tiers.IRON, 1.5f, -1.5F))));
    public static final DeferredItem<Item> GOLDEN_DAGGER = ITEMS.register("golden_dagger",
            () -> new GoldenDaggerItem((new Item.Properties()).attributes(GoldenDaggerItem.createAttributes(Tiers.GOLD, 1.0f, -1.5F))));
    public static final DeferredItem<Item> DIAMOND_DAGGER = ITEMS.register("diamond_dagger",
            () -> new DiamondDaggerItem((new Item.Properties()).attributes(DiamondDaggerItem.createAttributes(Tiers.DIAMOND, 2.5f, -1.5F))));
    public static final DeferredItem<Item> NETHERITE_DAGGER = ITEMS.register("netherite_dagger",
            () -> new NetheriteDaggerItem((new Item.Properties()).attributes(NetheriteDaggerItem.createAttributes(Tiers.NETHERITE, 3f, -1.5F))));

    public static final DeferredItem<Item> IRON_CUTLASS = ITEMS.register("iron_cutlass",
            () -> new CutlassItem(Tiers.IRON, (new Item.Properties()).attributes(CutlassItem.createAttributes(Tiers.IRON, 2f, -2.25F))));
    public static final DeferredItem<Item> GOLDEN_CUTLASS = ITEMS.register("golden_cutlass",
            () -> new CutlassItem(Tiers.GOLD, (new Item.Properties()).attributes(CutlassItem.createAttributes(Tiers.GOLD, 2f, -2.25F))));
    public static final DeferredItem<Item> DIAMOND_CUTLASS = ITEMS.register("diamond_cutlass",
            () -> new CutlassItem(Tiers.DIAMOND, (new Item.Properties()).attributes(CutlassItem.createAttributes(Tiers.DIAMOND, 2f, -2.25F))));
    public static final DeferredItem<Item> NETHERITE_CUTLASS = ITEMS.register("netherite_cutlass",
            () -> new CutlassItem(Tiers.NETHERITE, (new Item.Properties()).attributes(CutlassItem.createAttributes(Tiers.NETHERITE, 2f, -2.25F))));

    public static final DeferredItem<Item> IRON_CAPTAIN_CUTLASS = ITEMS.register("iron_captain_cutlass",
            () -> new CaptainCutlassItem(Tiers.IRON, (new Item.Properties()).attributes(CaptainCutlassItem.createAttributes(Tiers.IRON, 2.5f, -2.25F))));
    public static final DeferredItem<Item> GOLDEN_CAPTAIN_CUTLASS = ITEMS.register("golden_captain_cutlass",
            () -> new CaptainCutlassItem(Tiers.GOLD, (new Item.Properties()).attributes(CaptainCutlassItem.createAttributes(Tiers.GOLD, 2.5f, -2.25F))));
    public static final DeferredItem<Item> DIAMOND_CAPTAIN_CUTLASS = ITEMS.register("diamond_captain_cutlass",
            () -> new CaptainCutlassItem(Tiers.DIAMOND, (new Item.Properties()).attributes(CaptainCutlassItem.createAttributes(Tiers.DIAMOND, 2.5f, -2.25F))));
    public static final DeferredItem<Item> NETHERITE_CAPTAIN_CUTLASS = ITEMS.register("netherite_captain_cutlass",
            () -> new CaptainCutlassItem(Tiers.NETHERITE, (new Item.Properties()).attributes(CaptainCutlassItem.createAttributes(Tiers.NETHERITE, 2.5f, -2.25F))));


    public static final DeferredItem<Item> SEA_FORTRESS_KEY = ITEMS.register("sea_fortress_key",
            () -> new SeaFortressKeyItem(new Item.Properties().rarity(Rarity.UNCOMMON)));

    //Smithing templates
    public static final DeferredItem<Item> ORDER_OF_SOULS_SMITHING_TEMPLATE = ITEMS.register("order_of_souls_armor_trim_smithing_template",
            () -> SmithingTemplateItem.createArmorTrimTemplate(ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "order_of_souls")));
    public static final DeferredHolder<Item, SmithingTemplateItem> CAPTAIN_UPGRADE_SMITHING_TEMPLATE = ITEMS.register(
            "captain_upgrade_smithing_template",
            () -> new SmithingTemplateItem(
                    Component.translatable("item.treasures_of_the_dead.captain_upgrade_smithing_template.applies_to"),
                    Component.translatable("item.treasures_of_the_dead.captain_upgrade_smithing_template.ingredients"),
                    Component.translatable("item.treasures_of_the_dead.captain_upgrade_smithing_template.upgrade"),
                    Component.translatable("item.treasures_of_the_dead.captain_upgrade_smithing_template.base_slot_description"),
                    Component.translatable("item.treasures_of_the_dead.captain_upgrade_smithing_template.additions_slot_description"),
                    // Иконки для слота BASE - показываем иконку сабли
                    List.of(EMPTY_SLOT_CUTLASS),
                    // Иконки для слота ADDITION - показываем иконку слитка
                    List.of(EMPTY_INGREDIENT_SLOT)
            )
    );


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
