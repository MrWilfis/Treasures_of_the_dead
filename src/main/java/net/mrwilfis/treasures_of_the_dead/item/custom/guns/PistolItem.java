package net.mrwilfis.treasures_of_the_dead.item.custom.guns;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.mrwilfis.treasures_of_the_dead.item.client.PistolRenderer;
import net.mrwilfis.treasures_of_the_dead.item.custom.ModGunItem;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.function.Consumer;

public class PistolItem extends ModGunItem implements GeoItem {

    private static final RawAnimation IDLE = RawAnimation.begin().then("animation.model.idle", Animation.LoopType.LOOP);

    public PistolItem(Properties pProperties, int bulletCount, int reloadDuration) {
        super(pProperties, bulletCount, reloadDuration);
    }

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<PistolItem> state) {
        state.getController().setAnimation(IDLE);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private PistolRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new PistolRenderer();

                return this.renderer;
            }
        });
    }
}
