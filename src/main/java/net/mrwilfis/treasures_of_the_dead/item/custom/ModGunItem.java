package net.mrwilfis.treasures_of_the_dead.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Random;
import java.util.function.Predicate;

public class ModGunItem extends ProjectileWeaponItem implements Vanishable {

    private final int bulletCount;
    private final int reloadDuration;

    public ModGunItem(Properties pProperties, int bulletCount, int reloadDuration) {
        super(pProperties);
        this.bulletCount = bulletCount;
        this.reloadDuration = reloadDuration;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 25;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (isCharged(itemstack)) {
            performShooting(level, player, hand, itemstack, 3.15f, 1.0f);
            setCharged(itemstack, false);
            return InteractionResultHolder.consume(itemstack);
        }
        if (!isCharged(itemstack)){
            if (findAmmo(player) == ItemStack.EMPTY && !player.getAbilities().instabuild) {
                return InteractionResultHolder.fail(itemstack);
            }
            setCharged(itemstack, false);
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.fail(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        System.out.println("release using, timeCharged - " + timeCharged);
//        if (isCharged(stack) && livingEntity instanceof Player player){
//            consumeAmmo(player, stack);
//        }

//        setCharged(stack, true);
//        int i = 30 - timeCharged;
//        float f = getPowerForTime(i, stack);
//        if (f >= 1.0f && !isCharged(stack)) {
//            setCharged(stack, true);
//
//
//        }
    }

    @Override
    public boolean useOnRelease(ItemStack pStack) {
        System.out.println("use on release");
        return pStack.is(this);
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseDuration) {
        int usingTicks = getUseDuration(stack) - remainingUseDuration;
        SoundEvent sound = SoundEvents.EXPERIENCE_ORB_PICKUP;
        System.out.println("on use tick " + remainingUseDuration + " " + usingTicks);
        if (usingTicks < getUseDuration(stack)-1) {
            setCharged(stack, false);
        }
        if (usingTicks >= getUseDuration(stack)-1 && !isCharged(stack)) {
            if (!isCharged(stack) && user instanceof Player player){
                consumeAmmo(player, stack);
            }
            setCharged(stack, true);
            level.playSound((Player)null, user.getX(), user.getY(), user.getZ(), sound, SoundSource.PLAYERS, 0.5f, 1.0f);
        }



//        setCharged(stack, true);
//        if (!level.isClientSide) {
//            float f = (float) (stack.getUseDuration() - remainingUseDuration) / (float) 30.0f;
//            if (f < 1.0f) {
//                this.charged = false;
//                setCharged(stack, false);
//            }
//            if (f >= 1.0f && !isCharged(stack)) {
//                this.charged = true;
//                setCharged(stack, true);
//            }
//        }
    }

    public static boolean isCharged(ItemStack gunItem) {
        CompoundTag compoundtag = gunItem.getTag();
        return compoundtag != null && compoundtag.getBoolean("Charged");
    }

    public static void setCharged(ItemStack gunItem, boolean pIsCharged) {
        CompoundTag compoundtag = gunItem.getOrCreateTag();
        compoundtag.putBoolean("Charged", pIsCharged);
    }
    
    public void consumeAmmo(Player player, ItemStack itemStack) {
        if (player.getAbilities().instabuild) return;
        
        ItemStack ammoStack = findAmmo(player);
        ammoStack.shrink(1);
        if (ammoStack.isEmpty()) {
            player.getInventory().removeItem(ammoStack);
        }
    }

    public static ItemStack findAmmo(Player player) {
        ItemStack stack = player.getItemBySlot(EquipmentSlot.OFFHAND);
        if (isAmmo(stack)) return stack;

        stack = player.getItemBySlot(EquipmentSlot.MAINHAND);
        if (isAmmo(stack)) return stack;

        int size = player.getInventory().getContainerSize();
        for (int i = 0; i < size; i++) {
            stack = player.getInventory().getItem(i);
            if (isAmmo(stack)) return stack;
        }
        return ItemStack.EMPTY;
    }

    public static boolean isAmmo(ItemStack stack) {
        return stack.getItem() == ModItems.CARTRIDGE.get();
    }

    public void performShooting(Level level, LivingEntity shooter, InteractionHand hand, ItemStack gunStack, float velocity, float inaccuracy) {

        for (int i = 0; i < this.bulletCount; ++i) {
            boolean flag = shooter instanceof Player && ((Player)shooter).getAbilities().instabuild;
            ItemStack arrow = new ItemStack(Items.ARROW); // изменить потом на ту пулю, которая в оружии.
            if (i == 0) {
                shootProjectile(level, shooter, hand, gunStack, arrow, 1.0f, flag, velocity, inaccuracy, 0.0f);
            } else if (i > 0) {
                Random rand = new Random();
                shootProjectile(level, shooter, hand, gunStack, arrow, 1.0f, flag,
                        velocity, rand.nextFloat(-20.0f, 20.0f), 0.0f); //for blunderbuss
            }

        }

    }

    private static void shootProjectile(Level level, LivingEntity shooter, InteractionHand hand, ItemStack gunItem, ItemStack ammostack, float pSoundPitch,
                                        boolean isCreativeMode, float velocity, float inaccuracy, float projectileAngle) {
        if (!level.isClientSide) {
            Object projectile = getArrow(level, shooter, gunItem, ammostack);
            if (isCreativeMode || projectileAngle != 0.0F) {
                ((AbstractArrow)projectile).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            // for player
            Vec3 vec31 = shooter.getUpVector(1.0F);
            Quaternionf quaternionf = (new Quaternionf()).setAngleAxis((double)(projectileAngle * 0.017453292F), vec31.x, vec31.y, vec31.z);
            Vec3 vec3 = shooter.getViewVector(1.0F);
            Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);
            ((Projectile)projectile).shoot((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), velocity, inaccuracy);


            gunItem.hurtAndBreak(1, shooter, (p_40858_) -> {
                p_40858_.broadcastBreakEvent(hand);
            });

            level.addFreshEntity((Entity)projectile);
            level.playSound((Player)null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, pSoundPitch);
        }

    }

    private static AbstractArrow getArrow(Level pLevel, LivingEntity pLivingEntity, ItemStack pCrossbowStack, ItemStack pAmmoStack) {
        ArrowItem arrowitem = (ArrowItem)(pAmmoStack.getItem() instanceof ArrowItem ? pAmmoStack.getItem() : Items.ARROW);
        AbstractArrow abstractarrow = arrowitem.createArrow(pLevel, pAmmoStack, pLivingEntity);
//        if (pLivingEntity instanceof Player) {
//            abstractarrow.setCritArrow(true); // crit particles
//}

//        abstractarrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
//        abstractarrow.setShotFromCrossbow(true);
//        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, pCrossbowStack);
//        if (i > 0) {
//            abstractarrow.setPierceLevel((byte)i);
//        }

        return abstractarrow;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.CROSSBOW;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return this.reloadDuration;
    }
}
