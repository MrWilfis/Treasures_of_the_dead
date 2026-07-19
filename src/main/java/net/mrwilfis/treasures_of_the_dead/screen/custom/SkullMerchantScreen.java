package net.mrwilfis.treasures_of_the_dead.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;

public class SkullMerchantScreen extends AbstractContainerScreen<SkullMerchantMenu> {
    private static final ResourceLocation TEXTURE = Treasures_of_the_dead.resource("textures/gui/skull_merchant.png");

    private Button sellButton;
    private Button shopButton;
    private Component sellInfoText;

    public SkullMerchantScreen(SkullMerchantMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
//        this.imageWidth = 176;
//        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();

        this.sellButton = Button.builder(
                Component.translatable("gui." + Treasures_of_the_dead.MOD_ID + ".sell"),
                button -> this.sellItem()
        ).bounds(this.leftPos + 70, this.topPos + 55, 36, 20).build();

        this.shopButton = Button.builder(
                Component.translatable("gui." + Treasures_of_the_dead.MOD_ID + ".open_shop"),
                button -> this.openShop()
        ).bounds(this.leftPos + 130, this.topPos + 5, 40, 20).build();

        this.addRenderableWidget(sellButton);
        this.addRenderableWidget(shopButton);

        this.sellInfoText = Component.translatable("gui" + Treasures_of_the_dead.MOD_ID + ".sell_info", 0);
    }

    private void sellItem() {
        this.minecraft.player.sendSystemMessage(Component.literal("Кнопка продажи предмета"));
    }

    private void openShop() {
        this.minecraft.player.sendSystemMessage(Component.literal("Кнопка открытия магазина"));
    }

//    public void updateSellInfo() {
//        int price = 0;
//        this.sellInfoText = Component.translatable("gui." + Treasures_of_the_dead.MOD_ID + ".sell_info", price);
//    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);

//        // Отрисовка информации о репутации (временная)
//        guiGraphics.drawString(this.font,
//                Component.translatable("gui." + Treasures_of_the_dead.MOD_ID + ".reputation", 0),
//                10, 10, 4210752, false);
//
//        // Отрисовка информации о цене продажи
//        guiGraphics.drawString(this.font, this.sellInfoText, 50, 50, 4210752, false);
    }

//    @Override
//    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
//        this.renderBackground(guiGraphics, mouseX, mouseY, delta);
//        super.render(guiGraphics, mouseX, mouseY, delta);
//        this.renderTooltip(guiGraphics, mouseX, mouseY);
//    }
}
