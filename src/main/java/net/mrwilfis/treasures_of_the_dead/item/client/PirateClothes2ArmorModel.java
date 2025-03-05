package net.mrwilfis.treasures_of_the_dead.item.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.custom.PirateClothes1ArmorItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.PirateClothes2ArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class PirateClothes2ArmorModel extends GeoModel<PirateClothes2ArmorItem> {
    @Override
    public ResourceLocation getModelResource(PirateClothes2ArmorItem pirateClothes2ArmorItem) {
        return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "geo/pirate_clothes2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PirateClothes2ArmorItem pirateClothes2ArmorItem) {
        return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/armor/pirate_clothes2.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PirateClothes2ArmorItem pirateClothes2ArmorItem) {
        return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "animations/armor/pirate_clothes1.animation.json");
    }
}
