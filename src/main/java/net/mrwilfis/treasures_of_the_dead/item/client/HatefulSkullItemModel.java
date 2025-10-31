package net.mrwilfis.treasures_of_the_dead.item.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.HatefulSkullItem;
import software.bernie.geckolib.model.GeoModel;

public class HatefulSkullItemModel extends GeoModel<HatefulSkullItem> {
    @Override
    public ResourceLocation getModelResource(HatefulSkullItem hatefulSkullItem) {
        return Treasures_of_the_dead.resource("geo/hateful_skull.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HatefulSkullItem hatefulSkullItem) {
          return Treasures_of_the_dead.resource("textures/entity/hateful_skull.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HatefulSkullItem hatefulSkullItem) {
        return Treasures_of_the_dead.resource("animations/entity/totd_skull.animation.json");
    }

}
