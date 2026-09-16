package net.mrwilfis.treasures_of_the_dead.config;

import net.minecraft.world.entity.player.Player;
import net.mrwilfis.treasures_of_the_dead.TOTDUtils;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.common.PlayerReputationData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Менеджер для управления предложениями магазина (покупка)
 */
public class BuyConfigManager {

    private static BuyConfigManager instance;
    private final Map<String, List<TOTDUtils.ShopOfferEntry>> companyOffers = new ConcurrentHashMap<>();
    private String currentCompany = "";

    private BuyConfigManager() {}

    public static BuyConfigManager getInstance() {
        if (instance == null) {
            instance = new BuyConfigManager();
        }
        return instance;
    }

    /**
     * Загружает конфигурацию для указанной компании
     */
    public void loadConfig(Player player, String companyId) {
        if (player == null || player.level().isClientSide()) return;

        if (!companyOffers.containsKey(companyId) || !companyId.equals(currentCompany)) {
            String jsonPath = "trading_companies/" + companyId + "/shop_offers.json";
            List<TOTDUtils.ShopOfferEntry> offerList = TOTDUtils.loadShopOffers(player, jsonPath);

            // sortOffers(offerList);

            companyOffers.put(companyId, offerList);
            currentCompany = companyId;

            Treasures_of_the_dead.LOGGER.info("Loaded {} shop offers for company: {}", offerList.size(), companyId);
        }
    }

    /**
     * Получает список предложений для компании, доступных игроку по уровню
     */
    public List<TOTDUtils.ShopOfferEntry> getAvailableOffers(Player player, String companyId) {
        if (player == null) return new ArrayList<>();

        int playerLevel = PlayerReputationData.getLevel(player, companyId);
        List<TOTDUtils.ShopOfferEntry> allOffers = companyOffers.getOrDefault(companyId, new ArrayList<>());
        List<TOTDUtils.ShopOfferEntry> availableOffers = new ArrayList<>();

        for (TOTDUtils.ShopOfferEntry offer : allOffers) {
            if (offer.displayLevel <= playerLevel) {
                availableOffers.add(offer);
            }
        }

        return availableOffers;
    }

    /**
     * Получает все предложения для компании
     */
    public List<TOTDUtils.ShopOfferEntry> getAllOffers(String companyId) {
        return companyOffers.getOrDefault(companyId, new ArrayList<>());
    }

    /**
     * Очищает кэш
     */
    public void clearCache() {
        companyOffers.clear();
    }
}