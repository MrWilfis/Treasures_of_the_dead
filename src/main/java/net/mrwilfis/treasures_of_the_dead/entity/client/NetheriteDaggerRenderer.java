package net.mrwilfis.treasures_of_the_dead.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.DaggerEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.NetheriteDaggerEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NetheriteDaggerRenderer extends GeoEntityRenderer<NetheriteDaggerEntity> {
    public NetheriteDaggerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new NetheriteDaggerModel());
    }

    @Override
    public ResourceLocation getTextureLocation(NetheriteDaggerEntity instance) {
        return Treasures_of_the_dead.resource("textures/item/netherite_dagger.png");
    }

    @Override
    public void render(NetheriteDaggerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();

        if (entity.getInGround()) {
            renderStuckDagger(entity, partialTicks, poseStack);
        } else {
            renderFlyingDagger(entity, partialTicks, poseStack);
        }

        if (entity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < 12.25)) {
            poseStack.scale(0.75f, 0.75f, 0.75f);
            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
        poseStack.popPose();
    }

    private void renderFlyingDagger(DaggerEntity entity, float partialTicks, PoseStack poseStack) {
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.getRenderingRotation())); // rotating

    }

    private void renderStuckDagger(DaggerEntity entity, float partialTicks, PoseStack poseStack) {
        Direction direction = entity.getHitDirection();

        switch (direction) {
            case UP -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
                poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, -entity.xRotO, -entity.getXRot())));
            }
            case DOWN -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
                poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, -entity.xRotO, -entity.getXRot())));
                poseStack.translate(0, -0.2, 0);
            }
            default -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
                poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
            }

        }
    }

}
