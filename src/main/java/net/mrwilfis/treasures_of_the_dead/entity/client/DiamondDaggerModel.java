package net.mrwilfis.treasures_of_the_dead.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.DiamondDaggerEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class DiamondDaggerModel extends GeoModel<DiamondDaggerEntity> {
    @Override
    public ResourceLocation getModelResource(DiamondDaggerEntity animatable) {
        return Treasures_of_the_dead.resource("geo/dagger.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DiamondDaggerEntity animatable) {
        return Treasures_of_the_dead.resource("textures/item/diamond_dagger.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DiamondDaggerEntity animatable) {
        return Treasures_of_the_dead.resource("animations/entity/dagger.animation.json");
    }

    @Override
    public void setCustomAnimations(DiamondDaggerEntity animatable, long instanceId, AnimationState<DiamondDaggerEntity> animationState) {

    }
}


