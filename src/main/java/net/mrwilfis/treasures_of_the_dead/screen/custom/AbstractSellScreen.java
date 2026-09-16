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
import net.mrwilfis.treasures_of_the_dead.common.PlayerReputationData;
import net.mrwilfis.treasures_of_the_dead.network.C2SOpenBuyMenuPacket;
import net.mrwilfis.treasures_of_the_dead.network.C2SRequestReputationSyncPacket;
import net.mrwilfis.treasures_of_the_dead.network.C2SSellItemPacket;
import net.neoforged.neoforge.network.PacketDistributor;

public abstract class AbstractSellScreen<T extends AbstractSellMenu> extends AbstractContainerScreen<T> {
    protected static final ResourceLocation DEFAULT_TEXTURE =
            Treasures_of_the_dead.resource("textures/gui/sell_menu.png");

    protected Button sellButton;
    protected Button shopButton;
    protected Component reputationText;
    protected Component statusText;
    protected int statusColor = 0xFFFFFF;
    protected int statusTimer = 0;
    protected static final int STATUS_DURATION = 80;

    protected boolean requestedSync = false;

    protected int sellSlotX = 80;
    protected int sellSlotY = 35;

    public AbstractSellScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 180;
    }

    @Override
    protected void init() {
        super.init();

        this.sellButton = Button.builder(
                Component.translatable("gui." + Treasures_of_the_dead.MOD_ID + ".sell"),
                button -> this.handleSell()
        ).bounds(this.leftPos + 110, this.topPos + 33, 55, 21).build();

        this.shopButton = Button.builder(
                Component.translatable("gui." + Treasures_of_the_dead.MOD_ID + ".open_shop"),
                button -> this.openShop()
        ).bounds(this.leftPos + 10, this.topPos + 33, 55, 21).build();

        this.addRenderableWidget(sellButton);
        this.addRenderableWidget(shopButton);

        requestReputationSync();

        updateAllInfo();
    }

    protected void requestReputationSync() {
        if (this.minecraft == null || this.minecraft.player == null) return;

        PacketDistributor.sendToServer(new C2SRequestReputationSyncPacket(
                this.menu.getCompanyId()
        ));

        requestedSync = true;
        //Treasures_of_the_dead.LOGGER.info("Requested reputation sync for {} from client", this.menu.getCompanyId());
    }

    protected void handleSell() {
        if (this.minecraft == null || this.minecraft.player == null) return;

        if (!this.menu.hasItemInSlot()) {
            setStatus(Component.translatable("gui." + Treasures_of_the_dead.MOD_ID + ".no_item_to_sell"),
                    0xFF0000);
            return;
        }

        PacketDistributor.sendToServer(new C2SSellItemPacket(this.menu.getCompanyId()));
    }

    protected void openShop() {
        if (this.minecraft == null || this.minecraft.player == null) return;

        String companyId = this.menu.getCompanyId();
        //Villager villager = this.menu.getVillager();

        PacketDistributor.sendToServer(new C2SOpenBuyMenuPacket(companyId));
    }

    protected void setStatus(Component text, int color) {
        this.statusText = text;
        this.statusColor = color;
        this.statusTimer = STATUS_DURATION;
    }

    public void showSellResult(int price, int experience) {
        setStatus(Component.translatable(
                "gui." + Treasures_of_the_dead.MOD_ID + ".sell_result_message",
                price, experience
        ), 0xFFFFFF);
    }

    public void showBadSellResult(int requiredLevel) {
        setStatus(Component.translatable(
                "gui." + Treasures_of_the_dead.MOD_ID + ".needs_higher_level",
                requiredLevel
        ), 0xFF5555);
    }

    protected void updateAllInfo() {
        updateReputationInfo();
    }

    protected void updateReputationInfo() {
        if (this.minecraft == null || this.minecraft.player == null) return;

        String companyId = this.menu.getCompanyId();
        int level = PlayerReputationData.getLevel(this.minecraft.player, companyId);
        int exp = PlayerReputationData.getExperience(this.minecraft.player, companyId);
        int requiredExp = PlayerReputationData.getRequiredExp(level);

        this.reputationText = Component.translatable(
                "gui." + Treasures_of_the_dead.MOD_ID + ".reputation",
                Component.translatable("trading_company." + Treasures_of_the_dead.MOD_ID + "." + companyId),
                level,
                exp,
                requiredExp
        );
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, getTexture());

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(getTexture(), x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected ResourceLocation getTexture() {
        return DEFAULT_TEXTURE;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Заголовок GUI
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);

        // Название инвентаря игрока
        guiGraphics.drawString(this.font, this.playerInventoryTitle,
                this.inventoryLabelX, this.inventoryLabelY, 4210752, false);

        // Информация о репутации (слева сверху)
        if (this.reputationText != null) {
            guiGraphics.drawString(
                    this.font,
                    this.reputationText,
                    5,
                    5,
                    0xFFFFFF,
                    false
            );
        }

        // Статусное сообщение
        if (this.statusText != null && statusTimer > 0) {
            int textWidth = this.font.width(this.statusText);
            int slotCenterX = sellSlotX + (18 / 2);
            int textX = slotCenterX - (textWidth / 2);
            int textY = sellSlotY - 18;

            guiGraphics.drawString(
                    this.font,
                    this.statusText,
                    textX,
                    textY,
                    this.statusColor,
                    false
            );
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        //this.renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        updateAllInfo();

        if (statusTimer > 0) {
            statusTimer--;
            if (statusTimer == 0) {
                statusText = null;
            }
        }
    }
}
