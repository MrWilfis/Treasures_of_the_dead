package net.mrwilfis.treasures_of_the_dead.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.BlunderBombEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BlunderBombRenderer extends GeoEntityRenderer<BlunderBombEntity> {
    public BlunderBombRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BlunderBombModel());
        //this.shadowRadius = 0.25f;
        //this.addRenderLayer(new AutoGlowingGeoLayer<BlunderBombEntity>(this));

    }

    @Override
    public ResourceLocation getTextureLocation(BlunderBombEntity instance) {
        return Treasures_of_the_dead.resource("textures/entity/blunder_bomb.png");
    }

    @Override
    public void render(BlunderBombEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack poseStack,
                       MultiBufferSource pBuffer, int pPackedLight) {

        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot())));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot())));

        if (pEntity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(pEntity) < 12.25)) {

            poseStack.scale(1.4f,1.4f,1.4f);
            super.render(pEntity, pEntityYaw, pPartialTicks, poseStack, pBuffer, pPackedLight);
        }
    }

}
