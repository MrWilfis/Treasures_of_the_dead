package net.mrwilfis.treasures_of_the_dead.item.custom.daggers;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mrwilfis.treasures_of_the_dead.Config;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.DaggerEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.DiamondDaggerEntity;
import net.mrwilfis.treasures_of_the_dead.item.custom.DaggerItem;

public class DiamondDaggerItem extends DaggerItem {
    public DiamondDaggerItem(Properties properties) {
        super(properties);
    }

    @Override
    public DaggerEntity getDaggerEntity(LivingEntity livingEntity, Level level) {
        return new DiamondDaggerEntity(ModEntities.DIAMOND_DAGGER.get(), livingEntity, level);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return Config.diamondDaggerMaxStackSize;
    }

    @Override
    public int getMaxChargeTime() {
        return Config.diamondDaggerMaxChargeTimeTicks;
    }

    @Override
    public int getCooldown() {
        return Config.diamondDaggerCooldownTicks;
    }

    @Override
    public float getMinSpeed() {
        return (float) Config.diamondDaggerMinSpeed;
    }

    @Override
    public float getMaxSpeed() {
        return (float)Config.diamondDaggerMaxSpeed;
    }
}
