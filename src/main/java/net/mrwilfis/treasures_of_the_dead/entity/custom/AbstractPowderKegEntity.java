package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class AbstractPowderKegEntity extends AnyTreasureClass {


    private static final EntityDataAccessor<Boolean> IS_GOING_TO_BLOW_UP = SynchedEntityData.defineId(AbstractPowderKegEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> PREPARE_TO_BLOW_UP = SynchedEntityData.defineId(AbstractPowderKegEntity.class, EntityDataSerializers.INT);
    public int maxPrepareToBlowUp;
    public float explodeRadius;

    public AbstractPowderKegEntity(EntityType pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noCulling = true;
        this.maxPrepareToBlowUp = this.getMaxPrepareToBlowUp();
        this.explodeRadius = this.getExplodeRadius();
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0f).build();
    }


    @Override
    public @NotNull InteractionResult mobInteract(Player pPlayer, @NotNull InteractionHand pHand) {
        if (pPlayer.isShiftKeyDown()) {
            this.setYRot(pPlayer.getYRot() - 180.0f);
            this.addTag("TOTD_Rotate");

            //    this.teleportTo(this.getX(), this.getY(), this.getZ());
        } else {
            boolean b = this.getIsGoingToBlowUp();
            if (!b) {
                setIsGoingToBlowUp(true);
                return InteractionResult.SUCCESS;
            } else {
                setIsGoingToBlowUp(false);
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(pPlayer, pHand);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide)
        {
            boolean b = this.getIsGoingToBlowUp();
            int i = this.getPrepareToBlowUp();

            //Add ticks
            if (b) {
                setPrepareToBlowUp(i+1);
            } else {
                if (i != 0) this.setPrepareToBlowUp(0);
            }

            //Explode
            if (i >= this.maxPrepareToBlowUp) {
                explodeKeg();
            }

            //Play sound
            if (b && i == 0) {
                this.playSound(SoundEvents.TNT_PRIMED, 1.0F, 0.5F);
            }

        }
        //Add particles
        if (this.getIsGoingToBlowUp()) {
            createFuseParticles(this.level(), this.random, this.position());
        }

    }

    protected void explodeKeg() {
        if (!this.level().isClientSide) {
            this.dead = true;
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), explodeRadius, Level.ExplosionInteraction.MOB);
            this.discard();
        }
    }

    protected void createFuseParticles(Level level, RandomSource random, Vec3 position) {
        double xOffset = this.random.nextDouble() * 0.2 - 0.1; // Случайный смещение по X
        double yOffset = this.random.nextDouble() * 0.2 - 0.1; // Случайный смещение по Y
        double zOffset = this.random.nextDouble() * 0.2 - 0.1; // Случайный смещение по Z

        double xSpeed = (this.random.nextDouble() * 0.04) - 0.02; // Случайная скорость по X от -0.2 до 0.2
        double ySpeed = (this.random.nextDouble() * 0.05) + 0.03; // Случайная скорость по Y от -0.2 до 0.2
        double zSpeed = (this.random.nextDouble() * 0.04) - 0.02; // Случайная скорость по Z от -0.2 до 0.2

        try {
            this.level().addParticle(ParticleTypes.SMOKE,
                    position.x + xOffset,
                    position.y + yOffset + 0.665,
                    position.z + zOffset,
                    xSpeed, ySpeed, zSpeed); // Скорость (0, 0, 0)

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public boolean isInvulnerableTo(DamageSource pSource) {
        return pSource.is(DamageTypes.IN_WALL) || pSource.is(DamageTypes.FALLING_BLOCK) || pSource.is(DamageTypes.CACTUS)
                || pSource.is(DamageTypes.DRAGON_BREATH)
                || pSource.is(DamageTypes.FREEZE)
                || pSource.is(DamageTypes.INDIRECT_MAGIC)
                || pSource.is(DamageTypes.EXPLOSION)
                || pSource.is(DamageTypes.PLAYER_EXPLOSION)
                || pSource.is(DamageTypes.ON_FIRE)
                || pSource.is(DamageTypes.HOT_FLOOR)
                || pSource.is(DamageTypes.LAVA);
        // || pSource.is(DamageTypes.FALLING_ANVIL) || pSource.is(DamageTypes.FALLING_STALACTITE)
        //        || pSource.is(DamageTypes.FIREWORKS) || pSource.is(DamageTypes.ON_FIRE) || pSource.is(DamageTypes.HOT_FLOOR)
        //|| pSource.is(DamageTypes.LIGHTNING_BOLT) || pSource.is(DamageTypes.LAVA);
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        boolean fastExplosion = pSource.is(DamageTypes.FALLING_ANVIL) || pSource.is(DamageTypes.FALLING_STALACTITE) || pSource.is(DamageTypes.FIREWORKS) ||
                pSource.is(DamageTypes.LIGHTNING_BOLT) || pSource.is(DamageTypes.ARROW) || pSource.is(DamageTypes.FIREBALL)
                || pSource.is(DamageTypes.UNATTRIBUTED_FIREBALL) || pSource.is(DamageTypes.TRIDENT) || pSource.is(DamageTypes.MOB_PROJECTILE)
                || pSource.is(DamageTypes.SONIC_BOOM);
        boolean slowExplosion = pSource.is(DamageTypes.LAVA) || pSource.is(DamageTypes.HOT_FLOOR) || pSource.is(DamageTypes.ON_FIRE);
        boolean isSourceExplosion =  pSource.is(DamageTypes.PLAYER_EXPLOSION) || pSource.is(DamageTypes.EXPLOSION);
        boolean isBlunderBomb = pSource.getDirectEntity() instanceof BlunderBombEntity;

        if (fastExplosion) {
            explodeKeg();
        } else if (isSourceExplosion && !isBlunderBomb) {
            if (pAmount > 30) {
                setPrepareToBlowUp(getMaxPrepareToBlowUp()-1);
            } else if (pAmount > 16) {
                setPrepareToBlowUp(getMaxPrepareToBlowUp()-20);
            }
            setIsGoingToBlowUp(true);
        } else if (isSourceExplosion && isBlunderBomb) {
            if (pAmount > 9) {
                setPrepareToBlowUp(getMaxPrepareToBlowUp()-1);
            } else if (pAmount > 5) {
                setPrepareToBlowUp(getMaxPrepareToBlowUp()-20);
            }
            setIsGoingToBlowUp(true);
        }
        else if (slowExplosion) {
            setIsGoingToBlowUp(true);
        }
        else if (!isInvulnerableTo(pSource)) {
            this.turnIntoItem();
        }
        return super.hurt(pSource, pAmount);
    }

    public int getMaxPrepareToBlowUp() {
        return 90;
    }

    public float getExplodeRadius() {
        return 3.0f;
    }

    public boolean getIsGoingToBlowUp() {
        return this.getEntityData().get(IS_GOING_TO_BLOW_UP).booleanValue();
    }

    public void setIsGoingToBlowUp(boolean b) {
        this.getEntityData().set(IS_GOING_TO_BLOW_UP, b);
    }

    public int getPrepareToBlowUp() {
        return this.getEntityData().get(PREPARE_TO_BLOW_UP).intValue();
    }

    public void setPrepareToBlowUp(int i) {
        this.getEntityData().set(PREPARE_TO_BLOW_UP, i);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setPrepareToBlowUp(tag.getInt("PrepareToBlowUp"));
        this.setIsGoingToBlowUp(tag.getBoolean("IsGoingToBlowUp"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("PrepareToBlowUp", getPrepareToBlowUp());
        tag.putBoolean("IsGoingToBlowUp", getIsGoingToBlowUp());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(PREPARE_TO_BLOW_UP, 0);
        this.getEntityData().define(IS_GOING_TO_BLOW_UP, false);
    }
}
