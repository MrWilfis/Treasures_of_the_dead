package net.mrwilfis.treasures_of_the_dead.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.chestVariants.TreasureChestEntity;
import net.mrwilfis.treasures_of_the_dead.entity.custom.skullVariants.FoulSkullEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class TreasureChestModel extends GeoModel<TreasureChestEntity> {
    @Override
    public ResourceLocation getModelResource(TreasureChestEntity TreasureChestEntity) {
        return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "geo/treasure_chest.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TreasureChestEntity TreasureChestEntity) {
          return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/treasure_chest.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TreasureChestEntity TreasureChestEntity) {
        return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "animations/entity/treasure_chest.animation.json");
    }
    @Override
    public void setCustomAnimations(TreasureChestEntity animatable, long instanceId, AnimationState<TreasureChestEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("chest");

        if (head != null) {
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD); // при нахождении сверху или снизу
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD); // при хождении по горизонтали

          //  head.setRotZ(entityModelData.netHeadYaw() * -0.002f);
        }
    }
}
