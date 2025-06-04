package net.mrwilfis.treasures_of_the_dead.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.RenderUtil;

public class NewArmorRenderer <T extends ArmorItem & GeoItem> extends GeoArmorRenderer<T> {
    protected BakedGeoModel lastModel = null;
    protected GeoBone legs = null;
    public NewArmorRenderer(GeoModel<T> model) {
        super(model);
    }
    //ALL this class was made by Korobeinik (serb_ninja), thank him very much!


    //add new bone for pants

    @javax.annotation.Nullable
    public GeoBone getArmorLegsBone(GeoModel<T> model) {
        return model.getBone("armorLegs").orElse(null);
    }

    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource,
                          @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight,
                          int packedOverlay, int colour) {
        this.entityRenderTranslations = new Matrix4f(poseStack.last().pose());

        this.applyBaseModel(this.baseModel);
        this.grabRelevantBones(model);
        this.applyBaseTransformations(this.baseModel);
        this.scaleModelForBaby(poseStack, animatable, partialTick, isReRender);
        this.scaleModelForRender(this.scaleWidth, this.scaleHeight, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
        if (!(this.currentEntity instanceof GeoAnimatable)) {
            this.applyBoneVisibilityBySlot(this.currentSlot);
        }

    }

    protected void grabRelevantBones(BakedGeoModel bakedModel) {
        if (this.lastModel == bakedModel)
            return;

        GeoModel<T> model = getGeoModel();

        this.lastModel = bakedModel;
        this.head = this.getHeadBone(model);
        this.body = this.getBodyBone(model);
        this.legs = this.getArmorLegsBone(model);
        this.rightArm = this.getRightArmBone(model);
        this.leftArm = this.getLeftArmBone(model);
        this.rightLeg = this.getRightLegBone(model);
        this.leftLeg = this.getLeftLegBone(model);
        this.rightBoot = this.getRightBootBone(model);
        this.leftBoot = this.getLeftBootBone(model);

    }

    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        this.setAllVisible(false);
        switch (currentSlot) {
            case HEAD:
                this.setBoneVisible(this.head, true);
                break;
            case CHEST:
                this.setBoneVisible(this.body, true);
                this.setBoneVisible(this.rightArm, true);
                this.setBoneVisible(this.leftArm, true);
                break;
            case LEGS:
                this.setBoneVisible(this.legs, true);
                this.setBoneVisible(this.rightLeg, true);
                this.setBoneVisible(this.leftLeg, true);
                break;
            case FEET:
                this.setBoneVisible(this.rightBoot, true);
                this.setBoneVisible(this.leftBoot, true);
        }
    }

    public void applyBoneVisibilityByPart(EquipmentSlot currentSlot, ModelPart currentPart, HumanoidModel<?> model) {
        this.setAllVisible(false);
        currentPart.visible = true;
        GeoBone bone = null;
        if (currentPart != model.hat && currentPart != model.head) {
            if (currentPart == model.body) {
                bone = currentSlot == EquipmentSlot.LEGS ? this.legs : this.body;
            } else if (currentPart == model.leftArm) {
                bone = this.leftArm;
            } else if (currentPart == model.rightArm) {
                bone = this.rightArm;
            } else if (currentPart == model.leftLeg) {
                bone = currentSlot == EquipmentSlot.FEET ? this.leftBoot : this.leftLeg;
            } else if (currentPart == model.rightLeg) {
                bone = currentSlot == EquipmentSlot.FEET ? this.rightBoot : this.rightLeg;
            }
        } else {
            bone = this.head;
        }

        if (bone != null) {
            bone.setHidden(false);
        }

    }

    protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
        if (this.head != null) {
            ModelPart headPart = baseModel.head;

            RenderUtil.matchModelPartRot(headPart, this.head);
            this.head.updatePosition(headPart.x, -headPart.y, headPart.z);
        }

        if (this.body != null) {
            ModelPart bodyPart = baseModel.body;

            RenderUtil.matchModelPartRot(bodyPart, this.body);
            this.body.updatePosition(bodyPart.x, -bodyPart.y, bodyPart.z);
            if (this.legs != null) {
                RenderUtil.matchModelPartRot(bodyPart, this.legs);
                this.legs.updatePosition(bodyPart.x, -bodyPart.y, bodyPart.z);
            }
        }

        if (this.rightArm != null) {
            ModelPart rightArmPart = baseModel.rightArm;

            RenderUtil.matchModelPartRot(rightArmPart, this.rightArm);
            this.rightArm.updatePosition(rightArmPart.x + 5.0F, 2.0F - rightArmPart.y, rightArmPart.z);
        }

        if (this.leftArm != null) {
            ModelPart leftArmPart = baseModel.leftArm;

            RenderUtil.matchModelPartRot(leftArmPart, this.leftArm);
            this.leftArm.updatePosition(leftArmPart.x - 5.0F, 2.0F - leftArmPart.y, leftArmPart.z);
        }

        if (this.rightLeg != null) {
            ModelPart rightLegPart = baseModel.rightLeg;

            RenderUtil.matchModelPartRot(rightLegPart, this.rightLeg);
            this.rightLeg.updatePosition(rightLegPart.x + 2.0F, 12.0F - rightLegPart.y, rightLegPart.z);
            if (this.rightBoot != null) {
                RenderUtil.matchModelPartRot(rightLegPart, this.rightBoot);
                this.rightBoot.updatePosition(rightLegPart.x + 2.0F, 12.0F - rightLegPart.y, rightLegPart.z);
            }
        }

        if (this.leftLeg != null) {
            ModelPart leftLegPart = baseModel.leftLeg;
            RenderUtil.matchModelPartRot(leftLegPart, this.leftLeg);
            this.leftLeg.updatePosition(leftLegPart.x - 2.0F, 12.0F - leftLegPart.y, leftLegPart.z);
            if (this.leftBoot != null) {
                RenderUtil.matchModelPartRot(leftLegPart, this.leftBoot);
                this.leftBoot.updatePosition(leftLegPart.x - 2.0F, 12.0F - leftLegPart.y, leftLegPart.z);
            }
        }

    }

    @Override
    public void setAllVisible(boolean pVisible) {
        super.setAllVisible(pVisible);
        this.setBoneVisible(this.legs, pVisible);
    }
}