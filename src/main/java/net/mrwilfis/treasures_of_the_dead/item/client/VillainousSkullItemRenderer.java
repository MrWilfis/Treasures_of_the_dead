package net.mrwilfis.treasures_of_the_dead.item.client;

import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.HatefulSkullItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.VillainousSkullItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class VillainousSkullItemRenderer extends GeoItemRenderer<VillainousSkullItem> {
    public VillainousSkullItemRenderer() {
        super(new VillainousSkullItemModel());

        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));

    }

}
