package net.mrwilfis.treasures_of_the_dead.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.mrwilfis.treasures_of_the_dead.common.ModDataComponents;
import net.mrwilfis.treasures_of_the_dead.item.ModItems;

public class DoubloonUtils {

    public static final int DOUBLOONS_PER_PILE = 9;

    /**
     * Подсчитывает общее количество дублонов у игрока
     * Учитывает: отдельные дублоны, кучки дублонов и мешочки
     */
    public static int countDoubloons(Player player) {
        if (player == null) return 0;

        int total = 0;

        for (ItemStack stack : player.getInventory().items) {
            if (stack.isEmpty()) continue;

            if (stack.is(ModItems.DOUBLOON.get())) {
                total += stack.getCount();
            } else if (stack.is(ModItems.DOUBLOON_PILE.get())) {
                total += stack.getCount() * DOUBLOONS_PER_PILE;
            } else if (stack.is(ModItems.DOUBLOON_POUCH.get())) {
                total += stack.getOrDefault(ModDataComponents.DOUBLOONS_IN_POUCH, 0);
            }
        }

        return total;
    }

    /**
     * Забирает указанное количество дублонов из инвентаря игрока
     * ПРИОРИТЕТ: мешочек -> отдельные дублоны -> кучки
     * @return true если удалось забрать все, false если не хватило
     */
    public static boolean removeDoubloons(Player player, int amount) {
        if (player == null || amount <= 0) return true;

        // Проверяем, хватает ли дублонов
        int totalDoubloons = countDoubloons(player);
        if (totalDoubloons < amount) {
            return false;
        }

        int remaining = amount;

        // 1. СНАЧАЛА забираем из мешочков
        for (ItemStack stack : player.getInventory().items) {
            if (remaining <= 0) break;
            if (stack.is(ModItems.DOUBLOON_POUCH.get())) {
                int pouchAmount = stack.getOrDefault(ModDataComponents.DOUBLOONS_IN_POUCH, 0);
                if (pouchAmount > 0) {
                    int toRemove = Math.min(pouchAmount, remaining);
                    stack.set(ModDataComponents.DOUBLOONS_IN_POUCH, pouchAmount - toRemove);
                    remaining -= toRemove;
                }
            }
        }

        // 2. ПОТОМ забираем отдельные дублоны
        for (ItemStack stack : player.getInventory().items) {
            if (remaining <= 0) break;
            if (stack.is(ModItems.DOUBLOON.get())) {
                int toRemove = Math.min(stack.getCount(), remaining);
                stack.shrink(toRemove);
                remaining -= toRemove;
            }
        }

        // 3. ПОТОМ забираем из кучек (с возвратом сдачи)
        for (ItemStack stack : player.getInventory().items) {
            if (remaining <= 0) break;
            if (stack.is(ModItems.DOUBLOON_PILE.get())) {
                int pileCount = stack.getCount();
                int totalInPiles = pileCount * DOUBLOONS_PER_PILE;

                if (totalInPiles <= 0) continue;

                // Сколько кучек нам нужно снять
                int pilesToRemove = (int) Math.ceil((double) remaining / DOUBLOONS_PER_PILE);
                int actualPilesToRemove = Math.min(pilesToRemove, pileCount);

                // Сколько дублонов мы снимаем этими кучками
                int removedFromPiles = actualPilesToRemove * DOUBLOONS_PER_PILE;

                // Обновляем количество кучек
                int newPileCount = pileCount - actualPilesToRemove;
                if (newPileCount <= 0) {
                    stack.setCount(0);
                } else {
                    stack.setCount(newPileCount);
                }

                remaining -= removedFromPiles;

                // Если мы сняли больше чем нужно - возвращаем сдачу
                if (remaining < 0) {
                    int change = Math.abs(remaining);
                    // Сдачу выдаем как отдельные дублоны (мелкие)
                    giveDoubloonsAsItems(player, change);
                    remaining = 0;
                }
            }
        }

        return remaining <= 0;
    }

    /**
     * Выдает дублоны игроку
     * Автоматически создает кучки для больших количеств
     * Если есть мешочек в инвентаре - складывает в него
     */
    public static void giveDoubloons(Player player, int amount) {
        if (player == null || amount <= 0) return;

        // 1. Сначала пробуем положить в существующий мешочек
        for (ItemStack stack : player.getInventory().items) {
            if (amount <= 0) break;
            if (stack.is(ModItems.DOUBLOON_POUCH.get())) {
                int currentAmount = stack.getOrDefault(ModDataComponents.DOUBLOONS_IN_POUCH, 0);
                stack.set(ModDataComponents.DOUBLOONS_IN_POUCH, currentAmount + amount);
                return;
            }
        }

        // 2. Если мешочка нет - выдаем кучками и отдельными дублонами
        giveDoubloonsAsItems(player, amount);
    }

    /**
     * Выдает дублоны в виде предметов (кучки и отдельные)
     */
    private static void giveDoubloonsAsItems(Player player, int amount) {
        if (player == null || amount <= 0) return;

        int piles = amount / DOUBLOONS_PER_PILE;
        int singles = amount % DOUBLOONS_PER_PILE;

        // Сначала выдаем кучки
        if (piles > 0) {
            ItemStack pileStack = new ItemStack(ModItems.DOUBLOON_PILE.get(), piles);
            if (!player.addItem(pileStack)) {
                player.drop(pileStack, false);
            }
        }

        // Потом отдельные дублоны
        if (singles > 0) {
            ItemStack singleStack = new ItemStack(ModItems.DOUBLOON.get(), singles);
            if (!player.addItem(singleStack)) {
                player.drop(singleStack, false);
            }
        }
    }

    /**
     * Проверяет, есть ли у игрока мешочек с дублонами
     */
    public static boolean hasDoubloonPouch(Player player) {
        if (player == null) return false;

        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(ModItems.DOUBLOON_POUCH.get())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Получает первый мешочек с дублонами из инвентаря
     */
    public static ItemStack getDoubloonPouch(Player player) {
        if (player == null) return ItemStack.EMPTY;

        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(ModItems.DOUBLOON_POUCH.get())) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}