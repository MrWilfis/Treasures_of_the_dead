package net.mrwilfis.treasures_of_the_dead.item.client;

import net.mrwilfis.treasures_of_the_dead.item.custom.BicornArmorItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.PirateClothes1ArmorItem;
import net.mrwilfis.treasures_of_the_dead.renderer.NewArmorRenderer;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class PirateClothes1ArmorRender extends NewArmorRenderer<PirateClothes1ArmorItem> {
    public PirateClothes1ArmorRender() {
        super(new PirateClothes1ArmorModel());
    }
}
