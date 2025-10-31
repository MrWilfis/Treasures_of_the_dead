package net.mrwilfis.treasures_of_the_dead.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.BulletEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class BulletRenderer extends GeoEntityRenderer<BulletEntity> {
    public BulletRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BulletModel());
        //this.shadowRadius = 0.25f;
        this.addRenderLayer(new AutoGlowingGeoLayer<BulletEntity>(this));

    }

    @Override
    public ResourceLocation getTextureLocation(BulletEntity instance) {
        return Treasures_of_the_dead.resource("textures/entity/bullet.png");
    }

    @Override
    public void render(BulletEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }


}
