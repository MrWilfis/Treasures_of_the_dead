package net.mrwilfis.treasures_of_the_dead.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.mrwilfis.treasures_of_the_dead.item.custom.AbstractPowderKegItem;
import net.neoforged.neoforge.client.event.RenderHandEvent;

public class PowderKegFirstPersonRenderer {
    // Позиция для активного состояния (isGoingToBlowUp = true)
    private static final float POS_X = 0.56F;
    private static final float POS_Y = -0.51F;
    private static final float POS_Z = -0.725F;

    private static final float ROT_X = 0.0F;
    private static final float ROT_Y = 0.0F;
    private static final float ROT_Z = 0.0F;

    public static void renderPowderKeg(RenderHandEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        ItemStack itemStack = player.getMainHandItem();
        if (!(itemStack.getItem() instanceof AbstractPowderKegItem kegItem)) return;

        // Получаем состояние бочки
        boolean isGoingToBlowUp = AbstractPowderKegItem.getIsGoingToBlowUp(itemStack);

        // Если бочка НЕ активирована - используем стандартный рендеринг
//        if (!isGoingToBlowUp) {
//            return;
//        }

        // ============ АКТИВНОЕ СОСТОЯНИЕ (isGoingToBlowUp = true) ============

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource bufferSource = event.getMultiBufferSource();
        int light = event.getPackedLight();

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();

        // Применяем трансформации для активного состояния
        poseStack.translate(POS_X, POS_Y, POS_Z);
        poseStack.mulPose(Axis.XP.rotationDegrees(ROT_X));
        poseStack.mulPose(Axis.YP.rotationDegrees(ROT_Y));
        poseStack.mulPose(Axis.ZP.rotationDegrees(ROT_Z));

        // Рендерим предмет
        itemRenderer.renderStatic(
                itemStack,
                ItemDisplayContext.FIRST_PERSON_RIGHT_HAND,
                light,
                light,
                poseStack,
                bufferSource,
                null,
                0
        );

        poseStack.popPose();

        // Отменяем стандартный рендеринг
        event.setCanceled(true);
    }
}
