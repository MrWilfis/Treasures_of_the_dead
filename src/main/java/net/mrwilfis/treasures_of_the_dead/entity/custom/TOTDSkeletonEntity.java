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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
import net.minecraft.world.phys.Vec3;
import net.mrwilfis.treasures_of_the_dead.entity.variant.TOTDSkeletonVariant;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import net.mrwilfis.treasures_of_the_dead.item.custom.AbstractPowderKegItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.UUID;

public class TOTDSkeletonEntity extends Monster implements GeoAnimatable, GeoEntity {
    protected int idleVariation = random.nextInt(1, 2+1);

    //spawn animation
    private final int maxSpawningTime = 80;
    private int spawningTime = 0;

    //keg blowing up
    private int maxPrepareToBlowUp = 90;
    private int prepareToBlowUp = 0;

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final UUID SPEED_MODIFIER_WITH_KEG_UUID = UUID.fromString("d27bb1e7-92bc-4161-8e9d-2f9a52b1598e");
    private static final AttributeModifier SPEED_MODIFIER_WITH_KEG = new AttributeModifier(SPEED_MODIFIER_WITH_KEG_UUID, "Keg Speed Modifier", -0.3D, AttributeModifier.Operation.MULTIPLY_BASE);

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(TOTDSkeletonEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> IS_GOING_TO_BLOW_UP = SynchedEntityData.defineId(TOTDSkeletonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SPAWNING = SynchedEntityData.defineId(TOTDSkeletonEntity.class, EntityDataSerializers.BOOLEAN);

    protected static final RawAnimation WALK_BODY1 = RawAnimation.begin().then("animation.model.walk_body1", Animation.LoopType.LOOP);
    protected static final RawAnimation WALK_HANDS1 = RawAnimation.begin().then("animation.model.walk_hands1", Animation.LoopType.LOOP);
    protected static final RawAnimation IDLE1 = RawAnimation.begin().then("animation.model.idle1", Animation.LoopType.LOOP);
    protected static final RawAnimation IDLE2 = RawAnimation.begin().then("animation.model.idle2", Animation.LoopType.LOOP);
    protected static final RawAnimation ATTACK1 = RawAnimation.begin().then("animation.model.attack1", Animation.LoopType.PLAY_ONCE);
    protected static final RawAnimation SHAKING_FROM_LIGHT = RawAnimation.begin().then("animation.model.shaking_from_light", Animation.LoopType.PLAY_ONCE);
    protected static final RawAnimation SHAKING_FROM_LIGHT_WITH_KEG = RawAnimation.begin().then("animation.model.shaking_from_light_with_keg", Animation.LoopType.PLAY_ONCE);
    protected static final RawAnimation WALK_KEG = RawAnimation.begin().then("animation.model.walk_keg", Animation.LoopType.LOOP);
    protected static final RawAnimation IDLE_KEG = RawAnimation.begin().then("animation.model.idle_keg", Animation.LoopType.LOOP);
    protected static final RawAnimation SPAWN2 = RawAnimation.begin().then("animation.model.spawn2", Animation.LoopType.PLAY_ONCE);

    public TOTDSkeletonEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason,
                                        @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = pLevel.getRandom();

        TOTDSkeletonVariant variant = Util.getRandom(TOTDSkeletonVariant.values(), this.random);
        setVariant(variant);

        if (pReason.equals(MobSpawnType.SPAWN_EGG) || pReason.equals(MobSpawnType.SPAWNER) || pReason.equals(MobSpawnType.DISPENSER)
                || pReason.equals(MobSpawnType.MOB_SUMMONED)) {

        } else {
            //this.setIsSpawning(true);
        }

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

    @Override
    public void tick() {

        // execute as @e[type=treasures_of_the_dead:totd_skeleton, distance=..10] at @p run item replace entity @s weapon.mainhand with tnt
        if (!this.level().isClientSide) {
            ItemStack mainHandItem = this.getItemInHand(InteractionHand.MAIN_HAND);

            if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof AbstractPowderKegItem) {

                if (!this.getAttribute(Attributes.MOVEMENT_SPEED).getModifiers().contains(SPEED_MODIFIER_WITH_KEG)) {
                    this.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(SPEED_MODIFIER_WITH_KEG);
                }

                if (this.prepareToBlowUp >= this.maxPrepareToBlowUp) {
                    explodeSkeleton();
                }

                LivingEntity target = this.getTarget();

                if (target == null) {
                    this.setIsGoingToBlowUp(false);
                } else {
                    if (this.distanceToSqr(target) > 64.0) {
                        this.setIsGoingToBlowUp(false);
                    } else if (this.distanceToSqr(target) < 25.0) {
                        this.setIsGoingToBlowUp(true);
                    }
                }


                boolean $$0 = this.getIsGoingToBlowUp();



//                System.out.println(this.prepareToBlowUp);
//                System.out.println($$0);

                if ($$0 && this.prepareToBlowUp == 0) {
                    this.playSound(SoundEvents.TNT_PRIMED, 1.0F, 0.5F);
                }

                if ($$0) {
                    this.prepareToBlowUp++;
                } else {
                    this.prepareToBlowUp = 0;
                }

            } else {
                if (this.getAttribute(Attributes.MOVEMENT_SPEED).getModifiers().contains(SPEED_MODIFIER_WITH_KEG))
                    this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_WITH_KEG);
            }

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

        if (this.getIsGoingToBlowUp()) {
            createFuseParticles(this.level(), this.random, this);
        }
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

    protected void explodeSkeleton() {
        if (!this.level().isClientSide) {
            this.dead = true;
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3.0f, Level.ExplosionInteraction.MOB);
            this.discard();

        }

    }

    protected void createFuseParticles(Level level, RandomSource random, TOTDSkeletonEntity entity) {

        Vec3 entityPos = entity.position();

        float yaw = entity.getYRot();
        double radians = Math.toRadians(yaw);

        Vec3 bodyDirection = new Vec3(-Math.sin(radians), 0, Math.cos(radians)).normalize();

        double distance = 0.25;

        Vec3 particlePosition = entityPos.add(bodyDirection.scale(distance));

        double xOffset = random.nextDouble() * 0.2 - 0.1; // Случайный смещение по X
        double yOffset = random.nextDouble() * 0.2 - 0.1; // Случайный смещение по Y
        double zOffset = random.nextDouble() * 0.2 - 0.1; // Случайный смещение по Z

        double xSpeed = random.nextDouble() * 0.04 - 0.02; // Случайная скорость по X от -0.2 до 0.2
        double ySpeed = (random.nextDouble() * 0.06) + 0.03; // Случайная скорость по Y от -0.2 до 0.2
        double zSpeed = (random.nextDouble() * 0.04) - 0.02; // Случайная скорость по Z от -0.2 до 0.2

        level.addParticle(ParticleTypes.SMOKE,
                particlePosition.x + xOffset,
                particlePosition.y + yOffset + 1.7,
                particlePosition.z + zOffset,
                xSpeed, ySpeed, zSpeed); // Скорость (0, 0, 0)


            //System.out.println("GOOOOOIDA");
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
 //       this.goalSelector.addGoal(1, new AttackWithKegGoal(this, 0.6D, false));
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
        controllerRegistrar.add(new AnimationController<>(this, "controller3", 0, this::spawning));
    }

    private <E extends GeoAnimatable> PlayState spawning(AnimationState<E> state) {
        if (this.getIsSpawning()) {
            state.getController().setAnimation(SPAWN2);
        }
        return PlayState.CONTINUE;
    }

    private <E extends GeoAnimatable> PlayState walkAndAttack(AnimationState<E> state) {
        ItemStack mainHandItem = this.getItemInHand(InteractionHand.MAIN_HAND);

        if (this.getIsSpawning()) {
            state.getController().stop();
        //    return PlayState.STOP;
        }
        if (this.swinging) {
            state.getController().stop();
            state.getController().setAnimation(ATTACK1);
            state.getController().setAnimationSpeed(1.0D);
            return PlayState.CONTINUE;
        } else if (!mainHandItem.isEmpty() && (mainHandItem.getItem() instanceof AbstractPowderKegItem)) {
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

    private <E extends GeoAnimatable> PlayState idleAndWalk(AnimationState<E> state) {
        ItemStack mainHandItem = this.getItemInHand(InteractionHand.MAIN_HAND);

        if (this.getIsSpawning()) {
            state.getController().stop();
        //    return PlayState.STOP;
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
    public boolean getIsSpawning() {
        return this.getEntityData().get(IS_SPAWNING).booleanValue();
    }

    public void setIsSpawning(boolean b) {
        this.getEntityData().set(IS_SPAWNING, b);
    }

    public boolean getIsGoingToBlowUp() {
        return this.getEntityData().get(IS_GOING_TO_BLOW_UP).booleanValue();
    }

    public void setIsGoingToBlowUp(boolean b) {
        this.getEntityData().set(IS_GOING_TO_BLOW_UP, b);
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
        this.setIsGoingToBlowUp(tag.getBoolean("IsGoingToBlowUp"));
        this.setIsSpawning(tag.getBoolean("IsSpawning"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsGoingToBlowUp", this.getIsGoingToBlowUp());
        tag.putBoolean("IsSpawning", this.getIsSpawning());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE_VARIANT, 0);
        this.getEntityData().define(IS_GOING_TO_BLOW_UP, false);
        this.getEntityData().define(IS_SPAWNING, false);
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
