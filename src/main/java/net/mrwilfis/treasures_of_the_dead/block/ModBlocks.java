package net.mrwilfis.treasures_of_the_dead.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.block.custom.*;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Treasures_of_the_dead.MOD_ID);
    //Points of interests
    public static final DeferredBlock<Block> SKULL_MERCHANT_TABLE = registerBlock("skull_merchant_table",
            () -> new SkullMerchantTableBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5f).sound(SoundType.WOOD).ignitedByLava()));

    //Braziers
    public static final DeferredBlock<Block> SMALL_IRON_BRAZIER = registerBlock("small_iron_brazier",
            () -> new SmallBrazierBlock(1, 15, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.METAL).strength(3.5f).sound(SoundType.LANTERN)));
    public static final DeferredBlock<Block> SMALL_IRON_SOUL_BRAZIER = registerBlock("small_iron_soul_brazier",
            () -> new SmallBrazierBlock(2, 10, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.METAL).strength(3.5f).sound(SoundType.LANTERN)));
    public static final DeferredBlock<Block> SMALL_IRON_PATINATED_BRAZIER = registerBlock("small_iron_patinated_brazier",
            () -> new SmallBrazierBlock(1, 15, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.METAL).strength(3.5f).sound(SoundType.LANTERN)));
    public static final DeferredBlock<Block> SMALL_GOLDEN_BRAZIER = registerBlock("small_golden_brazier",
            () -> new SmallBrazierBlock(1, 15, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.GOLD).strength(3.5f).sound(SoundType.LANTERN)));
    public static final DeferredBlock<Block> SMALL_GOLDEN_SOUL_BRAZIER = registerBlock("small_golden_soul_brazier",
            () -> new SmallBrazierBlock(2, 10, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.GOLD).strength(3.5f).sound(SoundType.LANTERN)));
    public static final DeferredBlock<Block> SMALL_GOLDEN_PATINATED_BRAZIER = registerBlock("small_golden_patinated_brazier",
            () -> new SmallBrazierBlock(1, 15, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.GOLD).strength(3.5f).sound(SoundType.LANTERN)));
    //All copper braziers
    public static final DeferredBlock<Block> SMALL_COPPER_BRAZIER = registerBlock("small_copper_brazier",
            () -> new WeatheringCopperSmallBrazierBlock(false, WeatheringCopper.WeatherState.UNAFFECTED,WeatheringChains.SMALL_COPPER_BRAZIER,1, 15, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.COLOR_ORANGE).strength(3.5f).sound(SoundType.LANTERN).randomTicks()));
    public static final DeferredBlock<Block> SMALL_COPPER_SOUL_BRAZIER = registerBlock("small_copper_soul_brazier",
            () -> new WeatheringCopperSmallBrazierBlock(false, WeatheringCopper.WeatherState.UNAFFECTED,WeatheringChains.SMALL_COPPER_SOUL_BRAZIER,2, 10, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.COLOR_ORANGE).strength(3.5f).sound(SoundType.LANTERN).randomTicks()));
    public static final DeferredBlock<Block> SMALL_COPPER_PATINATED_BRAZIER = registerBlock("small_copper_patinated_brazier",
            () -> new WeatheringCopperSmallBrazierBlock(false, WeatheringCopper.WeatherState.UNAFFECTED,WeatheringChains.SMALL_COPPER_PATINATED_BRAZIER,1, 15, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.COLOR_ORANGE).strength(3.5f).sound(SoundType.LANTERN).randomTicks()));
    public static final DeferredBlock<Block> EXPOSED_SMALL_COPPER_BRAZIER = registerBlock("exposed_small_copper_brazier",
            () -> new WeatheringCopperSmallBrazierBlock(false, WeatheringCopper.WeatherState.EXPOSED,WeatheringChains.SMALL_COPPER_BRAZIER,1, 15, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_BRAZIER.get()).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
    public static final DeferredBlock<Block> EXPOSED_SMALL_COPPER_SOUL_BRAZIER = registerBlock("exposed_small_copper_soul_brazier",
            () -> new WeatheringCopperSmallBrazierBlock(false, WeatheringCopper.WeatherState.EXPOSED,WeatheringChains.SMALL_COPPER_SOUL_BRAZIER,2, 10, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_SOUL_BRAZIER.get()).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
    public static final DeferredBlock<Block> EXPOSED_SMALL_COPPER_PATINATED_BRAZIER = registerBlock("exposed_small_copper_patinated_brazier",
            () -> new WeatheringCopperSmallBrazierBlock(false, WeatheringCopper.WeatherState.EXPOSED,WeatheringChains.SMALL_COPPER_PATINATED_BRAZIER,1, 15, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_PATINATED_BRAZIER.get()).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
    public static final DeferredBlock<Block> WEATHERED_SMALL_COPPER_BRAZIER = registerBlock("weathered_small_copper_brazier",
            () -> new WeatheringCopperSmallBrazierBlock(false, WeatheringCopper.WeatherState.WEATHERED,WeatheringChains.SMALL_COPPER_BRAZIER,1, 15, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_BRAZIER.get()).mapColor(MapColor.WARPED_STEM)));
    public static final DeferredBlock<Block> WEATHERED_SMALL_COPPER_SOUL_BRAZIER = registerBlock("weathered_small_copper_soul_brazier",
            () -> new WeatheringCopperSmallBrazierBlock(false, WeatheringCopper.WeatherState.WEATHERED,WeatheringChains.SMALL_COPPER_SOUL_BRAZIER,2, 10, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_SOUL_BRAZIER.get()).mapColor(MapColor.WARPED_STEM)));
    public static final DeferredBlock<Block> WEATHERED_SMALL_COPPER_PATINATED_BRAZIER = registerBlock("weathered_small_copper_patinated_brazier",
            () -> new WeatheringCopperSmallBrazierBlock(false, WeatheringCopper.WeatherState.WEATHERED,WeatheringChains.SMALL_COPPER_PATINATED_BRAZIER,1, 15, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_PATINATED_BRAZIER.get()).mapColor(MapColor.WARPED_STEM)));
    public static final DeferredBlock<Block> OXIDIZED_SMALL_COPPER_BRAZIER = registerBlock("oxidized_small_copper_brazier",
            () -> new WeatheringCopperSmallBrazierBlock(false, WeatheringCopper.WeatherState.OXIDIZED,WeatheringChains.SMALL_COPPER_BRAZIER,1, 15, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_BRAZIER.get()).mapColor(MapColor.WARPED_NYLIUM)));
    public static final DeferredBlock<Block> OXIDIZED_SMALL_COPPER_SOUL_BRAZIER = registerBlock("oxidized_small_copper_soul_brazier",
            () -> new WeatheringCopperSmallBrazierBlock(false, WeatheringCopper.WeatherState.OXIDIZED,WeatheringChains.SMALL_COPPER_SOUL_BRAZIER,2, 10, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_SOUL_BRAZIER.get()).mapColor(MapColor.WARPED_NYLIUM)));
    public static final DeferredBlock<Block> OXIDIZED_SMALL_COPPER_PATINATED_BRAZIER = registerBlock("oxidized_small_copper_patinated_brazier",
            () -> new WeatheringCopperSmallBrazierBlock(false, WeatheringCopper.WeatherState.OXIDIZED,WeatheringChains.SMALL_COPPER_PATINATED_BRAZIER,1, 15, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_PATINATED_BRAZIER.get()).mapColor(MapColor.WARPED_NYLIUM)));

    public static final DeferredBlock<Block> WAXED_SMALL_COPPER_BRAZIER = registerBlock("waxed_small_copper_brazier",
            () -> new CopperSmallBrazierBlock(true, WeatheringCopper.WeatherState.UNAFFECTED,WeatheringChains.SMALL_COPPER_BRAZIER,1, 15, BlockBehaviour.Properties.ofFullCopy(ModBlocks.SMALL_COPPER_BRAZIER.get()).mapColor(MapColor.COLOR_ORANGE)));
    public static final DeferredBlock<Block> WAXED_SMALL_COPPER_SOUL_BRAZIER = registerBlock("waxed_small_copper_soul_brazier",
            () -> new CopperSmallBrazierBlock(true, WeatheringCopper.WeatherState.UNAFFECTED,WeatheringChains.SMALL_COPPER_SOUL_BRAZIER,2, 10, BlockBehaviour.Properties.ofFullCopy(ModBlocks.SMALL_COPPER_SOUL_BRAZIER.get()).mapColor(MapColor.COLOR_ORANGE)));
    public static final DeferredBlock<Block> WAXED_SMALL_COPPER_PATINATED_BRAZIER = registerBlock("waxed_small_copper_patinated_brazier",
            () -> new CopperSmallBrazierBlock(true, WeatheringCopper.WeatherState.UNAFFECTED,WeatheringChains.SMALL_COPPER_PATINATED_BRAZIER,1, 15, BlockBehaviour.Properties.ofFullCopy(ModBlocks.SMALL_COPPER_PATINATED_BRAZIER.get()).mapColor(MapColor.COLOR_ORANGE)));
    public static final DeferredBlock<Block> WAXED_EXPOSED_SMALL_COPPER_BRAZIER = registerBlock("waxed_exposed_small_copper_brazier",
            () -> new CopperSmallBrazierBlock(true, WeatheringCopper.WeatherState.EXPOSED,WeatheringChains.SMALL_COPPER_BRAZIER,1, 15, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_BRAZIER.get()).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
    public static final DeferredBlock<Block> WAXED_EXPOSED_SMALL_COPPER_SOUL_BRAZIER = registerBlock("waxed_exposed_small_copper_soul_brazier",
            () -> new CopperSmallBrazierBlock(true, WeatheringCopper.WeatherState.EXPOSED,WeatheringChains.SMALL_COPPER_SOUL_BRAZIER,2, 10, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_SOUL_BRAZIER.get()).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
    public static final DeferredBlock<Block> WAXED_EXPOSED_SMALL_COPPER_PATINATED_BRAZIER = registerBlock("waxed_exposed_small_copper_patinated_brazier",
            () -> new CopperSmallBrazierBlock(true, WeatheringCopper.WeatherState.EXPOSED,WeatheringChains.SMALL_COPPER_PATINATED_BRAZIER,1, 15, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_PATINATED_BRAZIER.get()).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
    public static final DeferredBlock<Block> WAXED_WEATHERED_SMALL_COPPER_BRAZIER = registerBlock("waxed_weathered_small_copper_brazier",
            () -> new CopperSmallBrazierBlock(true, WeatheringCopper.WeatherState.WEATHERED,WeatheringChains.SMALL_COPPER_BRAZIER,1, 15, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_BRAZIER.get()).mapColor(MapColor.WARPED_STEM)));
    public static final DeferredBlock<Block> WAXED_WEATHERED_SMALL_COPPER_SOUL_BRAZIER = registerBlock("waxed_weathered_small_copper_soul_brazier",
            () -> new CopperSmallBrazierBlock(true, WeatheringCopper.WeatherState.WEATHERED,WeatheringChains.SMALL_COPPER_SOUL_BRAZIER,2, 10, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_SOUL_BRAZIER.get()).mapColor(MapColor.WARPED_STEM)));
    public static final DeferredBlock<Block> WAXED_WEATHERED_SMALL_COPPER_PATINATED_BRAZIER = registerBlock("waxed_weathered_small_copper_patinated_brazier",
            () -> new CopperSmallBrazierBlock(true, WeatheringCopper.WeatherState.WEATHERED,WeatheringChains.SMALL_COPPER_PATINATED_BRAZIER,1, 15, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_PATINATED_BRAZIER.get()).mapColor(MapColor.WARPED_STEM)));
    public static final DeferredBlock<Block> WAXED_OXIDIZED_SMALL_COPPER_BRAZIER = registerBlock("waxed_oxidized_small_copper_brazier",
            () -> new CopperSmallBrazierBlock(true, WeatheringCopper.WeatherState.OXIDIZED,WeatheringChains.SMALL_COPPER_BRAZIER,1, 15, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_BRAZIER.get()).mapColor(MapColor.WARPED_NYLIUM)));
    public static final DeferredBlock<Block> WAXED_OXIDIZED_SMALL_COPPER_SOUL_BRAZIER = registerBlock("waxed_oxidized_small_copper_soul_brazier",
            () -> new CopperSmallBrazierBlock(true, WeatheringCopper.WeatherState.OXIDIZED,WeatheringChains.SMALL_COPPER_SOUL_BRAZIER,2, 10, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_SOUL_BRAZIER.get()).mapColor(MapColor.WARPED_NYLIUM)));
    public static final DeferredBlock<Block> WAXED_OXIDIZED_SMALL_COPPER_PATINATED_BRAZIER = registerBlock("waxed_oxidized_small_copper_patinated_brazier",
            () -> new CopperSmallBrazierBlock(true, WeatheringCopper.WeatherState.OXIDIZED,WeatheringChains.SMALL_COPPER_PATINATED_BRAZIER,1, 15, BlockBehaviour.Properties.ofFullCopy(SMALL_COPPER_PATINATED_BRAZIER.get()).mapColor(MapColor.WARPED_NYLIUM)));

    //Raid block
    public static final DeferredBlock<Block> SEA_FORTRESS_CORE = registerBlock("sea_fortress_core",
            () -> new SeaFortressCoreBlock(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.METAL).strength(60.0f, 1000.0f).sound(SoundType.TRIAL_SPAWNER)));

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
