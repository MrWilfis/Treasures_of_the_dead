package net.mrwilfis.treasures_of_the_dead.item.client;

import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.FoulSkullItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class FoulSkullItemRenderer extends GeoItemRenderer<FoulSkullItem> {
    public FoulSkullItemRenderer() {
        super(new FoulSkullItemModel());

    //    this.addRenderLayer(new AutoGlowingGeoLayer<>(this));

    }

}
