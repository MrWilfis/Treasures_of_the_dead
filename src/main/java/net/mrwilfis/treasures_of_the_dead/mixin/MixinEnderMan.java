package net.mrwilfis.treasures_of_the_dead.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.ItemStack;
import net.mrwilfis.treasures_of_the_dead.entity.custom.BlunderBombEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderMan.class)
public abstract class MixinEnderMan {

//    @Shadow
//    public abstract void teleport();
//
//    @Inject(method = "hurt", at = @At("HEAD"), cancellable = false)
//    private boolean hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
//        boolean totd_flag = source.getDirectEntity() instanceof BlunderBombEntity;
//        if (totd_flag) {
//            this.teleport();
//            System.out.println("lalalalaalaalalal");
//            return false;
//        }
//        return cir.getReturnValue();
//    }

}
