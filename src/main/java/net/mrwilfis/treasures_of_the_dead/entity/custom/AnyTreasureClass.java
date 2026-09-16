package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnyTreasureClass extends Animal {

    private static final EntityDataAccessor<Integer> HIT_COUNT = SynchedEntityData.defineId(AnyTreasureClass.class, EntityDataSerializers.INT);
    protected static final int MAX_HITS = 5;
    private int shakeTimer = 0;

    public AnyTreasureClass(EntityType pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noCulling = true;
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource pSource) {
        return super.isInvulnerableTo(pSource) || pSource.is(DamageTypes.IN_WALL) || pSource.is(DamageTypes.FALLING_BLOCK) || pSource.is(DamageTypes.CACTUS)
                || pSource.is(DamageTypes.DRAGON_BREATH) || pSource.is(DamageTypes.FALLING_ANVIL) || pSource.is(DamageTypes.FALLING_STALACTITE)
                || pSource.is(DamageTypes.FIREWORKS) || pSource.is(DamageTypes.ON_FIRE) || pSource.is(DamageTypes.HOT_FLOOR) || pSource.is(DamageTypes.FREEZE)
                || pSource.is(DamageTypes.INDIRECT_MAGIC) || pSource.is(DamageTypes.LIGHTNING_BOLT) || pSource.is(DamageTypes.LAVA) || pSource.is(DamageTypes.DROWN);
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
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
    public void tick() {
        if (this.shakeTimer > 0) {
            this.shakeTimer--;
        }

        int hits = this.getHitCount();
        if (hits > 0 && hits < MAX_HITS - 1 && this.tickCount % 20 == 0) {
            this.setHitCount(hits-1);
        }

        super.tick();
    }

    public int getHitCount() {
        return this.getEntityData().get(HIT_COUNT);
    }

    public void setHitCount(int hits) {
        this.getEntityData().set(HIT_COUNT, Math.max(0, Math.min(hits, MAX_HITS)));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setHitCount(tag.getInt("HitCount"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("HitCount", getHitCount());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HIT_COUNT, 0);
    }


    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return null;
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        boolean instantDestroy = pSource.is(DamageTypes.ARROW) || pSource.is(DamageTypes.FALLING_STALACTITE) ||
                pSource.is(DamageTypes.TRIDENT) || pSource.is(DamageTypes.MOB_PROJECTILE) || pSource.is(DamageTypes.SONIC_BOOM) ||
                pSource.is(DamageTypes.PLAYER_EXPLOSION) || pSource.is(DamageTypes.EXPLOSION);
        if (instantDestroy) {
            this.turnIntoItem();
            return true;
        }
        if (!isInvulnerableTo(pSource)) {

            this.playSound(SoundEvents.WOOD_HIT, 0.8F, 0.8F + this.random.nextFloat() * 0.4F);

            if (pSource.isCreativePlayer()) {
                this.turnIntoItem();
                return true;
            }

            int hits = this.getHitCount() + 1;
            this.setHitCount(hits);
            int var1 = random.nextBoolean() ? 1 : -1;
            this.yHeadRot = (this.getYRot() + (1.5f * (this.getHitCount() * 0.75f) * var1));
            this.setYRot(this.getYRot() + (1.5f * (this.getHitCount() * 0.75f) * var1));

            if (hits >= MAX_HITS) {
                this.turnIntoItem();
                return true;
            }

            return true;
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
