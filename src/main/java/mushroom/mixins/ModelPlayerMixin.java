package mushroom.mixins;

import mushroom.GUI.Configs;
import mushroom.Libs.RenderLib;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

import static mushroom.Libs.PlayerLib.mc;

@Mixin(ModelPlayer.class)
public class ModelPlayerMixin extends ModelBiped {

    @Unique
    public ModelRenderer left_leg;
    @Unique
    public ModelRenderer right_leg;
    @Unique
    public ModelRenderer body;
    @Unique
    public ModelRenderer eye;
    @Shadow
    public ModelRenderer bipedLeftArmwear;
    @Shadow
    public ModelRenderer bipedRightArmwear;
    @Shadow
    public ModelRenderer bipedLeftLegwear;
    @Shadow
    public ModelRenderer bipedRightLegwear;
    @Shadow
    public ModelRenderer bipedBodyWear;

    @ModifyConstant(method = "<init>", constant = @Constant(floatValue = 2.5F))
    private float fixAlexArmHeight(float original) {
        return 2F;
    }

    @Inject(method = {"render"}, at = {@At("HEAD")}, cancellable = true)
    public void renderHook(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, CallbackInfo ci) {
        if (Configs.modelModifier) {
            ci.cancel();


            if (body == null) {
                body = new ModelRenderer(this);
                body.setRotationPoint(0.0F, 0.0F, 0.0F);
                body.setTextureOffset(34, 8).addBox(-4.0F, 6.0F, -3.0F, 8, 12, 6);
                body.setTextureOffset(15, 10).addBox(-3.0F, 9.0F, 3.0F, 6, 8, 3);
                body.setTextureOffset(26, 0).addBox(-3.0F, 5.0F, -3.0F, 6, 1, 6);
                eye = new ModelRenderer(this);
                eye.setTextureOffset(0, 10).addBox(-3.0F, 7.0F, -4.0F, 6, 4, 1);
                left_leg = new ModelRenderer(this);
                left_leg.setRotationPoint(-2.0F, 18.0F, 0.0F);
                left_leg.setTextureOffset(0, 0).addBox(2.9F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
                right_leg = new ModelRenderer(this);
                right_leg.setRotationPoint(2.0F, 18.0F, 0.0F);
                right_leg.setTextureOffset(13, 0).addBox(-5.9F, 0.0F, -1.5F, 3, 6, 3);
            }


            GlStateManager.pushMatrix();
            if (entityIn == mc.thePlayer || Configs.newEveryoneModel) {
                this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;
                this.bipedHead.rotateAngleX = headPitch * 0.017453292F;
                this.bipedBody.rotateAngleY = 0.0F;
                float f = 1.0F;
                this.right_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
                this.left_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount / f;
                this.right_leg.rotateAngleY = 0.0F;
                this.left_leg.rotateAngleY = 0.0F;
                this.right_leg.rotateAngleZ = 0.0F;
                this.left_leg.rotateAngleZ = 0.0F;
                int bodyCustomColor = new Color(197, 16, 17).getRGB();
                int eyeCustomColor = new Color(254, 254, 254).getRGB();
                int legsCustomColor = new Color(122, 7, 56).getRGB();
                if (this.isChild) {
                    GlStateManager.scale(0.5F, 0.5F, 0.5F);
                    GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
                    this.body.render(scale);
                    this.left_leg.render(scale);
                    this.right_leg.render(scale);
                } else {
                    GlStateManager.translate(0.0D, -0.8D, 0.0D);
                    GlStateManager.scale(1.8D, 1.6D, 1.6D);
                    RenderLib.colorGL(bodyCustomColor);
                    GlStateManager.translate(0.0D, 0.15D, 0.0D);
                    this.body.render(scale);
                    RenderLib.colorGL(eyeCustomColor);
                    this.eye.render(scale);
                    RenderLib.colorGL(legsCustomColor);
                    GlStateManager.translate(0.0D, -0.15D, 0.0D);
                    this.left_leg.render(scale);
                    this.right_leg.render(scale);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                }
            } else {
                super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                if (isChild) {
                    float f = 2.0F;
                    GlStateManager.scale(0.5F, 0.5F, 0.5F);
                    GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
                    bipedLeftLegwear.render(scale);
                    bipedRightLegwear.render(scale);
                    bipedLeftArmwear.render(scale);
                    bipedRightArmwear.render(scale);
                    bipedBodyWear.render(scale);
                } else {
                    if (entityIn.isSneaking())
                        GlStateManager.translate(0.0F, 0.2F, 0.0F);
                    bipedLeftLegwear.render(scale);
                    bipedRightLegwear.render(scale);
                    bipedLeftArmwear.render(scale);
                    bipedRightArmwear.render(scale);
                    bipedBodyWear.render(scale);
                }
            }
            GlStateManager.popMatrix();

        }
    }
}