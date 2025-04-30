package net.mrwilfis.treasures_of_the_dead.item.custom.powderKegVariants;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.powderKegsVariants.PowderKegEntity;
import net.mrwilfis.treasures_of_the_dead.item.client.PowderKegItemRenderer;
import net.mrwilfis.treasures_of_the_dead.item.custom.AbstractPowderKegItem;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.function.Consumer;

public class PowderKegItem extends AbstractPowderKegItem implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    protected static final RawAnimation IDLE = RawAnimation.begin().then("animation.model.idle", Animation.LoopType.LOOP);
    protected static final RawAnimation FUSE = RawAnimation.begin().then("animation.model.fuse", Animation.LoopType.LOOP);

    public PowderKegItem(Properties pProperties) {
        super(pProperties);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        ItemStack stack = pContext.getPlayer().getItemInHand(pContext.getHand());

        PowderKegEntity keg = new PowderKegEntity(ModEntities.POWDER_KEG.get(), pContext.getLevel());     /* Which Skull will be placed */
        keg.addTag("TOTD_Rotate");
        if (stack.getTag() != null) {
            keg.setIsGoingToBlowUp(stack.getTag().getBoolean("IsGoingToBlowUp"));
            keg.setPrepareToBlowUp(getPrepareToBlowUp(stack));
        }
        BlockPos offset = pContext.getClickedPos().relative(pContext.getClickedFace(), 1);
        keg.moveTo(offset.getX() + 0.5, offset.getY(), offset.getZ() + 0.5, 0f,0f);
        float yaw = pContext.getPlayer().getYRot();
        if (pContext.getClickedFace() != Direction.UP) {
            yaw = pContext.getPlayer().getDirection().toYRot();

        }
        keg.setYRot(yaw - 180.0f);
        if (stack.hasCustomHoverName()) {
            keg.setCustomName(stack.getHoverName());
        }
        if (!pContext.getLevel().isClientSide) {
            pContext.getLevel().addFreshEntity(keg);
        }
        if (!pContext.getPlayer().isCreative()) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;

    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private PowderKegItemRenderer renderer;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if(this.renderer == null)
                    this.renderer = new PowderKegItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {


        if (false) {
            tAnimationState.getController().setAnimation(FUSE);
        }else{
            tAnimationState.getController().setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
