package net.mrwilfis.treasures_of_the_dead.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.mrwilfis.treasures_of_the_dead.Config;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CustomCaptainNamesManager {
    private static CustomCaptainNamesManager instance;
    private List<String> customNames = new ArrayList<>();
    private boolean isLoaded = false;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private CustomCaptainNamesManager() {
        loadCustomNames();
    }

    public static CustomCaptainNamesManager getInstance() {
        if (instance == null) {
            instance = new CustomCaptainNamesManager();
        }
        return instance;
    }

    public void loadCustomNames() {
        if (!Config.captainNamesLang.equals("custom")) {
            isLoaded = false;
            customNames.clear();
            return;
        }

        try {
            Path configPath = FMLPaths.CONFIGDIR.get().resolve("treasures_of_the_dead");
            Path jsonPath = configPath.resolve("custom_captain_names.json");


            if (!Files.exists(configPath)) {
                Files.createDirectories(configPath);
            }

            if (!Files.exists(jsonPath)) {
                createDefaultJson(jsonPath);
            }


            String jsonContent = new String(Files.readAllBytes(jsonPath), StandardCharsets.UTF_8);
            Type listType = new TypeToken<List<String>>() {}.getType();
            List<String> loadedNames = gson.fromJson(jsonContent, listType);

            if (loadedNames != null && !loadedNames.isEmpty()) {
                customNames = loadedNames;
                isLoaded = true;
                System.out.println("Loaded " + customNames.size() + " custom captain names");
            } else {
                createDefaultJson(jsonPath);
                loadCustomNames();
            }
        } catch (IOException e) {
            System.err.println("Failed to load custom captain names: " + e.getMessage());
            // Загрузка дефолтных имен в случае ошибки
            customNames = getDefaultNames();
            isLoaded = true;
        }
    }

    private void createDefaultJson(Path jsonPath) throws IOException {
        List<String> defaultNames = getDefaultNames();
        String json = gson.toJson(defaultNames);
        Files.write(jsonPath, json.getBytes(StandardCharsets.UTF_8));
    }

    private List<String> getDefaultNames() {
        List<String> defaults = new ArrayList<>();
        defaults.add("Captain Jack Sparrow");
        defaults.add("Great and Villainous Arthur");
        defaults.add("Black Beard");
        defaults.add("Captain Hook");
        defaults.add("Johny Silver");
        defaults.add("Davy Jones");
        defaults.add("Captain Ahah");
        return defaults;
    }

    public String getRandomName(java.util.Random random) {
        if (!isLoaded || customNames.isEmpty()) {
            loadCustomNames();
            if (customNames.isEmpty()) {
                return "Captain Default";
            }
        }
        return customNames.get(random.nextInt(customNames.size()));
    }

    public List<String> getAllNames() {
        return new ArrayList<>(customNames);
    }

    public boolean isCustomModeEnabled() {
        return Config.captainNamesLang.equals("custom") && isLoaded;
    }

    public void reloadCustomNames() {
        isLoaded = false;
        loadCustomNames();
    }

    public String getNamesCount() {
        return customNames.size() + " names loaded";
    }
}
