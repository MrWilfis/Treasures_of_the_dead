package net.mrwilfis.treasures_of_the_dead.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LightLayer;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.DisgracedSkullEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class DSEyesRenderLayer extends GeoRenderLayer<DisgracedSkullEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Treasures_of_the_dead.MOD_ID,"textures/entity/disgraced_skull.png");


    public DSEyesRenderLayer(DisgracedSkullRenderer render) {
        super(render);
    }

    @Override
    public void render(PoseStack poseStack, DisgracedSkullEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        BlockPos ratPos = animatable.getLightPosition();
        int i = animatable.level().getBrightness(LightLayer.SKY, ratPos);
        int j = animatable.level().getBrightness(LightLayer.BLOCK, ratPos);
        int brightness;
        brightness = Math.max(i, j);
        if (brightness < 7) {
            renderType = RenderType.eyes(TEXTURE);
            buffer = bufferSource.getBuffer(renderType);
        }
        this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, renderType, buffer, partialTick, packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
    //    super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }
}
