package net.mrwilfis.treasures_of_the_dead.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Treasures_of_the_dead.MOD_ID);

//    public static final RegistryObject<Block> ROTTEN_SKULL = BLOCKS.register("rotten_skull",
//            () -> new RottenSkull(BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
//    public static final RegistryObject<Block> DISGRACED_SKULL = BLOCKS.register("disgraced_skull",
//            () -> new DisgracedSkull(BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));



    private static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
