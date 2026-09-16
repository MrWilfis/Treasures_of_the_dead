package net.mrwilfis.treasures_of_the_dead.screen.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.mrwilfis.treasures_of_the_dead.TOTDUtils;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.common.ModDataComponents;
import net.mrwilfis.treasures_of_the_dead.common.PlayerReputationData;
import net.mrwilfis.treasures_of_the_dead.config.BuyConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class AbstractBuyMenu extends AbstractContainerMenu {
    protected final Villager villager;
    protected UUID villagerUUID = null;
    protected final String companyId;

    protected final Container shopContainer = new SimpleContainer(27) {
        @Override
        public void setItem(int slot, ItemStack stack) {
            super.setItem(slot, stack);
        }
    };

    protected List<TOTDUtils.ShopOfferEntry> currentOffers = new ArrayList<>();

    public static final int ROWS = 3;
    public static final int COLUMNS = 9;
    public static final int ITEM_SLOT_COUNT = ROWS * COLUMNS;

    public static final int PLAYER_INVENTORY_START = 0;
    public static final int PLAYER_HOTBAR_START = 27;
    public static final int ITEMS_START_INDEX = 36;

    public AbstractBuyMenu(MenuType<?> menuType, int containerId,
                           Inventory playerInventory, Villager villager, String companyId) {
        super(menuType, containerId);
        this.villager = villager;
        this.companyId = companyId;

        if (villager != null) {
            this.villagerUUID = villager.getUUID();
            // Активируем цель торговли при открытии меню
            villager.setTradingPlayer(playerInventory.player);
        }

        if (playerInventory.player != null) {
            BuyConfigManager.getInstance().loadConfig(playerInventory.player, companyId);
        }

        setupItemGrid();
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        if (playerInventory.player != null) {
            updateOffers(playerInventory.player);
        }
    }

    protected void setupItemGrid() {
        int startX = 8;
        int startY = 18;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                int slotIndex = row * COLUMNS + col;
                int x = startX + col * 18;
                int y = startY + row * 18;

                // Временно создаем пустые слоты
                this.addSlot(new Slot(shopContainer, slotIndex, x, y) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false;
                    }

                    @Override
                    public boolean mayPickup(Player player) {
                        return false;
                    }
                });
            }
        }
    }

    protected ItemStack createItemStackWithEnchantmentsAndComponents(TOTDUtils.ShopOfferEntry offer) {
        ItemStack stack = TOTDUtils.getItemStackFromString(offer.itemId, offer.count);
        if (stack.isEmpty()) return ItemStack.EMPTY;

        if (offer.hasEnchantments()) {
            applyEnchantments(stack, offer.enchantments);
        }
        if (offer.hasComponents()) {
            applyComponents(stack, offer.components);
        }

        return stack;
    }

    protected void applyEnchantments(ItemStack stack, Map<String, Integer> enchantments) {
        if (stack.isEmpty() || enchantments.isEmpty()) return;

        try {
            var level = villager != null ? villager.level() : null;
            if (level == null) {
                //Treasures_of_the_dead.LOGGER.warn("Cannot apply enchantments: level is null");
                return;
            }

            var enchantmentRegistry = level.registryAccess()
                    .registry(Registries.ENCHANTMENT)
                    .orElse(null);

            if (enchantmentRegistry == null) {
                Treasures_of_the_dead.LOGGER.warn("Enchantment registry not found");
                return;
            }

            ItemEnchantments.Mutable builder = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);

            for (Map.Entry<String, Integer> entry : enchantments.entrySet()) {
                String enchantmentId = entry.getKey();
                int level1 = entry.getValue();

                ResourceLocation location = ResourceLocation.parse(enchantmentId);

                var enchantmentHolder = enchantmentRegistry.getHolder(location);

                if (enchantmentHolder.isPresent()) {
                    builder.set(enchantmentHolder.get(), level1);
                } else {
                    Treasures_of_the_dead.LOGGER.warn("Enchantment not found: {}", enchantmentId);
                }
            }

            stack.set(DataComponents.ENCHANTMENTS, builder.toImmutable());

        } catch (Exception e) {
            Treasures_of_the_dead.LOGGER.error("Error applying enchantments: {}", e.getMessage(), e);
        }
    }

    protected void applyComponents(ItemStack stack, Map<String, Object> components) {
        if (stack.isEmpty() || components.isEmpty()) return;

        try {
            for (Map.Entry<String, Object> entry : components.entrySet()) {
                String componentId = entry.getKey();
                Object value = entry.getValue();

                switch (componentId) {
                    case "minecraft:custom_name" -> {
                        String name = value.toString().replaceAll("^\"|\"$", "");
                        stack.set(DataComponents.CUSTOM_NAME,
                                net.minecraft.network.chat.Component.literal(name));
                    }

                    case "minecraft:lore" -> {
                        List<net.minecraft.network.chat.Component> loreComponents = new ArrayList<>();

                        if (value instanceof List<?> list) {
                            for (Object item : list) {
                                String line = item.toString().replaceAll("^\"|\"$", "");
                                loreComponents.add(net.minecraft.network.chat.Component.literal(line));
                            }
                        } else if (value instanceof String stringValue) {
                            String line = stringValue.replaceAll("^\"|\"$", "");
                            loreComponents.add(net.minecraft.network.chat.Component.literal(line));
                        }

                        if (!loreComponents.isEmpty()) {
                            stack.set(DataComponents.LORE,
                                    new net.minecraft.world.item.component.ItemLore(loreComponents));
                        }
                    }

                    case "treasures_of_the_dead:difficulty" -> {
                        if (value instanceof Number number) {
                            int difficulty = number.intValue();
                            stack.set(ModDataComponents.DIFFICULTY, difficulty);
                            //Treasures_of_the_dead.LOGGER.debug("Applied difficulty: {}", difficulty);
                        }
                    }

                    default -> {
                        Treasures_of_the_dead.LOGGER.debug("Unknown component: {}", componentId);
                    }
                }
            }
        } catch (Exception e) {
            Treasures_of_the_dead.LOGGER.error("Error applying components: {}", e.getMessage(), e);
        }
    }

    protected boolean isItemAvailableForPlayer(Player player, ItemStack stack) {
        if (player == null) return false;

        int playerLevel = PlayerReputationData.getLevel(player, companyId);

        for (TOTDUtils.ShopOfferEntry offer : currentOffers) {
            if (offer.itemId.equals(getItemId(stack))) {
                return offer.minLevel <= playerLevel;
            }
        }
        return false;
    }

    protected String getItemId(ItemStack stack) {
        return net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
    }

    public void updateOffers(Player player) {
        if (player == null) return;

        // Очищаем контейнер
        for (int i = 0; i < shopContainer.getContainerSize(); i++) {
            shopContainer.setItem(i, ItemStack.EMPTY);
        }

        // Получаем доступные предложения
        currentOffers = BuyConfigManager.getInstance().getAvailableOffers(player, companyId);

        // Заполняем слоты
        int slotIndex = 0;
        for (TOTDUtils.ShopOfferEntry offer : currentOffers) {
            if (slotIndex >= ITEM_SLOT_COUNT) break;

            // Создаем предмет с зачарованиями
            ItemStack stack = createItemStackWithEnchantmentsAndComponents(offer);
            if (!stack.isEmpty()) {
                shopContainer.setItem(slotIndex, stack);
                slotIndex++;
            }
        }

        //Treasures_of_the_dead.LOGGER.debug("Updated shop offers: {} items displayed", slotIndex);
    }

    protected void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9,
                        8 + col * 18, 84 + row * 18));
            }
        }
    }

    protected void addPlayerHotbar(Inventory playerInventory) {
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    public String getCompanyId() {
        return companyId;
    }

    public Villager getVillager() {
        return villager;
    }

    public Container getShopContainer() {
        return shopContainer;
    }

    public List<TOTDUtils.ShopOfferEntry> getCurrentOffers() {
        return currentOffers;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.villager != null && this.villager.isAlive()
                && player.distanceToSqr(this.villager) <= 16.0D;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // Базовый быстрый перенос - просто перемещаем между инвентарями
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack sourceStack = slot.getItem();
        ItemStack copyStack = sourceStack.copy();

        // Если слот из инвентаря игрока (0-35) -> пытаемся переместить в сетку предметов
        if (index < 36) {
            if (!this.moveItemStackTo(sourceStack, ITEMS_START_INDEX, this.slots.size(), false)) {
                return ItemStack.EMPTY;
            }
        }
        // Если слот из сетки предметов -> пытаемся переместить в инвентарь игрока
        else if (index >= ITEMS_START_INDEX) {
            if (!this.moveItemStackTo(sourceStack, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (sourceStack.getCount() == 0) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        slot.onTake(player, sourceStack);
        return copyStack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        if (villager != null) {
            villager.setTradingPlayer(null);
        }
    }
}
