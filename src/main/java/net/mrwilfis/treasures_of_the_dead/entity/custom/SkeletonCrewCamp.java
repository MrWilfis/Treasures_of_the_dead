package net.mrwilfis.treasures_of_the_dead.entity.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.mrwilfis.treasures_of_the_dead.TOTDUtils;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.variant.*;

import java.io.InputStreamReader;
import java.util.*;

import static net.mrwilfis.treasures_of_the_dead.TOTDUtils.loadMinMaxParameterFromJson;

public class SkeletonCrewCamp extends Entity{
//    private static final EntityDataAccessor<Integer> CURRENT_WAVE = SynchedEntityData.defineId(SkeletonCrewCamp.class, EntityDataSerializers.INT);
//    private static final EntityDataAccessor<Integer> WAVES = SynchedEntityData.defineId(SkeletonCrewCamp.class, EntityDataSerializers.INT);
//    private static final EntityDataAccessor<Integer> DIFFICULTY = SynchedEntityData.defineId(SkeletonCrewCamp.class, EntityDataSerializers.INT);
    private final List<LivingEntity> campSkeletons = new ArrayList<>();
    private final List<UUID> campSkeletonUUIDs = new ArrayList<>();
    private final List<LivingEntity> skeletonsToSpawn = new ArrayList<>();
    private boolean loaded = false;
    private int countPirates;
    private int countCaptains;
    private int difficulty; // Влияет на шансы появление разных видов скелетов
    private int maxWaves;
    private int currentWave = 0;
    private boolean isFinalWave;
    private int wavesCooldown = 100;
    private int waveCDTimer = 0;
//    private final int maxSkeletonSpawnCooldown = 10;
    private int skeletonSpawnCDTimer = 0;
    private String[] currentWaveSkeletonTypes = new String[0];
    private final Random rand = new Random();
    private final Map<String, String> pirateTypeToCaptainType = new HashMap<>();

    public SkeletonCrewCamp(EntityType<?> entityType, Level level) {
        super(entityType, level);
        pirateTypeToCaptainTypeMethod();
    }

    public SkeletonCrewCamp(EntityType<?> entityType, Level level, int difficulty, int waves) {
        super(entityType, level);
        this.difficulty = difficulty;
        this.maxWaves = waves;
        pirateTypeToCaptainTypeMethod();
    }

    private void pirateTypeToCaptainTypeMethod() {
        this.pirateTypeToCaptainType.put("treasures_of_the_dead:totd_skeleton", "treasures_of_the_dead:captain_skeleton");
        this.pirateTypeToCaptainType.put("treasures_of_the_dead:blooming_skeleton", "treasures_of_the_dead:captain_blooming_skeleton");
        this.pirateTypeToCaptainType.put("treasures_of_the_dead:shadow_skeleton", "treasures_of_the_dead:captain_shadow_skeleton");
        this.pirateTypeToCaptainType.put("treasures_of_the_dead:golden_skeleton", "treasures_of_the_dead:captain_golden_skeleton");
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            if (this.skeletonSpawnCDTimer > 0) {
                this.skeletonSpawnCDTimer--;
            }
        }

        if (this.tickCount > 48000L) {
            this.remove(RemovalReason.DISCARDED);
        }

        if (!this.loaded) {
            loadCampSkeletonsFromUUID();
            if (!this.level().isClientSide) {
                loadMaxWaves();
                loadWavesCoolDown();
            }


            this.loaded = true;
        }

        if (!this.level().isClientSide) {
            if (this.campSkeletons.isEmpty()) {
                if (this.waveCDTimer > 0) {
                    this.waveCDTimer--;
                } else {
                    if (this.currentWave < this.maxWaves) {
                        this.spawnNextWave();
                        this.currentWave++;
                    } else {
                        System.out.println("CAMP CLEARED");
                        this.discard();
                    }
                }
            } else {
                this.campSkeletons.removeIf(skeleton -> {
                    if (!skeleton.isAlive()) {
                        campSkeletonUUIDs.remove(skeleton.getUUID());
                        return true;
                    }
                    return false;
                });
                if (campSkeletons.isEmpty()) {
                    this.waveCDTimer = wavesCooldown;
                }
            }
        }

        if (!this.level().isClientSide ) {
            if (!this.skeletonsToSpawn.isEmpty() && this.skeletonSpawnCDTimer <= 0) {
                LivingEntity entity = this.skeletonsToSpawn.getFirst();
                this.skeletonsToSpawn.removeFirst();

                this.campSkeletons.add(entity);
                this.campSkeletonUUIDs.add(entity.getUUID());

                this.level().addFreshEntity(entity);
                if (entity instanceof Monster entity1) {
                    entity1.playAmbientSound();
                }

                int totalSkeletonsInWave = this.countPirates + (this.currentWave + 1 == this.maxWaves ? this.countCaptains : 0);
                int calculatedSpawnCD = Math.max(1, (int) (30.0f / totalSkeletonsInWave));
                this.skeletonSpawnCDTimer = calculatedSpawnCD;
            }
        }
    }

    private void spawnNextWave() {
        System.out.println("NEW WAVE: " + (this.currentWave+1));

        List<Map.Entry<String, Integer>> entitiesConfigs = loadEntityConfigFromJson(this, "raid_entity_data/camp/camp_"+this.difficulty+".json", "entities");
        if (entitiesConfigs.isEmpty()) {
            System.err.println("Error: unable to load entity config for difficulty " + this.difficulty);
            return;
        }

        Map<String, Integer> entitiesCount = loadMinMaxParameterFromJson(this, "raid_entity_data/camp/camp_"+this.difficulty+".json", "entities_count");
        if (entitiesCount.isEmpty()) {
            System.err.println("Error: unable to load entity config for difficulty " + this.difficulty);
            this.countPirates = 5;
        } else {
            int min = entitiesCount.get("min");
            int max = entitiesCount.get("max");
            //System.out.println("ЗАЕБИСЬ: " + min + " " + max);
            if (min <= max) {
                this.countPirates = this.random.nextInt(min, max+1);
            } else {
                System.err.println("Error: min is higher than max in json file camp_" + this.difficulty + ".json" + " (entities_count)");
            }
        }

        Map<String, Integer> bossCount = loadMinMaxParameterFromJson(this, "raid_entity_data/camp/camp_"+this.difficulty+".json", "boss_count");
        if (bossCount.isEmpty()) {
            System.err.println("Error: unable to load entity config for difficulty " + this.difficulty);
            this.countCaptains = 1;
        } else {
            int min = bossCount.get("min");
            int max = bossCount.get("max");
            //System.out.println("ЗАЕБИСЬ: " + min + " " + max);
            if (min <= max) {
                this.countCaptains = this.random.nextInt(min, max+1);
            } else {
                System.err.println("Error: min is higher than max in json file camp_" + this.difficulty + ".json" + " (boss_count)");
            }
        }

        selectEntityTypesForWave(entitiesConfigs);


        for (int i = 0; i < this.countPirates; i++) {
            String selectedType = currentWaveSkeletonTypes[rand.nextInt(currentWaveSkeletonTypes.length)];
            LivingEntity pirate = newEntity(selectedType);
            if (pirate != null) {
                BlockPos pos = findNearbyPos();
                pirate.moveTo(pos.getX(), pos.getY(), pos.getZ(), rand.nextFloat(-180f, 180f), 0f);
                pirate = specialProcedures(pirate);
                skeletonsToSpawn.add(pirate);
                //this.level().addFreshEntity(pirate);
            }
        }
        if (this.currentWave+1 == this.maxWaves) {
            for (int j = 0; j < this.countCaptains; j++) {
                String selectedType = currentWaveSkeletonTypes[rand.nextInt(currentWaveSkeletonTypes.length)];
                LivingEntity captain = newEntity(pirateTypeToCaptainType.get(selectedType));
                if (captain != null) {
                    BlockPos pos = findNearbyPos();
                    captain.moveTo(pos.getX(), pos.getY(), pos.getZ(), rand.nextFloat(-180f, 180f), 0f);
                    captain = specialProcedures(captain);
                    skeletonsToSpawn.add(captain);
                    //this.level().addFreshEntity(captain);
                    //System.out.println("CAPTAIN SUMMONED: " + pirateTypeToCaptainType.get(selectedType));
                }
            }
        }
    }

    private void selectEntityTypesForWave(List<Map.Entry<String, Integer>> configs) {
        List<String> selectedTypes = new ArrayList<>();
        int totalWeight = configs.stream().mapToInt(Map.Entry::getValue).sum();

        int numTypes = (this.difficulty > 3) ? 2 : 1;

        for (int i = 0; i < numTypes; i++) {
            int randomWeight = rand.nextInt(totalWeight);
            int currentWeight = 0;
            String chosenType = null;
            for (Map.Entry<String, Integer> config : configs) {
                currentWeight += config.getValue();
                if (randomWeight < currentWeight) {
                    chosenType = config.getKey();
                    break;
                }
            }
            if (chosenType != null
                    //&& !selectedTypes.contains(chosenType)
            ) {
                selectedTypes.add(chosenType);
            }
//            else {
//                selectedTypes.add(configs.get(0).getKey());
//            }
        }
        this.currentWaveSkeletonTypes = selectedTypes.toArray(new String[0]);
        System.out.println("Selected types for wave: " + String.join(", ", currentWaveSkeletonTypes));
    }

    private LivingEntity newEntity(String typeId) {
        EntityType<?> entityType = EntityType.byString(typeId).orElse(ModEntities.BLOOMING_SKELETON.get());
        LivingEntity entity = (LivingEntity) entityType.create(this.level());

        if (entity != null) {
            return entity;
        }
        return null;
    }

    private LivingEntity specialProcedures(LivingEntity entity) {
        entity.addTag("TOTD_Rotate");
        ((Monster)entity).setPersistenceRequired();


        // is totd skeleton
        if (entity instanceof TOTDSkeletonEntity entity1) {
            entity1.setIsSpawning(true);
            entity1.setCampUUID(this.getUUID());
        }

        // set type for pirate skeletons
        if (entity instanceof BloomingSkeletonEntity entity1) {
            entity1.specialProcedures();
        }
        else if (entity instanceof ShadowSkeletonEntity entity1) {
            entity1.specialProcedures();
        }
        else if (entity instanceof GoldenSkeletonEntity entity1) {
            entity1.specialProcedures();
        }
        else if (entity instanceof TOTDSkeletonEntity entity1) {
            entity1.specialProcedures();
        }

        // set type for captain skeletons
        if (entity instanceof CaptainSkeletonEntity entity1) {
            entity1.specialProcedures();
            entity1.setCanDropKeysAndOrders(false);
        } else if (entity instanceof CaptainBloomingSkeletonEntity entity1) {
            entity1.specialProcedures();
            entity1.setCanDropKeysAndOrders(false);
        } else if (entity instanceof CaptainShadowSkeletonEntity entity1) {
            entity1.specialProcedures();
            entity1.setCanDropKeysAndOrders(false);
        } else if (entity instanceof CaptainGoldenSkeletonEntity entity1) {
            entity1.specialProcedures();
            entity1.setCanDropKeysAndOrders(false);
        }
        return entity;
    }

    private void loadWavesCoolDown() {
        int wavesCooldown = TOTDUtils.loadIntParameterFromJson(this, "raid_entity_data/camp/camp_"+this.difficulty+".json", "waves_cooldown");
        if (wavesCooldown < 0) {
            System.err.println("Error: waves cooldown are lower than 0 in camp_ " + this.difficulty + ".json");
            this.wavesCooldown = 100;
        } else {
            this.wavesCooldown = wavesCooldown;
        }
    }

    private void loadMaxWaves() {
        Map<String, Integer> waves = loadMinMaxParameterFromJson(this, "raid_entity_data/camp/camp_"+this.difficulty+".json", "waves");
        if (waves.isEmpty()) {
            System.err.println("Error: unable to load entity config for difficulty " + this.difficulty);
        } else {
            int min = waves.get("min");
            int max = waves.get("max");
            //System.out.println("ЗАЕБИСЬ: " + min + " " + max);

            if (min <= max) {
                if (this.maxWaves < min) { // Чтобы при перезаходе не перезаписывалось | for no rewriting on reenter the world
                    this.maxWaves = this.random.nextInt(min, max+1);
                }
            } else {
                System.err.println("Error: min is higher than max in json file camp_" + this.difficulty + ".json" + " (waves)");
            }

        }
    }

    private BlockPos findNearbyPos() {
        int x = (int)this.getX() + rand.nextInt(-4, 4);
        int z = (int)this.getZ() + rand.nextInt(-4, 4);

        int y = this.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

        return new BlockPos(x, y, z);
    }



    private List<Map.Entry<String, Integer>> loadEntityConfigFromJson(Entity thisEntity, String JsonPath, String entitiesOrBosses) {
        List<Map.Entry<String, Integer>> configs = new ArrayList<>();
        if (!(thisEntity.level() instanceof ServerLevel serverlevel)) {
            return configs;
        }

        ResourceManager resourceManager = serverlevel.getServer().getResourceManager();
        ResourceLocation configLocation = Treasures_of_the_dead.resource(JsonPath);

        try {
            var resource = resourceManager.getResource(configLocation).orElseThrow();
            JsonObject json = JsonParser.parseReader(new InputStreamReader(resource.open())).getAsJsonObject();
            JsonArray skeletonsArray = json.getAsJsonArray(entitiesOrBosses);
            for (JsonElement elem : skeletonsArray) {
                JsonObject skeletonObj = elem.getAsJsonObject();
                String type = skeletonObj.get("type").getAsString();
                int weight = skeletonObj.get("weight").getAsInt();
                configs.add(Map.entry(type, weight));
            }
        } catch (Exception e) {
            System.err.println("Error loading JSON for " + configLocation + ":" + e.getMessage());
        }
        return configs;
    }



    private void loadCampSkeletonsFromUUID() {
        this.campSkeletons.clear();
        for (UUID uuid : campSkeletonUUIDs) {
            Entity entity = ((ServerLevel)this.level()).getEntity(uuid);
            if (entity instanceof TOTDSkeletonEntity pirate) {
                campSkeletons.add(pirate);
            } else {
                campSkeletonUUIDs.remove(uuid);
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {

        if (tag.contains("MaxWaves")) {
            this.maxWaves = tag.getInt("MaxWaves");
        } else {
            this.maxWaves = 3; // Дефолт
        }
        if (tag.contains("CurrentWave")) {
            this.currentWave = tag.getInt("CurrentWave");
        } else {
            this.currentWave = 0; // Дефолт
        }
        if (tag.contains("Difficulty")) {
            this.difficulty = tag.getInt("Difficulty");
        } else {
            this.difficulty = 1; // Дефолт
        }if (tag.contains("WavesCooldown")) {
            this.wavesCooldown = tag.getInt("WavesCooldown");
        } else {
            this.wavesCooldown = 100; // Дефолт
        }
        if (tag.contains("CampSkeletonsUUIDs")) {
            ListTag piratesTag = tag.getList("CampSkeletonsUUIDs", ListTag.TAG_INT_ARRAY);
            this.campSkeletonUUIDs.clear();
            for (int i = 0; i < piratesTag.size(); i++) {
                campSkeletonUUIDs.add(NbtUtils.loadUUID(piratesTag.get(i)));
            }
        } else {
            this.campSkeletonUUIDs.clear(); // Дефолт: пустой список
        }
        this.loaded = false;

//        this.maxWaves = tag.getInt("MaxWaves");
//        this.currentWave = tag.getInt("CurrentWave");
//        ListTag piratesTag = tag.getList("CampSkeletonsUUIDs", ListTag.TAG_INT_ARRAY);
//        this.campSkeletonUUIDs.clear();
//        for (int i = 0; i < piratesTag.size(); i++) {
//            campSkeletonUUIDs.add(NbtUtils.loadUUID(piratesTag.get(i)));
//        }
//        this.loaded = false;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("MaxWaves", this.maxWaves);
        tag.putInt("CurrentWave", this.currentWave);
        tag.putInt("Difficulty", this.difficulty);
        tag.putInt("WavesCooldown", this.wavesCooldown);
        ListTag piratesTag = new ListTag();
        for (UUID uuid : this.campSkeletonUUIDs) {
            piratesTag.add(NbtUtils.createUUID(uuid));
        }
        tag.put("CampSkeletonsUUIDs", piratesTag);
    }
}
