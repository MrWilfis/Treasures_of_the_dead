package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.mrwilfis.treasures_of_the_dead.entity.ai.goal.GeckoAnimateAttackGoal;
import net.mrwilfis.treasures_of_the_dead.entity.variant.GhostVariant;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

public class GhostEntity extends Monster implements GeoEntity, CrossbowAttackMob {

    private final int maxSpawningTime = 40;
    private int spawningTime = 0;

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(GhostEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> IS_SPAWNING = SynchedEntityData.defineId(GhostEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_CHARGING = SynchedEntityData.defineId(GhostEntity.class, EntityDataSerializers.BOOLEAN);

    public GhostEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason,
                                        @Nullable SpawnGroupData pSpawnData) {

//        boolean b = pReason.equals(MobSpawnType.SPAWN_EGG) || pReason.equals(MobSpawnType.SPAWNER) || pReason.equals(MobSpawnType.DISPENSER)
//                || pReason.equals(MobSpawnType.MOB_SUMMONED);
//        if (!b) {
//            this.setIsSpawning(true);
//            this.playAmbientSound();
//        }
//
        this.specialProcedures();
//        this.populateDefaultEquipmentEnchantments(pLevel, this.random, pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    public void specialProcedures() {
        GhostVariant variant = Util.getRandom(GhostVariant.values(), this.random);
        setVariant(variant);
        //this.populateDefaultEquipmentSlots(this.random);
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.95f)
                .add(Attributes.MOVEMENT_SPEED, 0.24f).build();
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide) {
            //spawn animation logic
            if (this.getIsSpawning()) {
                this.setNoAi(true);
                this.spawningTime++;
            }
            if (this.spawningTime >= maxSpawningTime) {
                this.setNoAi(false);
                this.setIsSpawning(false);
                this.spawningTime = 0;
            }
        }
        super.tick();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RangedCrossbowAttackGoal<>(this, 1.0, 8.0F));
        //this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.25D, false));
        this.goalSelector.addGoal(2, new GeckoAnimateAttackGoal(this, 1.25D, 7, 17, "attack"));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[]{GhostEntity.class})));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, TOTDSkeletonEntity.class, true));

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public boolean isHoldingCrossbow() {
        return this.getMainHandItem().getItem() instanceof CrossbowItem;
    }

    @Override
    public double getTick(Object o) {
        //       return RenderUtils.getCurrentTick(); // ваще гавно, 20 фпс
        return (double)((Entity)o).tickCount;
    }

    public void setIsSpawning(boolean b) {
        this.entityData.set(IS_SPAWNING, b);
    }

    public boolean getIsSpawning() {
        return this.entityData.get(IS_SPAWNING);
    }

    @Override
    public void setChargingCrossbow(boolean b) {
        this.entityData.set(IS_CHARGING, b);
    }

    public boolean getChargingCrossbow() {
        return this.entityData.get(IS_CHARGING);
    }

    public GhostVariant getVariant() {
        return GhostVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    public void setVariant(GhostVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
        this.setIsSpawning(tag.getBoolean("IsSpawning"));
        this.setChargingCrossbow(tag.getBoolean("IsCharging"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsSpawning", this.getIsSpawning());
        tag.putBoolean("IsCharging", this.getChargingCrossbow());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
        builder.define(IS_SPAWNING, false);
        builder.define(IS_CHARGING, false);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {
        this.performCrossbowAttack(this, 1.6F);
    }
}
