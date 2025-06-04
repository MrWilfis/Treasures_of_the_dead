package net.mrwilfis.treasures_of_the_dead.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.VillainousSkullEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class VillainousSkullRenderer extends GeoEntityRenderer<VillainousSkullEntity> {
    public VillainousSkullRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VillainousSkullModel());
        //this.shadowRadius = 0.25f;
        this.addRenderLayer(new AutoGlowingGeoLayer<VillainousSkullEntity>(this));

    }

    @Override
    public ResourceLocation getTextureLocation(VillainousSkullEntity instance) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "textures/entity/villainous_skull.png");
    }

    @Override
    public void render(VillainousSkullEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
    //    pMatrixStack.scale(2,2,2);
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
