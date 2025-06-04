package net.mrwilfis.treasures_of_the_dead.item.client;

import net.mrwilfis.treasures_of_the_dead.item.custom.guns.PistolItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class PistolRenderer extends GeoItemRenderer<PistolItem> {
    public PistolRenderer() {
        super(new PistolModel());
    }
}
