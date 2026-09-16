package net.mrwilfis.treasures_of_the_dead.item.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.mrwilfis.treasures_of_the_dead.Config;

import java.util.UUID;

public class CutlassItem extends SwordItem {

    private static final UUID CUTLASS_SPEED_MODIFIER_UUID = UUID.fromString("b2c3d4e5-f6a7-890b-cdef-1234267670ab");
    public static final AttributeModifier CUTLASS_SPEED_MODIFIER = new AttributeModifier(ResourceLocation.parse(CUTLASS_SPEED_MODIFIER_UUID.toString()),5.0D*0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL); // 0.5 to make DEFAULT speed and X to modify

    // Compensation modifier: minecraft has a bug when player moving with RMB and presses WD or WA then they are mowing faster than should. This modifier fixes this.
    private static final UUID COMPENSATION_UUID = UUID.fromString("d20b510e-6788-471a-b13e-fca09900eca7");
    public static final AttributeModifier DIAGONAL_COMPENSATION = new AttributeModifier(ResourceLocation.parse(COMPENSATION_UUID.toString()), -0.293, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
    );

    public CutlassItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return (int)(super.getMaxDamage(stack) * 1.125);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        if (!player.swinging) {
            player.startUsingItem(usedHand);
        }
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (!(livingEntity instanceof Player player)) {
            super.onUseTick(level, livingEntity, stack, remainingUseDuration);
            return;
        }

        if (level.isClientSide()) {
            applySpeedModifier(player);
            handleDiagonalMovement(player);
            if (this.isCanHandleDash()) {
                handleDash(player);
            }
        }

        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
    }

    public boolean isCanHandleDash() {
        return false;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof Player player && level.isClientSide()) {
            removeSpeedModifier(player);
            removeCompensation(player);
        }
        super.releaseUsing(stack, level, livingEntity, timeCharged);
    }

    private void handleDiagonalMovement(Player player) {
        if (!player.isUsingItem() || !player.onGround()) return;

        float forward = player.zza;
        float strafe = player.xxa;

        if (forward != 0 && strafe != 0) {
            applyCompensation(player);
        } else {
            removeCompensation(player);
        }
    }

    private void handleDash(Player player) {
        if (player.onGround() && !player.getCooldowns().isOnCooldown(this)) {
            if (player.isShiftKeyDown()) { // should be SPACE button but there's no needed method.
                FoodData foodData = player.getFoodData();
                int currentFood = foodData.getFoodLevel();

                if (currentFood <= 6) {
                    return;
                }

                boolean isDodging = false;
                Vec3 dashVector = Vec3.ZERO;

                Vec3 lookDirection = player.getLookAngle();

                float forward = player.zza;
                float strafe = player.xxa;

                // (A)
                if (strafe < 0 && forward == 0) {
                    Vec3 leftDirection = new Vec3(-lookDirection.z, 0, lookDirection.x).normalize();
                    dashVector = leftDirection.scale(Config.cutlassDashPower);
                    isDodging = true;
                }
                // (D)
                else if (strafe > 0 && forward == 0) {
                    Vec3 rightDirection = new Vec3(lookDirection.z, 0, -lookDirection.x).normalize();
                    dashVector = rightDirection.scale(Config.cutlassDashPower);
                    isDodging = true;
                }
                // (S)
                else if (forward < 0 && strafe == 0) {
                    Vec3 backDirection = new Vec3(-lookDirection.x, 0, -lookDirection.z).normalize();
                    dashVector = backDirection.scale(Config.cutlassDashPower / 1.75);
                    isDodging = true;
                }
                // (WA)
                else if (forward > 0 && strafe < 0) {
                    Vec3 forwardDir = new Vec3(lookDirection.x, 0, lookDirection.z).normalize();
                    Vec3 leftDir = new Vec3(-lookDirection.z, 0, lookDirection.x).normalize();
                    Vec3 diagonalDir = forwardDir.add(leftDir).normalize();
                    dashVector = diagonalDir.scale(Config.cutlassDashPower);
                    isDodging = true;
                }
                // (WD)
                else if (forward > 0 && strafe > 0) {
                    Vec3 forwardDir = new Vec3(lookDirection.x, 0, lookDirection.z).normalize();
                    Vec3 rightDir = new Vec3(lookDirection.z, 0, -lookDirection.x).normalize();
                    Vec3 diagonalDir = forwardDir.add(rightDir).normalize();
                    dashVector = diagonalDir.scale(Config.cutlassDashPower);
                    isDodging = true;
                }
                // (SA)
                else if (forward < 0 && strafe < 0) {
                    Vec3 backDir = new Vec3(-lookDirection.x, 0, -lookDirection.z).normalize();
                    Vec3 leftDir = new Vec3(-lookDirection.z, 0, lookDirection.x).normalize();
                    Vec3 diagonalDir = backDir.add(leftDir).normalize();
                    dashVector = diagonalDir.scale(Config.cutlassDashPower / 1.75);
                    isDodging = true;
                }
                // (SD)
                else if (forward < 0 && strafe > 0) {
                    Vec3 backDir = new Vec3(-lookDirection.x, 0, -lookDirection.z).normalize();
                    Vec3 rightDir = new Vec3(lookDirection.z, 0, -lookDirection.x).normalize();
                    Vec3 diagonalDir = backDir.add(rightDir).normalize();
                    dashVector = diagonalDir.scale(Config.cutlassDashPower / 1.75);
                    isDodging = true;
                }

                if (isDodging) {
                    dashVector = dashVector.add(0, Config.cutlassDashPower * Config.cutlassVerticalDashMultiplier, 0);
                    player.setDeltaMovement(dashVector);

                    player.setOnGround(false);
                    player.hurtMarked = true;

                    applyCooldownToAllCutlasses(player, Config.cutlassDashCooldownTicks);

                    player.xxa = 0;
                    player.zza = 0;

                }
            }
        }
    }

    public static void applyCooldownToAllCutlasses(Player player, int cooldownTicks) {
        if (player == null) return;

        // Проходим по всему инвентарю
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof CutlassItem) {
                player.getCooldowns().addCooldown(stack.getItem(), cooldownTicks);
            }
        }
    }

    private void applySpeedModifier(Player player) {
        boolean b = player.getAttribute(Attributes.MOVEMENT_SPEED).getModifiers().contains(CUTLASS_SPEED_MODIFIER);
        if (b) return;
        player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(CUTLASS_SPEED_MODIFIER);
    }

    private void removeSpeedModifier(Player player) {
        boolean b = player.getAttribute(Attributes.MOVEMENT_SPEED).getModifiers().contains(CUTLASS_SPEED_MODIFIER);
        if (!b) return;
        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(CUTLASS_SPEED_MODIFIER);
    }

    private void applyCompensation(Player player) {
        boolean b = player.getAttribute(Attributes.MOVEMENT_SPEED).getModifiers().contains(DIAGONAL_COMPENSATION);
        if (b) return;
        player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(DIAGONAL_COMPENSATION);
    }

    private void removeCompensation(Player player) {
        boolean b = player.getAttribute(Attributes.MOVEMENT_SPEED).getModifiers().contains(DIAGONAL_COMPENSATION);
        if (!b) return;
        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(DIAGONAL_COMPENSATION);
    }
}
