package net.mrwilfis.treasures_of_the_dead.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ModLootTables {

    private static final Set<ResourceKey<LootTable>> LOCATIONS = new HashSet();
    private static final Set<ResourceKey<LootTable>> IMMUTABLE_LOCATIONS;

    public static final ResourceKey<LootTable> DEFAULT_TREASURE_CHEST_LOOT;
    public static final ResourceKey<LootTable> SKULL_MERCHANT_SHOP;
    public static final ResourceKey<LootTable> SEA_FORTRESS_OUTSIDE_FOOD_BARREL;
    public static final ResourceKey<LootTable> SEA_FORTRESS_SUPPLIES_STORAGE;
    public static final ResourceKey<LootTable> SEA_FORTRESS_POT;
    public static final ResourceKey<LootTable> SEA_FORTRESS_MAIN_HALL_FOOD_STORAGE;
    public static final ResourceKey<LootTable> SEA_FORTRESS_FUEL_STORAGE;
    public static final ResourceKey<LootTable> SEA_FORTRESS_FLOOR3_CHEST;
    public static final ResourceKey<LootTable> SEA_FORTRESS_ROOF_WAREHOUSE_CHEST;

    public ModLootTables() {

    }

    static {
        IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(LOCATIONS);
        DEFAULT_TREASURE_CHEST_LOOT = register("gameplay/open_treasure");//give @p minecraft:barrel{BlockEntityTag:{LootTable:"treasures_of_the_dead:gameplay/open_treasure"}}
        SKULL_MERCHANT_SHOP = register("chests/skull_merchant_shop/skull_merchant_shop");
        SEA_FORTRESS_OUTSIDE_FOOD_BARREL = register("chests/sea_fortress/outside_food_barrel");
        SEA_FORTRESS_SUPPLIES_STORAGE = register("chests/sea_fortress/supplies_storage");
        SEA_FORTRESS_POT = register("chests/sea_fortress/pot");
        SEA_FORTRESS_MAIN_HALL_FOOD_STORAGE =  register("chests/sea_fortress/main_hall_food_storage");
        SEA_FORTRESS_FUEL_STORAGE =  register("chests/sea_fortress/fuel_storage");
        SEA_FORTRESS_FLOOR3_CHEST =  register("chests/sea_fortress/floor3_chest");
        SEA_FORTRESS_ROOF_WAREHOUSE_CHEST =  register("chests/sea_fortress/roof_warehouse_chest");
    }

    private static ResourceKey<LootTable> register(String name) {
        return register(ResourceKey.create(Registries.LOOT_TABLE, Treasures_of_the_dead.resource(name)));
    }

    private static ResourceKey<LootTable> register(ResourceKey<LootTable> name) {
        if (LOCATIONS.add(name)) {
            return name;
        } else {
            throw new IllegalArgumentException(String.valueOf(name.location()) + " is already a registered built-in loot table (TOTD)");
        }

    }

    public static Set<ResourceKey<LootTable>> all() {
        return IMMUTABLE_LOCATIONS;
    }
}
