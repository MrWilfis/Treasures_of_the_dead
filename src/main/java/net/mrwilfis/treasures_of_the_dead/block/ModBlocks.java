package net.mrwilfis.treasures_of_the_dead.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.block.custom.SkullMerchantTableBlock;
import net.mrwilfis.treasures_of_the_dead.block.custom.SmallBrazierBlock;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Treasures_of_the_dead.MOD_ID);

//    public static final RegistryObject<Block> ROTTEN_SKULL = BLOCKS.register("rotten_skull",
//            () -> new RottenSkull(BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
//    public static final RegistryObject<Block> DISGRACED_SKULL = BLOCKS.register("disgraced_skull",
//            () -> new DisgracedSkull(BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> SKULL_MERCHANT_TABLE = registryBlock("skull_merchant_table",
            () -> new SkullMerchantTableBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.5f).sound(SoundType.WOOD).ignitedByLava()));
    public static final RegistryObject<Block> SMALL_IRON_BRAZIER = registryBlock("small_iron_brazier",
            () -> new SmallBrazierBlock(1, 15, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.METAL).strength(3.5f).sound(SoundType.LANTERN)));
    public static final RegistryObject<Block> SMALL_GOLDEN_BRAZIER = registryBlock("small_golden_brazier",
            () -> new SmallBrazierBlock(1, 15, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.METAL).strength(3.5f).sound(SoundType.LANTERN)));
    public static final RegistryObject<Block> SMALL_IRON_SOUL_BRAZIER = registryBlock("small_iron_soul_brazier",
            () -> new SmallBrazierBlock(2, 10, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.METAL).strength(3.5f).sound(SoundType.LANTERN)));
    public static final RegistryObject<Block> SMALL_GOLDEN_SOUL_BRAZIER = registryBlock("small_golden_soul_brazier",
            () -> new SmallBrazierBlock(2, 10, BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.METAL).strength(3.5f).sound(SoundType.LANTERN)));



    private static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
