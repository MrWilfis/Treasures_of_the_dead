package net.mrwilfis.treasures_of_the_dead.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.block.ModBlocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Treasures_of_the_dead.MOD_ID);

    public static final Supplier<CreativeModeTab> TREASURES_OF_THE_DEAD = CREATIVE_MODE_TABS.register("totd_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.HATEFUL_SKULL_ITEM.get()))
                    .title(Component.translatable("creativetab.totd_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                    //    output.accept(ModItems.RUBY.get());
                    //    output.accept(ModItems.BICORN_TEST.get());

                        output.accept(ModItems.BLUE_BANDANA.get());
                        output.accept(ModItems.GREEN_BANDANA.get());
                        output.accept(ModItems.RED_BANDANA.get());

                        output.accept(ModItems.BICORN.get());
                        output.accept(ModItems.CAPTAIN_JACKET.get());
                        output.accept(ModItems.CAPTAIN_PANTS.get());

                        output.accept(ModItems.CAPTAIN_HAT.get());
                        output.accept(ModItems.CAPTAIN_CROP_VEST.get());
                        output.accept(ModItems.CAPTAIN_SKIRT.get());

                        output.accept(ModItems.BLUE_VEST.get());
                        output.accept(ModItems.BLUE_PANTS.get());
                        output.accept(ModItems.BLUE_BOOTS.get());

                        output.accept(ModItems.VEST.get());
                        output.accept(ModItems.PANTS.get());
                        output.accept(ModItems.BOOTS.get());

                        output.accept(ModItems.BLACK_VEST.get());
                        output.accept(ModItems.BLACK_PANTS.get());
                        output.accept(ModItems.BLACK_BOOTS.get());

                        output.accept(ModItems.FOUL_SKULL_ITEM.get());
                        output.accept(ModItems.DISGRACED_SKULL_ITEM.get());
                        output.accept(ModItems.HATEFUL_SKULL_ITEM.get());
                        output.accept(ModItems.VILLAINOUS_SKULL_ITEM.get());

                        output.accept(ModItems.TOTD_SKELETON_SPAWN_EGG.get());
                        output.accept(ModItems.CAPTAIN_SKELETON_SPAWN_EGG.get());
                        output.accept(ModItems.BLOOMING_SKELETON_SPAWN_EGG.get());
                        output.accept(ModItems.CAPTAIN_BLOOMING_SKELETON_SPAWN_EGG.get());
                        output.accept(ModItems.SHADOW_SKELETON_SPAWN_EGG.get());
                        output.accept(ModItems.CAPTAIN_SHADOW_SKELETON_SPAWN_EGG.get());

                   //     output.accept(ModItems.MESSAGE_IN_BOTTLE.get());
                        output.accept(ModItems.SKELETONS_ORDER.get());
                        output.accept(ModItems.SKELETON_CREW_ASSIGNMENT.get());

                        output.accept(ModItems.TREASURE_CHEST_ITEM.get());
                        output.accept(ModItems.TREASURE_KEY.get());

                        output.accept(ModItems.POWDER_KEG_ITEM.get());

                        output.accept(ModItems.ORDER_OF_SOULS_SMITHING_TEMPLATE.get());

                        output.accept(ModBlocks.SKULL_MERCHANT_TABLE.get());

                    //    output.accept(ModItems.PISTOL.get());
                    //    output.accept(ModItems.CARTRIDGE.get());


                    })
                    .build());
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
