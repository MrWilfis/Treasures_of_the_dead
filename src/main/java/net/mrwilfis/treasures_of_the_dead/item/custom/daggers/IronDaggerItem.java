package net.mrwilfis.treasures_of_the_dead.item.custom.daggers;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mrwilfis.treasures_of_the_dead.Config;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.DaggerEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.IronDaggerEntity;
import net.mrwilfis.treasures_of_the_dead.item.custom.DaggerItem;

public class IronDaggerItem extends DaggerItem {
    public IronDaggerItem(Properties properties) {
        super(properties);
    }

    @Override
    public DaggerEntity getDaggerEntity(LivingEntity livingEntity, Level level) {
        return new IronDaggerEntity(ModEntities.IRON_DAGGER.get(), livingEntity, level);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return Config.ironDaggerMaxStackSize;
    }

    @Override
    public int getMaxChargeTime() {
        return Config.ironDaggerMaxChargeTimeTicks;
    }

    @Override
    public int getCooldown() {
        return Config.ironDaggerCooldownTicks;
    }

    @Override
    public float getMinSpeed() {
        return (float) Config.ironDaggerMinSpeed;
    }

    @Override
    public float getMaxSpeed() {
        return (float)Config.ironDaggerMaxSpeed;
    }
}
