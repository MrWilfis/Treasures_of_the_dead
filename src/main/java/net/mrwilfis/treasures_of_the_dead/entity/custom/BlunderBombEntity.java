package net.mrwilfis.treasures_of_the_dead.entity.custom;

import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.List;

public class BlunderBombEntity extends ThrowableProjectile implements GeoAnimatable {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private final float MAX_DAMAGE = 12.0f;
    private final float MIN_DISTANCE = 5.0f;
    private final float ON_HIT_ENTITY_DAMAGE = 6.0f;

    public BlunderBombEntity(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public BlunderBombEntity(EntityType<? extends ThrowableProjectile> entityType, LivingEntity shooter, Level level) {
        super(entityType, shooter, level);
    }

    public BlunderBombEntity(Level level, double x, double y, double z) {
        super(ModEntities.BLUNDER_BOMB.get(), x, y, z, level);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide) {
            this.explodeBomb();
            this.level().broadcastEntityEvent(this, (byte)42);
            this.level().playSound(this, this.getOnPos(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.AMBIENT, 2.0f, 1.0f);
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity entity = result.getEntity();
            entity.hurt(entity.damageSources().explosion(entity, this.getOwner()), ON_HIT_ENTITY_DAMAGE);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);
        createParticles(this.level(), this.random, this.position());
    }

    private void explodeBomb() {
        AABB aabb = this.getBoundingBox().inflate(MIN_DISTANCE, MIN_DISTANCE / 2, MIN_DISTANCE);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, aabb);
        if (!list.isEmpty()) {
            for (LivingEntity entity : list) {
                float distance = this.distanceTo(entity);
                float damage = calculateDamage(distance);

                entity.hurt(entity.damageSources().explosion(entity, this.getOwner()), damage);

                if (!entity.isSpectator()) {
                    Vec3 explosionCenter = this.position();
                    Vec3 entityPos = entity.position();

                    Vec3 direction = entityPos.subtract(explosionCenter).normalize();

                    double knockbackStrength = (1.0 - (distance / MIN_DISTANCE)) * 0.75 + 0.2;

                    entity.knockback(knockbackStrength, -direction.x, -direction.z);

                    if (entity instanceof ServerPlayer) {
                        ((ServerPlayer)entity).hurtMarked = true;
                    }
                }
            }
        }
    }

    private void createParticles(Level level, RandomSource random, Vec3 position) {
        for (int i = 0; i < 25; i++) {

            double xOffset = random.nextDouble() * 0.5 - 0.25;
            double yOffset = random.nextDouble() * 0.5 - 0.25;
            double zOffset = random.nextDouble() * 0.5 - 0.25;

            double xSpeed = (random.nextDouble() * 0.5) - 0.25;
            double ySpeed = (random.nextDouble() * 0.5);
            double zSpeed = (random.nextDouble() * 0.5) - 0.25;

                level.addParticle(ParticleTypes.EXPLOSION, // Тип частиц
                        position.x + xOffset, // Позиция X
                        position.y + yOffset, // Позиция Y
                        position.z + zOffset, // Позиция Z
                        xSpeed, ySpeed, zSpeed); // Скорость (0, 0, 0)
        }
        //System.out.println("PARTICLES CREATED");
    }

    private float calculateDamage(float distance) {
        float damage = MAX_DAMAGE * (1 - distance / MIN_DISTANCE);
        return Math.max(damage, 0.0f);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.055d;
    }

    @Override
    public DoubleDoubleImmutablePair calculateHorizontalHurtKnockbackDirection(LivingEntity entity, DamageSource damageSource) {
        double d0 = entity.position().x - this.position().x;
        double d1 = entity.position().z - this.position().z;
        return DoubleDoubleImmutablePair.of(d0, d1);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

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
