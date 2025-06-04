package net.mrwilfis.treasures_of_the_dead.entity.ai.goal;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.mrwilfis.treasures_of_the_dead.entity.custom.TOTDSkeletonEntity;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class AttackWithKegGoal extends Goal {

    private final TOTDSkeletonEntity skeleton;
    private final double speedModifier;
    @Nullable
    private LivingEntity target;

    public AttackWithKegGoal(TOTDSkeletonEntity pSkeleton, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        this.skeleton = pSkeleton;
        this.speedModifier = pSpeedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        ItemStack mainHandItem = this.skeleton.getItemInHand(InteractionHand.MAIN_HAND);

        if (!mainHandItem.isEmpty() && mainHandItem.getItem() == Items.TNT) {
            LivingEntity $$0 = this.skeleton.getTarget();
            return this.skeleton.getIsGoingToBlowUp() || $$0 != null && this.skeleton.distanceToSqr($$0) < 9.0;
        } else {
            return false;
        }

    }

    @Override
    public void start() {
        this.target = this.skeleton.getTarget();
        if (this.target != null) this.skeleton.getNavigation().moveTo(this.target, this.speedModifier);
    }

    @Override
    public void stop() {
    //    this.target = null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.target == null) {
            this.skeleton.setIsGoingToBlowUp(false);
        } else if (this.skeleton.distanceToSqr(this.target) > 49.0) {
            this.skeleton.setIsGoingToBlowUp(false);
        } else if (!this.skeleton.getSensing().hasLineOfSight(this.target)) {
            this.skeleton.setIsGoingToBlowUp(false);
        } else {
            this.skeleton.setIsGoingToBlowUp(true);
        }
    }
}
