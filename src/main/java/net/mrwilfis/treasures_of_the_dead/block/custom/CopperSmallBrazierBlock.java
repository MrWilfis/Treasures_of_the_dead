package net.mrwilfis.treasures_of_the_dead.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.mrwilfis.treasures_of_the_dead.block.WeatheringChain;

import java.util.Optional;

public class CopperSmallBrazierBlock extends SmallBrazierBlock{
    private final WeatheringCopper.WeatherState weatherState;
    private final WeatheringChain chain; // Храним ссылку на цепочку
    private final boolean isWaxed;

    public CopperSmallBrazierBlock(boolean isWaxed, WeatheringCopper.WeatherState weatherState, WeatheringChain chain, int fireDamage, int lightLevel, Properties properties) {
        super(fireDamage, lightLevel, properties);
        this.isWaxed = isWaxed;
        this.weatherState = weatherState;
        this.chain = chain;
    }

    public WeatheringCopper.WeatherState getWeatherState() {
        return this.weatherState;
    }

    public WeatheringChain getWeatheringChain() {
        return this.chain;
    }

    public Optional<BlockState> getUnwaxed(BlockState state) {
        return getWeatheringChain().getUnwaxed(getWeatherState())
                .map(block -> block.defaultBlockState()
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                        .setValue(LIT, state.getValue(LIT))
                );
    }

    public boolean getIsWaxed() {
        return this.isWaxed;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(ItemTags.AXES) && getIsWaxed()) {
            Optional<BlockState> waxed = getUnwaxed(state);
            if (waxed.isPresent()) {
                BlockState newState = waxed.get()
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                        .setValue(LIT, state.getValue(LIT));

                level.playSound(player, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.levelEvent(player, 3004, pos, 0); // WAX_OFF particles

                if (!level.isClientSide) {
                    level.setBlock(pos, newState, 11);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }

                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
