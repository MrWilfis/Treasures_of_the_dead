package net.mrwilfis.treasures_of_the_dead.item.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mrwilfis.treasures_of_the_dead.common.ModDataComponents;

public class AbstractPowderKegItem extends Item {

    public int maxPrepareToBlowUp;
    public float explodeRadius;

    public AbstractPowderKegItem(Properties pProperties) {
        super(pProperties);
        this.maxPrepareToBlowUp = this.getMaxPrepareToBlowUp();
        this.explodeRadius = this.getExplodeRadius();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack kegItem = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide) {

            boolean b = getIsGoingToBlowUp(kegItem);

            if (!b) {
                setIsGoingToBlowUp(kegItem, true);
            } else {
                setIsGoingToBlowUp(kegItem, false);
            }
        }


        return InteractionResultHolder.pass(kegItem);
    }

    @Override
    public void inventoryTick(ItemStack kegItem, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {

        if (!pLevel.isClientSide) {
            if (!pIsSelected) {
                if (getIsGoingToBlowUp(kegItem)) {
                    //setPrepareToBlowUp(kegItem, 0);
                    setIsGoingToBlowUp(kegItem, false);
                //    prepareToBlowUp = 0;
                    setPrepareToBlowUp(kegItem, 0);
                }
            } else {
                boolean b = getIsGoingToBlowUp(kegItem);
            //    int i = prepareToBlowUp;
                int i = getPrepareToBlowUp(kegItem);

                //Add ticks
                if (b) {
                //    prepareToBlowUp = i+1;
                    setPrepareToBlowUp(kegItem, i+1);
                } else {
                    if (i != 0) setPrepareToBlowUp(kegItem, 0);
                }

                //Explode
                if (i >= this.maxPrepareToBlowUp) {
                    explodeKeg((LivingEntity) pEntity, kegItem);
                }

                //Play sound
                if (b && i == 0) {
                    pLevel.playSound((Player) null, pEntity.getX(), pEntity.getY(), pEntity.getZ(), SoundEvents.TNT_PRIMED, SoundSource.PLAYERS, 1.0F, 0.5F);
                }
            }
        }
        //Add particles
        if (getIsGoingToBlowUp(kegItem)) {
            createFuseParticles(pLevel, pLevel.random, pEntity);
        }

        super.inventoryTick(kegItem, pLevel, pEntity, pSlotId, pIsSelected);
    }

    public void explodeKeg(LivingEntity livingEntity, ItemStack kegItem) {
        if (!livingEntity.level().isClientSide) {
            kegItem.shrink(1);
            livingEntity.level().explode(livingEntity, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), explodeRadius, Level.ExplosionInteraction.MOB);
            setPrepareToBlowUp(kegItem, 0);
            setIsGoingToBlowUp(kegItem, false);
            livingEntity.hurt(livingEntity.damageSources().explosion(livingEntity, livingEntity), 50f);
        }
    }

    protected void createFuseParticles(Level level, RandomSource random, Entity entity) {

        Vec3 entityPos = entity.position();

        float yaw = entity.getYRot();
        double radians = Math.toRadians(yaw);

        Vec3 bodyDirection = new Vec3(-Math.sin(radians), 0, Math.cos(radians)).normalize();

        double distance = 0.35;

        Vec3 particlePosition = entityPos.add(bodyDirection.scale(distance));

        double xOffset = random.nextDouble() * 0.2 - 0.1; // Случайный смещение по X
        double yOffset = random.nextDouble() * 0.2 - 0.1; // Случайный смещение по Y
        double zOffset = random.nextDouble() * 0.2 - 0.1; // Случайный смещение по Z

        double xSpeed = random.nextDouble() * 0.04 - 0.02; // Случайная скорость по X от -0.2 до 0.2
        double ySpeed = (random.nextDouble() * 0.06) + 0.03; // Случайная скорость по Y от -0.2 до 0.2
        double zSpeed = (random.nextDouble() * 0.04) - 0.02; // Случайная скорость по Z от -0.2 до 0.2

        level.addParticle(ParticleTypes.SMOKE,
                particlePosition.x + xOffset,
                particlePosition.y + yOffset + 0.6 * entity.getBbHeight(),
                particlePosition.z + zOffset,
                xSpeed, ySpeed, zSpeed); // Скорость (0, 0, 0)
    }

    private float getExplodeRadius() {
        return 3.0f;
    }

    private int getMaxPrepareToBlowUp() {
        return 90;
    }

    public static boolean getIsGoingToBlowUp(ItemStack kegItem) {
//        CompoundTag compoundtag = kegItem.getTag();
//        return compoundtag != null && compoundtag.getBoolean("IsGoingToBlowUp");
        if (kegItem.get(ModDataComponents.KEG_IS_GOING_TO_BLOW_UP) == null) {
            return false;
        } else {
            return kegItem.get(ModDataComponents.KEG_IS_GOING_TO_BLOW_UP).booleanValue();
        }
    }

    public static void setIsGoingToBlowUp(ItemStack kegItem, boolean b) {
//        CompoundTag compoundtag = kegItem.getOrCreateTag();
//        compoundtag.putBoolean("IsGoingToBlowUp", b);
        kegItem.set(ModDataComponents.KEG_IS_GOING_TO_BLOW_UP, b);
    }
    public static int getPrepareToBlowUp(ItemStack kegItem) {
//        CompoundTag compoundtag = kegItem.getTag();
//        if (compoundtag != null) return compoundtag.getInt("PrepareToBlowUp");
//        else return 0;
        if (kegItem.get(ModDataComponents.KEG_PREPARE_TO_BLOW_UP) == null) {
            return 0;
        } else {
            return kegItem.get(ModDataComponents.KEG_PREPARE_TO_BLOW_UP).intValue();
        }
    }

    public static void setPrepareToBlowUp(ItemStack kegItem, int i) {
//        CompoundTag compoundtag = kegItem.getOrCreateTag();
//        compoundtag.putInt("PrepareToBlowUp", i);
        kegItem.set(ModDataComponents.KEG_PREPARE_TO_BLOW_UP, i);
    }
}
