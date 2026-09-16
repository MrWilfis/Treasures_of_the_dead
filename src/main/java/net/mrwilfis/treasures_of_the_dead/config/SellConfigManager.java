// SellConfigManager.java
package net.mrwilfis.treasures_of_the_dead.config;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.mrwilfis.treasures_of_the_dead.TOTDUtils;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.common.PlayerReputationData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SellConfigManager {
    private static SellConfigManager instance;
    private final Map<String, Map<String, TOTDUtils.SellEntry>> companyEntries = new ConcurrentHashMap<>();
    private final Map<String, Boolean> loadedCompanies = new ConcurrentHashMap<>();

    public static SellConfigManager getInstance() {
        if (instance == null) {
            instance = new SellConfigManager();
        }
        return instance;
    }

    /**
     * Загрузить конфигурацию для конкретной компании
     */
    public void loadConfig(Entity entity, String company) {
        if (loadedCompanies.getOrDefault(company, false)) return;

        var entries = TOTDUtils.loadSellEntries(
                entity,
                "trading_companies/" + company + "/sell_prices.json"
        );

        companyEntries.put(company, entries);
        loadedCompanies.put(company, true);

        Treasures_of_the_dead.LOGGER.info("Sell config loaded for {} with {} entries", company, entries.size());
    }

    /**
     * Загрузить конфигурацию для компании по умолчанию (Order of Souls)
     */
    public void loadConfig(Entity entity) {
        loadConfig(entity, PlayerReputationData.ORDER_OF_SOULS);
    }

    /**
     * Загрузить конфигурацию для всех компаний
     */
    public void loadAllConfigs(Entity entity) {
        loadConfig(entity, PlayerReputationData.ORDER_OF_SOULS);
        loadConfig(entity, PlayerReputationData.GOLD_HOARDERS);
        loadConfig(entity, PlayerReputationData.MERCHANT_ALLIANCE);
        loadConfig(entity, PlayerReputationData.REAPERS_BONES);
    }

    /**
     * Получить все записи для компании
     */
    public Map<String, TOTDUtils.SellEntry> getEntries(String company) {
        return companyEntries.getOrDefault(company, new ConcurrentHashMap<>());
    }

    /**
     * Получить все записи для компании по умолчанию
     */
    public Map<String, TOTDUtils.SellEntry> getEntries() {
        return getEntries(PlayerReputationData.ORDER_OF_SOULS);
    }

    /**
     * Получить запись по ItemStack для компании
     */
    public TOTDUtils.SellEntry getEntry(String company, ItemStack stack) {
        if (stack.isEmpty()) return null;

        String registryId = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        var entries = companyEntries.get(company);
        return entries != null ? entries.get(registryId) : null;
    }

    /**
     * Получить запись по ItemStack для компании по умолчанию
     */
    public TOTDUtils.SellEntry getEntry(ItemStack stack) {
        return getEntry(PlayerReputationData.ORDER_OF_SOULS, stack);
    }

    /**
     * Получить запись по ID предмета для компании
     */
    public TOTDUtils.SellEntry getEntry(String company, String itemId) {
        var entries = companyEntries.get(company);
        return entries != null ? entries.get(itemId) : null;
    }

    /**
     * Получить запись по ID предмета для компании по умолчанию
     */
    public TOTDUtils.SellEntry getEntry(String itemId) {
        return getEntry(PlayerReputationData.ORDER_OF_SOULS, itemId);
    }

    /**
     * Получить цену для компании
     */
    public int getPrice(String company, ItemStack stack) {
        TOTDUtils.SellEntry entry = getEntry(company, stack);
        if (entry != null) {
            return entry.price * stack.getCount();
        }
        return 0;
    }

    /**
     * Получить цену для компании по умолчанию
     */
    public int getPrice(ItemStack stack) {
        return getPrice(PlayerReputationData.ORDER_OF_SOULS, stack);
    }

    /**
     * Получить цену по ID предмета для компании
     */
    public int getPriceById(String company, String itemId) {
        TOTDUtils.SellEntry entry = getEntry(company, itemId);
        return entry != null ? entry.price : 0;
    }

    /**
     * Получить цену по ID предмета для компании по умолчанию
     */
    public int getPriceById(String itemId) {
        return getPriceById(PlayerReputationData.ORDER_OF_SOULS, itemId);
    }

    /**
     * Получить опыт для компании
     */
    public int getExperienceGain(String company, ItemStack stack) {
        TOTDUtils.SellEntry entry = getEntry(company, stack);
        if (entry != null) {
            return entry.experienceGain * stack.getCount();
        }
        return 0;
    }

    /**
     * Получить опыт для компании по умолчанию
     */
    public int getExperienceGain(ItemStack stack) {
        return getExperienceGain(PlayerReputationData.ORDER_OF_SOULS, stack);
    }

    /**
     * Получить минимальный уровень для компании
     */
    public int getMinLevel(String company, ItemStack stack) {
        TOTDUtils.SellEntry entry = getEntry(company, stack);
        return entry != null ? entry.minLevel : Integer.MAX_VALUE;
    }

    /**
     * Получить минимальный уровень для компании по умолчанию
     */
    public int getMinLevel(ItemStack stack) {
        return getMinLevel(PlayerReputationData.ORDER_OF_SOULS, stack);
    }

    /**
     * Проверить, можно ли продать предмет для компании
     */
    public boolean canSell(String company, ItemStack stack) {
        return getEntry(company, stack) != null;
    }

    /**
     * Проверить, можно ли продать предмет для компании по умолчанию
     */
    public boolean canSell(ItemStack stack) {
        return canSell(PlayerReputationData.ORDER_OF_SOULS, stack);
    }

    /**
     * Проверить, загружена ли компания
     */
    public boolean isLoaded(String company) {
        return loadedCompanies.getOrDefault(company, false);
    }

    /**
     * Проверить, загружена ли компания по умолчанию
     */
    public boolean isLoaded() {
        return isLoaded(PlayerReputationData.ORDER_OF_SOULS);
    }

    /**
     * Получить все загруженные компании
     */
    public List<String> getLoadedCompanies() {
        return new ArrayList<>(loadedCompanies.keySet());
    }

    /**
     * Очистить данные для компании
     */
    public void clear(String company) {
        companyEntries.remove(company);
        loadedCompanies.remove(company);
    }

    /**
     * Очистить все данные
     */
    public void clearAll() {
        companyEntries.clear();
        loadedCompanies.clear();
    }
}