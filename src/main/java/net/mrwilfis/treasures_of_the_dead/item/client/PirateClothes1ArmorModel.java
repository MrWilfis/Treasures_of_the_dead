package net.mrwilfis.treasures_of_the_dead.item.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.custom.PirateClothes1ArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class PirateClothes1ArmorModel extends GeoModel<PirateClothes1ArmorItem> {
    @Override
    public ResourceLocation getModelResource(PirateClothes1ArmorItem pirateClothes1ArmorItem) {
        return Treasures_of_the_dead.resource("geo/pirate_clothes1.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PirateClothes1ArmorItem pirateClothes1ArmorItem) {
        return Treasures_of_the_dead.resource("textures/armor/pirate_clothes1.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PirateClothes1ArmorItem pirateClothes1ArmorItem) {
        return Treasures_of_the_dead.resource("animations/armor/pirate_clothes1.animation.json");
    }
}
