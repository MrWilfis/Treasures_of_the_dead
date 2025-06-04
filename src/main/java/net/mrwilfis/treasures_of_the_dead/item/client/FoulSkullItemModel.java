package net.mrwilfis.treasures_of_the_dead.item.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.FoulSkullItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.HatefulSkullItem;
import software.bernie.geckolib.model.GeoModel;

public class FoulSkullItemModel extends GeoModel<FoulSkullItem> {
    @Override
    public ResourceLocation getModelResource(FoulSkullItem foulSkullItem) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "geo/foul_skull.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FoulSkullItem foulSkullItem) {
          return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "textures/entity/foul_skull.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FoulSkullItem foulSkullItem) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "animations/entity/totd_skull.animation.json");
    }

}
