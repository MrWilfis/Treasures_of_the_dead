package net.mrwilfis.treasures_of_the_dead.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.CaptainGoldenSkeletonEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.GoldenSkeletonEntity;
import net.mrwilfis.treasures_of_the_dead.entity.variant.CaptainGoldenSkeletonVariant;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class CaptainGoldenSkeletonModel extends GeoModel<CaptainGoldenSkeletonEntity> {
    @Override
    public ResourceLocation getModelResource(CaptainGoldenSkeletonEntity captainGoldenSkeletonEntity) {
        return CaptainGoldenSkeletonRenderer.LOCATION_BY_VARIANT_MODEL.get(captainGoldenSkeletonEntity.getCaptainGoldenVariant());
    }

    @Override
    public ResourceLocation getTextureResource(CaptainGoldenSkeletonEntity captainGoldenSkeletonEntity) {
        if (captainGoldenSkeletonEntity.getIsRusted()) {
            return CaptainGoldenSkeletonRenderer.LOCATION_BY_VARIANT_RUSTED.get(captainGoldenSkeletonEntity.getCaptainGoldenVariant());
        } else {
            return CaptainGoldenSkeletonRenderer.LOCATION_BY_VARIANT.get(captainGoldenSkeletonEntity.getCaptainGoldenVariant());
        }
      //  return GoldenSkeletonRenderer.LOCATION_BY_VARIANT.get(goldenSkeletonEntity.getGoldenVariant());
      //  return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/totd_skeleton.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CaptainGoldenSkeletonEntity captainGoldenSkeletonEntity) {
        return Treasures_of_the_dead.resource("animations/entity/totd_skeleton.animation.json");
    }

    @Override
    public void setCustomAnimations(CaptainGoldenSkeletonEntity animatable, long instanceId, AnimationState<CaptainGoldenSkeletonEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("main_head");

        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD); // при нахождении сверху или снизу
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD); // при хождении по горизонтали

            head.setRotZ(entityModelData.netHeadYaw() * -0.002f);

        }
    }


}
