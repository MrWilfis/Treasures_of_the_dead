package net.mrwilfis.treasures_of_the_dead.item.custom.daggers;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mrwilfis.treasures_of_the_dead.Config;
import net.mrwilfis.treasures_of_the_dead.entity.ModEntities;
import net.mrwilfis.treasures_of_the_dead.entity.custom.DaggerEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.NetheriteDaggerEntity;
import net.mrwilfis.treasures_of_the_dead.item.custom.DaggerItem;

public class NetheriteDaggerItem extends DaggerItem {
    public NetheriteDaggerItem(Properties properties) {
        super(properties);
    }

    @Override
    public DaggerEntity getDaggerEntity(LivingEntity livingEntity, Level level) {
        return new NetheriteDaggerEntity(ModEntities.NETHERITE_DAGGER.get(), livingEntity, level);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return Config.netheriteDaggerMaxStackSize;
    }

    @Override
    public int getMaxChargeTime() {
        return Config.netheriteDaggerMaxChargeTimeTicks;
    }

    @Override
    public int getCooldown() {
        return Config.netheriteDaggerCooldownTicks;
    }

    @Override
    public float getMinSpeed() {
        return (float) Config.netheriteDaggerMinSpeed;
    }

    @Override
    public float getMaxSpeed() {
        return (float)Config.netheriteDaggerMaxSpeed;
    }
}
