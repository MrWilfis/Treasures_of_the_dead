package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.mrwilfis.treasures_of_the_dead.entity.variant.GoldenSkeletonVariant;
import net.mrwilfis.treasures_of_the_dead.item.custom.AbstractPowderKegItem;
import net.mrwilfis.treasures_of_the_dead.particle.ModParticles;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;

import java.util.Random;
import java.util.UUID;

public class GoldenSkeletonEntity extends TOTDSkeletonEntity{

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(GoldenSkeletonEntity.class, EntityDataSerializers.INT);

    private static final UUID SPEED_MODIFIER_RUSTED_UUID = UUID.fromString("75211345-f704-4de5-b9ec-5cbb31bfce54");
    private static final AttributeModifier SPEED_MODIFIER_RUSTED = new AttributeModifier(ResourceLocation.parse(SPEED_MODIFIER_RUSTED_UUID.toString()),-0.455D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    private static final EntityDataAccessor<Boolean> IS_RUSTED = SynchedEntityData.defineId(GoldenSkeletonEntity.class, EntityDataSerializers.BOOLEAN);

    private int maxRustedTime = 1200;
    private int rustedTimer = 0;

    private int maxTwitchTime = 10;
    private int twitchTimer = 0;

    public GoldenSkeletonEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason,
                                        @Nullable SpawnGroupData pSpawnData) {


        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
    }

    @Override
    public void specialProcedures() {
        GoldenSkeletonVariant variant = Util.getRandom(GoldenSkeletonVariant.values(), this.random);
        setVariant(variant);
        this.populateDefaultEquipmentSlots(this.random);
    }

    public static AttributeSupplier setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 26.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5f)
                //    .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.2f)
                .add(Attributes.MOVEMENT_SPEED, 0.18f).build();
    }

    @Override
    public void tick() {
        super.tick();

        //making particles and twitch animation
        if (this.getIsRusted() && this.tickCount % 7 == 0) {
            if (this.random.nextFloat() > 0.75f) {
                //this.twitchTimer = 0;
                this.twitchTimer = maxTwitchTime;
                this.playSound(SoundEvents.CHAIN_HIT, 0.5f, 0.3f);
                this.level().broadcastEntityEvent(this, (byte)42);
            }
        }

        //timer
        if (this.twitchTimer > 0) {
            this.twitchTimer--;
        }

        //set to rusted
        if (this.isInWaterRainOrBubble()) {
            this.setIsRusted(true);
            this.rustedTimer = 0;
        }

        //set fo normal
        if (this.getIsRusted()) {
            this.rustedTimer++;
            if (this.rustedTimer >= maxRustedTime) {
                this.setIsRusted(false);
                this.rustedTimer = 0;
            }
        }

        //modifying speed
        if (this.getIsRusted()) {
            if (!this.getAttribute(Attributes.MOVEMENT_SPEED).getModifiers().contains(SPEED_MODIFIER_RUSTED)) {
                this.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(SPEED_MODIFIER_RUSTED);
            }
        } else {
            if (this.getAttribute(Attributes.MOVEMENT_SPEED).getModifiers().contains(SPEED_MODIFIER_RUSTED)) {
                this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_RUSTED);
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.ARROW) || source.is(DamageTypes.FALL) || source.is(DamageTypes.CACTUS)
                || source.is(DamageTypes.FREEZE) || source.is(DamageTypes.MAGIC) || source.is(DamageTypes.MOB_ATTACK)
                || source.is(DamageTypes.MOB_PROJECTILE) || source.is(DamageTypes.MOB_ATTACK_NO_AGGRO) || source.is(DamageTypes.SPIT)
                || source.is(DamageTypes.STING) || source.is(DamageTypes.SWEET_BERRY_BUSH) || source.is(DamageTypes.THORNS)
                || source.is(DamageTypes.WITHER);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        float newAmount = amount;
        if (source.is(DamageTypes.CAMPFIRE) || source.is(DamageTypes.HOT_FLOOR) || source.is(DamageTypes.FIREBALL) || source.is(DamageTypes.FIREWORKS)
                || source.is(DamageTypes.ON_FIRE) || source.is(DamageTypes.LAVA) || source.is(DamageTypes.UNATTRIBUTED_FIREBALL) || source.is(DamageTypes.WITHER_SKULL)) {
            newAmount = (this.getIsRusted()) ? amount * 2.0f : amount * 0.5f;
        } else if (source.is(DamageTypes.TRIDENT) || source.is(DamageTypes.THROWN) || source.is(DamageTypes.FALLING_ANVIL) || source.is(DamageTypes.FALLING_STALACTITE)
                || source.is(DamageTypes.FALLING_BLOCK)) {
            newAmount = (this.getIsRusted()) ? amount * 0.3f : amount * 0.2f;
        } else if (source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION)) {
            newAmount = (this.getIsRusted()) ? amount * 1.5f : amount * 0.5f;
        } else if (source.is(DamageTypes.PLAYER_ATTACK)) {
            if (source.getEntity() instanceof Player) {
                if (!((Player)source.getEntity()).isCreative()) {
                    source.getEntity().playSound(SoundEvents.TRIDENT_HIT_GROUND, 1, 1);
                    source.getEntity().playSound(SoundEvents.ANVIL_PLACE, 1, 1.5f);
                    return false;
                }
            }
        }
        if (this.isDeadOrDying()) {
            this.playSound(getDeathSound(), 0.7f, 0.4f);
        }

        return super.hurt(source, newAmount);
    }

    protected void createRustedParticles(Level level, Random random, Vec3 position) {

        double xOffset = random.nextDouble() * 0.5 - 0.25;
        double yOffset = random.nextDouble() * 0.5 - 0.25;
        double zOffset = random.nextDouble() * 0.5 - 0.25;

        double xSpeed = (random.nextDouble() * 0.4) - 0.2;
        double ySpeed = (random.nextDouble() * 0.4) - 0.2;
        double zSpeed = (random.nextDouble() * 0.4) - 0.2;

        for (int i = 0; i < random.nextInt(3, 5+1); i++) {

            xSpeed += random.nextFloat(-0.125f, +0.125f);
            ySpeed += random.nextFloat(-0.125f, +0.125f);
            zSpeed += random.nextFloat(-0.125f, +0.125f);


            level.addParticle(ModParticles.RUSTED_GOLDEN_SKELETON_PARTICLES.get(), // Тип частиц
                    position.x + xOffset, // Позиция X
                    position.y + yOffset + 1.3, // Позиция Y
                    position.z + zOffset, // Позиция Z
                    xSpeed, ySpeed + 0.075f, zSpeed); // Скорость (0, 0, 0)
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);

        if (id == 42) {
            Random rand = new Random();
            createRustedParticles(this.level(), rand, this.position());
//            this.level().playSound(this, this.getOnPos(), SoundEvents.CHAIN_HIT, SoundSource.HOSTILE, 1f, 0.3f);
//            this.playSound(SoundEvents.CHAIN_HIT, 1f, 0.3f);
            if (this.random.nextBoolean()) {
                this.triggerAnim("controller4", "rusty_twitch1");
            } else {
                this.triggerAnim("controller5", "rusty_twitch2");
            }
        }

    }

    private boolean isActuallyMoving() {
        double dx = this.getX() - this.xo;
        double dz = this.getZ() - this.zo;
        float threshold = 0.00005f;
        return dx * dx + dz * dz > threshold * threshold;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller1", 3, this::idleAndWalk));
        controllerRegistrar.add(new AnimationController<>(this, "controller2", 3, this::walkAndAttack));
        controllerRegistrar.add(new AnimationController<>(this, "controller3", 0, this::spawning));
        controllerRegistrar.add(new AnimationController<>(this, "controller4", 0, this::twitching)
                .triggerableAnim("rusty_twitch1", RUSTY_TWITCH1));
        controllerRegistrar.add(new AnimationController<>(this, "controller5", 0, this::twitching)
                .triggerableAnim("rusty_twitch2", RUSTY_TWITCH2));
    }

    private PlayState twitching(AnimationState<GoldenSkeletonEntity> state) {
        if (this.twitchTimer > 0) {
                //state.getController().setAnimation(RUSTY_TWITCH1);
        }
        return PlayState.CONTINUE;
    }

    private PlayState spawning(AnimationState<GoldenSkeletonEntity> state) {
        if (this.getIsSpawning()) {
            state.getController().setAnimation(SPAWN2);
            return PlayState.CONTINUE;
        }
        state.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    private PlayState walkAndAttack(AnimationState<GoldenSkeletonEntity> state) {
        ItemStack mainHandItem = this.getItemInHand(InteractionHand.MAIN_HAND);
        double modificator = this.getIsRusted() ? 2.8D : 1.444D;

        if (this.getIsSpawning()) {
            state.getController().stop();
            //    return PlayState.STOP;
        }
        if (this.swinging) {
            state.getController().stop();
            state.getController().setAnimation(ATTACK1);
            state.getController().setAnimationSpeed(1.0D * 0.8D);
            return PlayState.CONTINUE;
        } else if (!mainHandItem.isEmpty() && (mainHandItem.getItem() instanceof AbstractPowderKegItem)) {
            state.getController().stop();
            //    state.getController().setAnimationSpeed(1.0D);
            return  PlayState.CONTINUE;
        }
        else if (isActuallyMoving() && this.isAggressive()) {
            state.getController().setAnimation(WALK_HANDS1);
            state.getController().setAnimationSpeed(1.25D / modificator);
            return PlayState.CONTINUE;
        }
        else if (isActuallyMoving() && !this.isAggressive()) {
            state.getController().setAnimation(WALK_HANDS1);
            state.getController().setAnimationSpeed(1.0D / modificator);
            return PlayState.CONTINUE;
        }
        else if (!isActuallyMoving() && !this.swinging) {
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    private PlayState idleAndWalk(AnimationState<GoldenSkeletonEntity> state) {
        ItemStack mainHandItem = this.getItemInHand(InteractionHand.MAIN_HAND);
        double modificator = this.getIsRusted() ? 2.8D : 1.444D;

        if (this.getIsSpawning()) {
            state.getController().stop();
            //    return PlayState.STOP;
        }
        if (isActuallyMoving() && this.isAggressive() && !mainHandItem.isEmpty() && (mainHandItem.getItem() instanceof AbstractPowderKegItem)) {
            state.getController().setAnimation(WALK_KEG);
            state.getController().setAnimationSpeed(1.6875D / modificator);
            return  PlayState.CONTINUE;
        } else if (isActuallyMoving() && !this.isAggressive() && !mainHandItem.isEmpty() && (mainHandItem.getItem() instanceof AbstractPowderKegItem)) {
            state.getController().setAnimation(WALK_KEG);
            state.getController().setAnimationSpeed(1.35D / modificator);
            return  PlayState.CONTINUE;
        } else if (!isActuallyMoving() && !mainHandItem.isEmpty() && (mainHandItem.getItem() instanceof AbstractPowderKegItem)) {
            state.getController().setAnimation(IDLE_KEG);
            return PlayState.CONTINUE;
        }
        if (isActuallyMoving() && this.isAggressive()) {
            state.getController().setAnimation(WALK_BODY1);
            state.getController().setAnimationSpeed(1.25D / modificator);
            return PlayState.CONTINUE;
        } else if (isActuallyMoving() && !this.isAggressive()) {
            state.getController().setAnimation(WALK_BODY1);
            state.getController().setAnimationSpeed(1.0D / modificator);
            if ((double) random.nextFloat() < 0.5f) {
                this.idleVariation = 1;
            } else if ((double) random.nextFloat() < 1.0f) {
                this.idleVariation = 2;
            }
            return PlayState.CONTINUE;
        } else if (!isActuallyMoving()) {
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
    protected SoundEvent getStepSound() {
        return SoundEvents.CHAIN_STEP;
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(this.getStepSound(), 0.5F, 0.2F);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.CHAIN_PLACE;
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        this.ambientSoundTime = -this.getAmbientSoundInterval();
        this.playSound(this.getHurtSound(source), 0.6F, 0.5F);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CHAIN_BREAK;
    }

    @Override
    public void playAmbientSound() {
        this.playSound(getAmbientSound(), getSoundVolume(), 0.7f);
    }

    public GoldenSkeletonVariant getGoldenVariant() {
        return GoldenSkeletonVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    public void setVariant(GoldenSkeletonVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    public boolean getIsRusted() {
        return this.entityData.get(IS_RUSTED);
    }

    public void setIsRusted(boolean b) {
        this.entityData.set(IS_RUSTED, b);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_ID_TYPE_VARIANT, tag.getInt("Variant"));
        this.entityData.set(IS_RUSTED, tag.getBoolean("IsRusted"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeVariant());
        tag.putBoolean("IsRusted", this.getIsRusted());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 0);
        builder.define(IS_RUSTED, false);
    }
}
