package net.mrwilfis.treasures_of_the_dead.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.NetheriteDaggerEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class NetheriteDaggerModel extends GeoModel<NetheriteDaggerEntity> {
    @Override
    public ResourceLocation getModelResource(NetheriteDaggerEntity animatable) {
        return Treasures_of_the_dead.resource("geo/dagger.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NetheriteDaggerEntity animatable) {
        return Treasures_of_the_dead.resource("textures/item/netherite_dagger.png");
    }

    @Override
    public ResourceLocation getAnimationResource(NetheriteDaggerEntity animatable) {
        return Treasures_of_the_dead.resource("animations/entity/dagger.animation.json");
    }

    @Override
    public void setCustomAnimations(NetheriteDaggerEntity animatable, long instanceId, AnimationState<NetheriteDaggerEntity> animationState) {

    }
}


