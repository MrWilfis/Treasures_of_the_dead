package net.mrwilfis.treasures_of_the_dead.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.GhostEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import static net.mrwilfis.treasures_of_the_dead.TOTDAnims.animateCrossbowCharge;
import static net.mrwilfis.treasures_of_the_dead.TOTDAnims.animateCrossbowHold;

public class GhostModel extends GeoModel<GhostEntity> {
    @Override
    public ResourceLocation getModelResource(GhostEntity entity) {
        return Treasures_of_the_dead.resource("geo/ghost.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GhostEntity entity) {
        return GhostRenderer.LOCATION_BY_VARIANT.get(entity.getVariant());
      //  return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/totd_skeleton.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GhostEntity entity) {
        return Treasures_of_the_dead.resource("animations/entity/ghost.animation.json");
    }

    @Override
    public void setCustomAnimations(GhostEntity animatable, long instanceId, AnimationState<GhostEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("main_head");

        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD); // при нахождении сверху или снизу
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD); // при хождении по горизонтали

            head.setRotZ(entityModelData.netHeadYaw() * -0.002f);

        }

        if (animatable.isHoldingCrossbow()) {
            GeoBone rightArm = this.getAnimationProcessor().getBone("right_arm");
            GeoBone leftArm = this.getAnimationProcessor().getBone("left_arm");

            if (rightArm != null && leftArm != null && head != null) {

                if (animatable.isUsingItem()) {
                    animateCrossbowCharge(rightArm, leftArm, animatable, !animatable.isLeftHanded());
                } else {
                    animateCrossbowHold(rightArm, leftArm, head, !animatable.isLeftHanded());
                }
            }
        }
    }
}
