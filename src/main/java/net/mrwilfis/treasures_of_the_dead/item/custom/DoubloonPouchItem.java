package net.mrwilfis.treasures_of_the_dead.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.mrwilfis.treasures_of_the_dead.common.ModDataComponents;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;

import java.util.List;

public class DoubloonPouchItem extends Item {
    private static final int DOUBLOONS_PER_PILE = 9;
    private static final int MAX_STACK_SIZE = 64;

    public DoubloonPouchItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack pound, Slot slot, ClickAction action, Player player) {
        int doubloonsInPouch = pound.getOrDefault(ModDataComponents.DOUBLOONS_IN_POUCH, 0);
        if (pound.getCount() != 1) return false;
        if (action != ClickAction.SECONDARY) return false;

        ItemStack slotItem = slot.getItem();

        if (slotItem.getItem() instanceof DoubloonPouchItem) return false;


        if (!slotItem.isEmpty() && (slotItem.is(ModItems.DOUBLOON.get()) || slotItem.is(ModItems.DOUBLOON_PILE.get()))) {
            int totalDoubloons = 0;

            if (slotItem.is(ModItems.DOUBLOON.get())) {
                totalDoubloons = slotItem.getCount();
            } else if (slotItem.is(ModItems.DOUBLOON_PILE.get())) {
                totalDoubloons = slotItem.getCount() * DOUBLOONS_PER_PILE;
            }

            slotItem.setCount(0);
            pound.set(ModDataComponents.DOUBLOONS_IN_POUCH, doubloonsInPouch + totalDoubloons);
            this.playRemoveOneSound(player);
            return true;
        }

        // === СЛУЧАЙ 2: В слоте пусто или другие предметы → ВЫТАСКИВАЕМ дублоны ===
        if (slotItem.isEmpty() || !(slotItem.is(ModItems.DOUBLOON.get()) || slotItem.is(ModItems.DOUBLOON_PILE.get()))) {
            if (doubloonsInPouch <= 0) {
                return false;
            }

            // Проверяем, можно ли что-то положить в слот
            int currentCount = slotItem.isEmpty() ? 0 : slotItem.getCount();
            int availableSpace = MAX_STACK_SIZE - currentCount;

            if (availableSpace <= 0) {
                return false; // Слот полон
            }

            // Определяем, что вытаскивать: сначала кучки
            int pilesToAdd = Math.min(availableSpace, doubloonsInPouch / DOUBLOONS_PER_PILE);
            int remainingDoubloons = doubloonsInPouch - (pilesToAdd * DOUBLOONS_PER_PILE);

            if (pilesToAdd > 0) {
                // Выдаем только кучки, остаток остается в мешочке
                pound.set(ModDataComponents.DOUBLOONS_IN_POUCH, remainingDoubloons);

                if (slotItem.isEmpty()) {
                    slot.set(new ItemStack(ModItems.DOUBLOON_PILE.get(), pilesToAdd));
                } else {
                    slotItem.setCount(currentCount + pilesToAdd);
                }

                this.playInsertSound(player);
                return true;
            } else {
                // Если кучек нет, выдаем отдельные дублоны (до 64)
                int singlesToAdd = Math.min(availableSpace, remainingDoubloons);
                if (singlesToAdd <= 0) {
                    return false;
                }

                pound.set(ModDataComponents.DOUBLOONS_IN_POUCH, remainingDoubloons - singlesToAdd);

                if (slotItem.isEmpty()) {
                    slot.set(new ItemStack(ModItems.DOUBLOON.get(), singlesToAdd));
                } else {
                    slotItem.setCount(currentCount + singlesToAdd);
                }

                this.playInsertSound(player);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pound, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (pound.getCount() != 1) {
            return false;
        }

        if (action != ClickAction.SECONDARY) {
            return false;
        }

        if (other.getItem() instanceof DoubloonPouchItem) return false;

        ItemStack cursorStack = player.containerMenu.getCarried();
        int doubloonsInPouch = pound.getOrDefault(ModDataComponents.DOUBLOONS_IN_POUCH, 0);

        // === СЛУЧАЙ 1: В курсоре есть дублоны или кучки → ЗАБИРАЕМ их ===
        if (!cursorStack.isEmpty() && (cursorStack.is(ModItems.DOUBLOON.get()) || cursorStack.is(ModItems.DOUBLOON_PILE.get()))) {
            int totalDoubloons = 0;

            if (cursorStack.is(ModItems.DOUBLOON.get())) {
                totalDoubloons = cursorStack.getCount();
            } else if (cursorStack.is(ModItems.DOUBLOON_PILE.get())) {
                totalDoubloons = cursorStack.getCount() * DOUBLOONS_PER_PILE;
            }

            player.containerMenu.setCarried(ItemStack.EMPTY);
            pound.set(ModDataComponents.DOUBLOONS_IN_POUCH, doubloonsInPouch + totalDoubloons);
            this.playInsertSound(player);
            return true;
        }

        // === СЛУЧАЙ 2: В курсоре пусто → ВЫТАСКИВАЕМ дублоны ===
        if (cursorStack.isEmpty()) {
            if (doubloonsInPouch <= 0) {
                return false;
            }

            // Определяем, что выдать в курсор: сначала кучки
            int pilesToGive = Math.min(MAX_STACK_SIZE, doubloonsInPouch / DOUBLOONS_PER_PILE);
            int remainingDoubloons = doubloonsInPouch - (pilesToGive * DOUBLOONS_PER_PILE);

            if (pilesToGive > 0) {
                // Выдаем кучки в курсор
                pound.set(ModDataComponents.DOUBLOONS_IN_POUCH, remainingDoubloons);
                player.containerMenu.setCarried(new ItemStack(ModItems.DOUBLOON_PILE.get(), pilesToGive));
                this.playRemoveOneSound(player);
                return true;
            } else {
                // Если кучек нет, выдаем отдельные дублоны (до 64)
                int singlesToGive = Math.min(MAX_STACK_SIZE, remainingDoubloons);
                if (singlesToGive <= 0) {
                    return false;
                }
                pound.set(ModDataComponents.DOUBLOONS_IN_POUCH, remainingDoubloons - singlesToGive);
                player.containerMenu.setCarried(new ItemStack(ModItems.DOUBLOON.get(), singlesToGive));
                this.playRemoveOneSound(player);
                return true;
            }
        }

        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack pound = player.getItemInHand(hand);

        if (pound.getCount() != 1) {
            return InteractionResultHolder.fail(pound);
        }

        int doubloonsInPouch = pound.getOrDefault(ModDataComponents.DOUBLOONS_IN_POUCH, 0);

        if (doubloonsInPouch <= 0) {
            return InteractionResultHolder.fail(pound);
        }

        // Определяем, что выдать: сначала кучки
        int pilesToGive = Math.min(MAX_STACK_SIZE, doubloonsInPouch / DOUBLOONS_PER_PILE);
        int remainingDoubloons = doubloonsInPouch - (pilesToGive * DOUBLOONS_PER_PILE);
        int singlesToGive = 0;

        if (!level.isClientSide()) {
            if (pilesToGive > 0) {
                // Выдаем кучки
                pound.set(ModDataComponents.DOUBLOONS_IN_POUCH, remainingDoubloons);
                ItemStack piles = new ItemStack(ModItems.DOUBLOON_PILE.get(), pilesToGive);
                if (!player.getInventory().add(piles)) {
                    player.drop(piles, false);
                }
            } else {
                // Если кучек нет, выдаем отдельные дублоны (до 64)
                singlesToGive = Math.min(MAX_STACK_SIZE, remainingDoubloons);
                if (singlesToGive > 0) {
                    pound.set(ModDataComponents.DOUBLOONS_IN_POUCH, remainingDoubloons - singlesToGive);
                    ItemStack singles = new ItemStack(ModItems.DOUBLOON.get(), singlesToGive);
                    if (!player.getInventory().add(singles)) {
                        player.drop(singles, false);
                    }
                }
            }
        }

        this.playDropContentsSound(player);
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(pound, level.isClientSide());
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        int doubloonsCount = stack.getOrDefault(ModDataComponents.DOUBLOONS_IN_POUCH, 0);

        int piles = doubloonsCount / DOUBLOONS_PER_PILE;
        int singles = doubloonsCount % DOUBLOONS_PER_PILE;

        tooltipComponents.add(Component.translatable("tooltip.treasures_of_the_dead.doubloon_pouch.doubloons_in_pouch", doubloonsCount));
        if (piles > 0 || singles > 0) {
            tooltipComponents.add(Component.translatable("tooltip.treasures_of_the_dead.doubloon_pouch.breakdown", piles, singles));
        }
    }
}