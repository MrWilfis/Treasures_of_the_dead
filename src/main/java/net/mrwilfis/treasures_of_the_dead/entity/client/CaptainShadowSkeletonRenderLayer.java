package net.mrwilfis.treasures_of_the_dead.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.CaptainShadowSkeletonEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.ShadowSkeletonEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class CaptainShadowSkeletonRenderLayer extends GeoRenderLayer<CaptainShadowSkeletonEntity> {
    private static final ResourceLocation TEXTURE = Treasures_of_the_dead.resource("textures/entity/shadow_skeleton_eyes.png");

    public CaptainShadowSkeletonRenderLayer(CaptainShadowSkeletonRenderer render) {
        super(render);
    }


    @Override
    public void render(PoseStack poseStack, CaptainShadowSkeletonEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable.getShadow()) {
            RenderType newRenderType = RenderType.eyes(TEXTURE);
            VertexConsumer newBuffer = bufferSource.getBuffer(newRenderType);
//            RenderType emissiveRenderType = this.getRenderType(animatable);
            this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, newRenderType, newBuffer,
                    partialTick, 15728640, OverlayTexture.NO_OVERLAY, 0xffffff);

        }
    }
}
