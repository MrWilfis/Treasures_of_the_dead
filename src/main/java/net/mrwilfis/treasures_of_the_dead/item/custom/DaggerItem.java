package net.mrwilfis.treasures_of_the_dead.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.DaggerEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.IronDaggerEntity;

public class DaggerItem extends Item implements ProjectileItem {
    public DaggerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        player.startUsingItem(usedHand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (!level.isClientSide) {

            int chargeTime = getUseDuration(stack, livingEntity) - timeCharged;
            float chargePercent = (float) Math.min(chargeTime, getMaxChargeTime()) / getMaxChargeTime(); //float chargePercent = Math.min(chargeTime, 20) / 20.0f;

            if (chargeTime > 0) {
                DaggerEntity dagger = getDaggerEntity(livingEntity, level);

                float speed = getMinSpeed() + chargePercent * (getMaxSpeed() - getMinSpeed()); //float speed = 0.5F + chargePercent * 0.75F;

                dagger.shootFromRotation(livingEntity, livingEntity.getXRot(), livingEntity.getYRot(), 0.0F, speed, 1.0F);
                dagger.setXRot(livingEntity.xRotO);
                level.addFreshEntity(dagger);

                if (livingEntity instanceof Player player) {
                    ((Player) livingEntity).awardStat(Stats.ITEM_USED.get(this));
                    player.getCooldowns().addCooldown(this, getCooldown());

                    if (!((Player) livingEntity).isCreative()) {
                        stack.shrink(1);
                    }
                }
            }

        }
    }

    public DaggerEntity getDaggerEntity(LivingEntity livingEntity, Level level) {
        return new IronDaggerEntity(ModEntities.IRON_DAGGER.get(), livingEntity, level);
    }

    public int getMaxChargeTime() {
        return 16;
    }

    public int getCooldown() {
        return 6;
    }

    public float getMinSpeed() {
        return 0.125F;
    }

    public float getMaxSpeed() {
        return 2.375F;
    }

    public static ItemAttributeModifiers createAttributes(Tier tier, int attackDamage, float attackSpeed) {
        return createAttributes(tier, (float)attackDamage, attackSpeed);
    }

    public static ItemAttributeModifiers createAttributes(Tier tier, float attackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (double)(attackDamage + tier.getAttackDamageBonus()) * 0.75, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, (double)attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {

    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        return null;
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }
}
