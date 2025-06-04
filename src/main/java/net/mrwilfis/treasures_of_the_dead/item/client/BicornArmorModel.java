package net.mrwilfis.treasures_of_the_dead.item.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.custom.BicornArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class BicornArmorModel extends GeoModel<BicornArmorItem> {
    @Override
    public ResourceLocation getModelResource(BicornArmorItem bicornArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "geo/bicorn.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BicornArmorItem bicornArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "textures/armor/bicorn.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BicornArmorItem bicornArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "animations/armor/bicorn.animation.json");
    }
}
