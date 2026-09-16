package net.mrwilfis.treasures_of_the_dead.screen.custom;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.mrwilfis.treasures_of_the_dead.config.SellConfigManager;

import java.util.UUID;

public abstract class AbstractSellMenu extends AbstractContainerMenu {
    protected final Villager villager;
    protected UUID villagerUUID = null;

    protected final String companyId;
    protected final Container sellSlot = new SimpleContainer(1) {
        @Override
        public void stopOpen(Player player) {
            // Пустой - предотвращаем выпадение предметов
        }
    };

    protected int currentPrice = 0;
    protected int currentExperience = 0;
    protected int currentCount = 0;
    protected int lastSoldExperience = 0;

    public static final int SELL_SLOT_INDEX = 36;

    public AbstractSellMenu(MenuType<?> menuType, int containerId,
                            Inventory playerInventory, Villager villager, String companyId) {
        super(menuType, containerId);
        this.villager = villager;
        this.companyId = companyId;

        if (playerInventory.player != null) {
            SellConfigManager.getInstance().loadConfig(playerInventory.player, companyId);
        }

        if (villager != null) {
            this.villagerUUID = villager.getUUID();
            // Активируем цель торговли при открытии меню
            villager.setTradingPlayer(playerInventory.player);
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        setupSellSlot();
        updateInfo();
    }

    protected void setupSellSlot() {
        this.addSlot(new Slot(sellSlot, 0, 80, 35) {
            @Override
            public void setChanged() {
                super.setChanged();
                updateInfo();
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                updateInfo();
            }

            @Override
            public boolean mayPlace(ItemStack stack) {
                return SellConfigManager.getInstance().canSell(companyId, stack);
            }

            @Override
            public boolean allowModification(Player player) {
                return true;
            }
        });
    }

    protected void updateInfo() {
        ItemStack stack = sellSlot.getItem(0);
        this.currentPrice = SellConfigManager.getInstance().getPrice(companyId, stack);
        this.currentExperience = SellConfigManager.getInstance().getExperienceGain(companyId, stack);
        this.currentCount = stack.getCount();
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public int getCurrentExperience() {
        return currentExperience;
    }

    public int getLastSoldExperience() {
        return lastSoldExperience;
    }

    public int getItemCount() {
        return currentCount;
    }

    public ItemStack getSellItem() {
        return sellSlot.getItem(0);
    }

    public boolean hasItemInSlot() {
        return !sellSlot.getItem(0).isEmpty();
    }

    public String getCompanyId() {
        return companyId;
    }

    public Villager getVillager() {
        return villager;
    }

    public void clearSellSlot() {
        this.sellSlot.clearContent();
        updateInfo();
    }

    protected void addPlayerInventory(Inventory playerInventory) {
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory) {
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        boolean canSell = SellConfigManager.getInstance().canSell(companyId, sourceStack);

        if (index < 36) {
            if (canSell) {
                if (this.sellSlot.getItem(0).isEmpty()) {
                    if (!moveItemStackTo(sourceStack, SELL_SLOT_INDEX, SELL_SLOT_INDEX + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY;
            }
        } else if (index == SELL_SLOT_INDEX) {
            if (!moveItemStackTo(sourceStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }

        sourceSlot.onTake(player, sourceStack);
        updateInfo();
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.villager != null && this.villager.isAlive()
                && player.distanceToSqr(this.villager) <= 16.0D;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        if (villager != null) {
            villager.setTradingPlayer(null);
        }

        if (!this.sellSlot.getItem(0).isEmpty()) {
            ItemStack stack = this.sellSlot.getItem(0);
            if (!player.addItem(stack)) {
                player.drop(stack, false);
            }
            this.sellSlot.clearContent();
        }
    }
}
