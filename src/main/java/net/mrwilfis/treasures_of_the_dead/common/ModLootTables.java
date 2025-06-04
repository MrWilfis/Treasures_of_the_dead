package net.mrwilfis.treasures_of_the_dead.common;

import com.google.common.collect.Sets;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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

    public ModLootTables() {

    }

    static {
        IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(LOCATIONS);
        DEFAULT_TREASURE_CHEST_LOOT = register("gameplay/open_treasure");//give @p minecraft:barrel{BlockEntityTag:{LootTable:"treasures_of_the_dead:gameplay/open_treasure"}}
        SKULL_MERCHANT_SHOP = register("chests/skull_merchant_shop/skull_merchant_shop");
    }

    private static ResourceKey<LootTable> register(String name) {
        return register(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, name)));
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
