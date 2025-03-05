package net.mrwilfis.treasures_of_the_dead.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import org.joml.Matrix4f;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class NewArmorRenderer <T extends ArmorItem & GeoItem> extends GeoArmorRenderer<T> {
    protected BakedGeoModel lastModel = null;
    protected GeoBone legs = null;
    public NewArmorRenderer(GeoModel<T> model) {
        super(model);
    }
    //ALL this class was made by Korobeinik (serb_ninja), thank him very much!


    //add new bone for pants

    @javax.annotation.Nullable
    public GeoBone getArmorLegsBone() {
        return this.model.getBone("armorLegs").orElse(null);
    }

    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @javax.annotation.Nullable MultiBufferSource bufferSource, @javax.annotation.Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.entityRenderTranslations = new Matrix4f(poseStack.last().pose());
        this.applyBaseModel(this.baseModel);
        this.grabRelevantBones(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(this.animatable)));
        this.applyBaseTransformations(this.baseModel);
        this.scaleModelForBaby(poseStack, animatable, partialTick, isReRender);
        this.scaleModelForRender(this.scaleWidth, this.scaleHeight, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
        if (!(this.currentEntity instanceof GeoAnimatable)) {
            this.applyBoneVisibilityBySlot(this.currentSlot);
        }

    }

    protected void grabRelevantBones(BakedGeoModel bakedModel) {
        if (this.lastModel != bakedModel) {
            this.lastModel = bakedModel;
            this.head = this.getHeadBone();
            this.body = this.getBodyBone();
            this.legs = this.getArmorLegsBone();
            this.rightArm = this.getRightArmBone();
            this.leftArm = this.getLeftArmBone();
            this.rightLeg = this.getRightLegBone();
            this.leftLeg = this.getLeftLegBone();
            this.rightBoot = this.getRightBootBone();
            this.leftBoot = this.getLeftBootBone();
        }
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
        ModelPart leftLegPart;
        if (this.head != null) {
            leftLegPart = baseModel.head;
            RenderUtils.matchModelPartRot(leftLegPart, this.head);
            this.head.updatePosition(leftLegPart.x, -leftLegPart.y, leftLegPart.z);
        }

        if (this.body != null) {
            leftLegPart = baseModel.body;
            RenderUtils.matchModelPartRot(leftLegPart, this.body);
            this.body.updatePosition(leftLegPart.x, -leftLegPart.y, leftLegPart.z);
            if (this.legs != null) {
                RenderUtils.matchModelPartRot(leftLegPart, this.legs);
                this.legs.updatePosition(leftLegPart.x, -leftLegPart.y, leftLegPart.z);
            }
        }

        if (this.rightArm != null) {
            leftLegPart = baseModel.rightArm;
            RenderUtils.matchModelPartRot(leftLegPart, this.rightArm);
            this.rightArm.updatePosition(leftLegPart.x + 5.0F, 2.0F - leftLegPart.y, leftLegPart.z);
        }

        if (this.leftArm != null) {
            leftLegPart = baseModel.leftArm;
            RenderUtils.matchModelPartRot(leftLegPart, this.leftArm);
            this.leftArm.updatePosition(leftLegPart.x - 5.0F, 2.0F - leftLegPart.y, leftLegPart.z);
        }

        if (this.rightLeg != null) {
            leftLegPart = baseModel.rightLeg;
            RenderUtils.matchModelPartRot(leftLegPart, this.rightLeg);
            this.rightLeg.updatePosition(leftLegPart.x + 2.0F, 12.0F - leftLegPart.y, leftLegPart.z);
            if (this.rightBoot != null) {
                RenderUtils.matchModelPartRot(leftLegPart, this.rightBoot);
                this.rightBoot.updatePosition(leftLegPart.x + 2.0F, 12.0F - leftLegPart.y, leftLegPart.z);
            }
        }

        if (this.leftLeg != null) {
            leftLegPart = baseModel.leftLeg;
            RenderUtils.matchModelPartRot(leftLegPart, this.leftLeg);
            this.leftLeg.updatePosition(leftLegPart.x - 2.0F, 12.0F - leftLegPart.y, leftLegPart.z);
            if (this.leftBoot != null) {
                RenderUtils.matchModelPartRot(leftLegPart, this.leftBoot);
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