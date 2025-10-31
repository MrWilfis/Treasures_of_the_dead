package net.mrwilfis.treasures_of_the_dead;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TOTDUtils {

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
}
