package net.mrwilfis.treasures_of_the_dead;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.mrwilfis.treasures_of_the_dead.block.ModBlocks;
import net.mrwilfis.treasures_of_the_dead.block.entity.ModBlockEntities;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.client.*;
import net.mrwilfis.treasures_of_the_dead.entity.custom.CaptainBloomingSkeletonEntity;
import net.mrwilfis.treasures_of_the_dead.item.ModCreativeModTabs;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Treasures_of_the_dead.MOD_ID)
public class Treasures_of_the_dead
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "treasures_of_the_dead";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public Treasures_of_the_dead()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModBlockEntities.register(modEventBus);

        ModEntities.register(modEventBus);

        GeckoLib.initialize();

    //    PiratesSpawner spawner = new PiratesSpawner();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
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
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
        }
    }
}
