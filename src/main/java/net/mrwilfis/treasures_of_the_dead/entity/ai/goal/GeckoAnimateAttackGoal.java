package net.mrwilfis.treasures_of_the_dead.entity.ai.goal;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class GeckoAnimateAttackGoal extends MeleeAttackGoal {
    protected boolean attack;
    public int attackTicks;
    protected final int actionPoint;
    protected final int attackLength;
    protected final String animationName;

    public GeckoAnimateAttackGoal(PathfinderMob attacker, double speed,
                                  int actionPoint, int attackLength,
                                  String animationName) {
        super(attacker, speed, false);
        this.actionPoint = actionPoint;
        this.attackLength = attackLength;
        this.animationName = animationName;
    }

    @Override
    public void start() {
        super.start();
        this.attackTicks = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.attack = false;
        this.mob.setAggressive(false);
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity livingentity = this.mob.getTarget();
        return super.canContinueToUse() || (livingentity != null && livingentity.isAlive() && this.attack);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target) {
        if (this.isTimeToAttack()) {
            if (this.canPerformAttack(target)) {
                this.doAttack(target);
            }
        } else if (this.attackTicks >= this.attackLength) {
            this.resetAttackCooldown();
        } else if (!this.attack) {
            if (!this.canPerformAttack(target)) {
                this.resetAttackCooldown();
            } else {
                this.attack = true;
                this.doTheAnimation();
            }
        }

        if (this.attack) {
            this.attackTicks = Mth.clamp(this.attackTicks + 1, 0, this.attackLength);
        } else {
            this.attackTicks = 0;
        }
    }

    protected void doAttack(LivingEntity living) {
        this.mob.doHurtTarget(living);
    }

    protected boolean canPerformAttack(LivingEntity target) {
        return this.mob.isWithinMeleeAttackRange(target) &&
                this.mob.getSensing().hasLineOfSight(target);
    }

    protected void doTheAnimation() {
        this.mob.level().broadcastEntityEvent(this.mob, (byte) 110);
        this.mob.playSound(SoundEvents.SHULKER_SHOOT, 1.0F, 1.0F); // placeholder
    }

    protected void resetAttackCooldown() {
        this.attackTicks = 0;
        this.attack = false;
    }

    protected boolean isTimeToAttack() {
        return this.attackTicks == this.actionPoint;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}