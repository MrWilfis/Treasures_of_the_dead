package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.phys.Vec3;
import net.mrwilfis.treasures_of_the_dead.entity.variant.BloomingSkeletonVariant;
import net.mrwilfis.treasures_of_the_dead.entity.variant.ShadowSkeletonVariant;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import net.mrwilfis.treasures_of_the_dead.item.custom.AbstractPowderKegItem;
import net.mrwilfis.treasures_of_the_dead.misc.TOTDBlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

public class ShadowSkeletonEntity extends TOTDSkeletonEntity{
    private static final EntityDataAccessor<Boolean> IS_SHADOW = SynchedEntityData.defineId(ShadowSkeletonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SHADOW_TIMER = SynchedEntityData.defineId(ShadowSkeletonEntity.class, EntityDataSerializers.INT);
    private boolean isShaking = false;
    private int shakingTimer = 0;
    private final int shadowTimer = 600;

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(ShadowSkeletonEntity.class, EntityDataSerializers.INT);

    public ShadowSkeletonEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason,
                                        @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public void specialProcedures() {
        ShadowSkeletonVariant variant = Util.getRandom(ShadowSkeletonVariant.values(), this.random);
        setVariant(variant);
        this.populateDefaultEquipmentSlots(this.random);
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 26.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5f)
                .add(Attributes.ARMOR, 2.0f)
                //    .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.2f)
                .add(Attributes.MOVEMENT_SPEED, 0.24f).build();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller1", 3, this::idleAndWalk2));
        controllerRegistrar.add(new AnimationController<>(this, "controller2", 3, this::walkAndAttack2));
        controllerRegistrar.add(new AnimationController<>(this, "controller3", 0, this::spawning));
    }

    private <E extends GeoAnimatable> PlayState spawning(AnimationState<E> state) {
        if (this.getIsSpawning()) {
            state.getController().setAnimation(SPAWN2);
        }
        return PlayState.CONTINUE;
    }

    private <E extends GeoAnimatable> PlayState walkAndAttack2(AnimationState<E> state) {
        ItemStack mainHandItem = this.getItemInHand(InteractionHand.MAIN_HAND);

        if (this.getIsSpawning()) {
            state.getController().stop();
            //    return PlayState.STOP;
        }
        if (getShadowTimer() >= shadowTimer) {
            if (mainHandItem.getItem() instanceof AbstractPowderKegItem) {
                state.getController().setAnimation(SHAKING_FROM_LIGHT_WITH_KEG);
                state.getController().setAnimationSpeed(1.0D);
                return PlayState.CONTINUE;
            } else {
                state.getController().setAnimation(SHAKING_FROM_LIGHT);
                state.getController().setAnimationSpeed(1.0D);
                return PlayState.CONTINUE;
            }
        }
        else if (this.swinging) {
            state.getController().stop();
            state.getController().setAnimation(ATTACK1);
            state.getController().setAnimationSpeed(1.0D);
            return PlayState.CONTINUE;
        }
        else if (!mainHandItem.isEmpty() && (mainHandItem.getItem() instanceof AbstractPowderKegItem)) {
            state.getController().stop();
            //    state.getController().setAnimationSpeed(1.0D);
            return  PlayState.CONTINUE;
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
        ItemStack mainHandItem = this.getItemInHand(InteractionHand.MAIN_HAND);

        if (this.getIsSpawning()) {
            state.getController().stop();
            //    return PlayState.STOP;
        }
        if (getShadowTimer() >= shadowTimer) {
            if (mainHandItem.getItem() instanceof AbstractPowderKegItem) {
                state.getController().setAnimation(SHAKING_FROM_LIGHT_WITH_KEG);
                state.getController().setAnimationSpeed(1.0D);
                return PlayState.CONTINUE;
            } else {
                state.getController().setAnimation(SHAKING_FROM_LIGHT);
                state.getController().setAnimationSpeed(1.0D);
                return PlayState.CONTINUE;
            }
        }
        if (state.isMoving() && this.isAggressive() && !mainHandItem.isEmpty() && (mainHandItem.getItem() instanceof AbstractPowderKegItem)) {
            state.getController().setAnimation(WALK_KEG);
            state.getController().setAnimationSpeed(1.6875D);
            return  PlayState.CONTINUE;
        } else if (state.isMoving() && !this.isAggressive() && !mainHandItem.isEmpty() && (mainHandItem.getItem() instanceof AbstractPowderKegItem)) {
            state.getController().setAnimation(WALK_KEG);
            state.getController().setAnimationSpeed(1.35D);
            return  PlayState.CONTINUE;
        } else if (!state.isMoving() && !mainHandItem.isEmpty() && (mainHandItem.getItem() instanceof AbstractPowderKegItem)) {
            state.getController().setAnimation(IDLE_KEG);
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
//        Vec3 position = this.position();
//
//        if (!this.getShadow()) {
//            for (int i = 0; i < 10; i++) {
//                double xOffset = this.random.nextDouble() * 1 - 0.5;
//                double yOffset = this.random.nextDouble() * 2 - 1;
//                double zOffset = this.random.nextDouble() * 1 - 0.5;
//
//                double xSpeed = (this.random.nextDouble() * 0.02) - 0.01;
//                double ySpeed = (this.random.nextDouble() * 0.02) - 0.01;
//                double zSpeed = (this.random.nextDouble() * 0.02) - 0.01;
//
//                this.level().addParticle(ParticleTypes.SMOKE,
//                        position.x + xOffset,
//                        position.y + yOffset + 1,
//                        position.z + zOffset,
//                        xSpeed, ySpeed, zSpeed);
//            }
//        }

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
        double randomValue = (double) this.random.nextFloat();

        if (randomValue < 0.2) {
            this.maybeWearEquipment(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD), pRandom, 0.5F);
        } else if (randomValue < 0.95){
            this.maybeWearEquipment(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD), pRandom, 0.5F);
        } else {
            this.maybeWearEquipment(EquipmentSlot.MAINHAND, new ItemStack(ModItems.POWDER_KEG_ITEM.get()), pRandom, 1.0F);
        }

        ItemStack mainHandItem = this.getItemInHand(InteractionHand.MAIN_HAND);
        if (mainHandItem.getItem() == ModItems.POWDER_KEG_ITEM.get()) {
            setLeftHanded(false);
            setDropChance(EquipmentSlot.MAINHAND, 1.0f);
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

    public ShadowSkeletonVariant getShadowVariant() {
        return ShadowSkeletonVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    public void setVariant(ShadowSkeletonVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
        this.setShadowTimer(tag.getInt("ShadowTimer"));
        this.setShadow(tag.getBoolean("IsShadow"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putInt("ShadowTimer", this.getShadowTimer());
        tag.putBoolean("IsShadow", this.getShadow());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(SHADOW_TIMER, 0);
        this.getEntityData().define(IS_SHADOW, true);
    }
}
