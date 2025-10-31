package net.mrwilfis.treasures_of_the_dead.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.chestVariants.TreasureChestEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TreasureChestRenderer extends GeoEntityRenderer<TreasureChestEntity> {
    public TreasureChestRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TreasureChestModel());
        //this.shadowRadius = 0.25f;
    //    this.addRenderLayer(new AutoGlowingGeoLayer<FoulSkullEntity>(this));

    }

    @Override
    public ResourceLocation getTextureLocation(TreasureChestEntity instance) {
        return Treasures_of_the_dead.resource("textures/entity/treasure_chest.png");
    }

    @Override
    public void render(TreasureChestEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
