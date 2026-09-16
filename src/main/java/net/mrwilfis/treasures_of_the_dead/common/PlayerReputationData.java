package net.mrwilfis.treasures_of_the_dead.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class PlayerReputationData {
    public static final String ORDER_OF_SOULS = "order_of_souls";
    public static final String GOLD_HOARDERS = "gold_hoarders";
    public static final String MERCHANT_ALLIANCE = "merchant_alliance";
    public static final String REAPERS_BONES = "reapers_bones";

    private static final int FIRST_LEVEL_EXP = 30;
    private static final int EXP_INCREMENT_PER_LEVEL = 5;

    private static String getExpTag(String company) {
        return company + "_exp";
    }

    private static String getLevelTag(String company) {
        return company + "_level";
    }

    /**
     * Получить опыт для указанной компании
     */
    public static int getExperience(Player player, String company) {
        CompoundTag persistentData = player.getPersistentData();
        return persistentData.getInt(getExpTag(company));
    }

    /**
     * Установить опыт для указанной компании
     */
    public static void setExperience(Player player, String company, int value) {
        CompoundTag persistentData = player.getPersistentData();
        persistentData.putInt(getExpTag(company), Math.max(0, value));
    }

    /**
     * Получить уровень для указанной компании
     */
    public static int getLevel(Player player, String company) {
        CompoundTag persistentData = player.getPersistentData();
        return persistentData.getInt(getLevelTag(company));
    }

    /**
     * Установить уровень для указанной компании
     */
    public static void setLevel(Player player, String company, int value) {
        CompoundTag persistentData = player.getPersistentData();
        persistentData.putInt(getLevelTag(company), Math.max(0, value));
    }

    /**
     * Получить количество опыта, необходимое для уровня
     * Формула: 50 + (уровень * 5)
     * 1 уровень: 50
     * 2 уровень: 55
     * 3 уровень: 60
     * и т.д.
     */
    public static int getRequiredExperienceForLevel(int level) {
        // Для 0 уровня нужно 50 опыта
        // Для 1 уровня нужно 55 опыта
        // Для 2 уровня нужно 60 опыта
        return FIRST_LEVEL_EXP + (level * EXP_INCREMENT_PER_LEVEL);
    }

    /**
     * Получить общее количество опыта для достижения уровня
     */
    public static int getTotalExperienceForLevel(int level) {
        int total = 0;
        for (int i = 0; i < level; i++) {
            total += getRequiredExperienceForLevel(i);
        }
        return total;
    }

    /**
     * Добавить опыт для указанной компании (с автоматическим повышением уровня)
     */
    public static void addExperience(Player player, String company, int amount) {
        int currentExp = getExperience(player, company);
        int currentLevel = getLevel(player, company);

        int newExp = currentExp + amount;
        int newLevel = currentLevel;

        // Проверяем, хватает ли опыта для повышения уровня
        while (newExp >= getRequiredExperienceForLevel(newLevel)) {
            newExp -= getRequiredExperienceForLevel(newLevel);
            newLevel++;
        }

        setExperience(player, company, newExp);
        setLevel(player, company, newLevel);
    }

    /**
     * Проверить, инициализированы ли данные для компании
     */
    public static boolean isInitialized(Player player, String company) {
        CompoundTag persistentData = player.getPersistentData();
        return persistentData.contains(getExpTag(company)) &&
                persistentData.contains(getLevelTag(company));
    }

    /**
     * Инициализировать данные для компании
     */
    public static void initialize(Player player, String company) {
        if (!isInitialized(player, company)) {
            setExperience(player, company, 0);
            setLevel(player, company, 0);
        }
    }

    /**
     * Инициализировать данные для всех компаний
     */
    public static void initializeAll(Player player) {
        initialize(player, ORDER_OF_SOULS);
        initialize(player, GOLD_HOARDERS);
        initialize(player, MERCHANT_ALLIANCE);
        initialize(player, REAPERS_BONES);
    }

    /**
     * Получить прогресс до следующего уровня
     */
    public static int getProgressToNextLevel(Player player, String company) {
        int currentExp = getExperience(player, company);
        int currentLevel = getLevel(player, company);
        int requiredExp = getRequiredExperienceForLevel(currentLevel);
        return Math.min(currentExp, requiredExp);
    }

    /**
     * Получить необходимый опыт для следующего уровня
     */
    public static int getRequiredExperienceForNextLevel(Player player, String company) {
        int currentLevel = getLevel(player, company);
        return getRequiredExperienceForLevel(currentLevel);
    }

    /**
     * Получить общее количество опыта для достижения указанного уровня
     */
    public static int getTotalExperienceForLevel(int level, int expPerLevel) {
        return level * expPerLevel;
    }

    public static int getRequiredExp(int level) {
        return getRequiredExperienceForLevel(level);
    }

    public static int getTotalExp(int level) {
        return getTotalExperienceForLevel(level);
    }

    // --- Order of Souls ---
    public static int getOrderOfSoulsExp(Player player) {
        return getExperience(player, ORDER_OF_SOULS);
    }

    public static void setOrderOfSoulsExp(Player player, int value) {
        setExperience(player, ORDER_OF_SOULS, value);
    }

    public static int getOrderOfSoulsLevel(Player player) {
        return getLevel(player, ORDER_OF_SOULS);
    }

    public static void setOrderOfSoulsLevel(Player player, int value) {
        setLevel(player, ORDER_OF_SOULS, value);
    }

    public static void addOrderOfSoulsExp(Player player, int amount) {
        addExperience(player, ORDER_OF_SOULS, amount);
    }

    public static void initializeOrderOfSouls(Player player) {
        initialize(player, ORDER_OF_SOULS);
    }

    // --- Gold Hoarders (для будущего) ---
    public static int getGoldHoardersExp(Player player) {
        return getExperience(player, GOLD_HOARDERS);
    }

    public static void setGoldHoardersExp(Player player, int value) {
        setExperience(player, GOLD_HOARDERS, value);
    }

    public static int getGoldHoardersLevel(Player player) {
        return getLevel(player, GOLD_HOARDERS);
    }

    public static void setGoldHoardersLevel(Player player, int value) {
        setLevel(player, GOLD_HOARDERS, value);
    }

    public static void addGoldHoardersExp(Player player, int amount) {
        addExperience(player, GOLD_HOARDERS, amount);
    }

    public static void initializeGoldHoarders(Player player) {
        initialize(player, GOLD_HOARDERS);
    }

    // --- Merchant Alliance (для будущего) ---
    public static int getMerchantAllianceExp(Player player) {
        return getExperience(player, MERCHANT_ALLIANCE);
    }

    public static void setMerchantAllianceExp(Player player, int value) {
        setExperience(player, MERCHANT_ALLIANCE, value);
    }

    public static int getMerchantAllianceLevel(Player player) {
        return getLevel(player, MERCHANT_ALLIANCE);
    }

    public static void setMerchantAllianceLevel(Player player, int value) {
        setLevel(player, MERCHANT_ALLIANCE, value);
    }

    public static void addMerchantAllianceExp(Player player, int amount) {
        addExperience(player, MERCHANT_ALLIANCE, amount);
    }

    public static void initializeMerchantAlliance(Player player) {
        initialize(player, MERCHANT_ALLIANCE);
    }

    // --- Reaper's Bones (для будущего) ---
    public static int getReapersBonesExp(Player player) {
        return getExperience(player, REAPERS_BONES);
    }

    public static void setReapersBonesExp(Player player, int value) {
        setExperience(player, REAPERS_BONES, value);
    }

    public static int getReapersBonesLevel(Player player) {
        return getLevel(player, REAPERS_BONES);
    }

    public static void setReapersBonesLevel(Player player, int value) {
        setLevel(player, REAPERS_BONES, value);
    }

    public static void addReapersBonesExp(Player player, int amount) {
        addExperience(player, REAPERS_BONES, amount);
    }

    public static void initializeReapersBones(Player player) {
        initialize(player, REAPERS_BONES);
    }
}