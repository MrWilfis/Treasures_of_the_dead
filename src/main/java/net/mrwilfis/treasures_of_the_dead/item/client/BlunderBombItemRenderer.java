package net.mrwilfis.treasures_of_the_dead.item.client;

import net.mrwilfis.treasures_of_the_dead.item.custom.BlunderBombItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.VillainousSkullItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class BlunderBombItemRenderer extends GeoItemRenderer<BlunderBombItem> {
    public BlunderBombItemRenderer() {
        super(new BlunderBombItemModel());

        //this.addRenderLayer(new AutoGlowingGeoLayer<>(this));

    }

}
