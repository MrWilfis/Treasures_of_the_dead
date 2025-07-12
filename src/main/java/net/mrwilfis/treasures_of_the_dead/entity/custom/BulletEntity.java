package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.*;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.Optional;
import java.util.function.Predicate;

public class BulletEntity extends AbstractHurtingProjectile implements GeoAnimatable {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final EntityDataAccessor<Float> INITIAL_SPEED = SynchedEntityData.defineId(BulletEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> DROP_REDUCTION = SynchedEntityData.defineId(BulletEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Byte> PELLET_COUNT = SynchedEntityData.defineId(BulletEntity.class, EntityDataSerializers.BYTE);


    private static final float BASE_DAMAGE = 4.0F;
    private static final float DAMAGE_MULTIPLIER = 0.5F;
    private static final int MAX_LIFETIME = 100;
    private static final double WATER_FRICTION = 0.6;
    private static final double AIR_FRICTION = 0.99;
    private static final double GRAVITY = 0.05;
    private static final float MAX_DISTANCE = 250.0F;
    private static final float WATER_SLOWDOWN = 0.8F;
    private static final float MIN_SPEED_THRESHOLD = 0.1F;

    private float distanceTraveled = 0.0f;
    private Vec3 initialPosition;
    private boolean inWater = false;
    private boolean touchedWater;
    private float damage;
    private float distanceTravelled;

    public BulletEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

//    public BulletEntity(EntityType<? extends AbstractHurtingProjectile> type, LivingEntity shooter, double x, double y, double z, Level level) {
//        super(type, x, y, z, level);
//        this.initialPosition = shooter.position();
//    }
    public float calculateEnergyFraction() {
        double maxEnergy = Math.pow(entityData.get(INITIAL_SPEED), 2);
        double energy = getDeltaMovement().lengthSqr();
        if (maxEnergy < energy) maxEnergy = energy; // empty entityData
        return (float)(energy / maxEnergy);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount > MAX_LIFETIME) {
            this.discard();
            System.out.println("DISCARD 1");
        }

        Level level = this.level();

        Vec3 velocity = getDeltaMovement();
        Vec3 from = position();
        Vec3 to = from.add(velocity);

        Vec3 waterPos = Vec3.ZERO;
        wasTouchingWater = updateFluidHeightAndDoFluidPushing(FluidTags.WATER, 0);
        if (wasTouchingWater) {
            waterPos = from;
            velocity = velocity.scale(WATER_FRICTION);
            to = from.add(velocity);
            setDeltaMovement(velocity);
        }

        HitResult hitResult = level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

        if (hitResult.getType() != HitResult.Type.MISS) {
            to = hitResult.getLocation();
        }

        EntityHitResult entityHitResult = findHitEntity(from, to);
        if (entityHitResult != null) {
            hitResult = entityHitResult;
            to = hitResult.getLocation();
        }

        if (!wasTouchingWater) {
            BlockHitResult fluidHitResult = level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, this));
            if (fluidHitResult.getType() == HitResult.Type.BLOCK) {
                FluidState fluid = level.getFluidState(fluidHitResult.getBlockPos());
                double distanceToFluid = fluidHitResult.getLocation().subtract(from).length();
                double distanceToHit = to.subtract(from).length();

                if (fluid.is(FluidTags.WATER)) {
                    wasTouchingWater = true;
                    waterPos = fluidHitResult.getLocation();
                    double speed = velocity.length();
                    double timeInWater = 1 - distanceToFluid / speed;
                    double newSpeed = speed * (1 - timeInWater + timeInWater * Math.pow(WATER_FRICTION, timeInWater));

                    if (hitResult.getType() != HitResult.Type.MISS) {
                        if (distanceToFluid < distanceToHit) {
                            if (distanceToHit < newSpeed) {
                                timeInWater = (distanceToHit - distanceToFluid) / speed;
                                newSpeed = speed * (1 - timeInWater + timeInWater * Math.pow(WATER_FRICTION, timeInWater));
                            } else {
                                hitResult = BlockHitResult.miss(null, null, null);
                            }
                        } else {
                            fluidHitResult = BlockHitResult.miss(null, null, null);
                        }
                    }
                    velocity = velocity.scale(newSpeed / speed);
                    to = from.add(velocity);
                    setDeltaMovement(velocity);

                    if (level.isClientSide && fluidHitResult.getType() != HitResult.Type.MISS) {
                        double yv = fluidHitResult.getDirection() == Direction.UP ? 0.02 : 0;
                    //    createHitParticles(ParticleTypes.SPLASH, waterPos, new Vec3(0.0, yv, 0.0));
                    //    playHitSound(Sounds.BULLET_WATER_HIT, waterPos);
                    }
                } else if (((FluidState) fluid).is(FluidTags.LAVA)) {
                    if (hitResult.getType() == HitResult.Type.MISS || distanceToFluid < distanceToHit) {
                        hitResult = fluidHitResult;
                        to = fluidHitResult.getLocation();
                    }
                }
            }
            if (wasTouchingWater) {
                touchedWater = true;
                extinguishFire();
            }
            if (!level.isClientSide) setSharedFlagOnFire(getRemainingFireTicks() > 0);

            if (hitResult.getType() != HitResult.Type.MISS) {
                if (touchedWater) {
                    damage *= calculateEnergyFraction();
                }
                if (!level.isClientSide) {
                    onHit(hitResult);
                    if (hitResult.getType() == HitResult.Type.BLOCK) {
                        tickCount = MAX_LIFETIME;
                    }

            } else if (level.isClientSide && !wasTouchingWater) {
                double length = velocity.length();
                Vec3 dir = velocity.scale(1.0 / length);
                float volume = calculateEnergyFraction();

                AABB aabbSelection = getBoundingBox().expandTowards(velocity).inflate(8.0);
                Predicate<Entity> predicate = entity -> (entity instanceof Player) && !entity.equals(getOwner());
                for (Entity entity : level().getEntities(this, aabbSelection, predicate)) {
                    Vec3 pos = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
                    Vec3 diff = pos.subtract(from);
                    double proj = dir.dot(diff);
                    if (proj > 0 && proj < length) {
                        Vec3 projPos = from.add(dir.scale(proj));
//                        level().playLocalSound(
//                                projPos.x, projPos.y, projPos.z,
//                                Sounds.BULLET_FLY_BY, getSoundSource(),
//                                volume, 0.92f + 0.16f * random.nextFloat(), false
//                        );
                    }
                }
            }

            if (level.isClientSide && wasTouchingWater) {
                double length = velocity.length();
                Vec3 step = velocity.scale(1 / length);
                Vec3 pos = waterPos.add(step.scale(0.5));
                float prob = 1.5f * calculateEnergyFraction();
                while (length > 0.5) {
                    pos = pos.add(step);
                    length -= 1;
                    if (random.nextFloat() < prob) {
                        level.addParticle(ParticleTypes.BUBBLE, pos.x, pos.y, pos.z, 0, 0, 0);
                    }
                }
            }

            if (!wasTouchingWater) velocity = velocity.scale(AIR_FRICTION);
            double gravity = GRAVITY * (1 - entityData.get(DROP_REDUCTION));
            setDeltaMovement(velocity.subtract(0, gravity, 0));
            setPos(to);
            distanceTravelled += to.subtract(from).length();
            checkInsideBlocks();
            }
        }

//        if (this.initialPosition == null) {
//            this.initialPosition = this.position();
//        }
//
//        if (this.initialPosition != null) {
//            this.distanceTraveled = (float) this.position().distanceTo(this.initialPosition);
//            if (this.distanceTraveled > MAX_DISTANCE) {
//                this.discard();
//                System.out.println("DISCARD 2");
//
//            }
//        }
//
//        boolean wasInWater = this.inWater;
//        this.inWater = this.isInWaterOrBubble();
//
//        if (this.inWater && !wasInWater) {
//            this.setDeltaMovement(this.getDeltaMovement().scale(WATER_SLOWDOWN));
//        }
//
//        Vec3 deltaMovement = this.getDeltaMovement();
//        float speed = (float) deltaMovement.length();
//
//        if (speed < MIN_SPEED_THRESHOLD) {
//            this.setDeltaMovement(deltaMovement.x, deltaMovement.y - 0.05F, deltaMovement.z);
//
//            if (this.onGround() || speed < 0.01F) {
//               // this.discard();
//               // System.out.println("DISCARD 3");
//
//            }
//        }


    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
    //    super.onHit(result);

        if (!this.level().isClientSide) {
            if (result.getType() == HitResult.Type.ENTITY) {
                float speed = (float) this.getDeltaMovement().length();
                float damage = this.damage + (speed * DAMAGE_MULTIPLIER);
                Entity entity = result.getEntity();
                Entity shooter = this.getOwner();
                entity.hurt(entity.damageSources().mobProjectile(this, (LivingEntity) shooter), damage);
                System.out.println("DAMAGE: " + damage);
                this.discard();
                System.out.println("DISCARD by hit");

            }
        }
    }

    public EntityHitResult findHitEntity(Vec3 start, Vec3 end) {
        Entity resultEntity = null;
        Vec3 resultPos = null;
        double resultDist = 0;

        AABB aabbSelection = getBoundingBox().expandTowards(getDeltaMovement()).inflate(0.5);
        for (Entity entity : level().getEntities(this, aabbSelection, this::canHitEntity)) {
            AABB aabb = entity.getBoundingBox();
            Optional<Vec3> clipResult = aabb.clip(start, end);

            if (!clipResult.isPresent()) {
                aabb = aabb.move( // previous tick position
                        entity.xOld - entity.getX(),
                        entity.yOld - entity.getY(),
                        entity.zOld - entity.getZ()
                );
                clipResult = aabb.clip(start, end);
            }
            if (clipResult.isPresent()) {
                double dist = start.distanceToSqr(clipResult.get());
                if (dist < resultDist || resultEntity == null) {
                    resultEntity = entity;
                    resultPos = clipResult.get();
                    resultDist = dist;
                }
            }
        }

        return resultEntity != null ? new EntityHitResult(resultEntity, resultPos) : null;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.discard();
        System.out.println("DISCARD 4 (on block hit)");

    }

    public void setVelocity(float bulletSpeed, Vec3 direction) {
        float tickSpeed = bulletSpeed / 20;

        setDeltaMovement(direction.scale(tickSpeed));
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(INITIAL_SPEED, 0.0f);
        builder.define(DROP_REDUCTION, 0.0f);
        builder.define(PELLET_COUNT, (byte)1);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        damage = compound.getFloat("damage");
        distanceTravelled = compound.getFloat("distanceTravelled");
        entityData.set(DROP_REDUCTION, compound.getFloat("dropReduction"));
        entityData.set(PELLET_COUNT, compound.getByte("pelletCount"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("damage", damage);
        compound.putFloat("distanceTravelled", distanceTravelled);
        compound.putFloat("dropReduction", entityData.get(DROP_REDUCTION));
        compound.putByte("pelletCount", entityData.get(PELLET_COUNT));
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
        return (double)((Entity)object).tickCount;
    }
}
