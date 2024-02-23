package mushroom.Features.Visual;

import mushroom.Features.Combat.AntiBot;
import mushroom.GUI.Configs;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.Libs.MathLib;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.RenderLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static mushroom.Libs.PlayerLib.mc;

public class NameTags {
    @SubscribeEvent
    public void onRender(final RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (Configs.nametags && event.entity instanceof EntityPlayer && (event.entity != mc.thePlayer || Configs.showownnametag) && event.entity.getDistanceToEntity(mc.thePlayer) < 100.0f && (AntiBot.isValidEntity(event.entity)) && (!Configs.targethud || event.entity.getDistanceToEntity(mc.thePlayer) > 6 || event.entity == mc.thePlayer || !Configs.followTargetHud) && PlayerLib.inGame()) {
            event.setCanceled(true);



            GlStateManager.alphaFunc(516, 0.1f);
            String name = event.entity.getName();
            if (event.entity == mc.thePlayer && Configs.nickhider) name = Configs.fakename;
            final double x = event.x;
            final double y = event.y;
            final double z = event.z;
            final float f = Math.max(1.4f, event.entity.getDistanceToEntity(mc.thePlayer) / 10.0f);
            final float scale = 0.016666668f * f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x + 0.0f, (float)y + event.entity.height + 0.5f, (float)z);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(-scale, -scale, scale);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            final float textWidth = (float) FontUtil.productsans19.getStringWidth(name);
            GlStateManager.disableTexture2D();

            int healthcolor = (int) ((event.entity.getHealth() / event.entity.getMaxHealth()) *255);
            healthcolor = Math.min(healthcolor, 255);

            //if (Configs.healthbaronside) {
            //    RenderLib.drawRect(-15, 20, -21, 91, new Color(23, 23, 23, 100).getRGB());
            //    RenderLib.drawRect(-16, 21, -20, ((70 * event.entity.getHealth() / event.entity.getMaxHealth())+20), new Color(255 - healthcolor, healthcolor, 0).getRGB());
            //}

            RenderLib.drawRect(-(textWidth/2) - 3.0f, (float)(FontUtil.productsans19.getHeight() + 3), (textWidth/2) + 3.0f, -3.0f, new Color(20, 20, 20, 80).getRGB());
            if (!Configs.healthbaronside) RenderLib.drawRect(-(textWidth/2) - 3.0f, (float) (FontUtil.productsans19.getHeight() + 3), (float) (((textWidth/2) + 3.0f) * ((MathLib.clamp(event.entity.getHealth() / event.entity.getMaxHealth(), 1.0, 0.0) - 0.5) * 2.0)), (float) (FontUtil.productsans19.getHeight() + 2), new Color(255 - healthcolor, healthcolor, 0).getRGB());
            GlStateManager.enableTexture2D();
            FontUtil.productsans19.drawSmoothString(name, -FontUtil.productsans19.getStringWidth(name) / 2.0, 0.0f, -1);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            FontUtil.productsans19.drawSmoothString(name, -FontUtil.productsans19.getStringWidth(name) / 2.0, 0.0f, -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();

        }
    }


    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Configs.nametags && Configs.healthbaronside) {
            for (final EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
                if ((AntiBot.isValidEntity(entityPlayer)) && (entityPlayer.getDistanceToEntity(mc.thePlayer) > 6 || !Configs.followTargetHud) && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && entityPlayer != mc.thePlayer) {
                    int healthcolor = (int) ((entityPlayer.getHealth() / entityPlayer.getMaxHealth()) *255);
                    healthcolor = Math.min(255, healthcolor);

                    double playX = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * event.partialTicks;
                    double playY = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * event.partialTicks;
                    double playZ = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * event.partialTicks;

                    RenderLib.renderBoundingBox(playX+0.9, playY, playZ, 0.11, 2.01, new Color(62, 64, 77, 255), event.partialTicks);
                    RenderLib.renderBoundingBox(playX+0.91, playY+0.01, playZ, 0.09, 1.99 * entityPlayer.getHealth() / entityPlayer.getMaxHealth(), new Color(255 - healthcolor, healthcolor, 0), event.partialTicks);
                }
            }
        }
    }
}
