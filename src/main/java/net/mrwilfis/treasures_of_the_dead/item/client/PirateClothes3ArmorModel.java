package net.mrwilfis.treasures_of_the_dead.item.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.custom.PirateClothes2ArmorItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.PirateClothes3ArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class PirateClothes3ArmorModel extends GeoModel<PirateClothes3ArmorItem> {
    @Override
    public ResourceLocation getModelResource(PirateClothes3ArmorItem pirateClothes3ArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "geo/pirate_clothes3.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PirateClothes3ArmorItem pirateClothes3ArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "textures/armor/pirate_clothes3.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PirateClothes3ArmorItem pirateClothes3ArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "animations/armor/pirate_clothes1.animation.json");
    }
}
