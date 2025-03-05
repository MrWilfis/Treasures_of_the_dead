package net.mrwilfis.treasures_of_the_dead.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.client.HatefulSkullModel;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.HatefulSkullEntity;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.HatefulSkullItem;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class HatefulSkullItemRenderer extends GeoItemRenderer<HatefulSkullItem> {
    public HatefulSkullItemRenderer() {
        super(new HatefulSkullItemModel());

        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));

    }

}
