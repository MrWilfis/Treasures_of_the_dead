package net.mrwilfis.treasures_of_the_dead.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.mrwilfis.treasures_of_the_dead.item.custom.AbstractPowderKegItem;
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
    //    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
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
}
