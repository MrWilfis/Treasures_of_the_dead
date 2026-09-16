package net.mrwilfis.treasures_of_the_dead.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.mrwilfis.treasures_of_the_dead.item.custom.CutlassItem;
import net.neoforged.neoforge.client.event.RenderHandEvent;

public class CutlassFirstPersonRenderer {

    private static final float START_X = 0.2F;
    private static final float START_Y = -0.50F;
    private static final float START_Z = -0.55F;

    private static final float START_X_ROT = 30.0F;
    private static final float START_Y_ROT = -35.0F;
    private static final float START_Z_ROT = 100.0F;

    private static final float END_X = 0.2F;
    private static final float END_Y = -0.25F;
    private static final float END_Z = -0.55F;

    private static final float END_X_ROT = 67.0F;
    private static final float END_Y_ROT = -35.0F;
    private static final float END_Z_ROT = 100.0F;

    private static final int ANIMATION_DURATION = 10;

    // Current animation progress (0.0 - 1.0)
    private static float animationProgress = 0.0F;
    private static boolean isBlocking = false;
    private static int ticksSinceBlockStart = 0;

    public static void renderCutlassBlocking(RenderHandEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        ItemStack itemStack = player.getItemInHand(event.getHand());
        if (!(itemStack.getItem() instanceof CutlassItem)) return;

        // Проверяем, блокирует ли игрок (зажат ПКМ)
        boolean isCurrentlyBlocking = player.isUsingItem() && player.getUseItem() == itemStack;

        // Обновляем состояние анимации
        updateAnimationState(isCurrentlyBlocking);

        // Если не блокирует и анимация завершена - выходим
        if (!isCurrentlyBlocking && animationProgress <= 0.0F) return;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource bufferSource = event.getMultiBufferSource();
        int light = event.getPackedLight();

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();

        // Получаем интерполированную позицию и поворот
        float[] pos = interpolatePosition();
        float[] rot = interpolateRotation();

        // Применяем трансформации
        poseStack.translate(pos[0], pos[1], pos[2]);
        poseStack.mulPose(Axis.XP.rotationDegrees(rot[0]));
        poseStack.mulPose(Axis.YP.rotationDegrees(rot[1]));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rot[2]));

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

    private static void updateAnimationState(boolean isCurrentlyBlocking) {
        if (isCurrentlyBlocking) {
            if (!isBlocking) {
                // Только начали блокировать
                isBlocking = true;
                ticksSinceBlockStart = 0;
                animationProgress = 0.0F;
            } else {
                // Продолжаем блокировать
                ticksSinceBlockStart++;
                if (ticksSinceBlockStart <= ANIMATION_DURATION) {
                    animationProgress = (float) ticksSinceBlockStart / ANIMATION_DURATION;
                } else {
                    animationProgress = 1.0F;
                }
            }
        } else {
            if (isBlocking) {
                // Перестали блокировать - анимация в обратную сторону
                ticksSinceBlockStart++;
                if (ticksSinceBlockStart <= ANIMATION_DURATION) {
                    animationProgress = 1.0F - ((float) ticksSinceBlockStart / ANIMATION_DURATION);
                } else {
                    isBlocking = false;
                    animationProgress = 0.0F;
                    ticksSinceBlockStart = 0;
                }
            }
        }
    }

    private static float[] interpolatePosition() {
        // Используем плавную интерполяцию (ease in-out)
        float t = smoothstep(animationProgress);

        float x = START_X + (END_X - START_X) * t;
        float y = START_Y + (END_Y - START_Y) * t;
        float z = START_Z + (END_Z - START_Z) * t;

        return new float[]{x, y, z};
    }

    private static float[] interpolateRotation() {
        float t = smoothstep(animationProgress);

        float xRot = START_X_ROT + (END_X_ROT - START_X_ROT) * t;
        float yRot = START_Y_ROT + (END_Y_ROT - START_Y_ROT) * t;
        float zRot = START_Z_ROT + (END_Z_ROT - START_Z_ROT) * t;

        return new float[]{xRot, yRot, zRot};
    }

    // Плавная интерполяция (ease in-out)
    private static float smoothstep(float t) {
        return t * t * (3.0F - 2.0F * t);
    }

    // Для отладки - можно добавить метод для сброса
    public static void resetAnimation() {
        animationProgress = 0.0F;
        isBlocking = false;
        ticksSinceBlockStart = 0;
    }
}