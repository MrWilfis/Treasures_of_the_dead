package net.mrwilfis.treasures_of_the_dead.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LightLayer;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.DisgracedSkullEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.HatefulSkullEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class DisgracedSkullRenderer extends GeoEntityRenderer<DisgracedSkullEntity> {
    public DisgracedSkullRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DisgracedSkullModel());
        //this.shadowRadius = 0.25f;

        this.addRenderLayer(new AutoGlowingGeoLayer<DisgracedSkullEntity>(this));
    //    this.addRenderLayer(new DSEyesRenderLayer(this));
    //    this.addRenderLayer(new DSEyesRenderLayer2(this));

    }

    @Override
    public ResourceLocation getTextureLocation(DisgracedSkullEntity skull) {
            return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/disgraced_skull.png");
    }

    @Override
    public void render(DisgracedSkullEntity animatable, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {

        super.render(animatable, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);


    }
}
