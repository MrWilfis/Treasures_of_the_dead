package net.mrwilfis.treasures_of_the_dead.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.Vec3;
import net.mrwilfis.treasures_of_the_dead.Config;
import net.mrwilfis.treasures_of_the_dead.item.custom.AbstractPowderKegItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.CutlassItem;
import net.mrwilfis.treasures_of_the_dead.util.IMixinLivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity implements IMixinLivingEntity {

    @Shadow
    public abstract ItemStack getItemInHand(InteractionHand pHand);

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = false)
    private void hurt(@NotNull DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        ItemStack mainHandItem = this.getItemInHand(InteractionHand.MAIN_HAND);
        if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof AbstractPowderKegItem) {
            boolean fastExplosion = pSource.is(DamageTypes.FALLING_ANVIL) || pSource.is(DamageTypes.FALLING_STALACTITE) || pSource.is(DamageTypes.FIREWORKS) ||
                    pSource.is(DamageTypes.LIGHTNING_BOLT) || pSource.is(DamageTypes.EXPLOSION) || pSource.is(DamageTypes.FIREBALL)
                    || pSource.is(DamageTypes.UNATTRIBUTED_FIREBALL) || pSource.is(DamageTypes.PLAYER_EXPLOSION)
                    || pSource.is(DamageTypes.SONIC_BOOM);

            if (fastExplosion && pAmount > 3.0f) {
                AbstractPowderKegItem item = (AbstractPowderKegItem) mainHandItem.getItem();
                try {
                    item.explodeKeg((LivingEntity) (Object) this, mainHandItem);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    @Shadow
    public abstract boolean isUsingItem();

    @Shadow
    public abstract ItemStack getMainHandItem();

    @Shadow
    public abstract ItemStack getOffhandItem();

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {


        LivingEntity entity = (LivingEntity) (Object) this;

        if (!(entity instanceof Player player)) return;

        LivingEntity attacker = null;
        if (source.getEntity() instanceof LivingEntity living) {
            attacker = living;
        }

        if (attacker == null || attacker == player) return;

        if (source.is(DamageTypes.GENERIC) ||
                source.is(DamageTypes.MOB_ATTACK_NO_AGGRO) ||
                source.is(DamageTypes.MOB_ATTACK) ||
                source.is(DamageTypes.PLAYER_ATTACK)) {

        } else {
            return;
        }


//        if (!hasWeaponInHand(attacker)) {
//            return;
//        }

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        boolean hasCutlass = mainHand.getItem() instanceof CutlassItem ||
                offHand.getItem() instanceof CutlassItem;

        if (!hasCutlass) return;

        if (!player.isUsingItem()) return;

        CutlassItem cutlass = (CutlassItem) (mainHand.getItem() instanceof CutlassItem ? mainHand.getItem() : offHand.getItem());
        if (player.getCooldowns().isOnCooldown(cutlass)) return;

        if (!isAttackerInFront(player, attacker)) {
            return;
        }

        CutlassItem.applyCooldownToAllCutlasses(player, Config.cutlassBlockCooldownTicks);
        //player.getCooldowns().addCooldown(cutlass, 70);
        damageCutlass(player, mainHand, offHand, amount);
        //spawnBlockParticles(player, attacker);
        playBlockSound(player, hasWeaponInHand(attacker));
        knockbackEntity(attacker, player, false);
        knockbackEntity(player, attacker, true);
        cir.setReturnValue(false);
    }

    private boolean hasWeaponInHand(LivingEntity attacker) {
        ItemStack mainHand = attacker.getMainHandItem();
        ItemStack offHand = attacker.getOffhandItem();

        // Проверяем основную руку
        if (isWeaponOrTool(mainHand.getItem())) {
            return true;
        }

        // Проверяем вторую руку
        if (isWeaponOrTool(offHand.getItem())) {
            return true;
        }

        return false;
    }

    private boolean isWeaponOrTool(Item item) {
        if (item == null) return false;

        if (item instanceof SwordItem) return true;
        if (item instanceof DiggerItem) return true;
        if (item instanceof TieredItem) return true;

//        if (item.getDefaultInstance().getAttributeModifiers().modifiers().contains(Attributes.ATTACK_DAMAGE)) {
//            return true;
//        }

        var modifiers = item.getDefaultInstance().getAttributeModifiers().modifiers();
        for (var modifier : modifiers) {

            if (modifier.attribute().value().equals(
                    net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE.value())) {
                return true;
            }
        }

        return false;
    }

    private boolean isAttackerInFront(Player defender, LivingEntity attacker) {
        Vec3 lookDirection = defender.getViewVector(1.0F).normalize();

        Vec3 toAttacker = attacker.position().subtract(defender.position()).normalize();

        double angle = Math.acos(lookDirection.dot(toAttacker));
        double angleDegrees = Math.toDegrees(angle);

        return angleDegrees <= 60.0;
    }

    private void knockbackEntity(LivingEntity entity, LivingEntity source, boolean isDefender) {
        if (entity == null) return;

        double power = isDefender ? Config.cutlassBlockKnockbackDefenderPower : Config.cutlassBlockKnockbackAttackerPower;
        double height = isDefender ? Config.cutlassBlockKnockbackDefenderVerticalPower : Config.cutlassBlockKnockbackAttackerVerticalPower;

        double knockbackResistance = entity.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.KNOCKBACK_RESISTANCE).getValue();

        double resistanceFactor = 1.0 - Math.min(knockbackResistance, 1.0);

        if (resistanceFactor <= 0.0) return;

        Vec3 direction = entity.position().subtract(source.position()).normalize();

        Vec3 knockback = new Vec3(
                direction.x * power * resistanceFactor,
                height * resistanceFactor,
                direction.z * power * resistanceFactor
        );

        entity.setDeltaMovement(entity.getDeltaMovement().add(knockback));
        entity.hurtMarked = true;
        entity.setOnGround(false);
    }

    private void damageCutlass(Player player, ItemStack mainHand, ItemStack offHand, float damageAmount) {
        int durabilityDamage = Math.max(1, Math.round(damageAmount / 2.0f));

        if (mainHand.getItem() instanceof CutlassItem) {
            mainHand.hurtAndBreak(durabilityDamage, player, net.minecraft.world.entity.player.Player.getSlotForHand(InteractionHand.MAIN_HAND));
        }

        if (offHand.getItem() instanceof CutlassItem) {
            offHand.hurtAndBreak(durabilityDamage, player, net.minecraft.world.entity.player.Player.getSlotForHand(InteractionHand.OFF_HAND));
        }
    }

    private void playBlockSound(Player player, boolean isWeaponStrike) {
        if (player == null || player.level() == null) return;

        if (isWeaponStrike) {
            player.level().playSound(null, player.blockPosition(),
                    SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 0.8F, 1.5F + player.level().random.nextFloat() * 0.2F);
            player.level().playSound(null, player.blockPosition(),
                    SoundEvents.SMALL_AMETHYST_BUD_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F + player.level().random.nextFloat() * 0.2F);
            player.level().playSound(null, player.blockPosition(),
                    SoundEvents.TRIDENT_HIT_GROUND, SoundSource.PLAYERS, 0.8F, 1F + player.level().random.nextFloat() * 0.2F);
        } else {
            player.level().playSound(null, player.blockPosition(),
                    SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 0.8F, 1F + player.level().random.nextFloat() * 0.2F);
            player.level().playSound(null, player.blockPosition(),
                    SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 0.8F, 1F + player.level().random.nextFloat() * 0.2F);
        }
    }
}
