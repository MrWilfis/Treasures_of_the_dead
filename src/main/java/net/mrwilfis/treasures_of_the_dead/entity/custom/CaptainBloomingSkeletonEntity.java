package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.mrwilfis.treasures_of_the_dead.entity.variant.BloomingSkeletonVariant;
import net.mrwilfis.treasures_of_the_dead.entity.variant.CaptainBloomingSkeletonVariant;
import net.mrwilfis.treasures_of_the_dead.entity.variant.CaptainSkeletonVariant;
import org.jetbrains.annotations.Nullable;

public class CaptainBloomingSkeletonEntity extends CaptainSkeletonEntity implements BloomingSkeletonInterface{
    private static final EntityDataAccessor<Float> DEATH_X = SynchedEntityData.defineId(CaptainBloomingSkeletonEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DEATH_Z = SynchedEntityData.defineId(CaptainBloomingSkeletonEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> CAN_DROP_KEYS_AND_ORDERS = SynchedEntityData.defineId(CaptainBloomingSkeletonEntity.class, EntityDataSerializers.BOOLEAN);


    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(CaptainBloomingSkeletonEntity.class, EntityDataSerializers.INT);

    public CaptainBloomingSkeletonEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason,
                                        @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = pLevel.getRandom();

        CaptainBloomingSkeletonVariant variant = Util.getRandom(CaptainBloomingSkeletonVariant.values(), this.random);
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
                //    .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.2f)
                .add(Attributes.MOVEMENT_SPEED, 0.24f).build();
    }

    @Override
    public int getTickCount() {
        return this.tickCount;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isInWaterOrRain()) {
            CreateBloomingParticles(this.level(), this.random, this.position());
            if (this.tickCount % 10 == 0) {
                this.heal(3.0f);
            }
        } else {
            if (this.tickCount % 10 == 0) {
                this.heal(1.0f);
            }
        }
    }

    public CaptainBloomingSkeletonVariant getCaptainBloomingVariant() {
        return CaptainBloomingSkeletonVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    public void setVariant(CaptainBloomingSkeletonVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    //    this.entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
        this.entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
        this.setDeathX(tag.getFloat("DeathX"));
        this.setDeathZ(tag.getFloat("DeathZ"));
        this.setCanDropKeysAndOrders(tag.getBoolean("CanDropKeysAndOrders"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    //    tag.putInt("Variant", this.getTypeVariant());
        tag.putInt("Variant", this.getTypeVariant());
        tag.putFloat("DeathX", this.getDeathX());
        tag.putFloat("DeathZ", this.getDeathZ());
        tag.putBoolean("CanDropKeysAndOrders", this.getCanDropKeysAndOrders());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    //    this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.getEntityData().define(DEATH_X, 0.0f);
        this.getEntityData().define(DEATH_Z, 0.0f);
        this.getEntityData().define(CAN_DROP_KEYS_AND_ORDERS, true);
    }
}
