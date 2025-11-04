package net.mrwilfis.treasures_of_the_dead.item.custom;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
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
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.mrwilfis.treasures_of_the_dead.Config;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.*;
import net.mrwilfis.treasures_of_the_dead.entity.custom.chestVariants.TreasureChestEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.VillainousSkullEntity;
import net.mrwilfis.treasures_of_the_dead.entity.variant.*;
import org.jetbrains.annotations.Nullable;


import java.util.List;
import java.util.Random;

public class RandomSpawningAdventureItem extends Item {
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



        if (!level.isClientSide) {



            level.getChunk(chunkX, chunkZ);

            if (!player.isCreative()) {
                stack.shrink(1);
            }

            if (taskType.equals("treasure_map") || (taskType.equals("random_task") && randomValue < 0.67f)) {
                Y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int) X, (int) Z);
                summonTreasure(X, Y, Z, player, level, random);
            } else if (taskType.equals("skeleton_crew") || (taskType.equals("random_task") && randomValue < 1.0f)) {
                BlockPos pos = new BlockPos((int)X, (int)Y, (int)Z);
                for (int i = 0; i < 100; i++) {
                    boolean isInWater = level.getBlockState(pos).getBlock().equals(Blocks.WATER) || level.getBlockState(pos).getBlock().equals(Blocks.SEAGRASS) ||
                            level.getBlockState(pos).getBlock().equals(Blocks.TALL_SEAGRASS) || level.getBlockState(pos).getBlock().equals(Blocks.KELP_PLANT);
                    if (!isInWater) {
                        break;
                    }
                    //System.out.println(X + " " + Y + " " + Z);
                    X = player.getX();
                    Z = player.getZ();
                    X = rand.nextDouble(X-range, X+range);
                    Z = rand.nextDouble(Z-range, Z+range);
                    Y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int) X, (int) Z);
                    pos = new BlockPos((int)X, (int)Y, (int)Z);
                }

                //summonSkeletonCrew(X, Y, Z, player, level, random);

                summonSkeletonCrewCamp(X, Y, Z, player, level, random, difficulty);
            }


        }

        player.awardStat(Stats.ITEM_USED.get(this));

        return super.use(level, player, pUsedHand);
    }

    private void summonSkeletonCrewCamp(double x, double y, double z, Player player, Level level, RandomSource random, int difficulty) {
        if (!level.isClientSide) {
            giveMap(x, y, z, player, level, random, "filled_map.treasures_of_the_dead.skeleton_crew", "skeleton_crew");
            SkeletonCrewCamp skeletonCrewCamp = new SkeletonCrewCamp(ModEntities.SKELETON_CREW_CAMP.get(), level, difficulty, 3);
            skeletonCrewCamp.moveTo(x, y, z);
            level.addFreshEntity(skeletonCrewCamp);
        }
    }

    private void summonSkeletonCrew(double x, double y, double z, Player player, Level level, RandomSource random) {
        if (!level.isClientSide) {

            Random rand = new Random();

            giveMap(x, y, z, player, level, random, "filled_map.treasures_of_the_dead.skeleton_crew", "skeleton_crew");

            byte captainsAmount = (byte)rand.nextInt(1, 3+1);

            double randomValue = (double) random.nextFloat();

            if (randomValue < 0.4) {
                for (int i = 0; i < captainsAmount; i++) {

                    CaptainSkeletonEntity captain = new CaptainSkeletonEntity(ModEntities.CAPTAIN_SKELETON.get(), level);
                    newSkeleton(x, y, z, level, random, captain, rand);

                    captain.setCanDropKeysAndOrders(false);
                    captain.setCustomName(Component.literal(captain.getRandomName(random)));

                    CaptainSkeletonVariant variant = Util.getRandom(CaptainSkeletonVariant.values(), random);
                    captain.setVariant(variant);

                    x = x + random.nextInt(-2, 2+1);
                    z = z + random.nextInt(-2, 2+1);
                    y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int) x, (int) z);
                }
                for (int i = 0; i < (captainsAmount / 2) + 3; i++) {
                    TOTDSkeletonEntity pirate = new TOTDSkeletonEntity(ModEntities.TOTD_SKELETON.get(), level);
                    newSkeleton(x, y, z, level, random, pirate, rand);
                    TOTDSkeletonVariant variant = Util.getRandom(TOTDSkeletonVariant.values(), random);
                    pirate.setVariant(variant);

                    x = x + random.nextInt(-2, 2+1);
                    z = z + random.nextInt(-2, 2+1);
                    y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int) x, (int) z);
                }
            } else if (randomValue < 0.7) {
                for (int i = 0; i < captainsAmount; i++) {

                    CaptainShadowSkeletonEntity captain = new CaptainShadowSkeletonEntity(ModEntities.CAPTAIN_SHADOW_SKELETON.get(), level);
                    newSkeleton(x, y, z, level, random, captain, rand);

                    captain.setCanDropKeysAndOrders(false);
                    captain.setCustomName(Component.literal(captain.getRandomName(random)));

                    CaptainShadowSkeletonVariant variant = Util.getRandom(CaptainShadowSkeletonVariant.values(), random);
                    captain.setVariant(variant);

                    x = x + random.nextInt(-2, 2+1);
                    z = z + random.nextInt(-2, 2+1);
                    y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int) x, (int) z);
                }
                for (int i = 0; i < (captainsAmount / 2) + 3; i++) {
                    ShadowSkeletonEntity pirate = new ShadowSkeletonEntity(ModEntities.SHADOW_SKELETON.get(), level);
                    newSkeleton(x, y, z, level, random, pirate, rand);
                    ShadowSkeletonVariant variant = Util.getRandom(ShadowSkeletonVariant.values(), random);
                    pirate.setVariant(variant);

                    x = x + random.nextInt(-2, 2+1);
                    z = z + random.nextInt(-2, 2+1);
                    y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int) x, (int) z);
                }

            } else {
                for (int i = 0; i < captainsAmount; i++) {

                    CaptainBloomingSkeletonEntity captain = new CaptainBloomingSkeletonEntity(ModEntities.CAPTAIN_BLOOMING_SKELETON.get(), level);
                    newSkeleton(x, y, z, level, random, captain, rand);

                    captain.setCanDropKeysAndOrders(false);
                    captain.setCustomName(Component.literal(captain.getRandomName(random)));

                    CaptainBloomingSkeletonVariant variant = Util.getRandom(CaptainBloomingSkeletonVariant.values(), random);
                    captain.setVariant(variant);

                    x = x + random.nextInt(-2, 2+1);
                    z = z + random.nextInt(-2, 2+1);
                    y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int) x, (int) z);
                }
                for (int i = 0; i < (captainsAmount / 2) + 3; i++) {
                    BloomingSkeletonEntity pirate = new BloomingSkeletonEntity(ModEntities.BLOOMING_SKELETON.get(), level);
                    newSkeleton(x, y, z, level, random, pirate, rand);
                    BloomingSkeletonVariant variant = Util.getRandom(BloomingSkeletonVariant.values(), random);
                    pirate.setVariant(variant);

                    x = x + random.nextInt(-2, 2+1);
                    z = z + random.nextInt(-2, 2+1);
                    y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int) x, (int) z);
                }
            }
        }
    }

    private static void newSkeleton(double x, double y, double z, Level level, RandomSource random, TOTDSkeletonEntity skeleton, Random rand) {
        skeleton.moveTo(x, y, z, rand.nextFloat(-180f, 180f), 0f);
        skeleton.addTag("TOTD_Rotate");
        skeleton.setPersistenceRequired();
        level.addFreshEntity(skeleton);
        skeleton.populateDefaultEquipmentSlots(random);
    //    skeleton.setIsSpawning(true);
    }

    private void summonTreasure(double x, double y, double z, Player player, Level level, RandomSource random) {
        if (!level.isClientSide) {

            Random rand = new Random();

            if ((double) random.nextFloat() < 0.9f) {
                TreasureChestEntity treasure = new TreasureChestEntity(ModEntities.TREASURE_CHEST.get(), level);
                treasure.moveTo(x, y, z, rand.nextFloat(-180f, 180f), 0f);
                treasure.addTag("TOTD_Rotate");
                level.addFreshEntity(treasure);
                giveMap(x, y, z, player, level, random, "filled_map.treasures_of_the_dead.buried_treasure", "treasure_map");
                buryTheTreasure(treasure, level, random);
            } else if ((double) random.nextFloat() < 1.0f) {
                VillainousSkullEntity treasure = new VillainousSkullEntity(ModEntities.VILLAINOUS_SKULL.get(), level);
                treasure.moveTo(x, y, z, rand.nextFloat(-180f, 180f), 0f);
                treasure.addTag("TOTD_Rotate");
                level.addFreshEntity(treasure);
                giveMap(x, y, z, player, level, random, "filled_map.treasures_of_the_dead.buried_treasure", "treasure_map");
                buryTheTreasure(treasure, level, random);
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
        if (isInAirOrInSnow) {
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
            //tooltipComponents.add(Component.literal("Сложность задания: " + difficulty));
        }
    }
}
