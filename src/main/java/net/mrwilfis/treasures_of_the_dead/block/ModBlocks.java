package net.mrwilfis.treasures_of_the_dead.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.block.custom.SmallBrazierBlock;
import net.mrwilfis.treasures_of_the_dead.block.custom.SkullMerchantTableBlock;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Treasures_of_the_dead.MOD_ID);

    public static final DeferredBlock<Block> SKULL_MERCHANT_TABLE = registerBlock("skull_merchant_table",
            () -> new SkullMerchantTableBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5f).sound(SoundType.WOOD).ignitedByLava()));
    public static final DeferredBlock<Block> SMALL_IRON_BRAZIER = registerBlock("small_iron_brazier",
            () -> new SmallBrazierBlock(1, 15, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.METAL).strength(3.5f).sound(SoundType.LANTERN)));
    public static final DeferredBlock<Block> SMALL_GOLDEN_BRAZIER = registerBlock("small_golden_brazier",
            () -> new SmallBrazierBlock(1, 15, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.METAL).strength(3.5f).sound(SoundType.LANTERN)));
public static final DeferredBlock<Block> SMALL_IRON_SOUL_BRAZIER = registerBlock("small_iron_soul_brazier",
            () -> new SmallBrazierBlock(2, 10, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.METAL).strength(3.5f).sound(SoundType.LANTERN)));
    public static final DeferredBlock<Block> SMALL_GOLDEN_SOUL_BRAZIER = registerBlock("small_golden_soul_brazier",
            () -> new SmallBrazierBlock(2, 10, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.METAL).strength(3.5f).sound(SoundType.LANTERN)));

    private static <T extends Block>DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn= BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
