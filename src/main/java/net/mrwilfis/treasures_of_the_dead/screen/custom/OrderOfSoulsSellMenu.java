package net.mrwilfis.treasures_of_the_dead.screen.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.mrwilfis.treasures_of_the_dead.common.PlayerReputationData;
import net.mrwilfis.treasures_of_the_dead.screen.TOTDMenuTypes;

public class OrderOfSoulsSellMenu extends AbstractSellMenu{
    // Конструктор для сервера (с Villager)
    public OrderOfSoulsSellMenu(int containerId, Inventory playerInventory, Villager villager) {
        super(TOTDMenuTypes.ORDER_OF_SOULS_SELL_MENU.get(), containerId,
                playerInventory, villager, PlayerReputationData.ORDER_OF_SOULS);
    }

    // Конструктор для клиента (без Villager, с FriendlyByteBuf)
    public OrderOfSoulsSellMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, (Villager) null);
    }

    // Конструктор для клиента (без Villager)
    public OrderOfSoulsSellMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, (Villager) null);
    }
}
