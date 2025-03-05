package net.mrwilfis.treasures_of_the_dead.item.custom;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;


public class AbstractSkullItem extends Item implements Equipable {

    public AbstractSkullItem(Properties pProperties) {
        super(pProperties);

    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    // Yes, this is empty. Увы.

}
