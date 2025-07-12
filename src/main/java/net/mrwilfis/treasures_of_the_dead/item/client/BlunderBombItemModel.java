package net.mrwilfis.treasures_of_the_dead.item.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.custom.BlunderBombItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.VillainousSkullItem;
import software.bernie.geckolib.model.GeoModel;

public class BlunderBombItemModel extends GeoModel<BlunderBombItem> {
    @Override
    public ResourceLocation getModelResource(BlunderBombItem blunderBombItem) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "geo/blunder_bomb.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BlunderBombItem blunderBombItem) {
          return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "textures/item/blunder_bomb.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BlunderBombItem blunderBombItem) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "animations/item/blunder_bomb.animation.json");
    }

}
