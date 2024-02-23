package mushroom.Features.Visual;

import mushroom.Features.Combat.AntiBot;
import mushroom.GUI.Configs;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.Libs.MathLib;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.RenderLib;
import mushroom.Libs.events.GuiChatEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;

import static mushroom.Libs.PlayerLib.isNPC;
import static mushroom.Libs.PlayerLib.mc;

public class TargetHud {
    @SubscribeEvent
    public void onRender(final RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (Configs.targethud && event.entity instanceof EntityPlayer && event.entity != mc.thePlayer && event.entity.getDistanceToEntity(mc.thePlayer) < 6 && AntiBot.isValidEntity(event.entity)) {
            if (Configs.followTargetHud) {
                event.setCanceled(true);
                GlStateManager.alphaFunc(516, 0.1f);
                final String name = event.entity.getName();
                final double x = event.x;
                final double y = event.y;
                final double z = event.z;
                final float f = Math.max(1.4f, event.entity.getDistanceToEntity(mc.thePlayer) / 10.0f);
                final float scale = 0.016666668f * f;
                GlStateManager.pushMatrix();
                GlStateManager.translate((float) x, (float) y + event.entity.height - 0.7, (float) z);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
                GlStateManager.scale(-scale, -scale, scale);
                GlStateManager.disableLighting();
                GlStateManager.depthMask(false);
                GlStateManager.disableDepth();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                float textWidth = (float) FontUtil.productsans19.getStringWidth(name);
                // if (textWidth < FontUtil.comicsans19.getStringWidth("h: " + Math.round(event.entity.getHealth())) + FontUtil.comicsans19.getStringWidth("d: " + Math.round(event.entity.getDistanceToEntity(mc.thePlayer))) + 6) {
                //    textWidth = (float) (FontUtil.comicsans19.getStringWidth("h: " + Math.round(event.entity.getHealth())) + FontUtil.comicsans19.getStringWidth("d: " + Math.round(event.entity.getDistanceToEntity(mc.thePlayer))) + 6);
                //}
                GlStateManager.disableTexture2D();
                RenderLib.drawRect(0, 20, textWidth + 30, -3.0f, new Color(20, 20, 20, 80).getRGB());
                int healthcolor = (int) ((event.entity.getHealth() / event.entity.getMaxHealth()) * 255);
                if (healthcolor > 255) {
                    healthcolor = 255;
                }
                RenderLib.drawRect(0, 18, ((textWidth + 30) * event.entity.getHealth() / event.entity.getMaxHealth()), 20, new Color(255 - healthcolor, healthcolor, 0).getRGB());
                RenderLib.drawHead(4, 1, 16, 16, (AbstractClientPlayer) event.entity);

                GlStateManager.enableTexture2D();
                FontUtil.productsans19.drawSmoothString(name, 25, 0, -1);

                float health = Float.parseFloat(new DecimalFormat("#.##").format(event.entity.getHealth()).replaceAll(",", "."));
                FontUtil.productsans19.drawSmoothString(String.valueOf(health), 25, 10, -1);

                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                FontUtil.productsans19.drawSmoothString(name, 25, 0, -1);
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.popMatrix();
            }
        }
    }

    int targetHudX = 0;
    int targetHudY = 0;
    float targetHudW = 45 + targetHudX;
    float targetHudH = 40 + targetHudY;

    @SubscribeEvent
    public void renderString(TickEvent.RenderTickEvent tick) {

        if (Configs.targethud && PlayerLib.inGame()) {
            if (!Configs.followTargetHud) {
                for (final EntityPlayer entityPlayer : mc.theWorld.playerEntities) {

                    if (entityPlayer != mc.thePlayer && entityPlayer.getDistanceToEntity(mc.thePlayer) < 6 && AntiBot.isValidEntity(entityPlayer) && !PlayerLib.isTeam(entityPlayer) && (mc.currentScreen == null || mc.currentScreen instanceof GuiChat)) {

                        ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft());

                        final String name = entityPlayer.getName();
                        float textWidth = (float) FontUtil.productsans35.getStringWidth(name);

                        float healthPercent = (entityPlayer.getHealth() / Math.min(entityPlayer.getHealth(), entityPlayer.getMaxHealth()));
                        int healthcolor = (int) Math.max((healthPercent * 255), 255);
                        float health = Float.parseFloat(new DecimalFormat("#.##").format(entityPlayer.getHealth()).replaceAll(",", "."));

                        float boxW = targetHudW + textWidth;


                        RenderLib.drawRoundedRect2(targetHudX, targetHudY-3, boxW-targetHudX, targetHudH-targetHudY+2, 6, new Color(20, 20, 20, 80).getRGB(), true);

                        RenderLib.drawRect(targetHudX, targetHudH - 5, (targetHudX + ((boxW-targetHudX) * (entityPlayer.getHealth() / entityPlayer.getMaxHealth()))), targetHudH, new Color(255 - healthcolor, healthcolor, 0).getRGB());

                        RenderLib.drawHead((targetHudX + 4), (int) (targetHudY + 1), 32, 32, (AbstractClientPlayer) entityPlayer);

                        FontUtil.productsans35.drawSmoothString(name, targetHudX + 41, targetHudY, -1);
                        FontUtil.productsans35.drawSmoothString(String.valueOf(health), targetHudX + 41, targetHudY + 17, -1);

                        return;
                    }
                }

                if (showOwn && mc.currentScreen instanceof GuiChat) {
                    final String name = mc.thePlayer.getName();
                    float textWidth = (float) FontUtil.productsans35.getStringWidth(name);

                    float healthPercent = (mc.thePlayer.getHealth() / mc.thePlayer.getMaxHealth());
                    int healthcolor = (int) Math.max((healthPercent * 255), 255);
                    float health = Float.parseFloat(new DecimalFormat("#.##").format(mc.thePlayer.getHealth()).replaceAll(",", "."));

                    float boxW = targetHudW + textWidth;


                    RenderLib.drawRoundedRect2(targetHudX, targetHudY-3, boxW-targetHudX, targetHudH-targetHudY+2, 6, new Color(20, 20, 20, 80).getRGB(), true);

                    RenderLib.drawRect(targetHudX, targetHudH - 5, (targetHudX+ ((boxW-targetHudX) * mc.thePlayer.getHealth() / mc.thePlayer.getMaxHealth())), targetHudH, new Color(255 - healthcolor, healthcolor, 0).getRGB());

                    RenderLib.drawHead((int) (targetHudX + 4), (targetHudY + 1), 32, 32, mc.thePlayer);

                    FontUtil.productsans35.drawSmoothString(name, targetHudX + 41, targetHudY, -1);
                    FontUtil.productsans35.drawSmoothString(String.valueOf(health), targetHudX + 41, targetHudY + 17, -1);

                }
            }
        }
    }

    boolean showOwn = false;

    boolean dragging = false;

    int offsetX = 0;
    int offsetY = 0;

    @SubscribeEvent
    public void onChatEvent(final GuiChatEvent event) {
        if (!Configs.targethud || Configs.followTargetHud) {
            return;
        }

        showOwn = true;

        if (event instanceof GuiChatEvent.MouseClicked) {
            if (event.mouseX >= targetHudX && event.mouseX <= targetHudX + targetHudW && event.mouseY >= targetHudY && event.mouseY <= targetHudY + targetHudH) {
                dragging = true;
                offsetX = event.mouseX - targetHudX;
                offsetY = event.mouseY - targetHudY;
            }
        }

        else if (event instanceof GuiChatEvent.Closed) {
            showOwn = false;
            dragging = false;
        }
        else if (event instanceof GuiChatEvent.MouseReleased) {
            dragging = false;
        }
    }

    public int mouseX() {
        ScaledResolution s = new ScaledResolution(mc);
        return Mouse.getX() * s.getScaledWidth() / mc.displayWidth;
    }

    public int mouseY() {
        ScaledResolution s = new ScaledResolution(mc);
        return s.getScaledHeight() - Mouse.getY() * s.getScaledHeight() / mc.displayHeight - 1;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (dragging) {
            targetHudX = mouseX() - offsetX;
            targetHudY = mouseY() - offsetY;
            targetHudW = 45 + targetHudX;
            targetHudH = 40 + targetHudY;
        }
    }
}
