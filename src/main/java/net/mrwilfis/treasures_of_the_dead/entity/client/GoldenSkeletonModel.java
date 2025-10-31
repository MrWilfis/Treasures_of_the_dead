package net.mrwilfis.treasures_of_the_dead.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.BloomingSkeletonEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.GoldenSkeletonEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GoldenSkeletonModel extends GeoModel<GoldenSkeletonEntity> {
    @Override
    public ResourceLocation getModelResource(GoldenSkeletonEntity goldenSkeletonEntity) {
        return Treasures_of_the_dead.resource("geo/golden_skeleton.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GoldenSkeletonEntity goldenSkeletonEntity) {
        if (goldenSkeletonEntity.getIsRusted()) {
            return GoldenSkeletonRenderer.LOCATION_BY_VARIANT_RUSTED.get(goldenSkeletonEntity.getGoldenVariant());
        } else {
            return GoldenSkeletonRenderer.LOCATION_BY_VARIANT.get(goldenSkeletonEntity.getGoldenVariant());
        }
      //  return GoldenSkeletonRenderer.LOCATION_BY_VARIANT.get(goldenSkeletonEntity.getGoldenVariant());
      //  return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/totd_skeleton.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GoldenSkeletonEntity goldenSkeletonEntity) {
        return Treasures_of_the_dead.resource("animations/entity/totd_skeleton.animation.json");
    }

    @Override
    public void setCustomAnimations(GoldenSkeletonEntity animatable, long instanceId, AnimationState<GoldenSkeletonEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("main_head");

        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD); // при нахождении сверху или снизу
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD); // при хождении по горизонтали

            head.setRotZ(entityModelData.netHeadYaw() * -0.002f);

        }
    }


}
