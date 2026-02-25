package net.mrwilfis.treasures_of_the_dead.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.mrwilfis.treasures_of_the_dead.block.entity.ModBlockEntities;
import net.mrwilfis.treasures_of_the_dead.block.entity.SeaFortressCoreBlockEntity;
import org.jetbrains.annotations.Nullable;

public class SeaFortressCoreBlock extends BaseEntityBlock{
    public static final MapCodec<SeaFortressCoreBlock> CODEC = simpleCodec(SeaFortressCoreBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<CoreState> STATE = EnumProperty.create("state",CoreState.class);

    public SeaFortressCoreBlock(Properties properties) {
        super(properties);
    }

    public enum CoreState implements StringRepresentable {
        WAITING("waiting"),
        ACTIVE("active"),
        COOLDOWN("cooldown"),
        REWARDING("rewarding");

        private final String name;

        CoreState(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(STATE, CoreState.WAITING);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
        builder.add(new Property[]{STATE});
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

        @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SeaFortressCoreBlockEntity(blockPos, blockState);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        //super.useItemOn(stack,state,level,pos,player,hand,hitResult);
        if (!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof SeaFortressCoreBlockEntity seaFortressCoreBlockEntity) {
                ItemStack item = player.getItemInHand(hand);

                return seaFortressCoreBlockEntity.onKeyUsed(item, player);
            }
        }
        return ItemInteractionResult.FAIL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return null;
        }

        return createTickerHelper(blockEntityType, ModBlockEntities.SEA_FORTRESS_CORE_BE.get(),
                (level2, blockPos, blockState, blockEntity) -> blockEntity.tick(level2, blockPos, blockState, blockEntity));
    }

    public static void updateBlockState(SeaFortressCoreBlockEntity.State entityState, Level level, BlockPos pos) {
        if (level != null && !level.isClientSide) {
            BlockState blockState = level.getBlockState(pos);
            CoreState newCoreState = convertEntityStateToBlockState(entityState);

            if (blockState.hasProperty(STATE) && blockState.getValue(STATE) != newCoreState) {
                level.setBlock(pos, blockState.setValue(STATE, newCoreState), 3);
            }
        }
    }

    private static CoreState convertEntityStateToBlockState(SeaFortressCoreBlockEntity.State entityState) {
        switch (entityState) {
            case WAITING: return CoreState.WAITING;
            case ACTIVE: return CoreState.ACTIVE;
            case COOLDOWN: return CoreState.COOLDOWN;
            case REWARDING: return CoreState.REWARDING;
            default: return CoreState.WAITING;
        }
    }
}
