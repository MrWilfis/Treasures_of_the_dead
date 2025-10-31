package net.mrwilfis.treasures_of_the_dead.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.BlunderBombEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.VillainousSkullEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class BlunderBombModel extends GeoModel<BlunderBombEntity> {
    @Override
    public ResourceLocation getModelResource(BlunderBombEntity blunderBombEntity) {
        return Treasures_of_the_dead.resource("geo/blunder_bomb.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BlunderBombEntity blunderBombEntity) {
          return Treasures_of_the_dead.resource("textures/entity/blunder_bomb.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BlunderBombEntity blunderBombEntity) {
        return Treasures_of_the_dead.resource("animations/entity/blunder_bomb.animation.json");
    }
    @Override
    public void setCustomAnimations(BlunderBombEntity animatable, long instanceId, AnimationState<BlunderBombEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("bomb");

        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD); // при нахождении сверху или снизу
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD); // при хождении по горизонтали

          //  head.setRotZ(entityModelData.netHeadYaw() * -0.002f);
        }
    }
}
