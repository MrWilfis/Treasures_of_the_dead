package net.mrwilfis.treasures_of_the_dead.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;
import net.mrwilfis.treasures_of_the_dead.block.custom.SeaFortressCoreBlock;
import net.mrwilfis.treasures_of_the_dead.common.ModDataComponents;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.AnyTreasureClass;
import net.mrwilfis.treasures_of_the_dead.entity.custom.CaptainSkeletonEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.TOTDSkeletonEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.chestVariants.TreasureChestEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.DisgracedSkullEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.FoulSkullEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.HatefulSkullEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.VillainousSkullEntity;
import net.mrwilfis.treasures_of_the_dead.item.custom.SeaFortressKeyItem;
import net.mrwilfis.treasures_of_the_dead.particle.ModParticles;

import java.util.*;

public class SeaFortressCoreBlockEntity extends BlockEntity {

    public enum State {
        WAITING,
        ACTIVE,
        COOLDOWN,
        REWARDING
    }

    private State currentState = State.WAITING;
    private UUID seaFortressUUID;
    private UUID captainUUID = UUID.randomUUID();
    private int playerDetectionRaduis = 40;
    private final List<BlockPos> availableBlocksForReward = new ArrayList<>();
    private final List<BlockPos> blockedBlocksForReward = new ArrayList<>();
    private final List<BlockPos> blocksForEnemiesSpawning = new ArrayList<>();
    private final List<UUID> spawnedMobs = new ArrayList<>();
    private List<AnyTreasureClass> generatedRewards = new ArrayList<>();
    private int currentRewardIndex = 0;
    private int activeTicks = 0;
    private int currentWave = 0;
    private int maxWaves = 10;
    private int waveSpawnDelay = 100;
    private long nextTimeActive = 0;
    private long lastTimeActive = 0;
    private boolean isProcessingReward = false;
    private int rewardingTicks = 0;
    private int rewardsGiven = 0;
    private int totalEnemies = 0;
    private final Random rand = new Random();
    private final int MAX_COOLDOWN_TICKS = 20*60*30;
    private final int MAX_ACTIVE_TICKS = 20*60*30;



    public SeaFortressCoreBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SEA_FORTRESS_CORE_BE.get(), pos, blockState);
        if (this.seaFortressUUID == null) {
            this.seaFortressUUID = UUID.randomUUID();
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SeaFortressCoreBlockEntity blockEntity) {
        if (level.isClientSide) return;

        ServerLevel serverLevel = (ServerLevel) level;

        switch (blockEntity.currentState) {
            case WAITING -> blockEntity.tickWaiting(serverLevel, pos);
            case ACTIVE -> blockEntity.tickActive(serverLevel, pos);
            case COOLDOWN -> blockEntity.tickCooldown(serverLevel, pos);
            case REWARDING -> blockEntity.tickRewarding(serverLevel, pos);
        }

        blockEntity.markUpdated();
    }

    private void updateBlockState() {
        if (level != null && !level.isClientSide) {
            SeaFortressCoreBlock.updateBlockState(currentState, level, worldPosition);
        }
    }

    private void tickWaiting(ServerLevel level, BlockPos pos) {
        AABB detectionBox = new AABB(pos).inflate(playerDetectionRaduis);
        List<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, detectionBox);

        if (!nearbyPlayers.isEmpty()) {
            currentState = State.ACTIVE;
            activeTicks = 0;
            currentWave = 0;
            waveSpawnDelay = 100;
            totalEnemies = 0;
            maxWaves = level.random.nextInt(8, 12+1);
            findBlocksForEnemiesSpawning(level, pos);

                level.playSound(null, pos, SoundEvents.VAULT_ACTIVATE,
                        SoundSource.BLOCKS, 1.0f, 1.0f);
            updateBlockState();
//            System.out.println("ACTIVE");
        }

        level.sendParticles(ModParticles.GHOST_PARTICLES.get(),
                this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5,
                1,
                0.3,
                0.3,
                0.3,
                1);
    }

    private void tickActive(ServerLevel level, BlockPos pos) {
        activeTicks++;

        if (!spawnedMobs.isEmpty()) {
            Iterator<UUID> iterator = spawnedMobs.iterator();
            while (iterator.hasNext()) {
                UUID mobId = iterator.next();
                Entity mob = level.getEntity(mobId);
                if (activeTicks % 100 == 0) {
                    ((Mob)mob).setTarget(level.getNearestPlayer(mob, 40));
                }
                if (mob == null || !mob.isAlive()) {
                    iterator.remove();
                }
            }

            if (currentWave == maxWaves && captainUUID != null) {
//                Mob captain = (Mob)level.getEntity(captainUUID);
//                if (captain != null) {
//                    if (captain.isDeadOrDying()) {
//                        onCaptainDefeated(level, captain.getUUID(), captain.getOnPos());
//                    }
//                }
                Entity captainEntity = level.getEntity(captainUUID);
                if (captainEntity instanceof LivingEntity captain) {
                    if (captain.isDeadOrDying() && !captain.isAlive()) {
                        onCaptainDefeated(level, captain.getUUID(), captain.getOnPos());
                    }
                }

            }
        }

        if (spawnedMobs.isEmpty()) {
            if (waveSpawnDelay > 0) {
                waveSpawnDelay--;
            } else if (currentWave < maxWaves) {
                currentWave++;
                spawnNextWave(level, pos);
                waveSpawnDelay = 180;
            } else {
                currentState = State.COOLDOWN;
                lastTimeActive = this.level.getGameTime();
                nextTimeActive = lastTimeActive + MAX_COOLDOWN_TICKS;

                updateBlockState();

//                level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP,
//                        SoundSource.BLOCKS, 10.0f, 1.0f);
//                System.out.println("COOLDOWN");
            }
        }

        if (waveSpawnDelay > 0 & currentWave != maxWaves) {
            BlockPos bellPos = findBell(level, pos);
            if (bellPos != null) {
                if (waveSpawnDelay == 80 || waveSpawnDelay == 64 || waveSpawnDelay == 48) {
                    if (level.getBlockState(bellPos).is(Blocks.BELL)) {
                        BellBlock bell = (BellBlock) level.getBlockState(bellPos).getBlock();
                        bell.attemptToRing(level, bellPos, randomBellHitDirection());
                    }

                }
            }
        }
        if (waveSpawnDelay > 0 & currentWave == maxWaves) {
            BlockPos bellPos = findBell(level, pos);
            if (bellPos != null) {
                if (waveSpawnDelay == 68 || waveSpawnDelay == 64 || waveSpawnDelay == 54 || waveSpawnDelay == 50) {
                    if (level.getBlockState(bellPos).is(Blocks.BELL)) {
                        BellBlock bell = (BellBlock) level.getBlockState(bellPos).getBlock();
                        bell.attemptToRing(level, bellPos, randomBellHitDirection());
                    }

                }
            }
        }

        if (activeTicks > 0) {
            level.sendParticles(ModParticles.GHOST_PARTICLES.get(),
                    this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5,
                    1,
                    0.3,
                    0.3,
                    0.3,
                    1);
        }



        if (activeTicks>=MAX_ACTIVE_TICKS) {
            activeTicks = 0;
            lastTimeActive = this.level.getGameTime();
            nextTimeActive = lastTimeActive + MAX_COOLDOWN_TICKS;
            spawnedMobs.clear();
            currentState = State.COOLDOWN;
            updateBlockState();
//            level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP,
//                    SoundSource.BLOCKS, 10.0f, 1.0f);
//            System.out.println("COOLDOWN");
        }
    }

    private void tickCooldown(ServerLevel level, BlockPos pos) {
        if (this.level.getGameTime() >= nextTimeActive) {
            currentState = State.WAITING;
            updateBlockState();
//            System.out.println("WAITING");
        }
    }

    private void tickRewarding(ServerLevel level, BlockPos pos) {
        if (!isProcessingReward) return;

        rewardingTicks++;

//        if (rewardingTicks <= 1) {
//            findStoneBricks(level, pos);
//        }
        if (rewardingTicks % 20 == 0) {
            findBlocksForRewards(level, pos);
            spawnReward(level);
            rewardsGiven++;

//            if (false) {
//                isProcessingReward = false;
//                currentState = State.COOLDOWN;
//
//                updateBlockState();
//
//                rewardsGiven = 0;
//                blockedBlocksForReward.clear();
//                System.out.println("COOLDOWN");
//            }
        }

        if (rewardingTicks % 2 == 0) {
            level.sendParticles(ModParticles.GHOST_PARTICLES.get(),
                    this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 1, this.worldPosition.getZ() + 0.5,
                    1,
                    0.15,
                    0.1,
                    0.15,
                    1);
        }
    }

    private void spawnNextWave(ServerLevel level, BlockPos pos) {
        int mobsCount = 3 + currentWave + rand.nextInt(1, + 3+1);
        findBlocksForEnemiesSpawning(level, pos);
        for (int i = 0; i < mobsCount; i++) {
            if (blocksForEnemiesSpawning.isEmpty()) {
                if (spawnedMobs.isEmpty()) {
                    currentState = State.COOLDOWN;
                    updateBlockState();
                    lastTimeActive = this.level.getGameTime();
                    nextTimeActive = lastTimeActive + MAX_COOLDOWN_TICKS;
                    activeTicks = 0;
                }
                break;
            }

            int randomPosId = rand.nextInt(blocksForEnemiesSpawning.size());

            BlockPos spawnPos = blocksForEnemiesSpawning.get(randomPosId);

            float var1;
            if (isTopSlab(level, spawnPos)) {
                var1 = 1.0f;
            } else {
                var1 = 0.5f;
            }

            if (i == 0 & currentWave == maxWaves) {
                CaptainSkeletonEntity captain = new CaptainSkeletonEntity(ModEntities.CAPTAIN_SKELETON.get(), level);
                captain.setPos(spawnPos.getX()+0.5, spawnPos.getY()+var1, spawnPos.getZ()+0.5);
                captain.yHeadRot = rand.nextFloat(0.0f, 360.0f);
                captain.setIsSpawning(true);
                captain.specialProcedures();
                captain.setPersistenceRequired();
                captain.setTarget(level.getNearestPlayer(captain, 40));
                captain.setCanDropKeysAndOrders(false);
//                System.out.println("CAPTAIN");
                if (level.addFreshEntity(captain)) {
                    totalEnemies++;
                    captainUUID = captain.getUUID();
                    spawnedMobs.add(captain.getUUID());
                    blocksForEnemiesSpawning.remove(randomPosId);
                }
            } else {
                TOTDSkeletonEntity enemy = new TOTDSkeletonEntity(ModEntities.TOTD_SKELETON.get(), level);
                enemy.setPos(spawnPos.getX()+0.5, spawnPos.getY()+var1, spawnPos.getZ()+0.5);
                enemy.yHeadRot = rand.nextFloat(0.0f, 360.0f);
                enemy.setIsSpawning(true);
                enemy.specialProcedures();
                enemy.setPersistenceRequired();
                enemy.setTarget(level.getNearestPlayer(enemy, 40));
                if (level.addFreshEntity(enemy)) {
                    totalEnemies++;
                    spawnedMobs.add(enemy.getUUID());
                    blocksForEnemiesSpawning.remove(randomPosId);
                }
            }

        }
//        System.out.println("NEW WAVE: " + currentWave);
    }

    private void markUpdated() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    private void spawnSeaFortressKey(ServerLevel level, BlockPos seaFortressPos, BlockPos dropPos) {
        ItemStack key = SeaFortressKeyItem.createViaSeaFortress(seaFortressPos, totalEnemies * 0.843f);
        ItemEntity itemEntity = new ItemEntity(level, dropPos.getX(), dropPos.getY(), dropPos.getZ(), key);
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);
    }

    private void findBlocksForRewards(ServerLevel level, BlockPos center) {
        availableBlocksForReward.clear();
        int radius = 5;

        for (int x = -radius; x <= radius; x++) {
            for (int y = 0; y <= 6; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos checkPos = center.offset(x, y, z);
                    if (level.getBlockState(checkPos).is(Blocks.TUFF_BRICK_SLAB) ||
                            level.getBlockState(checkPos).is(Blocks.CHISELED_TUFF_BRICKS)) {
                        if (isEnoughSpaceToSpawn(level, checkPos, 1)) {
                            availableBlocksForReward.add(checkPos.immutable());
                        }
                    }
                }
            }
        }
    }

    private void findBlocksForEnemiesSpawning(ServerLevel level, BlockPos center) {
        blocksForEnemiesSpawning.clear();
        int radius = 30;

        for (int x = -radius; x <= radius; x++) {
            for (int y = 5; y <= 17; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos checkPos = center.offset(x, y, z);
                    if (level.getBlockState(checkPos).is(Blocks.TUFF_BRICK_SLAB)
                            || level.getBlockState(checkPos).is(Blocks.TUFF_BRICKS)
                            || level.getBlockState(checkPos).is(Blocks.OAK_SLAB)) {
                        BlockPos checkSky = new BlockPos(checkPos.getX(), checkPos.getY()+1, checkPos.getZ());
                        if (!level.canSeeSky(checkSky)) {
                            if (isEnoughSpaceToSpawn(level, checkPos, 2)) {
                                blocksForEnemiesSpawning.add(checkPos.immutable());
                            }
                        }
                    }
                }
            }
        }
    }

    private BlockPos findBell(ServerLevel level, BlockPos center) {
        for (int x = -40; x <= 40; x++) {
            for (int y = 0; y <= 30; y++) {
                for (int z = -40; z <= 40; z++) {
                    BlockPos checkPos = center.offset(x, y, z);
                    if (level.getBlockState(checkPos).is(Blocks.BELL)) {
                        return checkPos;
                    }
                }
            }
        }
        return null;
    }

    private boolean isEnoughSpaceToSpawn(ServerLevel level, BlockPos pos, int height) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        for (int i = 1; i <= height; i++) {
            BlockPos checkPos = new BlockPos(x, y+i, z);
            if (!level.getBlockState(checkPos).isAir()) {
                return false;
            }
        }
        return true;
    }

    public ItemInteractionResult onKeyUsed(ItemStack item, Player player) {

        if (level == null || level.isClientSide) return null;

        if (item.getItem() instanceof SeaFortressKeyItem) {
            if (currentState == State.WAITING || currentState == State.COOLDOWN) {
                BlockPos pos = item.get(ModDataComponents.SEA_FORTRESS_COORDINATES);

//                System.out.println(pos.getX() + " " + pos.getY() + " " + pos.getZ());
//                System.out.println(worldPosition.getX() + " " + worldPosition.getY() + " " + worldPosition.getZ());

                if (pos == null || pos.equals(worldPosition) || player.isCreative()) {
                    if (!isProcessingReward) {

                        Float lootValue = item.get(ModDataComponents.LOOT_VALUE);
                        if (lootValue == null) lootValue = 67f;

                        if (level instanceof ServerLevel serverLevel) {
                            generatedRewards = generateRewardsFromKey(serverLevel, lootValue);
//                            System.out.println("Generated " + generatedRewards.size() +
//                                    " rewards for lootValue: " + lootValue);

                            if (generatedRewards.isEmpty()) {
//                                player.displayClientMessage(Component.translatable(
//                                        "message.treasures_of_the_dead.no_rewards"), true);
                                return ItemInteractionResult.FAIL;
                            }
                        }

                        startRewardProcess();
                        if (!player.isCreative()) {
                            item.shrink(1);
                        }
                        return ItemInteractionResult.SUCCESS;
                    }
                } else {
                    player.displayClientMessage(Component.translatable("tooltip.treasures_of_the_dead.sea_fortress_key_use_wrong.tooltip"), true);
                }

            } else {
                player.displayClientMessage(Component.translatable("tooltip.treasures_of_the_dead.sea_fortress_key_use_active.tooltip"), true);
            }
        }

        return ItemInteractionResult.FAIL;
    }

    private void startRewardProcess() {
        //System.out.println("REWARDING");
        currentState = State.REWARDING;

        updateBlockState();

        isProcessingReward = true;
        rewardingTicks = 0;
        rewardsGiven = 0;
        currentRewardIndex = 0;

        if (level != null) {
            level.playSound(null, worldPosition, SoundEvents.DECORATED_POT_INSERT,
                    SoundSource.BLOCKS, 1.0f, 1.0f);
            level.playSound(null, worldPosition, SoundEvents.CONDUIT_ACTIVATE,
                    SoundSource.BLOCKS, 0.5f, 0.5f);
        }
    }

    private List<AnyTreasureClass> generateRewardsFromKey(ServerLevel level, float lootValue) {
        List<AnyTreasureClass> rewards = new ArrayList<>();

        if (level == null) {
            return getFallbackRewards(level);
        }

        TreasureData.ensureLoaded(level);

        Map<ResourceLocation, Integer> spawnedCounts = new HashMap<>();

        while (lootValue > 0) {
            TreasureData.TreasureConfig config = TreasureData.getWeightedRandomTreasure(level, lootValue, rand);

            if (config == null) {
                break;
            }

            int currentCount = spawnedCounts.getOrDefault(config.entityId(), 0);
            if (currentCount >= config.maxCount()) {
                continue;
            }

            AnyTreasureClass treasure = TreasureData.createTreasureFromId(config.entityId(), level);
            if (treasure != null) {
                rewards.add(treasure);
                lootValue -= config.value();
                spawnedCounts.put(config.entityId(), currentCount + 1);

//                System.out.println("Added treasure: " + config.entityId() +
//                        " (value: " + config.value() +
//                        ", lootValue left: " + lootValue + ")");
            }
        }

        Collections.shuffle(rewards, new Random(level.random.nextLong()));

        //System.out.println("Final rewards amount: " + rewards.size());
        return rewards;
    }

    private void spawnReward(ServerLevel level) {
        if (availableBlocksForReward.isEmpty()) return;

        if (currentRewardIndex >= generatedRewards.size()) {
            isProcessingReward = false;
            currentState = State.COOLDOWN;

            updateBlockState();

            rewardsGiven = 0;
            blockedBlocksForReward.clear();
            generatedRewards.clear();
            currentRewardIndex = 0;
            level.playSound(null, worldPosition, SoundEvents.CONDUIT_DEACTIVATE,
                    SoundSource.BLOCKS, 0.5f,1.5f);
            //System.out.println("COOLDOWN - all rewards are given");
            return;
        }

        BlockPos spawnPos = availableBlocksForReward.get(
                level.random.nextInt(availableBlocksForReward.size()));

        if (!haveSameElements(blockedBlocksForReward, availableBlocksForReward)) {
            if (blockedBlocksForReward.contains(spawnPos)) {
                spawnReward(level);
                return;
            }
            blockedBlocksForReward.add(spawnPos);
        }


        float var1;
        if (isTopSlab(level, spawnPos)) {
            var1 = 1.0f;
        } else {
            var1 = 0.5f;
        }

        AnyTreasureClass treasure = generatedRewards.get(currentRewardIndex);
        currentRewardIndex++;

        if (treasure instanceof LivingEntity reward) {
            reward.moveTo(spawnPos.getX() + 0.5, spawnPos.getY() + var1, spawnPos.getZ() + 0.5);

            Random rand = new Random();
            float randomView = rand.nextFloat(0.0f, 360.0f);
            reward.setYRot(randomView);
            reward.yHeadRot = randomView;

            level.addFreshEntity(reward);

            level.sendParticles(ModParticles.GHOST_PARTICLES.get(),
                    spawnPos.getX() + 0.5, spawnPos.getY() + 0.4 + var1, spawnPos.getZ() + 0.5,
                    20,
                    0.3,
                    0.3,
                    0.3,
                    1);
            level.playSound(null, spawnPos.getX(), spawnPos.getY() + 1, spawnPos.getZ(),
                    SoundEvents.SHULKER_SHOOT, SoundSource.AMBIENT,
                    0.25f, rand.nextFloat(0.73f, 0.77f));
        }

        rewardsGiven++;
        if (currentRewardIndex >= generatedRewards.size()) {
            rewardingTicks = 20;
        }




    }

    public boolean haveSameElements(List<BlockPos> list1, List<BlockPos> list2) {
        if (list1 == list2) return true;
        if (list1 == null || list2 == null) return false;
        if (list1.size() != list2.size()) return false;

        Set<BlockPos> set1 = new HashSet<>(list1);
        Set<BlockPos> set2 = new HashSet<>(list2);

        return set1.equals(set2);
    }

    private boolean isTopSlab(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof SlabBlock) {
            SlabType slabType = state.getValue(SlabBlock.TYPE);
            return slabType == SlabType.TOP || slabType == SlabType.DOUBLE;
        }

        return true;
    }

    public void onCaptainDefeated(ServerLevel level, UUID defeatedCaptainUUID, BlockPos deathPos) {
        if (level == null || level.isClientSide) return;

        if (captainUUID != null && captainUUID.equals(defeatedCaptainUUID)) {
            //System.out.println("Sea fortress captain defeated!!!");

            spawnSeaFortressKey(level, worldPosition, deathPos);

            captainUUID = UUID.randomUUID();
        }
    }

    private List<AnyTreasureClass> getFallbackRewards(ServerLevel level) {
        if (level == null) return new ArrayList<>();

        return Arrays.asList(
                new TreasureChestEntity(ModEntities.TREASURE_CHEST.get(), level),
                new FoulSkullEntity(ModEntities.FOUL_SKULL.get(), level),
                new DisgracedSkullEntity(ModEntities.DISGRACED_SKULL.get(), level),
                new HatefulSkullEntity(ModEntities.HATEFUL_SKULL.get(), level),
                new VillainousSkullEntity(ModEntities.VILLAINOUS_SKULL.get(), level)
        );
    }

    private Direction randomBellHitDirection() {
        int i = level.random.nextInt(1, 4+1);
        Direction var1 = Direction.EAST;
        switch (i) {
            case 1 -> var1 = Direction.EAST;
            case 2 -> var1 = Direction.WEST;
            case 3 -> var1 = Direction.NORTH;
            case 4 -> var1 = Direction.SOUTH;
        }
        return var1;
    }

    private AnyTreasureClass getFallbackRandomReward(ServerLevel level) {
        List<AnyTreasureClass> rewards = getFallbackRewards(level);
        return rewards.get(level.random.nextInt(rewards.size()));
    }

    public UUID getSeaFortressUUID() {
        if (seaFortressUUID == null) {
            seaFortressUUID = UUID.randomUUID();
        }
        return seaFortressUUID;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (seaFortressUUID != null) {
            tag.putUUID("SeaFortressUUID", seaFortressUUID);
        } else {
            seaFortressUUID = UUID.randomUUID();
            tag.putUUID("SeaFortressUUID", seaFortressUUID);
        }

        tag.putString("State", currentState.name());
        tag.putInt("ActiveTicks", activeTicks);
        tag.putLong("NextTimeActive", nextTimeActive);
        tag.putLong("LastTimeActive", lastTimeActive);
        tag.putInt("CurrentWave", currentWave);
        tag.putInt("MaxWaves", maxWaves);
        tag.putInt("WaveSpawnDelay", waveSpawnDelay);
        tag.putInt("RewardingTicks", rewardingTicks);
        tag.putBoolean("ProcessingReward", isProcessingReward);
        tag.putInt("RewardsGiven", rewardsGiven);
        tag.putInt("TotalEnemies", totalEnemies);
        tag.putUUID("CaptainUUID", captainUUID);

        ListTag mobsTags = new ListTag();
        for (UUID mobId : spawnedMobs) {
            CompoundTag mobTag = new CompoundTag();
            mobTag.putUUID("SeaFortress", mobId);
            mobsTags.add(mobTag);
        }
        tag.put("SpawnedMobs", mobsTags);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.hasUUID("SeaFortressUUID")) {
            seaFortressUUID = tag.getUUID("SeaFortressUUID");
        } else {
            seaFortressUUID = UUID.randomUUID();
        }

        if (tag.contains("State")) {
            currentState = State.valueOf(tag.getString("State"));
            if (level != null && !level.isClientSide) {
                updateBlockState();
            }
        }
        activeTicks = tag.getInt("ActiveTicks");
        nextTimeActive = tag.getLong("NextTimeActive");
        lastTimeActive = tag.getLong("LastTimeActive");
        currentWave = tag.getInt("CurrentWave");
        maxWaves = tag.getInt("MaxWaves");
        waveSpawnDelay = tag.getInt("WaveSpawnDelay");
        rewardingTicks = tag.getInt("RewardingTicks");
        isProcessingReward = tag.getBoolean("ProcessingReward");
        rewardsGiven = tag.getInt("RewardsGiven");
        totalEnemies = tag.getInt("TotalEnemies");
        captainUUID = tag.getUUID("CaptainUUID");

        spawnedMobs.clear();
        if (tag.contains("SpawnedMobs")) {
            ListTag mobsTags = tag.getList("SpawnedMobs", CompoundTag.TAG_COMPOUND);
            for (int i = 0; i < mobsTags.size(); i++) {
                CompoundTag mobTag = mobsTags.getCompound(i);
                spawnedMobs.add(mobTag.getUUID("SeaFortress"));
            }
        }


    }
}
