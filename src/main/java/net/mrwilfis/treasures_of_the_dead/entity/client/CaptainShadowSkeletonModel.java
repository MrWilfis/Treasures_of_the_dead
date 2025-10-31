package net.mrwilfis.treasures_of_the_dead.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.CaptainShadowSkeletonEntity;
import net.mrwilfis.treasures_of_the_dead.entity.variant.CaptainShadowSkeletonVariant;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class CaptainShadowSkeletonModel extends GeoModel<CaptainShadowSkeletonEntity> {
    @Override
    public ResourceLocation getModelResource(CaptainShadowSkeletonEntity captainShadowSkeletonEntity) {
        if (captainShadowSkeletonEntity.getCaptainShadowVariant().equals(CaptainShadowSkeletonVariant.VAR1)) {
            return Treasures_of_the_dead.resource("geo/captain_shadow_skeleton1.geo.json");
        } else if (captainShadowSkeletonEntity.getCaptainShadowVariant().equals(CaptainShadowSkeletonVariant.VAR2)) {
            return Treasures_of_the_dead.resource("geo/captain_shadow_skeleton2.geo.json");
        } else if (captainShadowSkeletonEntity.getCaptainShadowVariant().equals(CaptainShadowSkeletonVariant.VAR3)) {
            return Treasures_of_the_dead.resource("geo/captain_shadow_skeleton2.geo.json");
        } else {
            return Treasures_of_the_dead.resource("geo/captain_shadow_skeleton1.geo.json");
        }
    }

    @Override
    public ResourceLocation getTextureResource(CaptainShadowSkeletonEntity captainShadowSkeletonEntity) {
        return CaptainShadowSkeletonRenderer.LOCATION_BY_VARIANT.get(captainShadowSkeletonEntity.getCaptainShadowVariant());
      //  return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/totd_skeleton.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CaptainShadowSkeletonEntity captainShadowSkeletonEntityy) {
        return Treasures_of_the_dead.resource("animations/entity/totd_skeleton.animation.json");
    }

    @Override
    public void setCustomAnimations(CaptainShadowSkeletonEntity animatable, long instanceId, AnimationState<CaptainShadowSkeletonEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("main_head");

        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD); // при нахождении сверху или снизу
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD); // при хождении по горизонтали

            head.setRotZ(entityModelData.netHeadYaw() * -0.002f);

        }
    }


}
