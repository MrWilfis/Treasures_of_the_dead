package net.mrwilfis.treasures_of_the_dead.item.client;

import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.FoulSkullItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.HatefulSkullItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class FoulSkullItemRenderer extends GeoItemRenderer<FoulSkullItem> {
    public FoulSkullItemRenderer() {
        super(new FoulSkullItemModel());

    //    this.addRenderLayer(new AutoGlowingGeoLayer<>(this));

    }

}
