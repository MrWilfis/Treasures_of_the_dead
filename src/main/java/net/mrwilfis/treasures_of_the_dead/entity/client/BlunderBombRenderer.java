package net.mrwilfis.treasures_of_the_dead.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.BlunderBombEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.VillainousSkullEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class BlunderBombRenderer extends GeoEntityRenderer<BlunderBombEntity> {
    public BlunderBombRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BlunderBombModel());
        //this.shadowRadius = 0.25f;
        //this.addRenderLayer(new AutoGlowingGeoLayer<BlunderBombEntity>(this));

    }

    @Override
    public ResourceLocation getTextureLocation(BlunderBombEntity instance) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "textures/entity/blunder_bomb.png");
    }

    @Override
    public void render(BlunderBombEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(pEntity) < 12.25)) {

            pMatrixStack.scale(1.4f,1.4f,1.4f);
            super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        }
    }

}
