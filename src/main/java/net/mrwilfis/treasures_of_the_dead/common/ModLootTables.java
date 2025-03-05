package net.mrwilfis.treasures_of_the_dead.common;

import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.Set;

public class ModLootTables {

    private static final Set<ResourceLocation> LOCATIONS = Sets.newHashSet();
    private static final Set<ResourceLocation> IMMUTABLE_LOCATIONS;
    public static final ResourceLocation DEFAULT_TREASURE_CHEST_LOOT;

    static {
        IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(LOCATIONS);
        DEFAULT_TREASURE_CHEST_LOOT = register("treasures_of_the_dead:gameplay/open_treasure"); //give @p minecraft:barrel{BlockEntityTag:{LootTable:"treasures_of_the_dead:gameplay/open_treasure"}}
    }

    private static ResourceLocation register(String pId) {
        return register(new ResourceLocation(pId));
    }

    private static ResourceLocation register(ResourceLocation pId) {
        if (LOCATIONS.add(pId)) {
            return pId;
        } else {
            throw new IllegalArgumentException("" + pId + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceLocation> all() {
        return IMMUTABLE_LOCATIONS;
    }
}
