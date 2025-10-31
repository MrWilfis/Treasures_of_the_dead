package net.mrwilfis.treasures_of_the_dead.item.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.custom.guns.PistolItem;
import software.bernie.geckolib.model.GeoModel;

public class PistolModel extends GeoModel<PistolItem> {
    @Override
    public ResourceLocation getModelResource(PistolItem pistolItem) {
        return Treasures_of_the_dead.resource("geo/pistol.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PistolItem pistolItem) {
        return Treasures_of_the_dead.resource("textures/item/pistol.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PistolItem pistolItem) {
        return Treasures_of_the_dead.resource("animations/item/pistol.animation.json");
    }
}
