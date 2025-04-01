package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.mrwilfis.treasures_of_the_dead.entity.variant.TOTDSkeletonVariant;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

public class TOTDSkeletonEntity extends Monster implements GeoAnimatable, GeoEntity {
    protected int idleVariation = random.nextInt(1, 3);

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(TOTDSkeletonEntity.class, EntityDataSerializers.INT);

    protected static final RawAnimation WALK_BODY1 = RawAnimation.begin().then("animation.model.walk_body1", Animation.LoopType.LOOP);
    protected static final RawAnimation WALK_HANDS1 = RawAnimation.begin().then("animation.model.walk_hands1", Animation.LoopType.LOOP);
    protected static final RawAnimation IDLE1 = RawAnimation.begin().then("animation.model.idle1", Animation.LoopType.LOOP);
    protected static final RawAnimation IDLE2 = RawAnimation.begin().then("animation.model.idle2", Animation.LoopType.LOOP);
    protected static final RawAnimation ATTACK1 = RawAnimation.begin().then("animation.model.attack1", Animation.LoopType.PLAY_ONCE);
    protected static final RawAnimation SHAKING_FROM_LIGHT = RawAnimation.begin().then("animation.model.shaking_from_light", Animation.LoopType.PLAY_ONCE);

    public TOTDSkeletonEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    //    this.idleVariation = idleVariation;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason,
                                        @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = pLevel.getRandom();

        TOTDSkeletonVariant variant = Util.getRandom(TOTDSkeletonVariant.values(), this.random);
        setVariant(variant);

        this.populateDefaultEquipmentSlots(randomsource);
        this.populateDefaultEquipmentEnchantments(randomsource, pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
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

    public void populateDefaultEquipmentSlots(RandomSource pRandom) {

        double randomValue;

        randomValue = (double) this.random.nextFloat();

        //Spawn clothes
        if (randomValue < 0.3) {
            this.maybeWearEquipment(EquipmentSlot.CHEST, new ItemStack(ModItems.VEST.get()), pRandom, 0.65F);
            this.maybeWearEquipment(EquipmentSlot.LEGS, new ItemStack(ModItems.PANTS.get()), pRandom, 0.5F);
            this.maybeWearEquipment(EquipmentSlot.FEET, new ItemStack(ModItems.BOOTS.get()), pRandom, 0.7F);
        } else if (randomValue < 0.6) {
            this.maybeWearEquipment(EquipmentSlot.CHEST, new ItemStack(ModItems.BLACK_VEST.get()), pRandom, 0.65F);
            this.maybeWearEquipment(EquipmentSlot.LEGS, new ItemStack(ModItems.BLACK_PANTS.get()), pRandom, 0.5F);
            this.maybeWearEquipment(EquipmentSlot.FEET, new ItemStack(ModItems.BLACK_BOOTS.get()), pRandom, 0.7F);
        } else if (randomValue < 0.9) {
            this.maybeWearEquipment(EquipmentSlot.CHEST, new ItemStack(ModItems.BLUE_VEST.get()), pRandom, 0.65F);
            this.maybeWearEquipment(EquipmentSlot.LEGS, new ItemStack(ModItems.BLUE_PANTS.get()), pRandom, 0.5F);
            this.maybeWearEquipment(EquipmentSlot.FEET, new ItemStack(ModItems.BLUE_BOOTS.get()), pRandom, 0.7F);
        }

        randomValue = (double) this.random.nextFloat();

        //Spawn Bandanas or others
        if (randomValue < 0.25) {
            this.maybeWearEquipment(EquipmentSlot.HEAD, new ItemStack(ModItems.GREEN_BANDANA.get()), pRandom, 0.6F);
        } else if (randomValue < 0.5) {
            this.maybeWearEquipment(EquipmentSlot.HEAD, new ItemStack(ModItems.RED_BANDANA.get()), pRandom, 0.6F);
        } else if (randomValue < 0.75) {
            this.maybeWearEquipment(EquipmentSlot.HEAD, new ItemStack(ModItems.BLUE_BANDANA.get()), pRandom, 0.6F);
        }

        randomValue = (double) this.random.nextFloat();

        //Spawn weapons
        if (randomValue < 0.2) {
            this.maybeWearEquipment(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD), pRandom, 0.5F);
        } else {
            this.maybeWearEquipment(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD), pRandom, 0.5F);
        }
    }

    public void spawnRandomBandanas(RandomSource pRandom) {

        double randomValue = (double) this.random.nextFloat();

        if (randomValue < 0.33) {
            this.maybeWearEquipment(EquipmentSlot.HEAD, new ItemStack(ModItems.GREEN_BANDANA.get()), pRandom, 1.0F);
        } else if (randomValue < 0.67) {
            this.maybeWearEquipment(EquipmentSlot.HEAD, new ItemStack(ModItems.RED_BANDANA.get()), pRandom, 1.0F);
        } else if (randomValue < 1.0) {
            this.maybeWearEquipment(EquipmentSlot.HEAD, new ItemStack(ModItems.BLUE_BANDANA.get()), pRandom, 1.0F);
        }
    }

    public void maybeWearEquipment(EquipmentSlot pSlot, ItemStack pStack, RandomSource pRandom, double chance) {
        if (pRandom.nextFloat() < chance) {
            this.setItemSlot(pSlot, pStack);
        }
    }



//    public static String getRandomName(RandomSource random) throws IOException {
//
//        String dirpath = "config/treasures_of_the_dead/captainnames.txt";
//        List<String> names = List.of();
//
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(dirpath));
//            String line = reader.readLine();
//            int counter = 0;
//            while (line != null ) {
//                counter++;
//                names.add(line);
//            }
//
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return names.get(random.nextInt(0,2));
//    }

    @Override
    protected void registerGoals() {
         this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.25D, false));
         this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
 //        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

         this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[]{TOTDSkeletonEntity.class})));
         this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
 //        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
         this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller1", 3, this::idleAndWalk));
        controllerRegistrar.add(new AnimationController<>(this, "controller2", 3, this::walkAndAttack));
    }

    private <E extends GeoAnimatable> PlayState walkAndAttack(AnimationState<E> state) {

        if (this.swinging) {
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

    private <E extends GeoAnimatable> PlayState idleAndWalk(AnimationState<E> state) {


        if (state.isMoving() && this.isAggressive()) {
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
    public double getTick(Object o) {
 //       return RenderUtils.getCurrentTick(); // ваще гавно, 20 фпс
        return (double)((Entity)o).tickCount;
    }

//    @Override
//    public void tick() {
//        super.tick();
//
//        if (this.isInWaterOrRain()) {
//            CreateBloomingParticles(this.level(), this.random, this.position());
//            if (this.tickCount % 10 == 0) {
//                this.heal(3.0f);
//            }
//        } else {
//            if (this.tickCount % 10 == 0) {
//                this.heal(1.0f);
//            }
//        }
//    }


    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    protected float getSoundVolume() {
        return 1.0f;
    }

//    protected boolean isSunSensitive() {
//        return false;

//    }

    public @NotNull MobType getMobType() {
        return MobType.UNDEAD;
    }

    public TOTDSkeletonVariant getVariant() {
        return TOTDSkeletonVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    public void setVariant(TOTDSkeletonVariant variant) {
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
