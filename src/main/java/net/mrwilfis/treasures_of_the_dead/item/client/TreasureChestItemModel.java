package net.mrwilfis.treasures_of_the_dead.item.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.item.custom.chestVariants.TreasureChestItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.FoulSkullItem;
import software.bernie.geckolib.model.GeoModel;

public class TreasureChestItemModel extends GeoModel<TreasureChestItem> {
    @Override
    public ResourceLocation getModelResource(TreasureChestItem TreasureChestItem) {
        return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "geo/treasure_chest.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TreasureChestItem TreasureChestItem) {
          return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/treasure_chest.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TreasureChestItem TreasureChestItem) {
        return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "animations/entity/treasure_chest.animation.json");
    }

}
