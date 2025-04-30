package net.mrwilfis.treasures_of_the_dead.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.chestVariants.TreasureChestEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.powderKegsVariants.PowderKegEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PowderKegRenderer extends GeoEntityRenderer<PowderKegEntity> {
    public PowderKegRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PowderKegModel());

    }

    @Override
    public ResourceLocation getTextureLocation(PowderKegEntity instance) {
        return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/powder_keg.png");
    }

    @Override
    public void render(PowderKegEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
