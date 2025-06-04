package net.mrwilfis.treasures_of_the_dead.item.client;

import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.DisgracedSkullItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.skullVariantsItem.FoulSkullItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class DisgracedSkullItemRenderer extends GeoItemRenderer<DisgracedSkullItem> {
    public DisgracedSkullItemRenderer() {
        super(new DisgracedSkullItemModel());

        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));

    }

}
