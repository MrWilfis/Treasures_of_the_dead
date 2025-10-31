package net.mrwilfis.treasures_of_the_dead.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.FoulSkullEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class FoulSkullModel extends GeoModel<FoulSkullEntity> {
    @Override
    public ResourceLocation getModelResource(FoulSkullEntity foulSkullEntity) {
        return Treasures_of_the_dead.resource("geo/foul_skull.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FoulSkullEntity foulSkullEntity) {
          return Treasures_of_the_dead.resource("textures/entity/foul_skull.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FoulSkullEntity foulSkullEntity) {
        return Treasures_of_the_dead.resource("animations/entity/totd_skull.animation.json");
    }
    @Override
    public void setCustomAnimations(FoulSkullEntity animatable, long instanceId, AnimationState<FoulSkullEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD); // при нахождении сверху или снизу
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD); // при хождении по горизонтали

          //  head.setRotZ(entityModelData.netHeadYaw() * -0.002f);
        }
    }
}
