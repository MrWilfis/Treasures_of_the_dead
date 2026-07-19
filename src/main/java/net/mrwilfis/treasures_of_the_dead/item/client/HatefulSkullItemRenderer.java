package net.mrwilfis.treasures_of_the_dead.item.client;

import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.HatefulSkullItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class HatefulSkullItemRenderer extends GeoItemRenderer<HatefulSkullItem> {
    public HatefulSkullItemRenderer() {
        super(new HatefulSkullItemModel());

        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));

    }

}
