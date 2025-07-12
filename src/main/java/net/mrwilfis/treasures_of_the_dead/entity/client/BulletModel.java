package net.mrwilfis.treasures_of_the_dead.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.BulletEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class BulletModel extends GeoModel<BulletEntity> {
    @Override
    public ResourceLocation getModelResource(BulletEntity bulletEntity) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "geo/bullet.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BulletEntity bulletEntity) {
          return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "textures/entity/bullet.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BulletEntity bulletEntity) {
        return ResourceLocation.fromNamespaceAndPath(Treasures_of_the_dead.MOD_ID, "animations/entity/bullet.animation.json");
    }
    @Override
    public void setCustomAnimations(BulletEntity bulletEntity, long instanceId, AnimationState<BulletEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD); // при нахождении сверху или снизу
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD); // при хождении по горизонтали

          //  head.setRotZ(entityModelData.netHeadYaw() * -0.002f);
        }
    }
}
