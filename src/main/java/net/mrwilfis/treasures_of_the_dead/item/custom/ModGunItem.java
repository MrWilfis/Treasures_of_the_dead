package net.mrwilfis.treasures_of_the_dead.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.mrwilfis.treasures_of_the_dead.common.ModDataComponents;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;

public class ModGunItem extends Item{

    private final int bulletCount;
    private final int maxReloadingTime;

    public static ItemStack activeMainHandStack;
    public static ItemStack activeOffhandStack;

    public ModGunItem(Properties pProperties, int bulletCount, int maxReloadingTIme) {
        super(pProperties);
        this.bulletCount = bulletCount;
        this.maxReloadingTime = maxReloadingTIme;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack gun = player.getItemInHand(usedHand);
        if (getCharged(gun)) {
        //    shoot();
            player.playSound(SoundEvents.ARROW_SHOOT);
            setCharged(gun, false);
            return InteractionResultHolder.consume(gun);
        } else if (true){
            player.startUsingItem(usedHand);
            setCharged(gun, false);
            return InteractionResultHolder.consume(gun);
        } else {
            setCharged(gun, false);
            return InteractionResultHolder.fail(gun);
        }


    //    return super.use(level, player, usedHand);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int howMuchTimeItIsAlreadySet) {
        if (!level.isClientSide) {
            System.out.println("release using, timeset - " + howMuchTimeItIsAlreadySet);
            if (howMuchTimeItIsAlreadySet <= 0) {
                setCharged(stack, true);
            }
        }

    //    super.releaseUsing(stack, level, livingEntity, howMuchTimeItIsAlreadySet);
    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        System.out.println("use one release");
        return stack.is(this);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (!level.isClientSide) {
            System.out.println("on use tick, remaining use duration - " + remainingUseDuration);
        }
        int usingTicks = getUseDuration(stack, livingEntity) - remainingUseDuration;
        if (usingTicks >= getUseDuration(stack, livingEntity) && !getCharged(stack)) {
            level.playSound((Player)null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0f, 0.5f);

        }


//        if (!level.isClientSide) {
//            int usingTicks = getUseDuration(stack, livingEntity) - remainingUseDuration;
//
//        }
//
//
//
//        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
    }

    public boolean canUseFrom(LivingEntity entity, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            return true;
        }
        if (twoHanded()) {
            return false;
        }

        ItemStack stack = entity.getMainHandItem();
        if (!stack.isEmpty() && stack.getItem() instanceof ModGunItem gunItem) {
            return !gunItem.twoHanded();
        }
        return true;
    }

    public static ItemStack findAmmo(Player player) {
        //checking left hand
        ItemStack stack = player.getItemBySlot(EquipmentSlot.OFFHAND);
        if (isAmmo(stack)) return stack;

        //checking right hand
        stack = player.getItemBySlot(EquipmentSlot.MAINHAND);
        if (isAmmo(stack)) return stack;

        //checking inventory
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

    public static ItemStack getActiveStack(InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            return activeMainHandStack;
        } else {
            return activeOffhandStack;
        }
    }

    public static void setActiveStack(InteractionHand hand, ItemStack stack) {
        if (hand == InteractionHand.MAIN_HAND) {
            activeMainHandStack = stack;
        } else {
            activeOffhandStack = stack;
        }
    }

    public boolean twoHanded() {
        return false;
    }

    public static boolean getCharged(ItemStack gun) {
        Boolean charged = gun.get(ModDataComponents.GUN_IS_CHARGED);
        return charged != null ? charged : false;
    }

    public static void setCharged(ItemStack gun, boolean b) {
        gun.set(ModDataComponents.GUN_IS_CHARGED, b);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CROSSBOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return this.maxReloadingTime;
    }
}
