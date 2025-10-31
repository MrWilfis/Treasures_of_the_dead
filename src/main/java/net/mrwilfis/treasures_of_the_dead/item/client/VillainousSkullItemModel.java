package net.mrwilfis.treasures_of_the_dead.item.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.HatefulSkullItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.VillainousSkullItem;
import software.bernie.geckolib.model.GeoModel;

public class VillainousSkullItemModel extends GeoModel<VillainousSkullItem> {
    @Override
    public ResourceLocation getModelResource(VillainousSkullItem villainousSkullItem) {
        return Treasures_of_the_dead.resource("geo/villainous_skull.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VillainousSkullItem villainousSkullItem) {
          return Treasures_of_the_dead.resource("textures/entity/villainous_skull.png");
    }

    @Override
    public ResourceLocation getAnimationResource(VillainousSkullItem villainousSkullItem) {
        return Treasures_of_the_dead.resource("animations/entity/totd_skull.animation.json");
    }

}
