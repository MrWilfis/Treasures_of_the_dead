package net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.HatefulSkullEntity;
import net.mrwilfis.treasures_of_the_dead.item.client.HatefulSkullItemRenderer;
import net.mrwilfis.treasures_of_the_dead.item.custom.AbstractSkullItem;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.function.Consumer;

public class HatefulSkullItem extends AbstractSkullItem implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public HatefulSkullItem(Properties pProperties) {
        super(pProperties);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);

    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        ItemStack stack = pContext.getPlayer().getItemInHand(pContext.getHand());

        HatefulSkullEntity skull = new HatefulSkullEntity(ModEntities.HATEFUL_SKULL.get(), pContext.getLevel());     /* Which Skull will be placed */
        skull.addTag("TOTD_Rotate");
        BlockPos offset = pContext.getClickedPos().relative(pContext.getClickedFace(), 1);
        skull.moveTo(offset.getX() + 0.5, offset.getY(), offset.getZ() + 0.5, 0f,0f);
        float yaw = pContext.getPlayer().getYRot();
        if (pContext.getClickedFace() != Direction.UP) {
            yaw = pContext.getPlayer().getDirection().toYRot();

        }
        skull.setYRot(yaw - 180.0f);
//        if (stack.hasCustomHoverName()) {
//            skull.setCustomName(stack.getHoverName());
//        }
        if (!pContext.getLevel().isClientSide) {
            pContext.getLevel().addFreshEntity(skull);
        }
        if (!pContext.getPlayer().isCreative()) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;

    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private HatefulSkullItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new HatefulSkullItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.model.default0", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
