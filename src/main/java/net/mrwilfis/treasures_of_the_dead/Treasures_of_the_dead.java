package net.mrwilfis.treasures_of_the_dead;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.block.ModBlocks;
import net.mrwilfis.treasures_of_the_dead.common.ModDataComponents;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.client.*;
import net.mrwilfis.treasures_of_the_dead.event.ModEvents;
import net.mrwilfis.treasures_of_the_dead.item.ModCreativeModTabs;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import net.mrwilfis.treasures_of_the_dead.villager.ModVillagers;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.nio.file.Path;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Treasures_of_the_dead.MOD_ID)
public class Treasures_of_the_dead
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "treasures_of_the_dead";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("treasures_of_the_dead.txt");

    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Treasures_of_the_dead(IEventBus modEventBus, ModContainer modContainer)
    {
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
    //    modContainer.registerConfig(ModConfig.Type.SERVER, ConfigServer.SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeModTabs.register(modEventBus);
        ModEntities.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModVillagers.register(modEventBus);


        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntities.TOTD_SKELETON.get(), TOTDSkeletonRenderer::new);
            EntityRenderers.register(ModEntities.CAPTAIN_SKELETON.get(), CaptainSkeletonRenderer::new);
            EntityRenderers.register(ModEntities.BLOOMING_SKELETON.get(), BloomingSkeletonRenderer::new);
            EntityRenderers.register(ModEntities.CAPTAIN_BLOOMING_SKELETON.get(), CaptainBloomingSkeletonRenderer::new);
            EntityRenderers.register(ModEntities.SHADOW_SKELETON.get(), ShadowSkeletonRenderer::new);
            EntityRenderers.register(ModEntities.CAPTAIN_SHADOW_SKELETON.get(), CaptainShadowSkeletonRenderer::new);

            EntityRenderers.register(ModEntities.FOUL_SKULL.get(), FoulSkullRenderer::new);
            EntityRenderers.register(ModEntities.DISGRACED_SKULL.get(), DisgracedSkullRenderer::new);
            EntityRenderers.register(ModEntities.HATEFUL_SKULL.get(), HatefulSkullRenderer::new);
            EntityRenderers.register(ModEntities.VILLAINOUS_SKULL.get(), VillainousSkullRenderer::new);

            EntityRenderers.register(ModEntities.TREASURE_CHEST.get(), TreasureChestRenderer::new);

            EntityRenderers.register(ModEntities.POWDER_KEG.get(), PowderKegRenderer::new);
        }
    }
}
