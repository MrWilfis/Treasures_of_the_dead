package net.mrwilfis.treasures_of_the_dead;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;

public class TOTDAnims {
    public static void animateCrossbowHold(CoreGeoBone rightArm, CoreGeoBone leftArm, CoreGeoBone head, boolean rightHanded) {
        CoreGeoBone modelpart1 = rightHanded ? rightArm : leftArm;
        CoreGeoBone modelpart2 = rightHanded ? leftArm : rightArm;
        modelpart1.setRotY((rightHanded ? 0.3F : -0.3F) + head.getRotY());
        modelpart2.setRotY((rightHanded ? -0.6F : 0.6F) + head.getRotY());
        modelpart1.setRotX(1.6707964F + head.getRotX() - 0.1F);
        modelpart2.setRotX(1.6F + head.getRotX());
    }

    public static void animateCrossbowCharge(CoreGeoBone rightArm, CoreGeoBone leftArm, LivingEntity livingEntity, boolean rightHanded) {
        CoreGeoBone modelpart1 = rightHanded ? rightArm : leftArm;
        CoreGeoBone modelpart2 = rightHanded ? leftArm : rightArm;
        modelpart1.setRotY(rightHanded ? 0.8F : -0.8F);
        modelpart1.setRotX(0.97079635F);
        modelpart2.setRotX(modelpart1.getRotX());
        float f = (float) CrossbowItem.getChargeDuration(livingEntity.getUseItem());
        float f1 = Mth.clamp((float)livingEntity.getTicksUsingItem(), 0.0F, f);
        float f2 = f1 / f;
        modelpart2.setRotY(Mth.lerp(f2, 0.4F, 0.85F) * (float)(rightHanded ? -1 : 1));
        modelpart2.setRotX(Mth.lerp(f2, modelpart2.getRotX(), 1.5707964F));
    }
}
