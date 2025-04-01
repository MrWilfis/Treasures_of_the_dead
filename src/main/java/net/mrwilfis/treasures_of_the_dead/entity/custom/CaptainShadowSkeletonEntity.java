package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.mrwilfis.treasures_of_the_dead.entity.variant.CaptainShadowSkeletonVariant;
import net.mrwilfis.treasures_of_the_dead.entity.variant.ShadowSkeletonVariant;
import net.mrwilfis.treasures_of_the_dead.misc.TOTDBlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

public class CaptainShadowSkeletonEntity extends CaptainSkeletonEntity {

    private static final EntityDataAccessor<Float> DEATH_X = SynchedEntityData.defineId(CaptainShadowSkeletonEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DEATH_Z = SynchedEntityData.defineId(CaptainShadowSkeletonEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> CAN_DROP_KEYS_AND_ORDERS = SynchedEntityData.defineId(CaptainShadowSkeletonEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> IS_SHADOW = SynchedEntityData.defineId(CaptainShadowSkeletonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SHADOW_TIMER = SynchedEntityData.defineId(CaptainShadowSkeletonEntity.class, EntityDataSerializers.INT);
    private boolean isShaking = false;
    private int shakingTimer = 0;
    private final int shadowTimer = 600;

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(CaptainShadowSkeletonEntity.class, EntityDataSerializers.INT);

    public CaptainShadowSkeletonEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason,
                                        @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = pLevel.getRandom();

        CaptainShadowSkeletonVariant variant = Util.getRandom(CaptainShadowSkeletonVariant.values(), this.random);
        setVariant(variant);

        this.populateDefaultEquipmentSlots(randomsource);
        this.populateDefaultEquipmentEnchantments(randomsource, pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.65f)
                .add(Attributes.ARMOR, 4.0f)
                //    .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.2f)
                .add(Attributes.MOVEMENT_SPEED, 0.24f).build();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller1", 3, this::idleAndWalk2));
        controllerRegistrar.add(new AnimationController<>(this, "controller2", 3, this::walkAndAttack2));
    }

    private <E extends GeoAnimatable> PlayState walkAndAttack2(AnimationState<E> state) {

        if (getShadowTimer() >= shadowTimer) {
            //    System.out.println("SHAKING ANIMATION");
            state.getController().setAnimation(SHAKING_FROM_LIGHT);
            state.getController().setAnimationSpeed(1.0D);
            return PlayState.CONTINUE;
        }
        else if (this.swinging) {
            state.getController().stop();
            state.getController().setAnimation(ATTACK1);
            state.getController().setAnimationSpeed(1.0D);
            return PlayState.CONTINUE;
        }
        else if (state.isMoving() && this.isAggressive()) {
            state.getController().setAnimation(WALK_HANDS1);
            state.getController().setAnimationSpeed(1.25D);
            return PlayState.CONTINUE;
        }
        else if (state.isMoving() && !this.isAggressive()) {
            state.getController().setAnimation(WALK_HANDS1);
            state.getController().setAnimationSpeed(1.0D);
            return PlayState.CONTINUE;
        }
        else if (!state.isMoving() && !this.swinging) {
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    private <E extends GeoAnimatable> PlayState idleAndWalk2(AnimationState<E> state) {

        if (getShadowTimer() >= shadowTimer) {
            //    System.out.println("SHAKING ANIMATION");
            state.getController().setAnimation(SHAKING_FROM_LIGHT);
            state.getController().setAnimationSpeed(1.0D);
            return PlayState.CONTINUE;
        }
        else if (state.isMoving() && this.isAggressive()) {
            state.getController().setAnimation(WALK_BODY1);
            state.getController().setAnimationSpeed(1.25D);
            return PlayState.CONTINUE;
        } else if (state.isMoving() && !this.isAggressive()) {
            state.getController().setAnimation(WALK_BODY1);
            state.getController().setAnimationSpeed(1.0D);
            if ((double) random.nextFloat() < 0.5f) {
                this.idleVariation = 1;
            } else if ((double) random.nextFloat() < 1.0f) {
                this.idleVariation = 2;
            }
            return PlayState.CONTINUE;
        } else if (!state.isMoving()) {
            if (this.idleVariation == 1) {
                state.getController().setAnimation(IDLE1);
            } else if (this.idleVariation == 2) {
                state.getController().setAnimation(IDLE2);
            }
            state.getController().setAnimationSpeed(1.0D);
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {


            BlockPos Pos = this.getLightPosition();
            int i;
            int j = this.level().getBrightness(LightLayer.BLOCK, Pos);
            if (this.level().isNight()) {
                i = 0;
            } else {
                i = this.level().getBrightness(LightLayer.SKY, Pos);
            }
            int brightness;
            brightness = Math.max(i, j);


            if (this.isShaking) {
                setShadow(false);
                setInvulnerable(false);
                setNoAi(true);
                this.shakingTimer--;
                if (this.shakingTimer <= 0) {

                    setShadowTimer(getShadowTimer() - 1);

                    this.isShaking = false;
                    setNoAi(false);
                }
            }


            if (brightness >= 7)
            {
                if (getShadow() && !this.isShaking) {
                    this.isShaking = true;
                    this.shakingTimer = 60;
                    this.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 0.5f, 0.75f);
                    this.playSound(SoundEvents.ALLAY_DEATH, 1.0f, 0.0f);
                    setShadowTimer(shadowTimer);
                }
            }
            else
            {
                if (!getShadow() && getShadowTimer() > 0 && !this.isShaking) {
                    setShadowTimer(getShadowTimer()-1);
                }
                if (getShadowTimer() <= 0) {
                    setShadow(true);
                    setInvulnerable(true);
                }
            }
        }
    }

    @Override
    public void populateDefaultEquipmentSlots(RandomSource pRandom) {
        if ((double) this.random.nextFloat() < 0.8) {
            this.maybeWearEquipment(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD), pRandom, 0.9F);
        } else {
            this.maybeWearEquipment(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD), pRandom, 0.9F);
        }
    }

    public BlockPos getLightPosition() {
        BlockPos pos = TOTDBlockPos.fromVec3(this.position());
        if (!level().getBlockState(pos).canOcclude()) {
            return pos.above();
        }
        return pos;
    }

    public int getShadowTimer() {
        return this.getEntityData().get(SHADOW_TIMER).intValue();
    }

    public void setShadowTimer(int i) {
        this.getEntityData().set(SHADOW_TIMER, i);
    }

    public boolean getShadow() {
        return this.getEntityData().get(IS_SHADOW).booleanValue();
    }

    public void setShadow(boolean b) {
        this.getEntityData().set(IS_SHADOW, b);
    }

    public CaptainShadowSkeletonVariant getCaptainShadowVariant() {
        return CaptainShadowSkeletonVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    public void setVariant(CaptainShadowSkeletonVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
        this.setShadowTimer(tag.getInt("ShadowTimer"));
        this.setShadow(tag.getBoolean("IsShadow"));

        this.setDeathX(tag.getFloat("DeathX"));
        this.setDeathZ(tag.getFloat("DeathZ"));
        this.setCanDropKeysAndOrders(tag.getBoolean("CanDropKeysAndOrders"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putInt("ShadowTimer", this.getShadowTimer());
        tag.putBoolean("IsShadow", this.getShadow());

        tag.putFloat("DeathX", this.getDeathX());
        tag.putFloat("DeathZ", this.getDeathZ());
        tag.putBoolean("CanDropKeysAndOrders", this.getCanDropKeysAndOrders());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(SHADOW_TIMER, 0);
        this.getEntityData().define(IS_SHADOW, true);

        this.getEntityData().define(DEATH_X, 0.0f);
        this.getEntityData().define(DEATH_Z, 0.0f);
        this.getEntityData().define(CAN_DROP_KEYS_AND_ORDERS, true);
    }
}
