package net.mrwilfis.treasures_of_the_dead.entity.custom.powderKegsVariants;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mrwilfis.treasures_of_the_dead.entity.custom.AbstractPowderKegEntity;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class PowderKegEntity extends AbstractPowderKegEntity implements GeoAnimatable, GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    protected static final RawAnimation IDLE = RawAnimation.begin().then("animation.model.idle", Animation.LoopType.LOOP);
    protected static final RawAnimation FUSE = RawAnimation.begin().then("animation.model.fuse", Animation.LoopType.LOOP);

    public PowderKegEntity(EntityType pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));

    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> state) {
        if (this.getIsGoingToBlowUp()) {
            state.getController().setAnimation(FUSE);
        } else {
            state.getController().setAnimation(IDLE);
        }
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

    public ItemStack getTreasureItem() {
        return new ItemStack(ModItems.POWDER_KEG_ITEM.get());

    }
}
