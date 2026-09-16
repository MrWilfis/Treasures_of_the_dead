package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mrwilfis.treasures_of_the_dead.Config;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import net.mrwilfis.treasures_of_the_dead.item.custom.DaggerItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.daggers.NetheriteDaggerItem;

public class NetheriteDaggerEntity extends DaggerEntity{
    public NetheriteDaggerEntity(EntityType<? extends DaggerEntity> entityType, Level level) {
        super(entityType, level);
    }

    public NetheriteDaggerEntity(EntityType<? extends DaggerEntity> entityType, LivingEntity shooter, Level level) {
        super(ModEntities.NETHERITE_DAGGER.get(), shooter, level, new ItemStack(ModItems.NETHERITE_DAGGER.get()), null);
    }

    @Override
    public float getBreakChance() {
        return (float) Config.netheriteDaggerBreakChance;
    }

    @Override
    public float getDamageMultiplier() {
        return  (float)Config.netheriteDaggerDamageModifier;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.NETHERITE_DAGGER.get());
    }

    @Override
    public DaggerItem getDaggerItem() {
        return (NetheriteDaggerItem) ModItems.NETHERITE_DAGGER.get();
    }
}
