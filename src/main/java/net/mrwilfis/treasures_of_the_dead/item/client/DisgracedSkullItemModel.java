package net.mrwilfis.treasures_of_the_dead.item.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.DisgracedSkullItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.FoulSkullItem;
import software.bernie.geckolib.model.GeoModel;

public class DisgracedSkullItemModel extends GeoModel<DisgracedSkullItem> {
    @Override
    public ResourceLocation getModelResource(DisgracedSkullItem disgracedSkullItem) {
        return Treasures_of_the_dead.resource("geo/disgraced_skull.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DisgracedSkullItem disgracedSkullItem) {
          return Treasures_of_the_dead.resource("textures/entity/disgraced_skull.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DisgracedSkullItem disgracedSkullItem) {
        return Treasures_of_the_dead.resource("animations/entity/totd_skull.animation.json");
    }

}
