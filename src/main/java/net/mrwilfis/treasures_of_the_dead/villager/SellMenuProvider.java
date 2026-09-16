package net.mrwilfis.treasures_of_the_dead.villager;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.mrwilfis.treasures_of_the_dead.screen.TradeMenuFactory;
import org.jetbrains.annotations.Nullable;

public class SellMenuProvider implements MenuProvider {
    private final Villager villager;
    private final String companyId;

    public SellMenuProvider(Villager villager, String companyId) {
        this.villager = villager;
        this.companyId = companyId;
    }

    @Override
    public Component getDisplayName() {
//        return Component.translatable("container." +
//                net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead.MOD_ID +
//                "." + companyId + "_sell");
        return Component.empty();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return TradeMenuFactory.createSellMenu(companyId, containerId, inventory, villager);
    }
}
