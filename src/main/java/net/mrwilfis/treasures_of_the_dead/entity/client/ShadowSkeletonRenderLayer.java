package net.mrwilfis.treasures_of_the_dead.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LightLayer;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.ShadowSkeletonEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.DisgracedSkullEntity;
import net.mrwilfis.treasures_of_the_dead.entity.variant.ShadowSkeletonVariant;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.Map;

public class ShadowSkeletonRenderLayer extends GeoRenderLayer<ShadowSkeletonEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID,"textures/entity/shadow_skeleton_eyes.png");

    public ShadowSkeletonRenderLayer(ShadowSkeletonRenderer render) {
        super(render);
    }


    @Override
    public void render(PoseStack poseStack, ShadowSkeletonEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable.getShadow()) {
            RenderType newRenderType = RenderType.eyes(TEXTURE);
            VertexConsumer newBuffer = bufferSource.getBuffer(newRenderType);
//            RenderType emissiveRenderType = this.getRenderType(animatable);
            this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, newRenderType, newBuffer,
                    partialTick, 15728640, OverlayTexture.NO_OVERLAY, 0xffffff);

        }
    }
}
