package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import net.mrwilfis.treasures_of_the_dead.item.custom.DaggerItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class DaggerEntity extends AbstractArrow implements GeoAnimatable {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final EntityDataAccessor<Integer> DATA_HIT_DIRECTION =
            SynchedEntityData.defineId(DaggerEntity.class, EntityDataSerializers.INT);

    private Direction hitDirection = Direction.UP;
    private float rotation;
    private boolean breakOnLanding;

    protected static final RawAnimation FLY = RawAnimation.begin().then("animation.dagger.fly", Animation.LoopType.LOOP);

    public DaggerEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public DaggerEntity(LivingEntity shooter, Level level) {
        super(ModEntities.IRON_DAGGER.get(), shooter, level, new ItemStack(ModItems.IRON_DAGGER.get()), null);
    }

    public DaggerEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity shooter, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(entityType, shooter, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            if (this.firstTick) {
                this.playSound(SoundEvents.WITCH_THROW, 0.4f, 0.8f);
            }
        }

        super.tick();

    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.BAMBOO_PLACE;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        if (!this.level().isClientSide) {
            this.setHitDirection(result.getDirection());
        }
        if (this.breakOnLanding) {
            this.playSound(SoundEvents.ITEM_BREAK);
            this.discard();
            return;
        }
        this.setSoundEvent(SoundEvents.BAMBOO_PLACE);
    }

    public float getBreakChance() {
        return 0.1f;
    }

    public float getDamageMultiplier() {
        return 3.75f;
    }

    public DaggerItem getDaggerItem() {
        return (DaggerItem) ModItems.IRON_DAGGER.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().arrow(this, (Entity)(entity1 == null ? this : entity1));

        float velocity = (float) this.getDeltaMovement().length();
        float damage = velocity * getDamageMultiplier();
        damage = Math.max(damage, 1.0F);

        // ===== ОТЛАДКА =====
//        if (!this.level().isClientSide) {
//            String debugMessage = String.format(
//                    "[Dagger Debug] Скорость: %.3f | Множитель: %.1f | Итоговый урон: %.1f | Цель: %s",
//                    velocity,
//                    getDamageMultiplier(),
//                    damage,
//                    entity.getName().getString()
//            );
//            System.out.println(debugMessage);
//
//            if (entity1 instanceof Player player) {
//                player.sendSystemMessage(
//                        Component.literal("§6[⚔ Dagger] §fСкорость: §e" + String.format("%.2f", velocity) +
//                                " §f| Урон: §c" + String.format("%.1f", damage) +
//                                " §f(§b" + String.format("%.1f", getDamageMultiplier()) + " §f× скорость§f)")
//                );
//            }
//        }
        // ===================

        if (entity.hurt(damagesource, damage)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                this.doKnockback(livingentity, damagesource);
                this.doPostHurtEffects(livingentity);
            }

            if (this.breakOnLanding) {
                this.playSound(SoundEvents.ITEM_BREAK);
                this.discard();
                return;
            }
        }

        float randomValue = random.nextFloat();
        if (randomValue <= getBreakChance()) {
            this.breakOnLanding = true;
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
    }

    @Override
    public void playerTouch(Player player) {
        // Проверяем, что нож в земле, не трясется, и игрок сидит (зажат Shift)
        if (!this.level().isClientSide && this.inGround && this.shakeTime <= 0) {
            // Проверяем, зажат ли Shift (игрок сидит)
            if (player.isShiftKeyDown()) {
                // Игрок зажал Shift - можно подобрать
                if (this.tryPickup(player)) {
                    player.take(this, 1);
                    this.discard();
                }
            }
        }
    }

    @Override
    protected boolean tryPickup(Player player) {
        if (!player.isCreative()) {
            return player.getInventory().add(this.getPickupItem());
        }
        return true; // not pickup
    }

    public float getRenderingRotation() {
        rotation += 15f;
        if (rotation >= 360) {
            rotation = 0;
        }
        return rotation;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.IRON_DAGGER.get());
    }

    @Override
    protected void tickDespawn() {
        if (this.tickCount >= 6000) {
            this.discard();
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.075d;
    }

    public Direction getHitDirection() {
        return Direction.values()[this.entityData.get(DATA_HIT_DIRECTION)];
    }

    private void setHitDirection(Direction direction) {
        this.entityData.set(DATA_HIT_DIRECTION, direction.ordinal());
        this.hitDirection = direction;
    }

    public boolean getInGround() {
        return this.inGround;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_HIT_DIRECTION, Direction.UP.ordinal());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //controllers.add(new AnimationController<>(this, "controller1", 0, this::flying));
    }

    private PlayState flying(AnimationState<DaggerEntity> state) {
        if (this.inGround) {
            return PlayState.STOP;
        }
        state.getController().setAnimation(FLY);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object object) {
        return ((Entity)object).tickCount;
    }
}
