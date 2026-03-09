package net.mrwilfis.treasures_of_the_dead.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

public class AnyTreasureItem extends Item {
    public AnyTreasureItem(Properties properties) {
        super(properties);
    }

    protected float checkSpecialBlocks(UseOnContext context, BlockPos pos) {
        Level level = context.getLevel();
        if (level instanceof ServerLevel) {
            BlockState state = level.getBlockState(pos);
            if (isBottomSlab(state)) {
                return -0.5f;
            }
            if (!context.getPlayer().isShiftKeyDown()) {
                if (isComposterBlock(state)) {
                    return -0.875f;
                }
                if (isCauldronBlock(state)) {
                    return -0.75f;
                }
            }

        }
        return 0.0f;
    }

    protected boolean isBottomSlab(BlockState state) {
            if (state.getBlock() instanceof SlabBlock) {
                SlabType slabType = state.getValue(SlabBlock.TYPE);
                return slabType == SlabType.BOTTOM;
            }
        return false;

    }

    protected boolean isComposterBlock(BlockState state) {
        return state.getBlock() instanceof ComposterBlock;
    }

    protected boolean isCauldronBlock(BlockState state) {
        return state.getBlock() instanceof CauldronBlock;
    }
}
