package net.mrwilfis.treasures_of_the_dead.world.level.levelgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.TOTDSkeletonEntity;

public class PiratesSpawner implements CustomSpawner {
    private int nextTick;

    public PiratesSpawner() {
    }

    @Override
    public int tick(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies) {
        if (!spawnEnemies) {
            System.out.println("NE SRABOTALO");
            return 0;
        } else {
            System.out.println("SRABOTALO");
            RandomSource random = level.random;
            --this.nextTick;
            if (this.nextTick > 0) {
                return 0;
            } else {
                this.nextTick += 120 + random.nextInt(1);
                long days = level.getDayTime() / 24000L;
                if (days >= 5L && level.isDay()) {
                    if (random.nextInt(5) == 0) {
                        return 0;
                    } else {
                        int players = level.players().size();
                        if (players < 1) {
                            return 0;
                        } else {
                            Player player = (Player) level.players().get(random.nextInt(players));
                        //    player.sendSystemMessage(Component.literal("выбрался игрок, а именно ты"));
                            if (player.isSpectator()) {
                                return 0;
                            } else if (level.isCloseToVillage(player.blockPosition(), 2)) {
                                return 0;
                            } else {
                                int howFarX = (12 + random.nextInt(12)) * (random.nextBoolean() ? -1 : 1);
                                int howFarZ = (12 + random.nextInt(12)) * (random.nextBoolean() ? -1 : 1);
                                BlockPos.MutableBlockPos spawningPos = player.blockPosition().mutable().move(howFarX, 0, howFarZ);
                                int $$10 = 0;
                                if (!level.hasChunksAt(spawningPos.getX() - 10, spawningPos.getZ() - 10, spawningPos.getX() + 10, spawningPos.getZ() + 10)) {
                                    return 0;
                                } else {
                                    Holder<Biome> biome = level.getBiome(spawningPos);
                                    if (!biome.is(BiomeTags.IS_BEACH)) {
                                        return 0;
                                    } else {
                                        int $$12 = 0;
                                        int difficulty = (int) Math.ceil((double) level.getCurrentDifficultyAt(spawningPos).getEffectiveDifficulty()) + 1;

                                        for (int i = 0; i < difficulty; ++i) {
                                            ++$$12;
                                            spawningPos.setY(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, spawningPos).getY());
                                            if (i == 0) {
                                                if (this.spawnCrewMember(level, spawningPos, random, true)) {
                                                    break;
                                                }
                                            } else {
                                                this.spawnCrewMember(level, spawningPos, random, false);
                                            }

                                            spawningPos.setX(spawningPos.getX() + random.nextInt(-5, 5+1));
                                            spawningPos.setZ(spawningPos.getZ() + random.nextInt(-5, 5+1));

                                        }

                                        return $$12;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    return 0;
                }
            }
        }
    }

    private boolean spawnCrewMember(ServerLevel level, BlockPos pos, RandomSource random, boolean leader) {
        BlockState blockState = level.getBlockState(pos);
        if (!NaturalSpawner.isValidEmptySpawnBlock(level, pos, blockState, blockState.getFluidState(), ModEntities.TOTD_SKELETON.get())) {
            return false;
        } else {
            TOTDSkeletonEntity pirate = (TOTDSkeletonEntity) ModEntities.TOTD_SKELETON.get().create(level);
            if (pirate != null) {
                if (leader) {

                }

                pirate.setPos((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
            //    pirate.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.EVENT, (SpawnGroupData) null, (CompoundTag) null);
                level.addFreshEntity(pirate);
                return true;
            } else {
                return false;
            }
        }
    }
}
