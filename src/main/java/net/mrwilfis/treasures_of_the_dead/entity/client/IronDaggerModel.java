package net.mrwilfis.treasures_of_the_dead.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.IronDaggerEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class IronDaggerModel extends GeoModel<IronDaggerEntity> {
    @Override
    public ResourceLocation getModelResource(IronDaggerEntity animatable) {
        return Treasures_of_the_dead.resource("geo/dagger.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(IronDaggerEntity animatable) {
        return Treasures_of_the_dead.resource("textures/item/iron_dagger.png");
    }

    @Override
    public ResourceLocation getAnimationResource(IronDaggerEntity animatable) {
        return Treasures_of_the_dead.resource("animations/entity/dagger.animation.json");
    }

    @Override
    public void setCustomAnimations(IronDaggerEntity animatable, long instanceId, AnimationState<IronDaggerEntity> animationState) {

    }
}


