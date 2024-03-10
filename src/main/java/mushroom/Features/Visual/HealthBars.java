package mushroom.Features.Visual;

import mushroom.Features.Combat.AntiBot;
import mushroom.GUI.ClickGUI;
import mushroom.GUI.Configs;
import mushroom.Libs.ChatLib;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.RenderLib;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static mushroom.Libs.PlayerLib.mc;

public class HealthBars {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRender(final RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (Configs.healthBar && event.entity instanceof EntityPlayer && (event.entity != mc.thePlayer) && event.entity.getDistanceToEntity(mc.thePlayer) < 100.0f && (AntiBot.isValidEntity(event.entity)) && PlayerLib.inGame()) {
            final double x = event.x;
            final double y = event.y;
            final double z = event.z;
            final float f = 1.4f;
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
            GlStateManager.disableTexture2D();

            int healthcolor = (int) ((event.entity.getHealth() / event.entity.getMaxHealth()) *255);
            healthcolor = Math.min(healthcolor, 255);

            RenderLib.drawRect(-15, 20, -21, 91, new Color(23, 23, 23, 100).getRGB());
            RenderLib.drawRect(-16, 21, -20, ((70 * event.entity.getHealth() / event.entity.getMaxHealth())+20), new Color(255 - healthcolor, healthcolor, 0).getRGB());

            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();

        }
    }
}
