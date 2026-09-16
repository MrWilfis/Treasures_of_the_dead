package net.mrwilfis.treasures_of_the_dead.screen;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Inventory;
import net.mrwilfis.treasures_of_the_dead.common.PlayerReputationData;
import net.mrwilfis.treasures_of_the_dead.screen.custom.AbstractBuyMenu;
import net.mrwilfis.treasures_of_the_dead.screen.custom.AbstractSellMenu;
import net.mrwilfis.treasures_of_the_dead.screen.custom.OrderOfSoulsBuyMenu;
import net.mrwilfis.treasures_of_the_dead.screen.custom.OrderOfSoulsSellMenu;
import net.mrwilfis.treasures_of_the_dead.villager.ModVillagers;

import java.util.HashMap;
import java.util.Map;

public class TradeMenuFactory {
    // Маппинг профессий на компании
    private static final Map<VillagerProfession, String> PROFESSION_TO_COMPANY = new HashMap<>();

    static {
        PROFESSION_TO_COMPANY.put(ModVillagers.SKULL_MERCHANT.value(),
                PlayerReputationData.ORDER_OF_SOULS);
        // В будущем:
        // PROFESSION_TO_COMPANY.put(ModVillagers.GOLD_HOARDER.value(),
        //         PlayerReputationData.GOLD_HOARDERS);
    }

    /**
     * Создает меню продажи для указанной компании
     */
    public static AbstractSellMenu createSellMenu(String companyId, int containerId,
                                                  Inventory playerInventory, Villager villager) {
        return switch (companyId) {
            case PlayerReputationData.ORDER_OF_SOULS ->
                    new OrderOfSoulsSellMenu(containerId, playerInventory, villager);
            default -> throw new IllegalArgumentException(
                    "Unknown company for sell menu: " + companyId
            );
        };
    }

    /**
     * Создает меню покупки для указанной компании
     */
    public static AbstractBuyMenu createBuyMenu(String companyId, int containerId,
                                                Inventory playerInventory, Villager villager) {
        return switch (companyId) {
            case PlayerReputationData.ORDER_OF_SOULS ->
                    new OrderOfSoulsBuyMenu(containerId, playerInventory, villager);
            default -> throw new IllegalArgumentException(
                    "Unknown company for buy menu: " + companyId
            );
        };
    }

    /**
     * Определяет компанию по профессии жителя
     */
    public static String getCompanyForVillager(Villager villager) {
        VillagerProfession profession = villager.getVillagerData().getProfession();
        return PROFESSION_TO_COMPANY.get(profession);
    }
}
