package net.mrwilfis.treasures_of_the_dead;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import software.bernie.geckolib.cache.object.GeoBone;

public class TOTDAnims {
    public static void animateCrossbowHold(GeoBone rightArm, GeoBone leftArm, GeoBone head, boolean rightHanded) {
        GeoBone modelpart1 = rightHanded ? rightArm : leftArm;
        GeoBone modelpart2 = rightHanded ? leftArm : rightArm;
        modelpart1.setRotY((rightHanded ? 0.3F : -0.3F) + head.getRotY());
        modelpart2.setRotY((rightHanded ? -0.6F : 0.6F) + head.getRotY());
        modelpart1.setRotX(1.6707964F + head.getRotX() - 0.1F);
        modelpart2.setRotX(1.6F + head.getRotX());
    }

    public static void animateCrossbowCharge(GeoBone rightArm, GeoBone leftArm, LivingEntity livingEntity, boolean rightHanded) {
        GeoBone modelpart1 = rightHanded ? rightArm : leftArm;
        GeoBone modelpart2 = rightHanded ? leftArm : rightArm;
        modelpart1.setRotY(rightHanded ? 0.8F : -0.8F);
        modelpart1.setRotX(0.97079635F);
        modelpart2.setRotX(modelpart1.getRotX());
        float f = (float) CrossbowItem.getChargeDuration(livingEntity.getUseItem(), livingEntity);
        float f1 = Mth.clamp((float)livingEntity.getTicksUsingItem(), 0.0F, f);
        float f2 = f1 / f;
        modelpart2.setRotY(Mth.lerp(f2, 0.4F, 0.85F) * (float)(rightHanded ? -1 : 1));
        modelpart2.setRotX(Mth.lerp(f2, modelpart2.getRotX(), 1.5707964F));
    }
}
