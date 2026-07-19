package net.mrwilfis.treasures_of_the_dead.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
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

public class WeatheringCopperSmallBrazierBlock extends CopperSmallBrazierBlock implements WeatheringCopper {

    public WeatheringCopperSmallBrazierBlock(boolean isWaxed, WeatherState weatherState, WeatheringChain chain, int fireDamage, int lightLevel, Properties properties) {
        super(isWaxed, weatherState, chain, fireDamage, lightLevel, properties);
    }

    @Override
    public WeatherState getAge() {
        return getWeatherState();
    }

    public Optional<BlockState> getNext(BlockState state) {
        return getWeatheringChain().getNext(getWeatherState())
                .map(block -> block.defaultBlockState()
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                        .setValue(LIT, state.getValue(LIT)) // Ваши свойства
                );
    }

    public Optional<BlockState> getPrevious(BlockState state) {
        return getWeatheringChain().getPrevious(getWeatherState())
                .map(block -> block.defaultBlockState()
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                        .setValue(LIT, state.getValue(LIT))
                );
    }

    public Optional<BlockState> getWaxed(BlockState state) {
        return getWeatheringChain().getWaxed(getWeatherState())
                .map(block -> block.defaultBlockState()
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                        .setValue(LIT, state.getValue(LIT))
                );
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        // Handle honeycomb waxing
        if (stack.is(Items.HONEYCOMB)) {
            Optional<BlockState> waxed = getWaxed(state);
            if (waxed.isPresent()) {
                BlockState newState = waxed.get()
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                        .setValue(LIT, state.getValue(LIT));

                level.playSound(player, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.levelEvent(player, 3003, pos, 0); // WAX_ON particles

                if (!level.isClientSide) {
                    level.setBlock(pos, newState, 11);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                }

                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        // Handle axe scraping to reduce oxidation
        if (stack.is(ItemTags.AXES) && !getIsWaxed()) {
            Optional<BlockState> previous = getPrevious(state);
            if (previous.isPresent()) {
                BlockState newState = previous.get()
                        .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                        .setValue(LIT, state.getValue(LIT));

                level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.levelEvent(player, 3005, pos, 0); // SCRAPE particles

                if (!level.isClientSide) {
                    level.setBlock(pos, newState, 11);
                    if (!player.isCreative()) {
                        stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
                    }
                }

                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return this.getNext(state).isPresent();
    }
}