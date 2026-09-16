package net.mrwilfis.treasures_of_the_dead.screen;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;

import net.mrwilfis.treasures_of_the_dead.screen.custom.OrderOfSoulsBuyMenu;
import net.mrwilfis.treasures_of_the_dead.screen.custom.OrderOfSoulsSellMenu;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TOTDMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Treasures_of_the_dead.MOD_ID);

    // Используем вашу старую регистрацию
    public static final DeferredHolder<MenuType<?>, MenuType<OrderOfSoulsSellMenu>> ORDER_OF_SOULS_SELL_MENU =
            registerMenuType("order_of_souls_sell_menu", (windowId, inv, data) ->
                    new OrderOfSoulsSellMenu(windowId, inv, (Villager) null));
    public static final DeferredHolder<MenuType<?>, MenuType<OrderOfSoulsBuyMenu>> ORDER_OF_SOULS_BUY_MENU =
            registerMenuType("order_of_souls_buy_menu", (windowId, inv, data) ->
                    new OrderOfSoulsBuyMenu(windowId, inv, (Villager) null));

    // В будущем для других компаний
    // public static final DeferredHolder<MenuType<?>, MenuType<GoldHoardersSellMenu>> GOLD_HOARDERS_SELL_MENU =
    //         registerMenuType("gold_hoarders_sell_menu", GoldHoardersSellMenu::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>>
    registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
