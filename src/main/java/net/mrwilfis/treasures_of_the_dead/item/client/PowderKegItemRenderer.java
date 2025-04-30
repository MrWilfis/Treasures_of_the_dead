package net.mrwilfis.treasures_of_the_dead.item.client;

import net.mrwilfis.treasures_of_the_dead.item.custom.chestVariants.TreasureChestItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.powderKegVariants.PowderKegItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class PowderKegItemRenderer extends GeoItemRenderer<PowderKegItem> {
    public PowderKegItemRenderer() {
        super(new PowderKegItemModel());
    }

}
