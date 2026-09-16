package net.mrwilfis.treasures_of_the_dead.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mrwilfis.treasures_of_the_dead.Config;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;
import net.mrwilfis.treasures_of_the_dead.item.custom.DaggerItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.daggers.IronDaggerItem;

public class IronDaggerEntity extends DaggerEntity{
    public IronDaggerEntity(EntityType<? extends DaggerEntity> entityType, Level level) {
        super(entityType, level);
    }

    public IronDaggerEntity(EntityType<? extends DaggerEntity> entityType, LivingEntity shooter, Level level) {
        super(ModEntities.IRON_DAGGER.get(), shooter, level, new ItemStack(ModItems.IRON_DAGGER.get()), null);
    }

    @Override
    public float getBreakChance() {
        return (float)Config.ironDaggerBreakChance;
    }

    @Override
    public float getDamageMultiplier() {
        return  (float)Config.ironDaggerDamageModifier;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.IRON_DAGGER.get());
    }

    @Override
    public DaggerItem getDaggerItem() {
        return (IronDaggerItem) ModItems.IRON_DAGGER.get();
    }
}
