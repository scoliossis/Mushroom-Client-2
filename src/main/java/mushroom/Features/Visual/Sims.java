package mushroom.Features.Visual;

import mushroom.GUI.Configs;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;


import java.awt.*;

public class Sims {

    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (Configs.simscryst && (Minecraft.getMinecraft().gameSettings.thirdPersonView != 0 || Configs.firstPersontoo)) {
            drawPointyCircle(Minecraft.getMinecraft().thePlayer, event.partialTicks);
        }
    }

    public static void drawPointyCircle(final EntityLivingBase entity, final float partialTicks) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        GlStateManager.disableLighting();

        GL11.glTranslated(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosX, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosY + entity.height + (entity.isSneaking() ? (Configs.crytalpos - 0.23000000417232513) : Configs.crytalpos), entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosZ);
        GL11.glRotatef(((entity.ticksExisted + partialTicks) * Configs.spinspeed) - 90.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-(Minecraft.getMinecraft().thePlayer.prevRotationYawHead + (Minecraft.getMinecraft().thePlayer.rotationYawHead - Minecraft.getMinecraft().thePlayer.prevRotationYawHead) * partialTicks), 0.0f, 1.0f, 0.0f);
        final double radius = Configs.radiusCrstal;
        GL11.glLineWidth(2.0f);
        GL11.glBegin(2);

        int angles = (int) Configs.angles;

        for (int i = 0; i <= angles; ++i) {
            final Color color = getColor(i, (int)angles, false);
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
            GL11.glVertex3d(Math.cos(i * 3.141592653589793 / (angles / 2.0)) * radius, 0.0, Math.sin(i * 3.141592653589793 / (angles / 2.0)) * radius);
        }
        GL11.glEnd();
        GL11.glBegin(6);
        final Color c1 = getColor(0.0, angles, true);
        GL11.glColor4f(c1.getRed() / 255.0f, c1.getGreen() / 255.0f, c1.getBlue() / 255.0f, 0.8f);
        GL11.glVertex3d(0.0, Configs.heightCrystal, 0.0);
        for (int j = 0; j <= angles; ++j) {
            final Color color2 = getColor(j, angles, false);
            GL11.glColor4f(color2.getRed() / 255.0f, color2.getGreen() / 255.0f, color2.getBlue() / 255.0f, 0.3f);
            GL11.glVertex3d(Math.cos(j * 3.141592653589793 / (angles / 2.0)) * radius, 0.0, Math.sin(j * 3.141592653589793 / (angles / 2.0)) * radius);
        }


        GL11.glVertex3d(0.0, Configs.heightCrystal, 0.0);

        for (int i = 0; i <= angles; ++i) {
            final Color color = getColor(i, angles, false);
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
            GL11.glVertex3d(Math.cos(i * 3.141592653589793 / (angles / 2.0)) * radius, 0.0, Math.sin(i * 3.141592653589793 / (angles / 2.0)) * radius);
        }


        if (Configs.crystalMode == 0) {
            GL11.glEnd();
            GL11.glBegin(6);
            GL11.glColor4f(c1.getRed() / 255.0f, c1.getGreen() / 255.0f, c1.getBlue() / 255.0f, 0.8f);
            GL11.glVertex3d(0.0, -Configs.heightCrystal, 0.0);
            for (int j = 0; j <= angles; ++j) {
                final Color color2 = getColor(j, angles, false);
                GL11.glColor4f(color2.getRed() / 255.0f, color2.getGreen() / 255.0f, color2.getBlue() / 255.0f, 0.3f);
                GL11.glVertex3d(Math.cos(j * 3.141592653589793 / (angles / 2.0)) * radius, 0, Math.sin(j * 3.141592653589793 / (angles / 2.0)) * radius);
            }
            GL11.glVertex3d(0.0, -Configs.heightCrystal, 0.0);
        }

        GL11.glEnd();
        GL11.glShadeModel(7424);
        GL11.glEnable(2884);
        GlStateManager.resetColor();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }


    public static Color getColor(final double i, final double max, final boolean first) {

        // more colors soon
        return new Color(0, 200, 0);
    }
}
