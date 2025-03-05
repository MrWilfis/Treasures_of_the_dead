package net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mrwilfis.treasures_of_the_dead.entity.custom.AbstractSkullEntity;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class FoulSkullEntity extends AbstractSkullEntity implements GeoAnimatable, GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));

    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> state) {
        state.getController().setAnimation(RawAnimation.begin().then("animation.model.default0", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public double getTick(Object o) {
        return (double)((Entity)o).tickCount;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public FoulSkullEntity(EntityType pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public ItemStack getTreasureItem() {
        return new ItemStack(ModItems.FOUL_SKULL_ITEM.get());

    }

}
