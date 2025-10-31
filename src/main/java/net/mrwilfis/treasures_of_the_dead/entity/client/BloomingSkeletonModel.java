package net.mrwilfis.treasures_of_the_dead.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.BloomingSkeletonEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class BloomingSkeletonModel extends GeoModel<BloomingSkeletonEntity> {
    @Override
    public ResourceLocation getModelResource(BloomingSkeletonEntity BloomingSkeletonEntity) {
        return Treasures_of_the_dead.resource("geo/blooming_skeleton.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BloomingSkeletonEntity bloomingSkeletonEntity) {
        return BloomingSkeletonRenderer.LOCATION_BY_VARIANT.get(bloomingSkeletonEntity.getBloomingVariant());
      //  return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/totd_skeleton.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BloomingSkeletonEntity BloomingSkeletonEntity) {
        return Treasures_of_the_dead.resource("animations/entity/totd_skeleton.animation.json");
    }

    @Override
    public void setCustomAnimations(BloomingSkeletonEntity animatable, long instanceId, AnimationState<BloomingSkeletonEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("main_head");

        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD); // при нахождении сверху или снизу
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD); // при хождении по горизонтали

            head.setRotZ(entityModelData.netHeadYaw() * -0.002f);

        }
    }


}
