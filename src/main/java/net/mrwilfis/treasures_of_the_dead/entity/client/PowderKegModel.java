package net.mrwilfis.treasures_of_the_dead.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.powderKegsVariants.PowderKegEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class PowderKegModel extends GeoModel<PowderKegEntity> {
    @Override
    public ResourceLocation getModelResource(PowderKegEntity powderKegEntity) {
        return Treasures_of_the_dead.resource("geo/powder_keg.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PowderKegEntity powderKegEntity) {
          return Treasures_of_the_dead.resource("textures/entity/powder_keg.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PowderKegEntity powderKegEntity) {
        return Treasures_of_the_dead.resource("animations/entity/powder_keg.animation.json");
    }
    @Override
    public void setCustomAnimations(PowderKegEntity animatable, long instanceId, AnimationState<PowderKegEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("keg");

        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD); // при нахождении сверху или снизу
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD); // при хождении по горизонтали

          //  head.setRotZ(entityModelData.netHeadYaw() * -0.002f);
        }
    }
}
