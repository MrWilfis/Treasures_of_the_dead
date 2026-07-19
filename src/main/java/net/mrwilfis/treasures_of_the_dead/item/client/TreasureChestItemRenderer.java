package net.mrwilfis.treasures_of_the_dead.item.client;

import net.mrwilfis.treasures_of_the_dead.item.custom.chestVariants.TreasureChestItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class TreasureChestItemRenderer extends GeoItemRenderer<TreasureChestItem> {
    public TreasureChestItemRenderer() {
        super(new TreasureChestItemModel());

    //    this.addRenderLayer(new AutoGlowingGeoLayer<>(this));

    }

}
