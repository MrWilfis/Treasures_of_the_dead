package net.mrwilfis.treasures_of_the_dead.villager;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.mrwilfis.treasures_of_the_dead.screen.custom.SkullMerchantMenu;
import org.jetbrains.annotations.Nullable;

public class VillagerMenuProvider implements MenuProvider {
    private final Villager villager;

    public VillagerMenuProvider(Villager villager) {
        this.villager = villager;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("ГАВМНОООНОО");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new SkullMerchantMenu(i, inventory, villager);
    }
}
