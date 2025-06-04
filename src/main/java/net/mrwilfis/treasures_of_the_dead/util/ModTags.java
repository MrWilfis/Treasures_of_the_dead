package net.mrwilfis.treasures_of_the_dead.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;

public class ModTags {
    public static class Blocks {
        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> TROPHY_SKULLS = createTag("trophy_skulls");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, name));
        }
    }
}
