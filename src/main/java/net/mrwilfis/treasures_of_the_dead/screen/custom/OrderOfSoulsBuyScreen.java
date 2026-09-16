package net.mrwilfis.treasures_of_the_dead.screen.custom;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;

public class OrderOfSoulsBuyScreen extends AbstractBuyScreen<OrderOfSoulsBuyMenu> {
    private static final ResourceLocation TEXTURE =
            Treasures_of_the_dead.resource("textures/gui/order_of_souls_buy_menu.png");

    public OrderOfSoulsBuyScreen(OrderOfSoulsBuyMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
    }
}
