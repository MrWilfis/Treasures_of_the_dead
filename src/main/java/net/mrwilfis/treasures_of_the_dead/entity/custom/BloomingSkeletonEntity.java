package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.mrwilfis.treasures_of_the_dead.entity.variant.BloomingSkeletonVariant;
import org.jetbrains.annotations.Nullable;

public class BloomingSkeletonEntity extends TOTDSkeletonEntity implements BloomingSkeletonInterface{

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(BloomingSkeletonEntity.class, EntityDataSerializers.INT);

    public BloomingSkeletonEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason,
                                        @Nullable SpawnGroupData pSpawnData) {
        RandomSource randomsource = pLevel.getRandom();

        BloomingSkeletonVariant variant = Util.getRandom(BloomingSkeletonVariant.values(), this.random);
        setVariant(variant);

        this.populateDefaultEquipmentSlots(randomsource);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 26.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5f)
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

    @Override
    public void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pRandom, pDifficulty);

        if (this.getBloomingVariant().equals(BloomingSkeletonVariant.VAR2) ||
                this.getBloomingVariant().equals(BloomingSkeletonVariant.VAR3)) {
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.AIR));
        }

        if (this.getBloomingVariant().equals(BloomingSkeletonVariant.VAR4) ||
                this.getBloomingVariant().equals(BloomingSkeletonVariant.VAR5)) {
            spawnRandomBandanas(this.random);
        }
    }

    public BloomingSkeletonVariant getBloomingVariant() {
        return BloomingSkeletonVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    public void setVariant(BloomingSkeletonVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
    }
}
