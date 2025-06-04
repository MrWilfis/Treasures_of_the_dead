package net.mrwilfis.treasures_of_the_dead.item.client;

import net.mrwilfis.treasures_of_the_dead.item.custom.BicornArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BicornArmorRender extends GeoArmorRenderer<BicornArmorItem> {
    public BicornArmorRender() {
        super(new BicornArmorModel());
    }
}
