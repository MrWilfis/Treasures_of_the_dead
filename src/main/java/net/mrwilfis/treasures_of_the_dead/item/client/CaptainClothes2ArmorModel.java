package net.mrwilfis.treasures_of_the_dead.item.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.custom.CaptainClothes1ArmorItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.CaptainClothes2ArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class CaptainClothes2ArmorModel extends GeoModel<CaptainClothes2ArmorItem> {
    @Override
    public ResourceLocation getModelResource(CaptainClothes2ArmorItem CaptainClothes2ArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "geo/captain_clothes2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CaptainClothes2ArmorItem CaptainClothes2ArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "textures/armor/captain_clothes2.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CaptainClothes2ArmorItem CaptainClothes2ArmorItem) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "animations/armor/captain_clothes2.animation.json");
    }
}
