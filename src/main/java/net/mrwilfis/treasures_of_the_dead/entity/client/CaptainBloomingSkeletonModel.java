package net.mrwilfis.treasures_of_the_dead.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.CaptainBloomingSkeletonEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class CaptainBloomingSkeletonModel extends GeoModel<CaptainBloomingSkeletonEntity> {
    @Override
    public ResourceLocation getModelResource(CaptainBloomingSkeletonEntity captainBloomingSkeletonEntity) {
        return Treasures_of_the_dead.resource("geo/blooming_skeleton.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CaptainBloomingSkeletonEntity captainBloomingSkeletonEntity) {
        return CaptainBloomingSkeletonRenderer.LOCATION_BY_VARIANT.get(captainBloomingSkeletonEntity.getCaptainBloomingVariant());
      //  return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/totd_skeleton.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CaptainBloomingSkeletonEntity captainBloomingSkeletonEntity) {
        return Treasures_of_the_dead.resource("animations/entity/totd_skeleton.animation.json");
    }

    @Override
    public void setCustomAnimations(CaptainBloomingSkeletonEntity animatable, long instanceId, AnimationState<CaptainBloomingSkeletonEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("main_head");

        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD); // при нахождении сверху или снизу
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD); // при хождении по горизонтали

            head.setRotZ(entityModelData.netHeadYaw() * -0.002f);

        }
    }


}
