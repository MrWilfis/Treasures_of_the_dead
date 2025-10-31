package net.mrwilfis.treasures_of_the_dead.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.VillainousSkullEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class VillainousSkullModel extends GeoModel<VillainousSkullEntity> {
    @Override
    public ResourceLocation getModelResource(VillainousSkullEntity villainousSkullEntity) {
        return Treasures_of_the_dead.resource("geo/villainous_skull.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VillainousSkullEntity villainousSkullEntity) {
          return Treasures_of_the_dead.resource("textures/entity/villainous_skull.png");
    }

    @Override
    public ResourceLocation getAnimationResource(VillainousSkullEntity villainousSkullEntity) {
        return Treasures_of_the_dead.resource("animations/entity/totd_skull.animation.json");
    }
    @Override
    public void setCustomAnimations(VillainousSkullEntity animatable, long instanceId, AnimationState<VillainousSkullEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD); // при нахождении сверху или снизу
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD); // при хождении по горизонтали

          //  head.setRotZ(entityModelData.netHeadYaw() * -0.002f);
        }
    }
}
