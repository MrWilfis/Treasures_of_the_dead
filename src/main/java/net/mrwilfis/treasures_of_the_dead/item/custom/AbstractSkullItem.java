package net.mrwilfis.treasures_of_the_dead.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;


public class AbstractSkullItem extends Item implements Equipable {

    public AbstractSkullItem(Properties pProperties) {
        super(pProperties);

    }

    protected boolean isTopSlab(Level level, BlockPos pos) {
        if (level instanceof ServerLevel) {
            BlockState state = level.getBlockState(pos);

            if (state.getBlock() instanceof SlabBlock) {
                SlabType slabType = state.getValue(SlabBlock.TYPE);
                return slabType == SlabType.TOP || slabType == SlabType.DOUBLE;
            }
        }
        return true;

    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    // Yes, this is empty. Увы.

}
