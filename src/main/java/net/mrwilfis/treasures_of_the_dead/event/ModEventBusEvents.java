package net.mrwilfis.treasures_of_the_dead.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.client.CutlassFirstPersonRenderer;
import net.mrwilfis.treasures_of_the_dead.client.PowderKegFirstPersonRenderer;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.*;
import net.mrwilfis.treasures_of_the_dead.entity.custom.chestVariants.TreasureChestEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.powderKegsVariants.PowderKegEntity;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import net.mrwilfis.treasures_of_the_dead.item.custom.AbstractPowderKegItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.CutlassItem;
import net.mrwilfis.treasures_of_the_dead.network.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


@EventBusSubscriber(modid = Treasures_of_the_dead.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {

    }

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {

    }

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.TOTD_SKELETON.get(), TOTDSkeletonEntity.setAttributes());
        event.put(ModEntities.CAPTAIN_SKELETON.get(), CaptainSkeletonEntity.setAttributes());
        event.put(ModEntities.BLOOMING_SKELETON.get(), BloomingSkeletonEntity.setAttributes());
        event.put(ModEntities.CAPTAIN_BLOOMING_SKELETON.get(), CaptainBloomingSkeletonEntity.setAttributes());
        event.put(ModEntities.SHADOW_SKELETON.get(), ShadowSkeletonEntity.setAttributes());
        event.put(ModEntities.CAPTAIN_SHADOW_SKELETON.get(), CaptainShadowSkeletonEntity.setAttributes());
        event.put(ModEntities.GOLDEN_SKELETON.get(), GoldenSkeletonEntity.setAttributes());
        event.put(ModEntities.CAPTAIN_GOLDEN_SKELETON.get(), CaptainGoldenSkeletonEntity.setAttributes());
        event.put(ModEntities.GHOST.get(), GhostEntity.setAttributes());
        event.put(ModEntities.FOUL_SKULL.get(), AbstractSkullEntity.setAttributes());
        event.put(ModEntities.DISGRACED_SKULL.get(), AbstractSkullEntity.setAttributes());
        event.put(ModEntities.HATEFUL_SKULL.get(), AbstractSkullEntity.setAttributes());
        event.put(ModEntities.VILLAINOUS_SKULL.get(), AbstractSkullEntity.setAttributes());
        event.put(ModEntities.TREASURE_CHEST.get(), TreasureChestEntity.setAttributes());
        event.put(ModEntities.POWDER_KEG.get(), PowderKegEntity.setAttributes());
    }

    @SubscribeEvent
    public static void entitySpawnRestriction(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.TOTD_SKELETON.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.CAPTAIN_SKELETON.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.BLOOMING_SKELETON.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.CAPTAIN_BLOOMING_SKELETON.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.GHOST.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }


    @SubscribeEvent
    public static void registerDispenserBehaviors(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(
                    ModItems.BLUNDER_BOMB.get(),
                    new DefaultDispenseItemBehavior() {
                        @Override
                        protected ItemStack execute(BlockSource blockSource, ItemStack stack) {
                            Level level = blockSource.level();
                            Position pos = DispenserBlock.getDispensePosition(blockSource);
                            Direction dir = blockSource.state().getValue(DispenserBlock.FACING);

                            BlunderBombEntity bomb = new BlunderBombEntity(level, pos.x(), pos.y(), pos.z());
                            bomb.shoot(dir.getStepX(), dir.getStepY(), dir.getStepZ(), 1.4f, 1.0f);
                            level.addFreshEntity(bomb);
                            stack.shrink(1);
                            return stack;
                        }
                    }
            );
        });
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(
                    ModItems.IRON_DAGGER.get(),
                    new DefaultDispenseItemBehavior() {
                        @Override
                        protected ItemStack execute(BlockSource blockSource, ItemStack stack) {
                            Level level = blockSource.level();
                            Position pos = DispenserBlock.getDispensePosition(blockSource);
                            Direction dir = blockSource.state().getValue(DispenserBlock.FACING);

                            IronDaggerEntity dagger = new IronDaggerEntity(ModEntities.IRON_DAGGER.get(), level);
                            dagger.moveTo(pos.x(), pos.y(), pos.z());
                            dagger.shoot(dir.getStepX(), dir.getStepY(), dir.getStepZ(), dagger.getDaggerItem().getMaxSpeed(), 1.0f);
                            level.addFreshEntity(dagger);
                            stack.shrink(1);
                            return stack;
                        }
                    }
            );
        });
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(
                    ModItems.GOLDEN_DAGGER.get(),
                    new DefaultDispenseItemBehavior() {
                        @Override
                        protected ItemStack execute(BlockSource blockSource, ItemStack stack) {
                            Level level = blockSource.level();
                            Position pos = DispenserBlock.getDispensePosition(blockSource);
                            Direction dir = blockSource.state().getValue(DispenserBlock.FACING);

                            GoldenDaggerEntity dagger = new GoldenDaggerEntity(ModEntities.GOLDEN_DAGGER.get(), level);
                            dagger.moveTo(pos.x(), pos.y(), pos.z());
                            dagger.shoot(dir.getStepX(), dir.getStepY(), dir.getStepZ(), dagger.getDaggerItem().getMaxSpeed(), 1.0f);
                            level.addFreshEntity(dagger);
                            stack.shrink(1);
                            return stack;
                        }
                    }
            );
        });
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(
                    ModItems.DIAMOND_DAGGER.get(),
                    new DefaultDispenseItemBehavior() {
                        @Override
                        protected ItemStack execute(BlockSource blockSource, ItemStack stack) {
                            Level level = blockSource.level();
                            Position pos = DispenserBlock.getDispensePosition(blockSource);
                            Direction dir = blockSource.state().getValue(DispenserBlock.FACING);

                            DiamondDaggerEntity dagger = new DiamondDaggerEntity(ModEntities.DIAMOND_DAGGER.get(), level);
                            dagger.moveTo(pos.x(), pos.y(), pos.z());
                            dagger.shoot(dir.getStepX(), dir.getStepY(), dir.getStepZ(), dagger.getDaggerItem().getMaxSpeed(), 1.0f);
                            level.addFreshEntity(dagger);
                            stack.shrink(1);
                            return stack;
                        }
                    }
            );
        });
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(
                    ModItems.NETHERITE_DAGGER.get(),
                    new DefaultDispenseItemBehavior() {
                        @Override
                        protected ItemStack execute(BlockSource blockSource, ItemStack stack) {
                            Level level = blockSource.level();
                            Position pos = DispenserBlock.getDispensePosition(blockSource);
                            Direction dir = blockSource.state().getValue(DispenserBlock.FACING);

                            NetheriteDaggerEntity dagger = new NetheriteDaggerEntity(ModEntities.NETHERITE_DAGGER.get(), level);
                            dagger.moveTo(pos.x(), pos.y(), pos.z());
                            dagger.shoot(dir.getStepX(), dir.getStepY(), dir.getStepZ(), dagger.getDaggerItem().getMaxSpeed(), 1.0f);
                            level.addFreshEntity(dagger);
                            stack.shrink(1);
                            return stack;
                        }
                    }
            );
        });
    }

    @SubscribeEvent
    public static void onComputeFovModifier(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        boolean isUsingCutlass = (mainHand.getItem() instanceof CutlassItem || offHand.getItem() instanceof CutlassItem) && player.isUsingItem();
        if (isUsingCutlass) {
            event.setNewFovModifier(1.0f);
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        // Убрали проверку "if (event.getHand() != InteractionHand.MAIN_HAND) return;"
        // Теперь событие обрабатывается и для правой, и для левой руки.

        // Получаем предмет из самого события (это безопаснее, чем брать из player)
        ItemStack itemStack = event.getItemStack();

        if (itemStack.getItem() instanceof CutlassItem) {
            // Передаем событие целиком, рендерер сам определит, какая это рука
            CutlassFirstPersonRenderer.renderCutlassBlocking(event);
        }

        // Убедитесь, что здесь нет return, если хотите, чтобы обрабатывались оба предмета
        if (itemStack.getItem() instanceof AbstractPowderKegItem) {
            PowderKegFirstPersonRenderer.renderPowderKeg(event);
        }
    }

    @SubscribeEvent
    public static void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (player.level().isClientSide()) return;

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        boolean hasCutlass = mainHand.getItem() instanceof CutlassItem ||
                offHand.getItem() instanceof CutlassItem;

        if (!hasCutlass) return;
        if (!player.isUsingItem()) return;

        FoodData foodData = player.getFoodData();
        int currentFood = foodData.getFoodLevel();

        if (currentFood < 6+0 && !player.isCreative()) {
            return;
        }

        if (!player.isCreative()) {
            foodData.setFoodLevel(currentFood - 0);
        }
    }

    @SubscribeEvent
    public static void registerPayload(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0");

        registrar.playToServer(
                C2SRequestReputationSyncPacket.TYPE,
                C2SRequestReputationSyncPacket.STREAM_CODEC,
                C2SRequestReputationSyncPacket::handle
        );

        registrar.playToServer(
                C2SSellItemPacket.TYPE,
                C2SSellItemPacket.STREAM_CODEC,
                C2SSellItemPacket::handle
        );

        registrar.playToClient(
                S2CReputationSyncPacket.TYPE,
                S2CReputationSyncPacket.STREAM_CODEC,
                S2CReputationSyncPacket::handle
        );

        registrar.playToClient(
                S2CSellResultPacket.TYPE,
                S2CSellResultPacket.STREAM_CODEC,
                S2CSellResultPacket::handle
        );

        registrar.playToServer(
                C2SOpenBuyMenuPacket.TYPE,
                C2SOpenBuyMenuPacket.STREAM_CODEC,
                C2SOpenBuyMenuPacket::handle
        );

        registrar.playToServer(
                C2SOpenSellMenuPacket.TYPE,
                C2SOpenSellMenuPacket.STREAM_CODEC,
                C2SOpenSellMenuPacket::handle
        );
        registrar.playToServer(
                C2SRequestBuyPacket.TYPE,
                C2SRequestBuyPacket.STREAM_CODEC,
                C2SRequestBuyPacket::handle
        );
        registrar.playToClient(
                S2CPlaySoundPacket.TYPE,
                S2CPlaySoundPacket.STREAM_CODEC,
                S2CPlaySoundPacket::handle
        );
    }
}
