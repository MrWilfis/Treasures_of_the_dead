package net.mrwilfis.treasures_of_the_dead.item.custom.chestVariants;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.chestVariants.TreasureChestEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.FoulSkullEntity;
import net.mrwilfis.treasures_of_the_dead.item.client.FoulSkullItemRenderer;
import net.mrwilfis.treasures_of_the_dead.item.client.TreasureChestItemRenderer;
import net.mrwilfis.treasures_of_the_dead.item.custom.AbstractChestItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.AbstractSkullItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.DataTicket;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.function.Consumer;

public class TreasureChestItem extends AbstractChestItem implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().then("animation.model.idle", Animation.LoopType.LOOP);
    private static final RawAnimation IDLE_OPENED = RawAnimation.begin().then("animation.model.idle_opened", Animation.LoopType.LOOP);
    private static final RawAnimation OPENING = RawAnimation.begin().then("animation.model.opening", Animation.LoopType.HOLD_ON_LAST_FRAME);

    public TreasureChestItem(Properties pProperties) {
        super(pProperties);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);

    }



    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        ItemStack stack = pContext.getPlayer().getItemInHand(pContext.getHand());

        TreasureChestEntity chest = new TreasureChestEntity(ModEntities.TREASURE_CHEST.get(), pContext.getLevel());     /* Which Skull will be placed */
        chest.addTag("TOTD_Rotate");
        if (stack.getTag() != null) {
            chest.setIsOpen(stack.getTag().getBoolean("IsOpen"));
            chest.setIsRobbed(stack.getTag().getBoolean("IsRobbed"));
        }
        BlockPos offset = pContext.getClickedPos().relative(pContext.getClickedFace(), 1);
        chest.moveTo(offset.getX() + 0.5, offset.getY(), offset.getZ() + 0.5, 0f,0f);
        float yaw = pContext.getPlayer().getYRot();
        if (pContext.getClickedFace() != Direction.UP) {
            yaw = pContext.getPlayer().getDirection().toYRot();

        }
        chest.setYRot(yaw - 180.0f);
        if (stack.hasCustomHoverName()) {
            chest.setCustomName(stack.getHoverName());
        }
        if (!pContext.getLevel().isClientSide) {
            pContext.getLevel().addFreshEntity(chest);
        }
        if (!pContext.getPlayer().isCreative()) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;

    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.getTag() != null) {
            if (pStack.getTag().getBoolean("IsRobbed")) {
                pTooltipComponents.add(Component.translatable("tooltip.treasures_of_the_dead.chest_is_robbed.tooltip"));
            }
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private TreasureChestItemRenderer renderer;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if(this.renderer == null)
                    this.renderer = new TreasureChestItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        state.getController().setAnimation(IDLE);
        return PlayState.CONTINUE;

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
