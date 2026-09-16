package net.mrwilfis.treasures_of_the_dead.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.mrwilfis.treasures_of_the_dead.TOTDUtils;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.common.PlayerReputationData;
import net.mrwilfis.treasures_of_the_dead.network.C2SOpenSellMenuPacket;
import net.mrwilfis.treasures_of_the_dead.network.C2SRequestBuyPacket;
import net.mrwilfis.treasures_of_the_dead.util.DoubloonUtils;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public abstract class AbstractBuyScreen<T extends AbstractBuyMenu> extends AbstractContainerScreen<T> {
    protected static final ResourceLocation DEFAULT_TEXTURE =
            Treasures_of_the_dead.resource("textures/gui/buy_menu.png");

    protected Button backButton;
    protected Component reputationText;
    protected Component statusText;
    protected int statusColor = 0xFFFFFF;
    protected int statusTimer = 0;
    protected static final int STATUS_DURATION = 80;

    protected int gridStartX = 8;
    protected int gridStartY = 18;
    protected int gridRows = 3;
    protected int gridColumns = 9;

    public AbstractBuyScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 180;
    }

    @Override
    protected void init() {
        super.init();

        this.backButton = Button.builder(
                Component.translatable("gui." + Treasures_of_the_dead.MOD_ID + ".back"),
                button -> this.goBack()
        ).bounds(this.leftPos -65, this.topPos + 5, 55, 20).build();

        this.addRenderableWidget(backButton);

        updateAllInfo();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // ЛКМ (кнопка 0)
        if (button == 0) {
            int hoveredSlot = this.hoveredSlot != null ? this.hoveredSlot.index : -1;

            if (hoveredSlot >= 0 && hoveredSlot < AbstractBuyMenu.ITEM_SLOT_COUNT) {
                ItemStack stack = this.menu.getShopContainer().getItem(hoveredSlot);
                if (!stack.isEmpty()) {
                    TOTDUtils.ShopOfferEntry offer = getOfferForSlot(hoveredSlot);
                    if (offer != null) {
                        handleBuyClick(offer, hoveredSlot);
                        return true;
                    }
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    /**
     * Обрабатывает клик по предмету в магазине
     */
    private void handleBuyClick(TOTDUtils.ShopOfferEntry offer, int slotIndex) {
        if (this.minecraft == null || this.minecraft.player == null) return;

        // 1. Проверяем уровень репутации на клиенте
        int playerLevel = PlayerReputationData.getLevel(this.minecraft.player, this.menu.getCompanyId());
        if (playerLevel < offer.minLevel) {
            // Недостаточно уровня - проигрываем звук недовольства и выходим
            playVillagerNoSound();
            return;
        }

        // 2. Проверяем наличие дублонов на клиенте (с учетом мешочка)
        int doubloonCount = DoubloonUtils.countDoubloons(this.minecraft.player);
        if (doubloonCount < offer.price) {
            // Недостаточно дублонов - проигрываем звук недовольства и выходим
            playVillagerNoSound();
            return;
        }

        // Все проверки пройдены - отправляем запрос на сервер
        PacketDistributor.sendToServer(new C2SRequestBuyPacket(
                this.menu.getCompanyId(),
                slotIndex
        ));
    }

    private void playVillagerNoSound() {
        if (this.minecraft == null || this.minecraft.player == null) return;

        this.minecraft.player.playSound(
                SoundEvents.VILLAGER_NO,
                1.0F,
                1.0F
        );
    }

    protected void goBack() {
        if (this.minecraft == null || this.minecraft.player == null) return;

        String companyId = this.menu.getCompanyId();

        PacketDistributor.sendToServer(new C2SOpenSellMenuPacket(companyId));
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

    protected void setStatus(Component text, int color) {
        this.statusText = text;
        this.statusColor = color;
        this.statusTimer = STATUS_DURATION;
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

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        int hoveredSlot = this.hoveredSlot != null ? this.hoveredSlot.index : -1;

        if (hoveredSlot >= 0 && hoveredSlot < AbstractBuyMenu.ITEM_SLOT_COUNT) {
            ItemStack stack = this.menu.getShopContainer().getItem(hoveredSlot);
            if (!stack.isEmpty()) {
                TOTDUtils.ShopOfferEntry offer = getOfferForSlot(hoveredSlot);
                if (offer != null) {
                    renderOfferTooltip(guiGraphics, x, y, stack, offer);
                    return;
                }
            }
        }

        super.renderTooltip(guiGraphics, x, y);
    }

    private TOTDUtils.ShopOfferEntry getOfferForSlot(int slotIndex) {
        List<TOTDUtils.ShopOfferEntry> offers = this.menu.getCurrentOffers();
        if (slotIndex < offers.size()) {
            return offers.get(slotIndex);
        }
        return null;
    }

    private void renderOfferTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, ItemStack stack, TOTDUtils.ShopOfferEntry offer) {
        List<Component> tooltip = stack.getTooltipLines(
                Item.TooltipContext.of(this.minecraft.level),
                this.minecraft.player,
                TooltipFlag.Default.NORMAL
        );

        if (!tooltip.isEmpty()) {
            tooltip.add(Component.empty());
        }

        int playerLevel = PlayerReputationData.getLevel(this.minecraft.player, this.menu.getCompanyId());

        // Добавляем цену в дублонах (цвет уже в локализации)
        if (playerLevel >= offer.minLevel) {
            Component priceText = Component.translatable(
                    "gui." + Treasures_of_the_dead.MOD_ID + ".shop.price",
                    offer.price
            );
            tooltip.add(priceText);
        }


        if (playerLevel >= offer.minLevel) {

        } else {
            Component availabilityText;
            availabilityText = Component.translatable(
                    "gui." + Treasures_of_the_dead.MOD_ID + ".shop.requires_level",
                    offer.minLevel
            );
            tooltip.add(availabilityText);
        }

        guiGraphics.renderTooltip(this.font, tooltip, java.util.Optional.empty(), mouseX, mouseY);
    }

    protected ResourceLocation getTexture() {
        return DEFAULT_TEXTURE;
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);

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

        if (this.statusText != null && statusTimer > 0) {
            int textWidth = this.font.width(this.statusText);
            int gridCenterX = gridStartX + (gridColumns * 18) / 2;
            int textX = gridCenterX - (textWidth / 2);
            int textY = gridStartY - 10;

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