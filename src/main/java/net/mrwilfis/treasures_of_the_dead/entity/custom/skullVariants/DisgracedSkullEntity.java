package net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.mrwilfis.treasures_of_the_dead.entity.custom.AbstractSkullEntity;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;

public class DisgracedSkullEntity extends AbstractSkullEntity{

    public DisgracedSkullEntity(EntityType pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public ItemStack getTreasureItem() {
        return new ItemStack(ModItems.DISGRACED_SKULL_ITEM.get());

    }

}
