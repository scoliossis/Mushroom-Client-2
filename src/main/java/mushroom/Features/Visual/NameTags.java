package mushroom.Features.Visual;

import mushroom.Features.Combat.AntiBot;
import mushroom.GUI.ClickGUI;
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
        if (Configs.nametags && event.entity instanceof EntityPlayer && (event.entity != mc.thePlayer || Configs.showownnametag) && event.entity.getDistanceToEntity(mc.thePlayer) < 100.0f && (AntiBot.isValidEntity(event.entity)) && PlayerLib.inGame()) {
            event.setCanceled(true);

            GlStateManager.alphaFunc(516, 0.1f);
            String name = event.entity.getName();
            if (event.entity == mc.thePlayer && Configs.nickhider) name = Configs.fakename;
            final double x = event.x;
            final double y = event.y;
            final double z = event.z;
            final float f = Configs.customSizeNams ? Configs.nametagsScale : Math.max(1.4f, event.entity.getDistanceToEntity(mc.thePlayer) / 10.0f);
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
            final float textWidth = (float) FontUtil.font("productsans", 19).getStringWidth(name);
            GlStateManager.disableTexture2D();

            if (Configs.backgroundNameTag) {
                RenderLib.drawRoundedRect2((int) (-(textWidth / 2) - 3.0f), -3, (int) ((textWidth) + 6.0f), FontUtil.font("productsans", 19).getHeight() + 6, 5, ClickGUI.getColor(Configs.BackgroundColor).getRGB(), true);
                if (Configs.healthBarBelow) {
                    Color[] colors = RenderLib.getColorsFade(-(textWidth / 2) - 3.0f, (textWidth / 2) - 3.0f - ((((textWidth / 2) + 3.0f) * (event.entity.getHealth() / event.entity.getMaxHealth()))), textWidth + 6.0f, ClickGUI.getColor(Configs.healthBarColor1), ClickGUI.getColor(Configs.healthBarColor2), 0.01);
                    RenderLib.drawFade((int) (-(textWidth / 2) - 3.0f), (FontUtil.font("productsans", 19).getHeight() + 1), (int) ((((textWidth) + 6.0f) * (event.entity.getHealth() / event.entity.getMaxHealth()))), 2, colors[0], colors[1]);
                }
            }

            GlStateManager.enableTexture2D();
            FontUtil.font("productsans", 19).drawSmoothString(name, -FontUtil.font("productsans", 19).getStringWidth(name) / 2.0, 0.0f, -1);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();

        }
    }
}
