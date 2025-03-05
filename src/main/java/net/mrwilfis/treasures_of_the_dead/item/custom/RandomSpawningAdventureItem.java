package net.mrwilfis.treasures_of_the_dead.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.chestVariants.TreasureChestEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.VillainousSkullEntity;


import java.util.Random;

public class RandomSpawningAdventureItem extends Item {
    private final int range;
    private final String taskType; // TYPES: treasure_map, random_task, and I will be adding new types

    public RandomSpawningAdventureItem(Properties pProperties, int range, String taskType) {
        super(pProperties);
        this.range = range;
        this.taskType = taskType;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {

        RandomSource random = level.getRandom();
        Random rand = new Random();

        ItemStack stack = player.getItemInHand(pUsedHand);

        double X = player.getX();
        double Y = player.getY();
        double Z = player.getZ();
        if (stack.getTag() != null) {
            X = stack.getTag().getFloat("DeathX");
            Z = stack.getTag().getFloat("DeathZ");
        }

        X = rand.nextDouble(X-range, X+range);
        Z = rand.nextDouble(Z-range, Z+range);

        int chunkX = (int) (X / 16);
        int chunkZ = (int) (Z / 16);

        if (!level.isClientSide) {



            level.getChunk(chunkX, chunkZ);

            Y = level.getHeight(Heightmap.Types.OCEAN_FLOOR, (int) X, (int) Z);

            giveTreasureMap(X, Y, Z, player, level, random, "filled_map.treasures_of_the_dead.buried_treasure");

            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }

        return super.use(level, player, pUsedHand);
    }


    private void summonTreasure(double x, double y, double z, Player player, Level level, RandomSource random) {
        if (!level.isClientSide) {



            Random rand = new Random();

            if ((double) random.nextFloat() < 0.9f) {
                TreasureChestEntity treasure = new TreasureChestEntity(ModEntities.TREASURE_CHEST.get(), level);
                treasure.moveTo(x, y, z, rand.nextFloat(-180f, 180f), 0f);
                treasure.addTag("TOTD_Rotate");
                level.addFreshEntity(treasure);
                buryTheTreasure(treasure, level, random);
            } else if ((double) random.nextFloat() < 1.0f) {
                VillainousSkullEntity treasure = new VillainousSkullEntity(ModEntities.VILLAINOUS_SKULL.get(), level);
                treasure.moveTo(x, y, z, rand.nextFloat(-180f, 180f), 0f);
                treasure.addTag("TOTD_Rotate");
                level.addFreshEntity(treasure);
                buryTheTreasure(treasure, level, random);
            }


        }
    }

    private void giveTreasureMap(double x, double y, double z, Player player, Level level, RandomSource random, String mapName) {

        if (!level.isClientSide) {
            ItemStack map = MapItem.create(level, (int) x, (int) z, (byte) random.nextInt(0, 3), true, true);

            MapItemSavedData.addTargetDecoration(map, BlockPos.containing(x, y, z), "TREASURE", MapDecoration.Type.RED_X);

            map.setHoverName(Component.translatable(mapName));

            MapItem.renderBiomePreviewMap(level.getServer().overworld(), map);

            summonTreasure(x, y, z, player, level, random);

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
}
