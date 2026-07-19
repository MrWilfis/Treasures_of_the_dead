package net.mrwilfis.treasures_of_the_dead.util;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;

public class ModTags {
    public static class Blocks {
        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(Treasures_of_the_dead.resource(name));
        }
    }

    public static class Items {
        public static final TagKey<Item> TROPHY_SKULLS = createTag("trophy_skulls");

        public static final TagKey<Item> SMALL_IRON_BRAZIERS = createTag("small_iron_braziers");
        public static final TagKey<Item> SMALL_GOLDEN_BRAZIERS = createTag("small_golden_braziers");
        public static final TagKey<Item> SMALL_COPPER_BRAZIERS = createTag("small_copper_braziers");
        public static final TagKey<Item> EXPOSED_SMALL_COPPER_BRAZIERS = createTag("exposed_small_copper_braziers");
        public static final TagKey<Item> WEATHERED_SMALL_COPPER_BRAZIERS = createTag("weathered_small_copper_braziers");
        public static final TagKey<Item> OXIDIZED_SMALL_COPPER_BRAZIERS = createTag("oxidized_small_copper_braziers");
        public static final TagKey<Item> WAXED_SMALL_COPPER_BRAZIERS = createTag("waxed_small_copper_braziers");
        public static final TagKey<Item> WAXED_EXPOSED_SMALL_COPPER_BRAZIERS = createTag("waxed_exposed_small_copper_braziers");
        public static final TagKey<Item> WAXED_WEATHERED_SMALL_COPPER_BRAZIERS = createTag("waxed_weathered_small_copper_braziers");
        public static final TagKey<Item> WAXED_OXIDIZED_SMALL_COPPER_BRAZIERS = createTag("waxed_oxidized_small_copper_braziers");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(Treasures_of_the_dead.resource(name));
        }
    }
}
