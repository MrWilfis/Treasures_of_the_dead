package net.mrwilfis.treasures_of_the_dead.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;

import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;

import net.mrwilfis.treasures_of_the_dead.item.custom.*;
import net.mrwilfis.treasures_of_the_dead.item.custom.chestVariants.TreasureChestItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.guns.PistolItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.DisgracedSkullItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.FoulSkullItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.HatefulSkullItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.VillainousSkullItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Treasures_of_the_dead.MOD_ID);

    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TREASURE_KEY = ITEMS.register("treasure_key",
            () -> new Item(new Item.Properties()));

    //TROPHY SKULLS
    public static final RegistryObject<Item> FOUL_SKULL_ITEM = ITEMS.register("foul_skull",
            () -> new FoulSkullItem(new Item.Properties().stacksTo(4).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DISGRACED_SKULL_ITEM = ITEMS.register("disgraced_skull",
            () -> new DisgracedSkullItem(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> HATEFUL_SKULL_ITEM = ITEMS.register("hateful_skull",
            () -> new HatefulSkullItem(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> VILLAINOUS_SKULL_ITEM = ITEMS.register("villainous_skull",
            () -> new VillainousSkullItem(new Item.Properties().stacksTo(4).rarity(Rarity.RARE)));
    //TREASURE CHESTS
    public static final RegistryObject<Item> TREASURE_CHEST_ITEM = ITEMS.register("treasure_chest",
            () -> new TreasureChestItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> BICORN_TEST = ITEMS.register("bicorn_test",
            () -> new BicornArmorItem(ModArmorMaterials.BICORN, ArmorItem.Type.HELMET, new Item.Properties()));

    //Pirate Clothes 1
    public static final RegistryObject<Item> GREEN_BANDANA = ITEMS.register("green_bandana",
            () -> new PirateClothes1ArmorItem(ModArmorMaterials.PIRATE_CLOTHES1, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> VEST = ITEMS.register("vest",
            () -> new PirateClothes1ArmorItem(ModArmorMaterials.PIRATE_CLOTHES1, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> PANTS = ITEMS.register("pants",
            () -> new PirateClothes1ArmorItem(ModArmorMaterials.PIRATE_CLOTHES1, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> BOOTS = ITEMS.register("boots",
            () -> new PirateClothes1ArmorItem(ModArmorMaterials.PIRATE_CLOTHES1, ArmorItem.Type.BOOTS, new Item.Properties()));

    //Pirate Clothes 2
    public static final RegistryObject<Item> RED_BANDANA = ITEMS.register("red_bandana",
            () -> new PirateClothes2ArmorItem(ModArmorMaterials.PIRATE_CLOTHES2, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> BLACK_VEST = ITEMS.register("black_vest",
            () -> new PirateClothes2ArmorItem(ModArmorMaterials.PIRATE_CLOTHES2, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> BLACK_PANTS = ITEMS.register("black_pants",
            () -> new PirateClothes2ArmorItem(ModArmorMaterials.PIRATE_CLOTHES2, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> BLACK_BOOTS = ITEMS.register("black_boots",
            () -> new PirateClothes2ArmorItem(ModArmorMaterials.PIRATE_CLOTHES2, ArmorItem.Type.BOOTS, new Item.Properties()));

    //Pirate Clothes 3
    public static final RegistryObject<Item> BLUE_BANDANA = ITEMS.register("blue_bandana",
            () -> new PirateClothes3ArmorItem(ModArmorMaterials.PIRATE_CLOTHES3, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> BLUE_VEST = ITEMS.register("blue_vest",
            () -> new PirateClothes3ArmorItem(ModArmorMaterials.PIRATE_CLOTHES3, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> BLUE_PANTS = ITEMS.register("blue_pants",
            () -> new PirateClothes3ArmorItem(ModArmorMaterials.PIRATE_CLOTHES3, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> BLUE_BOOTS = ITEMS.register("blue_boots",
            () -> new PirateClothes3ArmorItem(ModArmorMaterials.PIRATE_CLOTHES3, ArmorItem.Type.BOOTS, new Item.Properties()));

    //Captain Clothes 1
    public static final RegistryObject<Item> BICORN = ITEMS.register("bicorn",
            () -> new CaptainClothes1ArmorItem(ModArmorMaterials.CAPTAIN_CLOTHES1, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> CAPTAIN_JACKET = ITEMS.register("captain_jacket",
            () -> new CaptainClothes1ArmorItem(ModArmorMaterials.CAPTAIN_CLOTHES1, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> CAPTAIN_PANTS = ITEMS.register("captain_pants",
            () -> new CaptainClothes1ArmorItem(ModArmorMaterials.CAPTAIN_CLOTHES1, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    //Captain Clothes 2
    public static final RegistryObject<Item> CAPTAIN_HAT = ITEMS.register("captain_hat",
            () -> new CaptainClothes2ArmorItem(ModArmorMaterials.CAPTAIN_CLOTHES1, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> CAPTAIN_CROP_VEST = ITEMS.register("captain_crop_vest",
            () -> new CaptainClothes2ArmorItem(ModArmorMaterials.CAPTAIN_CLOTHES1, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> CAPTAIN_SKIRT = ITEMS.register("captain_skirt",
            () -> new CaptainClothes2ArmorItem(ModArmorMaterials.CAPTAIN_CLOTHES2, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<Item> TOTD_SKELETON_SPAWN_EGG = ITEMS.register("totd_skeleton_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.TOTD_SKELETON, 0xdcdcdc, 0x696969,  new Item.Properties()));
    public static final RegistryObject<Item> CAPTAIN_SKELETON_SPAWN_EGG = ITEMS.register("captain_skeleton_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.CAPTAIN_SKELETON, 0xdcdcdc, 0xb48820,  new Item.Properties()));
    public static final RegistryObject<Item> BLOOMING_SKELETON_SPAWN_EGG = ITEMS.register("blooming_skeleton_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.BLOOMING_SKELETON, 0xa1c99d, 0x696969,  new Item.Properties()));


    public static final RegistryObject<Item> MESSAGE_IN_BOTTLE = ITEMS.register("message_in_bottle",
            () -> new RandomSpawningAdventureItem(new Item.Properties().stacksTo(1), 128, "random_task"));
    public static final RegistryObject<Item> SKELETONS_ORDER = ITEMS.register("skeletons_order",
            () -> new RandomSpawningAdventureItem(new Item.Properties().stacksTo(1), 128, "treasure_map"));

    public static final RegistryObject<Item> PISTOL = ITEMS.register("pistol",
            () -> new PistolItem(new Item.Properties().durability(200), 1, 30));
    public static final RegistryObject<Item> CARTRIDGE = ITEMS.register("cartridge",
            () -> new CartridgeItem(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
