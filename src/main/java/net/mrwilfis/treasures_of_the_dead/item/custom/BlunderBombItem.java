package net.mrwilfis.treasures_of_the_dead.item.custom;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.BlunderBombEntity;
import net.mrwilfis.treasures_of_the_dead.item.client.BlunderBombItemRenderer;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.function.Consumer;

public class BlunderBombItem extends Item implements GeoItem, GeoAnimatable, ProjectileItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public BlunderBombItem(Properties properties) {
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
            float chargePercent = Math.min(chargeTime, 20) / 20.0f;

            if (chargeTime > 0) {
                BlunderBombEntity blunderBomb = new BlunderBombEntity(ModEntities.BLUNDER_BOMB.get(), livingEntity, level);

                float speed = 0.5F + chargePercent * 0.75F;

                blunderBomb.shootFromRotation(livingEntity, livingEntity.getXRot(), livingEntity.getYRot(), 0.0F, speed, 1.0F);
                level.addFreshEntity(blunderBomb);

                if (livingEntity instanceof Player) {
                    ((Player) livingEntity).awardStat(Stats.ITEM_USED.get(this));
                    level.playSound((Player) livingEntity, ((Player) livingEntity).getOnPos(), SoundEvents.SPLASH_POTION_THROW, SoundSource.AMBIENT, 1.0f, 1.0f);

                    if (!((Player) livingEntity).isCreative()) {
                        stack.shrink(1);
                    }
                }
            }

        }
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
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private BlunderBombItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new BlunderBombItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        BlunderBombEntity bomb = new BlunderBombEntity(level, pos.x(), pos.y(), pos.z());
        return bomb;
    }
}
