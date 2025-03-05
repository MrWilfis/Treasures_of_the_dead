package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnyTreasureClass extends Animal {
    public AnyTreasureClass(EntityType pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noCulling = true;
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource pSource) {
        return super.isInvulnerableTo(pSource) || pSource.is(DamageTypes.IN_WALL) || pSource.is(DamageTypes.FALLING_BLOCK) || pSource.is(DamageTypes.CACTUS)
                || pSource.is(DamageTypes.DRAGON_BREATH) || pSource.is(DamageTypes.FALLING_ANVIL) || pSource.is(DamageTypes.FALLING_STALACTITE)
                || pSource.is(DamageTypes.FIREWORKS) || pSource.is(DamageTypes.ON_FIRE) || pSource.is(DamageTypes.HOT_FLOOR) || pSource.is(DamageTypes.FREEZE)
                || pSource.is(DamageTypes.INDIRECT_MAGIC) || pSource.is(DamageTypes.LIGHTNING_BOLT) || pSource.is(DamageTypes.LAVA);
    }

    @Override
    public boolean isNoAi() {
        return true;
    }

    public void onUpdate() {
        this.yBodyRotO = 0.0f;
        this.yHeadRotO = 0.0f;
        this.yBodyRot = 0.0f;
        this.yHeadRot = 0.0f;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }


    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return null;
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        if (!isInvulnerableTo(pSource)) {
            this.turnIntoItem();
        }
        return super.hurt(pSource, pAmount);
    }

    public void turnIntoItem() {
        if (isRemoved())
            return;
        this.remove(RemovalReason.KILLED);
        if (!this.level().isClientSide) {
            this.spawnAtLocation(getTreasureItem(), 0.0f);
        }
    }

    public ItemStack getTreasureItem() {

        return new ItemStack(ModItems.RUBY.get());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return null;
    }


    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(@NotNull Entity entity) {

    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }
}
