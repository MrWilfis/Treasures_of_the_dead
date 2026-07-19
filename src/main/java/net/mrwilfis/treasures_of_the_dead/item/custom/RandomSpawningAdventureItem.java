package net.mrwilfis.treasures_of_the_dead.item.custom;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.mrwilfis.treasures_of_the_dead.Config;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.*;
import net.mrwilfis.treasures_of_the_dead.entity.custom.chestVariants.TreasureChestEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.VillainousSkullEntity;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;


import java.util.List;
import java.util.Random;

public class RandomSpawningAdventureItem extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final String taskType; // TYPES: treasure_map, skeleton_crew, random_task, and I will be adding new types

    public RandomSpawningAdventureItem(Properties pProperties, String taskType) {
        super(pProperties);
        this.taskType = taskType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {

        int range = Config.randomAdventureItemDistanceInChunks * 16;

        RandomSource random = level.getRandom();
        Random rand = new Random();

        double randomValue = 0;

        if (this.taskType.equals("random_task")) {
            randomValue = random.nextDouble();
        }

        ItemStack stack = player.getItemInHand(pUsedHand);

        double X = player.getX();
        double Y = player.getY();
        double Z = player.getZ();

        int difficulty = 0;

        if (stack.getTag() != null) {
            if (stack.getTag().contains("DeathX") && stack.getTag().contains("DeathZ")) {
                X = stack.getTag().getFloat("DeathX");
                Z = stack.getTag().getFloat("DeathZ");
            }
        }

        if (stack.getTag() != null) {
            if (stack.getTag().contains("Difficulty")) {
                difficulty = stack.getTag().getInt("Difficulty");
            }
        }

        X = rand.nextDouble(X-range, X+range);
        Z = rand.nextDouble(Z-range, Z+range);

        Y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int) X, (int) Z);

        int chunkX = (int) (X / 16);
        int chunkZ = (int) (Z / 16);

        level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1, 1.5f);

        if (!level.isClientSide) {

            BlockPos spawnPos = new BlockPos((int)X, (int)Y, (int)Z);

            boolean chunkReady = forceLoadChunkAndWait(level, spawnPos, 100);

            if (!chunkReady) {
                LOGGER.warn("Failed to load chunk at {} after max attempts, trying alternative position", spawnPos);
                BlockPos alternativePos = findAndLoadPosition(level, (int)X, (int)Z, 3);
                if (alternativePos != null) {
                    X = alternativePos.getX();
                    Z = alternativePos.getZ();
                    Y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int) X, (int) Z);
                    spawnPos = new BlockPos((int)X, (int)Y, (int)Z);
                    LOGGER.info("Found and loaded alternative position: {}", spawnPos);
                } else {
                    LOGGER.error("Could not find any position to load, spawning at original position anyway");
                }
            } else {
                Y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int) X, (int) Z);
                spawnPos = new BlockPos((int)X, (int)Y, (int)Z);
                //LOGGER.info("Chunk loaded successfully at {}, height: {}", spawnPos, Y);
            }

            if (!player.isCreative()) {
                stack.shrink(1);
            }

            if (taskType.equals("treasure_map") || (taskType.equals("random_task") && randomValue < 0.67f)) {
                Y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int) X, (int) Z);
                summonTreasure(X, Y, Z, player, level, random);
            } else if (taskType.equals("skeleton_crew") || (taskType.equals("random_task") && randomValue < 1.0f)) {
                BlockPos pos = new BlockPos((int)X, (int)Y, (int)Z);
                int attempts = 0;
                int maxAttempts = 100;

                for (int i = 0; i < maxAttempts; i++) {
                    boolean isLoaded = level.isLoaded(pos);

                    if (!isLoaded) {
                        //LOGGER.debug("Position {} is not loaded, loading it", pos);
                        forceLoadChunkAndWait(level, pos, 100);
                        isLoaded = level.isLoaded(pos);
                    }

                    if (isLoaded) {
                        Y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int) X, (int) Z);
                        pos = new BlockPos(pos.getX(), (int)Y, pos.getZ());
                    }

                    if (!isLoaded) {
                        //LOGGER.debug("Not loaded. replacing position");
                        X = player.getX();
                        Z = player.getZ();
                        X = rand.nextDouble(X-range, X+range);
                        Z = rand.nextDouble(Z-range, Z+range);
                        Y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int) X, (int) Z);
                        pos = new BlockPos((int)X, (int)Y, (int)Z);
                        attempts++;
                        continue;
                    }

                    boolean isInWater = level.getBlockState(pos).getBlock().equals(Blocks.WATER) ||
                            level.getBlockState(pos).getBlock().equals(Blocks.SEAGRASS) ||
                            level.getBlockState(pos).getBlock().equals(Blocks.TALL_SEAGRASS) ||
                            level.getBlockState(pos).getBlock().equals(Blocks.KELP_PLANT);

                    if (!isInWater) {
                        break;
                    }

                    // if in water, finding other position
                    //LOGGER.debug("In-water position detected, replacing it");
                    X = player.getX();
                    Z = player.getZ();
                    X = rand.nextDouble(X-range, X+range);
                    Z = rand.nextDouble(Z-range, Z+range);
                    Y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int) X, (int) Z);
                    pos = new BlockPos((int)X, (int)Y, (int)Z);
                    attempts++;
                }

                if (attempts >= maxAttempts) {
                    LOGGER.warn("Could not find valid position for skeleton crew after {} attempts", maxAttempts);
                } else {
                    //LOGGER.info("Found valid position for skeleton crew at {} after {} attempts", pos, attempts);
                }

                summonSkeletonCrewCamp(X, Y, Z, player, level, random, difficulty);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));

        return super.use(level, player, pUsedHand);
    }

    private boolean forceLoadChunkAndWait(Level level, BlockPos pos, int maxTicks) {
        if (!(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
            return level.isLoaded(pos);
        }

        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;

        // is chunk loaded already
        if (serverLevel.isLoaded(pos)) {
            // are blocks in chunk generated
            BlockPos checkPos = new BlockPos(pos.getX(), Math.max(pos.getY() - 5, level.getMinBuildHeight()), pos.getZ());
            if (!level.isEmptyBlock(checkPos)) {
                //LOGGER.debug("Chunk at [{}, {}] is already fully loaded", chunkX, chunkZ);
                return true;
            }
        }

        //LOGGER.debug("Force loading chunk at [{}, {}]", chunkX, chunkZ);

        try {
            LevelChunk chunk = serverLevel.getChunk(chunkX, chunkZ);

            if (chunk == null) {
                LOGGER.warn("Failed to get chunk at [{}, {}]", chunkX, chunkZ);
                return false;
            }

            // waiting chunk load
            int ticksWaited = 0;
            boolean isLoaded = false;

            while (ticksWaited < maxTicks) {
                // is chunk loaded
                if (serverLevel.isLoaded(pos)) {
                    // are block generated
                    BlockPos checkBlock = new BlockPos(pos.getX(),
                            Math.max(pos.getY() - 5, level.getMinBuildHeight()),
                            pos.getZ());
                    if (!level.isEmptyBlock(checkBlock)) {
                        isLoaded = true;
                        break;
                    }
                }

                try {
                    Thread.sleep(1); // Ждем 1 мс
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                ticksWaited++;

                // every 10 ticks trying to load again
                if (ticksWaited % 10 == 0) {
                    serverLevel.getChunk(chunkX, chunkZ);
                }
            }

            if (isLoaded) {
                //LOGGER.debug("Chunk at {} successfully loaded after {} ticks", pos, ticksWaited);
                return true;
            } else {
                //LOGGER.warn("Chunk at {} not loaded after {} ticks", pos, ticksWaited);
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("Error loading chunk at [{}, {}]: {}", chunkX, chunkZ, e.getMessage());
            return false;
        }
    }

    /**
     * finding and loading position in search radius
     */
    private BlockPos findAndLoadPosition(Level level, int x, int z, int searchRadius) {
        if (!(level instanceof net.minecraft.server.level.ServerLevel)) {
            return null;
        }

        for (int dx = -searchRadius; dx <= searchRadius; dx++) {
            for (int dz = -searchRadius; dz <= searchRadius; dz++) {
                int checkX = x + dx * 16 + 8;
                int checkZ = z + dz * 16 + 8;
                BlockPos checkPos = new BlockPos(checkX, 0, checkZ);

                if (forceLoadChunkAndWait(level, checkPos, 50)) {
                    int y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, checkX, checkZ);
                    if (y > level.getMinBuildHeight()) {
                        BlockPos resultPos = new BlockPos(checkX, y, checkZ);
                        //LOGGER.debug("Found and loaded position at {}", resultPos);
                        return resultPos;
                    }
                }
            }
        }
        return null;
    }

    private void summonSkeletonCrewCamp(double x, double y, double z, Player player, Level level, RandomSource random, int difficulty) {
        if (!level.isClientSide) {
            BlockPos spawnPos = new BlockPos((int)x, (int)y, (int)z);

            // rechecking chunk load
            boolean chunkReady = forceLoadChunkAndWait(level, spawnPos, 100);

            if (!chunkReady) {
                //LOGGER.error("Could not load chunk at {}, spawning skeleton crew camp anyway", spawnPos);
                BlockPos altPos = findAndLoadPosition(level, (int)x, (int)z, 2);
                if (altPos != null) {
                    x = altPos.getX();
                    y = altPos.getY();
                    z = altPos.getZ();
                    spawnPos = altPos;
                    //LOGGER.info("Using alternative position for skeleton crew camp: {}", altPos);
                }
            } else {
                y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int)x, (int)z);
                spawnPos = new BlockPos((int)x, (int)y, (int)z);
                //LOGGER.info("Chunk loaded and ready at {}, spawning skeleton crew camp", spawnPos);
            }

            giveMap(x, y, z, player, level, random, "filled_map.treasures_of_the_dead.skeleton_crew", "skeleton_crew");
            SkeletonCrewCamp skeletonCrewCamp = new SkeletonCrewCamp(ModEntities.SKELETON_CREW_CAMP.get(), level, difficulty, 3);
            skeletonCrewCamp.moveTo(x, y, z);
            level.addFreshEntity(skeletonCrewCamp);

            //LOGGER.info("Skeleton crew camp spawned at [{}, {}, {}] with difficulty {}, chunk loaded: {}", (int)x, (int)y, (int)z, difficulty, chunkReady);
            //LOGGER.info("||||||||||||||||||||||||||||||||||");
        }
    }

    private void summonTreasure(double x, double y, double z, Player player, Level level, RandomSource random) {
        if (!level.isClientSide) {
            BlockPos spawnPos = new BlockPos((int)x, (int)y, (int)z);

            // rechecking chunk load
            boolean chunkReady = forceLoadChunkAndWait(level, spawnPos, 100);

            if (!chunkReady) {
                //LOGGER.warn("Could not load chunk at {}, spawning treasure anyway", spawnPos);
                BlockPos altPos = findAndLoadPosition(level, (int)x, (int)z, 2);
                if (altPos != null) {
                    x = altPos.getX();
                    y = altPos.getY();
                    z = altPos.getZ();
                    spawnPos = altPos;
                    //LOGGER.info("Using alternative position for treasure: {}", altPos);
                }
            } else {
                // Пересчитываем высоту после загрузки чанка
                y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int)x, (int)z);
                spawnPos = new BlockPos((int)x, (int)y, (int)z);
                //LOGGER.info("Chunk loaded and ready at {}, spawning treasure", spawnPos);
            }

            Random rand = new Random();

            if ((double) random.nextFloat() < 0.9f) {
                TreasureChestEntity treasure = new TreasureChestEntity(ModEntities.TREASURE_CHEST.get(), level);
                treasure.moveTo(x, y, z, rand.nextFloat(-180f, 180f), 0f);
                treasure.addTag("TOTD_Rotate");
                treasure.setIsTrap(random.nextBoolean());
                level.addFreshEntity(treasure);
                giveMap(x, y, z, player, level, random, "filled_map.treasures_of_the_dead.buried_treasure", "treasure_map");
                buryTheTreasure(treasure, level, random);
                //LOGGER.info("Treasure chest spawned at [{}, {}, {}]", (int)x, (int)y, (int)z);
            } else if ((double) random.nextFloat() < 1.0f) {
                VillainousSkullEntity treasure = new VillainousSkullEntity(ModEntities.VILLAINOUS_SKULL.get(), level);
                treasure.moveTo(x, y, z, rand.nextFloat(-180f, 180f), 0f);
                treasure.addTag("TOTD_Rotate");
                level.addFreshEntity(treasure);
                giveMap(x, y, z, player, level, random, "filled_map.treasures_of_the_dead.buried_treasure", "treasure_map");
                buryTheTreasure(treasure, level, random);
                //LOGGER.info("Villainous skull spawned at [{}, {}, {}]", (int)x, (int)y, (int)z);
            }
        }
    }

    private void giveMap(double x, double y, double z, Player player, Level level, RandomSource random, String mapName, String decoration) {

        if (!level.isClientSide) {
            ItemStack map = MapItem.create(level, (int) x, (int) z, (byte) random.nextInt(0, 3), true, true);

            if (decoration.equals("treasure_map")) {
                MapItemSavedData.addTargetDecoration(map, BlockPos.containing(x, y, z), "TREASURE", MapDecoration.Type.RED_X);
            } else if (decoration.equals("skeleton_crew")) {
                MapItemSavedData.addTargetDecoration(map, BlockPos.containing(x, y, z), "TREASURE", MapDecoration.Type.BANNER_BLACK);
            }

            map.setHoverName(Component.translatable(mapName));

            MapItem.renderBiomePreviewMap(level.getServer().overworld(), map);

            if (player.getInventory().getFreeSlot() == -1) {
                ItemEntity mapEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), map);
                level.addFreshEntity(mapEntity);
            } else {
                player.addItem(map);
            }
        }
    }

    private void buryTheTreasure(LivingEntity treasure, Level level, RandomSource random) {
        int x = treasure.getBlockX();
        int y = treasure.getBlockY();
        int z = treasure.getBlockZ();

        BlockPos pos1 = new BlockPos(x, y, z);
        BlockPos pos2 = new BlockPos(x, y-1, z);

        boolean isInBush = (level.getBlockState(pos1).getBlock() instanceof BushBlock);
        if (level.getBlockState(pos1).getBlock() instanceof BushBlock && level.getFluidState(pos1).is(FluidTags.WATER)) {
            isInBush = false;
        }
        boolean isInWater = (level.getBlockState(pos1).getBlock() == Blocks.WATER);
        boolean isOnLeaves = (level.getBlockState(pos2).is(BlockTags.LEAVES));
        boolean isOnAir = (level.getBlockState(pos2).getBlock() == Blocks.AIR);
        boolean isInAirOrInSnow = (level.getBlockState(pos1).getBlock() == Blocks.AIR || level.getBlockState(pos1).getBlock() == Blocks.SNOW);

        if (isInWater) {
            treasure.moveTo(x, y, z);
        }
        while (isOnLeaves || isOnAir) {
            treasure.moveTo(x, y-1, z);
            y = treasure.getBlockY();
            pos1 = new BlockPos(x, y, z);
            pos2 = new BlockPos(x, y-1, z);
            isOnLeaves = (level.getBlockState(pos2).is(BlockTags.LEAVES));
            isOnAir = (level.getBlockState(pos2).getBlock() == Blocks.AIR);
        }
        if (isInAirOrInSnow || isInBush) {
            treasure.moveTo(x, y - random.nextInt(1, 4), z);
            y = treasure.getBlockY();
            pos1 = new BlockPos(x, y, z);
            pos2 = new BlockPos(x, y-1, z);
        }
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);
        int difficulty = 0;
        if (stack.getTag() != null) {
            difficulty = stack.getTag().getInt("Difficulty");
        }
        if (this.taskType.equals("skeleton_crew")) {
            tooltipComponents.add(Component.translatable("tooltip.treasures_of_the_dead.quest_difficulty.tooltip", difficulty));
        }
    }
}
