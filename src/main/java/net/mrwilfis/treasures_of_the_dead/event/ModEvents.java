package net.mrwilfis.treasures_of_the_dead.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;

import java.util.List;

@Mod.EventBusSubscriber(modid = Treasures_of_the_dead.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent tradeEvent) {
        if (tradeEvent.getType() == VillagerProfession.CLERIC) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = tradeEvent.getTrades();

            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemStack(ModItems.FOUL_SKULL_ITEM.get(), 1),
                    new ItemStack(Items.EMERALD, randomSource.nextInt(2, 5+1)),
                    4, 8, 0.0f));

            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemStack(ModItems.DISGRACED_SKULL_ITEM.get(), 1),
                    new ItemStack(Items.EMERALD, randomSource.nextInt(7, 13+1)),
                    4, 10, 0.0f));

            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemStack(ModItems.HATEFUL_SKULL_ITEM.get(), 1),
                    new ItemStack(Items.EMERALD, randomSource.nextInt(16, 26+1)),
                    4, 12, 0.0f));

            trades.get(4).add((entity, randomSource) -> new MerchantOffer(
                    new ItemStack(ModItems.VILLAINOUS_SKULL_ITEM.get(), 1),
                    new ItemStack(Items.EMERALD, randomSource.nextInt(28, 50+1)),
                    4, 12, 0.0f));

        }
    }
}
