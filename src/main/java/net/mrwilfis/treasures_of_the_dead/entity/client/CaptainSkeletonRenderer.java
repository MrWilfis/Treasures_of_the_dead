package net.mrwilfis.treasures_of_the_dead.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.Util;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.mrwilfis.treasures_of_the_dead.Treasures_of_the_dead;
import net.mrwilfis.treasures_of_the_dead.entity.custom.CaptainSkeletonEntity;
import net.mrwilfis.treasures_of_the_dead.entity.variant.CaptainSkeletonVariant;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class CaptainSkeletonRenderer extends GeoEntityRenderer<CaptainSkeletonEntity> {

    private static final String LEFT_HAND = "left_hand";
    private static final String RIGHT_HAND = "right_hand";
    private static final String LEFT_BOOT = "left_boot";
    private static final String RIGHT_BOOT = "right_boot";
    private static final String LEFT_ARMOR_LEG = "left_armor_leg";
    private static final String RIGHT_ARMOR_LEG = "right_armor_leg";
    private static final String CHESTPLATE = "chestplate";
    private static final String CHEST_LEGGINGS = "chest_leggings";
    private static final String HELMET = "helmet";
    private static final String RIGHT_SLEEVE = "right_sleeve";
    private static final String LEFT_SLEEVE = "left_sleeve";

    protected ItemStack mainHandItem;
    protected ItemStack offhandItem;

    public static final Map<CaptainSkeletonVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(CaptainSkeletonVariant.class), (p_114874_) -> {
                p_114874_.put(CaptainSkeletonVariant.DEFAULT,
                        new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/pirate_skeleton1.png"));
                p_114874_.put(CaptainSkeletonVariant.VAR1,
                        new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/pirate_skeleton2.png"));
                p_114874_.put(CaptainSkeletonVariant.VAR2,
                        new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/pirate_skeleton3.png"));
                p_114874_.put(CaptainSkeletonVariant.VAR3,
                        new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/pirate_skeleton4.png"));
                p_114874_.put(CaptainSkeletonVariant.VAR4,
                        new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/pirate_skeleton5.png"));
                p_114874_.put(CaptainSkeletonVariant.VAR5,
                        new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/pirate_skeleton6.png"));
                p_114874_.put(CaptainSkeletonVariant.VAR6,
                        new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/captain_skeleton1.png"));
                p_114874_.put(CaptainSkeletonVariant.VAR7,
                        new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/captain_skeleton2.png"));
            });


    public CaptainSkeletonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CaptainSkeletonModel());
        this.shadowRadius = 0.5f;

        this.addRenderLayer(new ItemArmorGeoLayer<CaptainSkeletonEntity>(this) {
            @Nullable
            protected ItemStack getArmorItemForBone(GeoBone bone, CaptainSkeletonEntity animatable) {
             //   ItemStack var10000;
                return switch (bone.getName()) {
                    case LEFT_BOOT, RIGHT_BOOT -> this.bootsStack;
                    case LEFT_ARMOR_LEG, RIGHT_ARMOR_LEG, CHEST_LEGGINGS -> this.leggingsStack;
                    case CHESTPLATE, RIGHT_SLEEVE, LEFT_SLEEVE -> this.chestplateStack;
                    case HELMET -> this.helmetStack;
                    default -> null;
                };

              //  return var10000;
            }

            @Nonnull
            protected EquipmentSlot getEquipmentSlotForBone(GeoBone bone, ItemStack stack, CaptainSkeletonEntity animatable) {
                //EquipmentSlot var10000;
                return switch (bone.getName()) {
                    case LEFT_BOOT, RIGHT_BOOT -> EquipmentSlot.FEET;
                    case LEFT_ARMOR_LEG, RIGHT_ARMOR_LEG, CHEST_LEGGINGS -> EquipmentSlot.LEGS;
                    case RIGHT_SLEEVE -> !animatable.isLeftHanded() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                    case LEFT_SLEEVE -> animatable.isLeftHanded() ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                    case CHESTPLATE -> EquipmentSlot.CHEST;
                    case HELMET -> EquipmentSlot.HEAD;
                    default -> super.getEquipmentSlotForBone(bone, stack, animatable);
                };

                //return var10000;
            }
            @Nonnull
            protected ModelPart getModelPartForBone(GeoBone bone, EquipmentSlot slot, ItemStack stack, CaptainSkeletonEntity animatable, HumanoidModel<?> baseModel) {
                //ModelPart var10000;
                return switch (bone.getName()) {
                    case LEFT_BOOT, LEFT_ARMOR_LEG -> baseModel.leftLeg;
                    case RIGHT_BOOT, RIGHT_ARMOR_LEG -> baseModel.rightLeg;
                    case RIGHT_SLEEVE -> baseModel.rightArm;
                    case LEFT_SLEEVE -> baseModel.leftArm;
                    case CHESTPLATE, CHEST_LEGGINGS -> baseModel.body;
                    case HELMET -> baseModel.head;
                    default -> super.getModelPartForBone(bone, slot, stack, animatable, baseModel);
                };

                //return var10000;
            }

        });
        this.addRenderLayer(new BlockAndItemGeoLayer<CaptainSkeletonEntity>(this) {
            @Nullable
            protected ItemStack getStackForBone(GeoBone bone, CaptainSkeletonEntity animatable) {
                //ItemStack var10000;
                return switch (bone.getName()) {
                    case LEFT_HAND -> animatable.isLeftHanded() ?
                            CaptainSkeletonRenderer.this.mainHandItem : CaptainSkeletonRenderer.this.offhandItem;
                    case RIGHT_HAND -> animatable.isLeftHanded() ?
                            CaptainSkeletonRenderer.this.offhandItem : CaptainSkeletonRenderer.this.mainHandItem;
                    default -> null;
                };

                //return var10000;
            }

            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, CaptainSkeletonEntity animatable) {
                //ItemDisplayContext var10000;
                return switch (bone.getName()) {
                    case LEFT_HAND, RIGHT_HAND -> ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                    default -> ItemDisplayContext.NONE;
                };

                // var10000;
            }

            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, CaptainSkeletonEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                if (stack == CaptainSkeletonRenderer.this.mainHandItem) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0.0, 0.125, -0.25);
                    }
                } else if (stack == CaptainSkeletonRenderer.this.offhandItem) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0.0, 0.125, 0.25);
                        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                    }
                }

                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(CaptainSkeletonEntity instance) {
        return LOCATION_BY_VARIANT.get(instance.getCaptainVariant());
        //return new ResourceLocation(Treasures_of_the_dead.MOD_ID, "textures/entity/totd_skeleton.png");
    }

    public void preRender(PoseStack poseStack, CaptainSkeletonEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        this.mainHandItem = animatable.getMainHandItem();
        this.offhandItem = animatable.getOffhandItem();
    }

    @Override
    public void render(CaptainSkeletonEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }



}
