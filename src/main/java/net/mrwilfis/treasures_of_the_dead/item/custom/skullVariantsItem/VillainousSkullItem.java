package net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.VillainousSkullEntity;
import net.mrwilfis.treasures_of_the_dead.item.client.VillainousSkullItemRenderer;
import net.mrwilfis.treasures_of_the_dead.item.custom.AbstractSkullItem;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.function.Consumer;

public class VillainousSkullItem extends AbstractSkullItem implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public VillainousSkullItem(Properties pProperties) {
        super(pProperties);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);

    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        ItemStack stack = pContext.getPlayer().getItemInHand(pContext.getHand());

        VillainousSkullEntity skull = new VillainousSkullEntity(ModEntities.VILLAINOUS_SKULL.get(), pContext.getLevel());     /* Which Skull will be placed */
        skull.addTag("TOTD_Rotate");
        BlockPos offset = pContext.getClickedPos().relative(pContext.getClickedFace(), 1);
        skull.moveTo(offset.getX() + 0.5, offset.getY(), offset.getZ() + 0.5, 0f,0f);
        float yaw = pContext.getPlayer().getYRot();
        if (pContext.getClickedFace() != Direction.UP) {
            yaw = pContext.getPlayer().getDirection().toYRot();

        }
        skull.setYRot(yaw - 180.0f);
        skull.yHeadRot = yaw - 180.0f;
        if (stack.hasCustomHoverName()) {
            skull.setCustomName(stack.getHoverName());
        }
        if (!pContext.getLevel().isClientSide) {
            pContext.getLevel().addFreshEntity(skull);
        }
        if (!pContext.getPlayer().isCreative()) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;

    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private VillainousSkullItemRenderer renderer;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if(this.renderer == null)
                    this.renderer = new VillainousSkullItemRenderer();

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
