package mushroom.mixins;

import mushroom.Features.Combat.Killaura;
import mushroom.GUI.Configs;
import mushroom.Libs.ChatLib;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSword;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.MathHelper;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { ItemRenderer.class }, priority = 1)
public abstract class AnimationsMixin {
    @Shadow
    private float prevEquippedProgress;
    @Shadow
    private float equippedProgress;
    @Shadow
    @Final
    private Minecraft mc;
    @Shadow
    private ItemStack itemToRender;

    @Shadow
    protected abstract void rotateArroundXAndY(final float p0, final float p1);

    @Shadow
    protected abstract void setLightMapFromPlayer(final AbstractClientPlayer p0);

    @Shadow
    protected abstract void rotateWithPlayerRotations(final EntityPlayerSP p0, final float p1);

    @Shadow
    protected abstract void renderItemMap(final AbstractClientPlayer p0, final float p1, final float p2, final float p3);

    @Shadow
    protected abstract void performDrinking(final AbstractClientPlayer p0, final float p1);

    @Shadow
    protected abstract void doItemUsedTransformations(final float p0);

    @Shadow
    public abstract void renderItem(final EntityLivingBase p0, final ItemStack p1, final ItemCameraTransforms.TransformType p2);

    @Shadow
    protected abstract void renderPlayerArm(final AbstractClientPlayer p0, final float p1, final float p2);

    @Shadow
    protected abstract void doBowTransformations(final float p0, final AbstractClientPlayer p1);

    /**
     * @author mojang
     * @reason what does reason mean
     */

    @Overwrite
    public void renderItemInFirstPerson(float partialTicks) {
        float f = 1.0f - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        final AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
        //abstractclientplayer.swingProgress *= 0.5f;
        float f2 = abstractclientplayer.getSwingProgress(partialTicks);

        final float f3 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        final float f4 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        this.rotateArroundXAndY(f3, f4);
        this.setLightMapFromPlayer(abstractclientplayer);
        this.rotateWithPlayerRotations((EntityPlayerSP)abstractclientplayer, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if (this.itemToRender != null) {

            EnumAction enumaction = this.itemToRender.getItemUseAction();
            //(Configs.autoblockmode != 4 || Killaura.isBlocking)
            boolean fakeblocksunglas = (Killaura.target != null && Configs.autoblockmode != 0 && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && mc.thePlayer.getDistanceToEntity(Killaura.target) < (Configs.aurareach) && (Configs.autoblockmode != 4 || (Killaura.isBlocking || !Configs.showBlocking)));
            if (fakeblocksunglas) enumaction = EnumAction.BLOCK;

            if (this.itemToRender.getItem() instanceof ItemMap) {
                this.renderItemMap(abstractclientplayer, f3, f, f2);
            }
            else if (abstractclientplayer.getItemInUseCount() > 0 || fakeblocksunglas) {
                switch (enumaction) {
                    case NONE:
                        this.transformFirstPersonItem(f, 0.0F);
                        break;

                    case EAT:
                    case DRINK:
                        this.performDrinking(abstractclientplayer, partialTicks);
                        this.transformFirstPersonItem(f, 0.0F);
                        break;

                    case BLOCK:
                        if (Configs.animations) {

                            switch (Configs.animationmode) {
                                case 0:
                                    this.transformFirstPersonItem(f, f2);
                                    this.doBlockTransformations();
                                    break;
                                case 1:
                                    this.transformFirstPersonItem(f, 0.0f);
                                    this.doBlockTransformations();
                                    final float var19 = MathHelper.sin(MathHelper.sqrt_float(f2) * 3.1415927f);
                                    GlStateManager.translate(-0.05f, 0.6f, 0.3f);
                                    GlStateManager.rotate(-var19 * 70.0f / 2.0f, -8.0f, -0.0f, 9.0f);
                                    GlStateManager.rotate(-var19 * 70.0f, 1.5f, -0.4f, -0.0f);
                                    break;
                                case 2:
                                    GlStateManager.rotate((float)(System.currentTimeMillis() / 3L % 360L), 0.0f, 0.0f, -0.1f);
                                    this.transformFirstPersonItem(f / 1.6f, 0.0f);
                                    this.doBlockTransformations();
                                    break;

                                case 3:
                                    this.transformFirstPersonItem(f, -f2);
                                    this.doBlockTransformations();
                                    break;

                                case 4:
                                    float f5 = MathHelper.sin(MathHelper.sqrt_float(f2) * 3.1415927f);
                                    this.transformFirstPersonItem(f / 2.0f - 0.18f, 0.0f);
                                    GL11.glRotatef(f5 * 60.0f / 2.0f, -f5 / 2.0f, -0.0f, -16.0f);
                                    GL11.glRotatef(-f5 * 30.0f, 1.0f, f5 / 2.0f, -1.0f);
                                    this.doBlockTransformations();
                                    break;
                                case 5:
                                    this.transformFirstPersonItem(f, 0.0f);
                                    this.doBlockTransformations();
                                    final float var24 = MathHelper.sin(MathHelper.sqrt_float(f2) * 3.1415927f);
                                    GlStateManager.translate(-0.05f, 0.6f, 0.3f);
                                    GlStateManager.rotate(-70.0f / 2.0f, -8.0f, -0.0f, 9.0f * var24);
                                    GlStateManager.rotate(-70.0f, 1.5f, -0.4f, -0.1f * var24);
                                    break;

                                case 6:
                                    this.transformFirstPersonItem(f, 0.0f);
                                    this.doBlockTransformations();
                                    final float var1 = MathHelper.sin(MathHelper.sqrt_float(f2) * 3.1415927f);
                                    GlStateManager.translate(-0.05f, 0.6f, 0.3f);
                                    GlStateManager.rotate(-var1 * 70.0f / 2.0f, -8.0f, -var1, 9.0f);
                                    GlStateManager.rotate(-var1 * 70.0f, 1.5f, -0.4f * var1, -0.0f);
                                    break;
                                case 7:
                                    this.transformFirstPersonItem(f, f2);
                                    this.doBlockTransformations();
                                    final float var7 = MathHelper.sin(MathHelper.sqrt_float(f2) * 3.1415927f);
                                    GlStateManager.translate(-0.05f, 0.6f, 0.3f);
                                    GlStateManager.rotate(-var7 * 70.0f / 2.0f, -8.0f, -var7, 9.0f);
                                    GlStateManager.rotate(-var7 * 70.0f, 1.5f, -0.4f * var7, -0.0f);
                                    break;
                            }
                            break;
                        }
                        this.transformFirstPersonItem(f, 0.0f);
                        this.doBlockTransformations();
                        break;

                    case BOW:
                        this.doItemUsedTransformations(f2);
                        this.transformFirstPersonItem(f, 0.0F);
                        this.doBowTransformations(partialTicks, abstractclientplayer);
                }
            }
            else {

                if (Configs.smoothSwing && Configs.animations) {
                    final float f5 = MathHelper.sin(MathHelper.sqrt_float(f2) * 3.1415927f);
                    this.transformFirstPersonItem(f / 2.0f - 0.18f, 0.0f);
                    GL11.glRotatef(f5 * 60.0f / 2.0f, -f5 / 2.0f, -0.0f, -16.0f);
                    GL11.glRotatef(-f5 * 30.0f, 1.0f, f5 / 2.0f, -1.0f);
                }
                else {
                    this.doItemUsedTransformations(f2);
                    this.transformFirstPersonItem(f, f2);
                }
            }

            this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        }
        else if (!abstractclientplayer.isInvisible()) {
            this.renderPlayerArm(abstractclientplayer, f, f2);
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void transformFirstPersonItem(float equipProgress, float swingProgress) {
        float size = Configs.animationsSize;
        float x = Configs.animationsX;
        float y = Configs.animationsY;
        float z = Configs.animationsZ;
        if (!Configs.animations) {
            x = 1;
            y = 1;
            z = 1;
            size = 1;
        }
        GlStateManager.translate(0.56f * x, -0.52f * y, -0.71999997f * z);
        GlStateManager.translate(0.0f, equipProgress * -0.6f, 0.0f);
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
        final float f = MathHelper.sin(swingProgress * swingProgress * 3.1415927f);
        final float f2 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * 3.1415927f);
        GlStateManager.rotate(f * -20.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(f2 * -20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(f2 * -80.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.4f * size, 0.4f * size, 0.4f * size);
    }


    /**
     * @author
     * @reason
     */
    @Overwrite
    private void doBlockTransformations() {
        GlStateManager.translate(-0.5F, 0.2F, 0.0F);
        GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
    }

}