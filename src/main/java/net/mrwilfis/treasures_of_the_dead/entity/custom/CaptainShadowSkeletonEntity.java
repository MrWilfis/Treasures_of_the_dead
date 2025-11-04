package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.mrwilfis.treasures_of_the_dead.entity.variant.CaptainShadowSkeletonVariant;
import net.mrwilfis.treasures_of_the_dead.entity.variant.CaptainSkeletonVariant;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import net.mrwilfis.treasures_of_the_dead.item.custom.AbstractPowderKegItem;
import net.mrwilfis.treasures_of_the_dead.misc.TOTDBlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

public class CaptainShadowSkeletonEntity extends ShadowSkeletonEntity implements CaptainSkeletonInterface {

    private static final EntityDataAccessor<Float> DEATH_X = SynchedEntityData.defineId(CaptainShadowSkeletonEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DEATH_Z = SynchedEntityData.defineId(CaptainShadowSkeletonEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> CAN_DROP_KEYS_AND_ORDERS = SynchedEntityData.defineId(CaptainShadowSkeletonEntity.class, EntityDataSerializers.BOOLEAN);



    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(CaptainShadowSkeletonEntity.class, EntityDataSerializers.INT);

    public CaptainShadowSkeletonEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
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
        CaptainShadowSkeletonVariant variant = Util.getRandom(CaptainShadowSkeletonVariant.values(), this.random);
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
                .add(Attributes.ARMOR, 4.0f)
                //    .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.2f)
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
        if ((double) this.random.nextFloat() < 0.8) {
            this.maybeWearEquipment(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD), pRandom, 0.9F);
        } else {
            this.maybeWearEquipment(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD), pRandom, 0.9F);
        }
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
