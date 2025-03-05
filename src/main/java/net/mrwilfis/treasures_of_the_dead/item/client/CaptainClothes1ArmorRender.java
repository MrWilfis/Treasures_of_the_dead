package net.mrwilfis.treasures_of_the_dead.item.client;

import net.mrwilfis.treasures_of_the_dead.item.custom.CaptainClothes1ArmorItem;
import net.mrwilfis.treasures_of_the_dead.item.custom.PirateClothes1ArmorItem;
import net.mrwilfis.treasures_of_the_dead.renderer.NewArmorRenderer;

public class CaptainClothes1ArmorRender extends NewArmorRenderer<CaptainClothes1ArmorItem> {
    public CaptainClothes1ArmorRender() {
        super(new CaptainClothes1ArmorModel());
    }
}
