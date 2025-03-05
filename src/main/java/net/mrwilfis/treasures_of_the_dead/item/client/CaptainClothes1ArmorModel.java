package net.mrwilfis.treasures_of_the_dead.item.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.custom.CaptainClothes1ArmorItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.PirateClothes1ArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class CaptainClothes1ArmorModel extends GeoModel<CaptainClothes1ArmorItem> {
    @Override
    public ResourceLocation getModelResource(CaptainClothes1ArmorItem captainClothes1ArmorItem) {
        return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "geo/captain_clothes1.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CaptainClothes1ArmorItem captainClothes1ArmorItem) {
        return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/armor/captain_clothes1.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CaptainClothes1ArmorItem captainClothes1ArmorItem) {
        return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "animations/armor/pirate_clothes1.animation.json");
    }
}
