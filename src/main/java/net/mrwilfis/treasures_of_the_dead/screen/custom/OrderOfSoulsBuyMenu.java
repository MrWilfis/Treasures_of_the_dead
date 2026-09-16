package net.mrwilfis.treasures_of_the_dead.screen.custom;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.mrwilfis.treasures_of_the_dead.common.PlayerReputationData;
import net.mrwilfis.treasures_of_the_dead.screen.TOTDMenuTypes;

public class OrderOfSoulsBuyMenu extends AbstractBuyMenu{
    public OrderOfSoulsBuyMenu(int containerId, Inventory playerInventory, Villager villager) {
        super(TOTDMenuTypes.ORDER_OF_SOULS_BUY_MENU.get(), containerId,
                playerInventory, villager, PlayerReputationData.ORDER_OF_SOULS);
    }
}
