package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.mrwilfis.treasures_of_the_dead.entity.variant.CaptainBloomingSkeletonVariant;
import net.mrwilfis.treasures_of_the_dead.entity.variant.CaptainSkeletonVariant;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import org.jetbrains.annotations.Nullable;

public class CaptainBloomingSkeletonEntity extends BloomingSkeletonEntity implements CaptainSkeletonInterface{
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

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public void specialProcedures() {
        CaptainBloomingSkeletonVariant variant = Util.getRandom(CaptainBloomingSkeletonVariant.values(), this.random);
        setVariant(variant);
        this.populateDefaultEquipmentSlots(this.random);
        this.setCustomName(Component.literal(getRandomName(this.random)));
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.65f)
                .add(Attributes.MOVEMENT_SPEED, 0.24f).build();
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);

        if (getCanDropKeysAndOrders()) {
            double randomValue = (double) this.random.nextFloat();
            if (randomValue < 0.4f) {
                this.setDeathX((float) this.getX());
                this.setDeathZ((float) this.getZ());
                ItemStack stack = new ItemStack(ModItems.SKELETONS_ORDER.get());
                stack.setTag(new CompoundTag());
                stack.getTag().putFloat("DeathX", this.getDeathX());
                stack.getTag().putFloat("DeathZ", this.getDeathZ());
                ItemEntity itemEntity = this.spawnAtLocation(stack);
            }
            else if (randomValue < 0.6f)  {
                this.setDeathX((float) this.getX());
                this.setDeathZ((float) this.getZ());
                ItemStack stack = new ItemStack(ModItems.SKELETON_CREW_ASSIGNMENT.get());
                stack.setTag(new CompoundTag());
                stack.getTag().putFloat("DeathX", this.getDeathX());
                stack.getTag().putFloat("DeathZ", this.getDeathZ());
                ItemEntity itemEntity = this.spawnAtLocation(stack);
            }
            else {
                ItemEntity itemEntity = this.spawnAtLocation(ModItems.TREASURE_KEY.get());
            }
        }
    }

    @Override
    public int getExperienceReward() {
        return  (int) (super.getExperienceReward() * 1.75);
    }

    @Override
    public void populateDefaultEquipmentSlots(RandomSource pRandom) {

        double randomValue = (double) this.random.nextFloat();

        //SPAWN OUTFITS
        if (randomValue < 0.1) {
            //Poor clothes
            if ((double) this.random.nextFloat() < 0.3) {
                spawnRandomBandanas(pRandom);
            } else if ((double) this.random.nextFloat() < 1.0) {
                this.maybeWearEquipment(EquipmentSlot.HEAD, new ItemStack(ModItems.BICORN.get()), pRandom, 1.0F);
                this.maybeWearEquipment(EquipmentSlot.HEAD, new ItemStack(ModItems.CAPTAIN_HAT.get()), pRandom, 0.5F);
            }
            this.maybeWearEquipment(EquipmentSlot.CHEST, new ItemStack(ModItems.BLACK_VEST.get()), pRandom, 1.0F);
            this.maybeWearEquipment(EquipmentSlot.LEGS, new ItemStack(ModItems.CAPTAIN_PANTS.get()), pRandom, 1.0F);
            this.maybeWearEquipment(EquipmentSlot.FEET, new ItemStack(ModItems.BLACK_BOOTS.get()), pRandom, 0.8F);
        } else if (randomValue < 0.55) {
            //Captain clothes 1
            if ((double) this.random.nextFloat() < 0.1) {
                spawnRandomBandanas(pRandom);
            } else if ((double) this.random.nextFloat() < 1.0) {
                this.maybeWearEquipment(EquipmentSlot.HEAD, new ItemStack(ModItems.BICORN.get()), pRandom, 0.92F);
                this.maybeWearEquipment(EquipmentSlot.HEAD, new ItemStack(ModItems.CAPTAIN_HAT.get()), pRandom, 0.08F);
            }
            this.maybeWearEquipment(EquipmentSlot.CHEST, new ItemStack(ModItems.CAPTAIN_JACKET.get()), pRandom, 1.0F);
            this.maybeWearEquipment(EquipmentSlot.LEGS, new ItemStack(ModItems.CAPTAIN_PANTS.get()), pRandom, 1.0F);
            this.maybeWearEquipment(EquipmentSlot.FEET, new ItemStack(ModItems.BLACK_BOOTS.get()), pRandom, 0.5F);
        } else if (randomValue < 1.0) {
            //Captain clothes 2
            if ((double) this.random.nextFloat() < 0.1) {
                spawnRandomBandanas(pRandom);
            } else if ((double) this.random.nextFloat() < 1.0) {
                this.maybeWearEquipment(EquipmentSlot.HEAD, new ItemStack(ModItems.CAPTAIN_HAT.get()), pRandom, 0.92F);
                this.maybeWearEquipment(EquipmentSlot.HEAD, new ItemStack(ModItems.BICORN.get()), pRandom, 0.08F);
            }
            this.maybeWearEquipment(EquipmentSlot.CHEST, new ItemStack(ModItems.CAPTAIN_CROP_VEST.get()), pRandom, 1.0F);
            this.maybeWearEquipment(EquipmentSlot.LEGS, new ItemStack(ModItems.CAPTAIN_SKIRT.get()), pRandom, 1.0F);
            this.maybeWearEquipment(EquipmentSlot.FEET, new ItemStack(ModItems.BLACK_BOOTS.get()), pRandom, 0.2F);
        }


        //Spawn weapons
        if ((double) this.random.nextFloat() < 0.8) {
            this.maybeWearEquipment(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD), pRandom, 0.9F);
        } else {
            this.maybeWearEquipment(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD), pRandom, 0.9F);
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

    public boolean getCanDropKeysAndOrders() {
        return this.getEntityData().get(CAN_DROP_KEYS_AND_ORDERS).booleanValue();
    }

    public void setCanDropKeysAndOrders(boolean b) {
        this.getEntityData().set(CAN_DROP_KEYS_AND_ORDERS, b);
    }

    public float getDeathX() {
        return this.getEntityData().get(DEATH_X).floatValue();
    }

    public void setDeathX(float var) {
        this.getEntityData().set(DEATH_X, var);
    }

    public float getDeathZ() {
        return this.getEntityData().get(DEATH_Z).floatValue();
    }

    public void setDeathZ(float var) {
        this.getEntityData().set(DEATH_Z, var);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
        this.setDeathX(tag.getFloat("DeathX"));
        this.setDeathZ(tag.getFloat("DeathZ"));
        this.setCanDropKeysAndOrders(tag.getBoolean("CanDropKeysAndOrders"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putFloat("DeathX", this.getDeathX());
        tag.putFloat("DeathZ", this.getDeathZ());
        tag.putBoolean("CanDropKeysAndOrders", this.getCanDropKeysAndOrders());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.getEntityData().define(DEATH_X, 0.0f);
        this.getEntityData().define(DEATH_Z, 0.0f);
        this.getEntityData().define(CAN_DROP_KEYS_AND_ORDERS, true);
    }
}
