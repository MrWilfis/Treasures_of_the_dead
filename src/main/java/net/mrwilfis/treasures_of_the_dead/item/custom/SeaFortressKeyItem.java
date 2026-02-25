package net.mrwilfis.treasures_of_the_dead.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.mrwilfis.treasures_of_the_dead.common.ModDataComponents;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;

import java.util.List;

public class SeaFortressKeyItem extends Item {
    public SeaFortressKeyItem(Properties properties) {
        super(properties);
    }

    public static ItemStack createViaSeaFortress(BlockPos pos, float value) {
        ItemStack item = new ItemStack(ModItems.SEA_FORTRESS_KEY.get());
        item.set(ModDataComponents.SEA_FORTRESS_COORDINATES, pos);
        item.set(ModDataComponents.LOOT_VALUE, value);
        return item;
    }



    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (stack.get(ModDataComponents.SEA_FORTRESS_COORDINATES) != null) {
            BlockPos pos = stack.get(ModDataComponents.SEA_FORTRESS_COORDINATES);
            tooltipComponents.add(Component.translatable("tooltip.treasures_of_the_dead.sea_fortress_key_fortress_x.tooltip", pos.getX()));
            tooltipComponents.add(Component.translatable("tooltip.treasures_of_the_dead.sea_fortress_key_fortress_z.tooltip", pos.getZ()));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.treasures_of_the_dead.sea_fortress_key_fortress_any.tooltip"));
        }

        if (stack.get(ModDataComponents.LOOT_VALUE) != null) {
            float value = stack.get(ModDataComponents.LOOT_VALUE);
            tooltipComponents.add(Component.translatable("tooltip.treasures_of_the_dead.sea_fortress_key_loot_value.tooltip", String.format("%.2f", value)));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.treasures_of_the_dead.sea_fortress_key_loot_value.tooltip", 20));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
