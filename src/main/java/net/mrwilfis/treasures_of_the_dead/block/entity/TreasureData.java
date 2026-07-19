package net.mrwilfis.treasures_of_the_dead.block.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.AnyTreasureClass;
import net.mrwilfis.treasures_of_the_dead.entity.custom.chestVariants.TreasureChestEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.DisgracedSkullEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.FoulSkullEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.HatefulSkullEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.VillainousSkullEntity;

import java.io.InputStreamReader;
import java.util.*;

public class TreasureData {
    public record TreasureConfig(
            ResourceLocation entityId,
            int minKeyLootValue,
            float value,
            int weight,
            int maxCount
    ) {}

    public static final Map<ResourceLocation, TreasureConfig> TREASURE_CONFIGS = new HashMap<>();

    public static void loadTreasureData(ServerLevel level) {
        if (level == null) return;

        loadTreasureDataFromJson(level.getServer().getResourceManager(),
                "sea_fortress_loot/sea_fortress_loot.json");
    }

    public static void loadTreasureDataFromJson(ResourceManager resourceManager, String jsonPath) {
        TREASURE_CONFIGS.clear();

        ResourceLocation configLocation = Treasures_of_the_dead.resource(jsonPath);

        try {
            var resource = resourceManager.getResource(configLocation).orElseThrow();
            JsonObject json = JsonParser.parseReader(
                    new InputStreamReader(resource.open())).getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                String treasureId = entry.getKey();
                JsonObject treasureData = entry.getValue().getAsJsonObject();

                ResourceLocation entityId = ResourceLocation.parse(treasureId);

                TreasureConfig config = new TreasureConfig(
                        entityId,
                        treasureData.get("min_key_loot_value").getAsInt(),
                        treasureData.get("value").getAsFloat(),
                        treasureData.get("weight").getAsInt(),
                        treasureData.get("max_count").getAsInt()
                );

                TREASURE_CONFIGS.put(entityId, config);
                System.out.println("Treasure loaded: " + treasureId + " (weight: " + config.weight() + ", value: " + config.value() + ")");
            }
            System.out.println("Loaded" + TREASURE_CONFIGS.size() + " treasure configs");
        } catch (Exception e) {
            System.err.println("Loading error JSON " + configLocation + ": " + e.getMessage());
            loadDefaultConfigs();
        }
    }

    private static void loadDefaultConfigs() {
        System.out.println("Loading default configs...");

        TREASURE_CONFIGS.put(
                ResourceLocation.parse("treasures_of_the_dead:foul_skull"),
                new TreasureConfig(
                        ResourceLocation.parse("treasures_of_the_dead:foul_skull"),
                        1, 5, 10, 100
                )
        );
        TREASURE_CONFIGS.put(
                ResourceLocation.parse("treasures_of_the_dead:disgraced_skull"),
                new TreasureConfig(
                        ResourceLocation.parse("treasures_of_the_dead:disgraced_skull"),
                        10, 10, 15, 100
                )
        );
        TREASURE_CONFIGS.put(
                ResourceLocation.parse("treasures_of_the_dead:hateful_skull"),
                new TreasureConfig(
                        ResourceLocation.parse("treasures_of_the_dead:hateful_skull"),
                        30, 16, 20, 100
                )
        );
        TREASURE_CONFIGS.put(
                ResourceLocation.parse("treasures_of_the_dead:villainous_skull"),
                new TreasureConfig(
                        ResourceLocation.parse("treasures_of_the_dead:villainous_skull"),
                        40, 27, 40, 100
                )
        );
        TREASURE_CONFIGS.put(
                ResourceLocation.parse("treasures_of_the_dead:treasure_chest"),
                new TreasureConfig(
                        ResourceLocation.parse("treasures_of_the_dead:treasure_chest"),
                        20, 12, 5, 3
                )
        );
    }

    public static Map<ResourceLocation, TreasureConfig> getTreasureConfigs() {
        return TREASURE_CONFIGS;
    }

    public static TreasureConfig getConfig(ResourceLocation treasureId) {
        return TREASURE_CONFIGS.get(treasureId);
    }

    public static void ensureLoaded(ServerLevel level) {
        if (TREASURE_CONFIGS.isEmpty()) {
            loadTreasureData(level);
        }
    }

    public static TreasureConfig getWeightedRandomTreasure(ServerLevel level, float lootValue, Random random) {
        ensureLoaded(level);

        List<TreasureConfig> available = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();

        for (TreasureConfig config : TREASURE_CONFIGS.values()) {
            if (lootValue >= config.minKeyLootValue() && config.weight() > 0) {
                available.add(config);
                weights.add(config.weight());
            }
        }

        if (available.isEmpty()) {
            return null;
        }

        int totalWeight = weights.stream().mapToInt(Integer::intValue).sum();
        int randomWeight = random.nextInt(totalWeight);
        int currentWeight = 0;

        for (int i = 0; i < available.size(); i++) {
            currentWeight += weights.get(i);
            if (randomWeight < currentWeight) {
                return available.get(i);
            }
        }

        return available.getFirst();
    }

    public static AnyTreasureClass createTreasureFromId(ResourceLocation treasureId, ServerLevel level) {
        if (level == null) return null;

        return switch (treasureId.toString()) {
            case "treasures_of_the_dead:foul_skull" ->
                    new FoulSkullEntity(ModEntities.FOUL_SKULL.get(), level);
            case "treasures_of_the_dead:disgraced_skull" ->
                    new DisgracedSkullEntity(ModEntities.DISGRACED_SKULL.get(), level);
            case "treasures_of_the_dead:hateful_skull" ->
                    new HatefulSkullEntity(ModEntities.HATEFUL_SKULL.get(), level);
            case "treasures_of_the_dead:villainous_skull" ->
                    new VillainousSkullEntity(ModEntities.VILLAINOUS_SKULL.get(), level);
            case "treasures_of_the_dead:treasure_chest" ->
                    new TreasureChestEntity(ModEntities.TREASURE_CHEST.get(), level);
            default -> {
                System.err.println("Unknown treasure ID: " + treasureId);
                yield null;
            }
        };
    }

}
