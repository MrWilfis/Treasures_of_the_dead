package net.mrwilfis.treasures_of_the_dead.entity.custom;

import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.particle.ModParticles;
import net.mrwilfis.treasures_of_the_dead.sound.ModSounds;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

import java.util.List;
import java.util.Random;

public class BlunderBombEntity extends ThrowableProjectile implements GeoAnimatable {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    protected static final RawAnimation FLY = RawAnimation.begin().then("animation.blunder_bomb.fly", Animation.LoopType.LOOP);

    private final float MAX_DAMAGE = 15.0f;
    private final float MIN_DISTANCE = 5.0f;
    private final float ON_HIT_ENTITY_DAMAGE = 4.0f;

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
    public void tick() {
        if (!this.level().isClientSide) {
            if (this.firstTick) {
                this.playSound(SoundEvents.WITCH_THROW, 0.4f, 0.8f);
            }
        }

        super.tick();

    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        Random rand = new Random();

        if (!this.level().isClientSide) {
            this.explodeBomb();
            this.level().broadcastEntityEvent(this, (byte)42);
            this.level().playSound(this, this.getOnPos(), ModSounds.BLUNDER_BOMB_EXPLODE.get(), SoundSource.AMBIENT, 2.0f, rand.nextFloat(0.8f, 1.2f));
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity entity = result.getEntity();
            entity.hurt(entity.damageSources().explosion(this, this.getOwner()), ON_HIT_ENTITY_DAMAGE);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        Entity entity = this.getOwner();

        Level level = this.level();
        BlockPos blockPos = result.getBlockPos();
        BlockState state = level.getBlockState(blockPos);
        Block block = state.getBlock();
        boolean isTNT = block instanceof TntBlock;
        if (isTNT) {
            ((TntBlock)block).onCaughtFire(state, level, blockPos, (Direction) null, entity instanceof LivingEntity ? (LivingEntity)entity : null);
            level.removeBlock(blockPos, false);
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

                //entity.hurt(entity.damageSources().explosion(this, this.getOwner()), damage);

                if (!entity.isSpectator() && entity.hurt(entity.damageSources().explosion(this, this.getOwner()), damage)) {
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
        for (int i = 0; i < 100; i++) {

            double xDir = (random.nextDouble() * 1) - 0.5;
            double yDir = (random.nextDouble() * 1) - 0.5;
            double zDir = (random.nextDouble() * 1) - 0.5;

            double length = Math.sqrt(xDir * xDir + yDir * yDir + zDir * zDir);
            xDir /= length;
            yDir /= length;
            zDir /= length;

            double speed = 2.0;
            double xSpeed = xDir * speed;
            double ySpeed = yDir * speed;
            double zSpeed = zDir * speed;


                level.addParticle(ModParticles.BLUNDER_BOMB_EXPLOSION_PARTICLES.get(), // Тип частиц
                        position.x, // Позиция X
                        position.y, // Позиция Y
                        position.z, // Позиция Z
                        xSpeed, ySpeed, zSpeed); // Скорость (0, 0, 0)
        }

        for (int i = 0; i < 4; i++) {

            double xOffset = random.nextDouble() * 0.5 - 0.25;
            double yOffset = random.nextDouble() * 0.5 - 0.25;
            double zOffset = random.nextDouble() * 0.5 - 0.25;

            double xSpeed = (random.nextDouble() * 0.8) - 0.4;
            double ySpeed = (random.nextDouble() * 0.8) - 0.4;
            double zSpeed = (random.nextDouble() * 0.8) - 0.4;

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
        controllers.add(new AnimationController<>(this, "controller1", 3, this::flying));
    }

    private PlayState flying(AnimationState<BlunderBombEntity> state) {
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
