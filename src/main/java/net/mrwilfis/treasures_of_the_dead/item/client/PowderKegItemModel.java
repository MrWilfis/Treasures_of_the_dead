package net.mrwilfis.treasures_of_the_dead.item.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.custom.powderKegVariants.PowderKegItem;
import software.bernie.geckolib.model.GeoModel;

public class PowderKegItemModel extends GeoModel<PowderKegItem> {
    @Override
    public ResourceLocation getModelResource(PowderKegItem powderKegItem) {
        return Treasures_of_the_dead.resource("geo/powder_keg.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PowderKegItem powderKegItem) {
          return Treasures_of_the_dead.resource("textures/entity/powder_keg.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PowderKegItem powderKegItem) {
        return Treasures_of_the_dead.resource("animations/entity/powder_keg.animation.json");
    }

}
