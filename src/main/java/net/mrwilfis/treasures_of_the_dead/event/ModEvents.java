package net.mrwilfis.treasures_of_the_dead.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrwilfis.treasures_of_the_dead.Config;
import net.mrwilfis.treasures_of_the_dead.TOTDUtils;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.*;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import net.mrwilfis.treasures_of_the_dead.villager.ModVillagers;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Treasures_of_the_dead.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn event) {
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

    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent tradeEvent) {
//        if (tradeEvent.getType() == VillagerProfession.CLERIC) {
//            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = tradeEvent.getTrades();
//
//            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
//                    new ItemStack(ModItems.FOUL_SKULL_ITEM.get(), 1),
//                    new ItemStack(Items.EMERALD, randomSource.nextInt(2, 5+1)),
//                    8, 4, 0.0f));
//
//            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
//                    new ItemStack(ModItems.DISGRACED_SKULL_ITEM.get(), 1),
//                    new ItemStack(Items.EMERALD, randomSource.nextInt(6, 12+1)),
//                    6, 6, 0.0f));
//
//            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
//                    new ItemStack(ModItems.HATEFUL_SKULL_ITEM.get(), 1),
//                    new ItemStack(Items.EMERALD, randomSource.nextInt(13, 22+1)),
//                    4, 8, 0.0f));
//
//            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
//                    new ItemStack(ModItems.VILLAINOUS_SKULL_ITEM.get(), 1),
//                    new ItemStack(Items.EMERALD, randomSource.nextInt(23, 44+1)),
//                    4, 12, 0.0f));
//
//            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
//                    new ItemStack(Items.EMERALD, randomSource.nextInt(8, 16+1)),
//                    new ItemStack(ModItems.FOUL_SKULL_ITEM.get(), randomSource.nextInt(1, 2+1)),
//                    new ItemStack(ModItems.SKELETON_CREW_ASSIGNMENT.get(), 1),
//                    1, 16, 0.0f));
//
//
//        }

        if (tradeEvent.getType() == ModVillagers.SKULL_MERCHANT.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = tradeEvent.getTrades();

            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemStack(Items.BONE, randomSource.nextInt(30, 34+1)),
                    new ItemStack(Items.EMERALD, 1),
                    16, 2, 0.05f));

            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemStack(ModItems.FOUL_SKULL_ITEM.get(), 1),
                    new ItemStack(Items.EMERALD, randomSource.nextInt(5, 6+1)),
                    8, 4, 0.05f));

            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemStack(ModItems.DISGRACED_SKULL_ITEM.get(), 1),
                    new ItemStack(Items.EMERALD, randomSource.nextInt(9, 11+1)),
                    8, 4, 0.05f));

            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemStack(Items.AMETHYST_SHARD, 21),
                    new ItemStack(Items.EMERALD, 1),
                    16, 4, 0.05f));

            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemStack(ModItems.HATEFUL_SKULL_ITEM.get(), 1),
                    new ItemStack(Items.EMERALD, randomSource.nextInt(15, 17+1)),
                    8, 8, 0.05f));

            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemStack(ModItems.VILLAINOUS_SKULL_ITEM.get(), 1),
                    new ItemStack(Items.EMERALD, randomSource.nextInt(26, 28+1)),
                    8, 12, 0.05f));
            trades.get(3).add((entity, randomSource) -> {
                ItemStack skeletonCrewAssignment = new ItemStack(ModItems.SKELETON_CREW_ASSIGNMENT.get(), 1);
                skeletonCrewAssignment.setTag(new CompoundTag());
                skeletonCrewAssignment.getTag().putInt("Difficulty", randomSource.nextInt(2, 3+1));
                return new MerchantOffer(
                        new ItemStack(Items.EMERALD, randomSource.nextInt(11, 14+1)),
                        skeletonCrewAssignment,
                        1, 16, 0.05f);
            });
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 1),
                    new ItemStack(ModItems.BLUNDER_BOMB.get(), 3),
                    32, 2, 0.05f));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 32),
                    new ItemStack(Items.DIAMOND, 7),
                    new ItemStack(ModItems.ORDER_OF_SOULS_SMITHING_TEMPLATE.get(), 1),
                    8, 16, 0.05f));
            trades.get(5).add((entity, randomSource) -> {
                ItemStack skeletonCrewAssignment = new ItemStack(ModItems.SKELETON_CREW_ASSIGNMENT.get(), 1);
                skeletonCrewAssignment.setTag(new CompoundTag());
                skeletonCrewAssignment.getTag().putInt("Difficulty", randomSource.nextInt(4, 5+1));
                return new MerchantOffer(
                        new ItemStack(Items.EMERALD, randomSource.nextInt(17, 20+1)),
                        skeletonCrewAssignment,
                        1, 16, 0.05f);
            });
        }
    }
}
