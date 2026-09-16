package net.mrwilfis.treasures_of_the_dead.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.mrwilfis.treasures_of_the_dead.Config;
import net.mrwilfis.treasures_of_the_dead.TOTDUtils;
import net.mrwilfis.treasures_of_the_dead.command.ReputationCommands;
import net.mrwilfis.treasures_of_the_dead.common.ModDataComponents;
import net.mrwilfis.treasures_of_the_dead.common.PlayerReputationData;
import net.mrwilfis.treasures_of_the_dead.entity.custom.*;
import net.mrwilfis.treasures_of_the_dead.item.custom.CutlassItem;
import net.mrwilfis.treasures_of_the_dead.screen.TradeMenuFactory;
import net.mrwilfis.treasures_of_the_dead.villager.ModVillagers;
import net.mrwilfis.treasures_of_the_dead.villager.SellMenuProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;
import java.util.Optional;

import static net.mrwilfis.treasures_of_the_dead.item.custom.CutlassItem.CUTLASS_SPEED_MODIFIER;
import static net.mrwilfis.treasures_of_the_dead.item.custom.CutlassItem.DIAGONAL_COMPENSATION;

@EventBusSubscriber(modid = Treasures_of_the_dead.MOD_ID)
public class ModEvents {


    @SubscribeEvent
    public static void onMobSpawn(FinalizeSpawnEvent event) {
        Mob spawningMob = event.getEntity();

        if (spawningMob instanceof TOTDSkeletonEntity) {
            if (spawningMob instanceof GoldenSkeletonEntity) {
                if (spawningMob instanceof CaptainGoldenSkeletonEntity) {
                    TOTDUtils.setAttribute(spawningMob, Attributes.ATTACK_DAMAGE, Config.captainGoldenSkeletonDamage);
                    TOTDUtils.setAttribute(spawningMob, Attributes.MAX_HEALTH, Config.captainGoldenSkeletonHealth);
                    spawningMob.setHealth((float)Config.captainGoldenSkeletonHealth);
                    return;
                }
                TOTDUtils.setAttribute(spawningMob, Attributes.ATTACK_DAMAGE, Config.goldenSkeletonDamage);
                TOTDUtils.setAttribute(spawningMob, Attributes.MAX_HEALTH, Config.goldenSkeletonHealth);
                spawningMob.setHealth((float)Config.goldenSkeletonHealth);
                return;
            }
            if (spawningMob instanceof ShadowSkeletonEntity) {
                if (spawningMob instanceof CaptainShadowSkeletonEntity) {
                    TOTDUtils.setAttribute(spawningMob, Attributes.ATTACK_DAMAGE, Config.captainShadowSkeletonDamage);
                    TOTDUtils.setAttribute(spawningMob, Attributes.MAX_HEALTH, Config.captainShadowSkeletonHealth);
                    spawningMob.setHealth((float)Config.captainShadowSkeletonHealth);
                    return;
                }
                TOTDUtils.setAttribute(spawningMob, Attributes.ATTACK_DAMAGE, Config.shadowSkeletonDamage);
                TOTDUtils.setAttribute(spawningMob, Attributes.MAX_HEALTH, Config.shadowSkeletonHealth);
                spawningMob.setHealth((float)Config.shadowSkeletonHealth);
                return;
            }
            if (spawningMob instanceof BloomingSkeletonEntity) {
                if (spawningMob instanceof CaptainBloomingSkeletonEntity) {
                    TOTDUtils.setAttribute(spawningMob, Attributes.ATTACK_DAMAGE, Config.captainBloomingSkeletonDamage);
                    TOTDUtils.setAttribute(spawningMob, Attributes.MAX_HEALTH, Config.captainBloomingSkeletonHealth);
                    spawningMob.setHealth((float)Config.captainBloomingSkeletonHealth);
                    return;
                }
                TOTDUtils.setAttribute(spawningMob, Attributes.ATTACK_DAMAGE, Config.bloomingSkeletonDamage);
                TOTDUtils.setAttribute(spawningMob, Attributes.MAX_HEALTH, Config.bloomingSkeletonHealth);
                spawningMob.setHealth((float)Config.bloomingSkeletonHealth);
                return;
            }
            if (spawningMob instanceof CaptainSkeletonEntity) {
                TOTDUtils.setAttribute(spawningMob, Attributes.ATTACK_DAMAGE, Config.captainSkeletonDamage);
                TOTDUtils.setAttribute(spawningMob, Attributes.MAX_HEALTH, Config.captainSkeletonHealth);
                spawningMob.setHealth((float)Config.captainSkeletonHealth);
                return;
            }
            TOTDUtils.setAttribute(spawningMob, Attributes.ATTACK_DAMAGE, Config.pirateSkeletonDamage);
            TOTDUtils.setAttribute(spawningMob, Attributes.MAX_HEALTH, Config.pirateSkeletonHealth);
            spawningMob.setHealth((float)Config.pirateSkeletonHealth);
        }

        if (spawningMob instanceof GhostEntity) {

        }

    }

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerReputationData.initializeAll(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof Villager villager) {
            try {
                String companyId = TradeMenuFactory.getCompanyForVillager(villager);

                if (companyId != null && !companyId.isEmpty()) {
                    event.setCanceled(true);

                    Player player = event.getEntity();

                    ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
                    if (stack.getItem().getUseDuration(stack, player) > 0) {
                        player.stopUsingItem();
                        player.getCooldowns().addCooldown(player.getItemInHand(InteractionHand.MAIN_HAND).getItem(), 1);
                    }
                    stack = player.getItemInHand(InteractionHand.OFF_HAND);
                    if (stack.getItem().getUseDuration(stack, player) > 0) {
                        player.stopUsingItem();
                        player.getCooldowns().addCooldown(player.getItemInHand(InteractionHand.OFF_HAND).getItem(), 1);
                    }

                    if (!event.getLevel().isClientSide) {
                        player.openMenu(new SellMenuProvider(villager, companyId));
                    }
                }
            } catch (Exception e) {
                Treasures_of_the_dead.LOGGER.debug("Villager interaction: company not found for profession");
            }
        }
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent tradeEvent) {
        if (tradeEvent.getType() == ModVillagers.SKULL_MERCHANT.value()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = tradeEvent.getTrades();

            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.BONE, randomSource.nextInt(30, 34+1)),
                    new ItemStack(Items.EMERALD, 1),
                    16, 2, 0.05f));

            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.FOUL_SKULL_ITEM.get(), 1),
                    new ItemStack(Items.EMERALD, randomSource.nextInt(5, 6+1)),
                    8, 4, 0.05f));

            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.DISGRACED_SKULL_ITEM.get(), 1),
                    new ItemStack(Items.EMERALD, randomSource.nextInt(9, 11+1)),
                    8, 4, 0.05f));

            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.AMETHYST_SHARD, 21),
                    new ItemStack(Items.EMERALD, 1),
                    16, 4, 0.05f));

            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.HATEFUL_SKULL_ITEM.get(), 1),
                    new ItemStack(Items.EMERALD, randomSource.nextInt(15, 17+1)),
                    8, 8, 0.05f));

            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(ModItems.VILLAINOUS_SKULL_ITEM.get(), 1),
                    new ItemStack(Items.EMERALD, randomSource.nextInt(26, 28+1)),
                    8, 12, 0.05f));
            trades.get(3).add((entity, randomSource) -> {
                ItemStack skeletonCrewAssignment = new ItemStack(ModItems.SKELETON_CREW_ASSIGNMENT.get(), 1);
                skeletonCrewAssignment.set(ModDataComponents.DIFFICULTY, randomSource.nextInt(2, 3+1));
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, randomSource.nextInt(11, 14+1)),
                        skeletonCrewAssignment,
                        1, 16, 0.05f);
            });
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    new ItemStack(ModItems.BLUNDER_BOMB.get(), 3),
                    32, 2, 0.05f));

            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 32),
                    Optional.of(new ItemCost(Items.DIAMOND, 7)),
                    new ItemStack(ModItems.ORDER_OF_SOULS_SMITHING_TEMPLATE.get(), 1),
                    8, 16, 0.05f));
            trades.get(5).add((entity, randomSource) -> {
                ItemStack skeletonCrewAssignment = new ItemStack(ModItems.SKELETON_CREW_ASSIGNMENT.get(), 1);
                skeletonCrewAssignment.set(ModDataComponents.DIFFICULTY, randomSource.nextInt(4,5+1));
                return new MerchantOffer(
                        new ItemCost(Items.EMERALD, randomSource.nextInt(17, 20+1)),
                        skeletonCrewAssignment,
                        1, 16, 0.05f);
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) return;

        if (!player.isUsingItem()) {
            boolean hasCutlassInMainHand = player.getMainHandItem().getItem() instanceof CutlassItem;
            boolean hasCutlassInOffHand = player.getOffhandItem().getItem() instanceof CutlassItem;

            if (!hasCutlassInMainHand && !hasCutlassInOffHand) {
                removeModifiers(player);
                return;
            }
            removeModifiers(player);
        } else {
            ItemStack usedItem = player.getUseItem();

            if (!(usedItem.getItem() instanceof CutlassItem)) {
                removeModifiers(player);
            }
        }
    }

    private static void removeModifiers(Player player) {
        var attribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attribute == null) return;

        boolean b1 = player.getAttribute(Attributes.MOVEMENT_SPEED).getModifiers().contains(CUTLASS_SPEED_MODIFIER);
        boolean b2 = player.getAttribute(Attributes.MOVEMENT_SPEED).getModifiers().contains(DIAGONAL_COMPENSATION);
        if (!b1 && !b2) return;
        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(CUTLASS_SPEED_MODIFIER);
        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(DIAGONAL_COMPENSATION);
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ReputationCommands.register(event.getDispatcher());
    }
}
