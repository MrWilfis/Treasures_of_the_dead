package net.mrwilfis.treasures_of_the_dead.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.GoldenDaggerEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class GoldenDaggerModel extends GeoModel<GoldenDaggerEntity> {
    @Override
    public ResourceLocation getModelResource(GoldenDaggerEntity animatable) {
        return Treasures_of_the_dead.resource("geo/dagger.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GoldenDaggerEntity animatable) {
        return Treasures_of_the_dead.resource("textures/item/golden_dagger.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GoldenDaggerEntity animatable) {
        return Treasures_of_the_dead.resource("animations/entity/dagger.animation.json");
    }

    @Override
    public void setCustomAnimations(GoldenDaggerEntity animatable, long instanceId, AnimationState<GoldenDaggerEntity> animationState) {

    }
}


