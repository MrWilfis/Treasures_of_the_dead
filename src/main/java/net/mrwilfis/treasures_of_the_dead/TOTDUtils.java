package net.mrwilfis.treasures_of_the_dead;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.io.InputStreamReader;
import java.util.*;

public class TOTDUtils {

    public static void setAttribute(LivingEntity entity, Holder<Attribute> attribute, double value) {
        AttributeInstance attributeInstance = entity.getAttribute(attribute);
        if (attributeInstance == null) {
            return;
        }
        attributeInstance.setBaseValue(value);
    }

    public static ItemStack getItemFromString(String itemId) {
        try {
            ResourceLocation resourceLocation = ResourceLocation.parse(itemId);

            Item item = BuiltInRegistries.ITEM.get(resourceLocation);

            if (item != null && item != Items.AIR) {
                return new ItemStack(item);
            } else {
                System.err.println("Item not found: " + itemId);
                return ItemStack.EMPTY;
            }
        } catch (Exception e) {
            System.err.println("Invalid item ID format: " + itemId);
            return ItemStack.EMPTY;
        }
    }

    public static ItemStack getItemStackFromString(String itemId, int count) {
        ItemStack stack = getItemFromString(itemId);
        if (!stack.isEmpty()) {
            stack.setCount(count);
        }
        return stack;
    }

    public static Map<String, Integer> loadMinMaxParameterFromJson(Entity thisEntity, String JsonPath, String parameter) {
        Map<String, Integer> configs = new HashMap<>();
        if (!(thisEntity.level() instanceof ServerLevel serverlevel)) {
            return configs;
        }
        ResourceManager resourceManager = serverlevel.getServer().getResourceManager();
        ResourceLocation configLocation = Treasures_of_the_dead.resource(JsonPath);

        try {
            var resource = resourceManager.getResource(configLocation).orElseThrow();
            JsonObject json = JsonParser.parseReader(new InputStreamReader(resource.open())).getAsJsonObject();
            JsonElement minMax = json.get(parameter);

            if (minMax != null && minMax.isJsonObject()) {
                JsonObject obj = minMax.getAsJsonObject();
                int min = obj.get("min").getAsInt();
                int max = obj.get("max").getAsInt();
                configs.put("min", min);
                configs.put("max", max);
            }


        } catch (Exception e) {
            System.err.println("Error loading JSON for " + configLocation + ":" + e.getMessage());
        }
        return configs;
    }

    public static int loadIntParameterFromJson(Entity thisEntity, String JsonPath, String parameter) {
        int configs = 0;
        if (!(thisEntity.level() instanceof ServerLevel serverlevel)) {
            return configs;
        }
        ResourceManager resourceManager = serverlevel.getServer().getResourceManager();
        ResourceLocation configLocation = Treasures_of_the_dead.resource(JsonPath);

        try {
            var resource = resourceManager.getResource(configLocation).orElseThrow();
            JsonObject json = JsonParser.parseReader(new InputStreamReader(resource.open())).getAsJsonObject();
            JsonElement value = json.get(parameter);
            if (value != null) {
                configs = value.getAsInt();
            }


        } catch (Exception e) {
            System.err.println("Error loading JSON for " + configLocation + ":" + e.getMessage());
        }
        return configs;
    }

    public static class SellEntry {
        public final String itemId;
        public final int price;
        public final int experienceGain;
        public final int minLevel;

        public SellEntry(String itemId, int price, int experienceGain, int minLevel) {
            this.itemId = itemId;
            this.price = price;
            this.experienceGain = experienceGain;
            this.minLevel = minLevel;
        }
    }

    /**
     * Загружает предметы для продажи с ценами и опытом из JSON
     * Формат: {"entries": [{"item": "mod:item", "price": 10, "experience": 5, "minLevel": 0}]}
     */
    public static Map<String, SellEntry> loadSellEntries(Entity thisEntity, String jsonPath) {
        Map<String, SellEntry> entries = new HashMap<>();

        if (!(thisEntity.level() instanceof ServerLevel serverLevel)) {
            return entries;
        }

        ResourceManager resourceManager = serverLevel.getServer().getResourceManager();
        ResourceLocation configLocation = Treasures_of_the_dead.resource(jsonPath);

        try {
            var resource = resourceManager.getResource(configLocation).orElseThrow();
            JsonObject json = JsonParser.parseReader(new InputStreamReader(resource.open())).getAsJsonObject();
            JsonElement entriesElement = json.get("entries");

            if (entriesElement != null && entriesElement.isJsonArray()) {
                for (JsonElement element : entriesElement.getAsJsonArray()) {
                    JsonObject obj = element.getAsJsonObject();
                    String itemId = obj.get("item").getAsString();
                    int price = obj.get("price").getAsInt();
                    int experience = obj.has("experience") ? obj.get("experience").getAsInt() : 0;
                    int minLevel = obj.has("minLevel") ? obj.get("minLevel").getAsInt() : 0;

                    entries.put(itemId, new SellEntry(itemId, price, experience, minLevel));
                }
            }

            Treasures_of_the_dead.LOGGER.info("Loaded {} sell entries from {}", entries.size(), jsonPath);

        } catch (Exception e) {
            Treasures_of_the_dead.LOGGER.error("Error loading sell entries from {}: {}", jsonPath, e.getMessage());
        }

        return entries;
    }

    public static class ShopOfferEntry {
        public final String itemId;
        public final int price;
        public final int minLevel;
        public final int displayLevel;
        public final int count;
        public final Map<String, Integer> enchantments;
        public final Map<String, Object> components;

        public ShopOfferEntry(String itemId, int price, int minLevel, int displayLevel, int count) {
            this(itemId, price, minLevel, displayLevel, count, new HashMap<>(), new HashMap<>());
        }

        public ShopOfferEntry(String itemId, int price, int minLevel, int displayLevel, int count,
                              Map<String, Integer> enchantments) {
            this(itemId, price, minLevel, displayLevel, count, enchantments, new HashMap<>());
        }

        public ShopOfferEntry(String itemId, int price, int minLevel, int displayLevel, int count, Map<String, Integer> enchantments, Map<String, Object> components) {
            this.itemId = itemId;
            this.price = price;
            this.minLevel = minLevel;
            this.displayLevel = displayLevel;
            this.count = count;
            this.enchantments = enchantments != null ? enchantments : new HashMap<>();
            this.components = components != null ? components : new HashMap<>();
        }

        public boolean hasEnchantments() {
            return enchantments != null && !enchantments.isEmpty();
        }

        public boolean hasComponents() {
            return components != null && !components.isEmpty();
        }
    }

    /**
     * Загружает предложения магазина из JSON
     * Формат: {"entries": [{"item": "mod:item", "price": 10, "minLevel": 1, "displayLevel": 0, "count": 1,
     *                       "enchantments": {"minecraft:sharpness": 3},
     *                       "components": {"treasures_of_the_dead:difficulty": 1, "minecraft:custom_name": "\"Name\""}}]}
     */
    public static List<ShopOfferEntry> loadShopOffers(Entity thisEntity, String jsonPath) {
        List<ShopOfferEntry> entries = new ArrayList<>();

        if (!(thisEntity.level() instanceof ServerLevel serverLevel)) {
            return entries;
        }

        ResourceManager resourceManager = serverLevel.getServer().getResourceManager();
        ResourceLocation configLocation = Treasures_of_the_dead.resource(jsonPath);

        try {
            var resource = resourceManager.getResource(configLocation).orElseThrow();
            JsonObject json = JsonParser.parseReader(new InputStreamReader(resource.open())).getAsJsonObject();
            JsonElement entriesElement = json.get("entries");

            if (entriesElement != null && entriesElement.isJsonArray()) {
                for (JsonElement element : entriesElement.getAsJsonArray()) {
                    JsonObject obj = element.getAsJsonObject();
                    String itemId = obj.get("item").getAsString();
                    int price = obj.get("price").getAsInt();
                    int minLevel = obj.has("minLevel") ? obj.get("minLevel").getAsInt() : 0;
                    int displayLevel = obj.has("displayLevel") ? obj.get("displayLevel").getAsInt() : 0;
                    int count = obj.has("count") ? obj.get("count").getAsInt() : 1;

                    // Загружаем зачарования
                    Map<String, Integer> enchantments = new HashMap<>();
                    if (obj.has("enchantments")) {
                        JsonObject enchObj = obj.getAsJsonObject("enchantments");
                        for (Map.Entry<String, JsonElement> entry : enchObj.entrySet()) {
                            String enchantmentId = entry.getKey();
                            int level = entry.getValue().getAsInt();
                            enchantments.put(enchantmentId, level);
                        }
                    }

                    // Загружаем компоненты
                    Map<String, Object> components = new HashMap<>();
                    if (obj.has("components")) {
                        JsonObject compObj = obj.getAsJsonObject("components");
                        for (Map.Entry<String, JsonElement> entry : compObj.entrySet()) {
                            String componentId = entry.getKey();
                            JsonElement value = entry.getValue();

                            // Определяем тип значения
                            if (value.isJsonPrimitive()) {
                                var primitive = value.getAsJsonPrimitive();
                                if (primitive.isNumber()) {
                                    components.put(componentId, primitive.getAsNumber());
                                } else if (primitive.isBoolean()) {
                                    components.put(componentId, primitive.getAsBoolean());
                                } else if (primitive.isString()) {
                                    components.put(componentId, primitive.getAsString());
                                }
                            } else if (value.isJsonObject()) {
                                // Для сложных объектов можно сохранить как строку JSON
                                components.put(componentId, value.toString());
                            } else if (value.isJsonArray()) {
                                components.put(componentId, value.toString());
                            } else {
                                components.put(componentId, value.toString());
                            }
                        }
                    }

                    entries.add(new ShopOfferEntry(itemId, price, minLevel, displayLevel, count, enchantments, components));
                }
            }

            Treasures_of_the_dead.LOGGER.info("Loaded {} shop offers from {}", entries.size(), jsonPath);

        } catch (Exception e) {
            Treasures_of_the_dead.LOGGER.error("Error loading shop offers from {}: {}", jsonPath, e.getMessage());
        }

        return entries;
    }
}
