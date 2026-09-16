package net.mrwilfis.treasures_of_the_dead.item.custom;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class CaptainCutlassItem extends CutlassItem{
    public CaptainCutlassItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean isCanHandleDash() {
        return true;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return (int)(super.getMaxDamage(stack) * 1.05);
    }
}
