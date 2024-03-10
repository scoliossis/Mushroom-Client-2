package mushroom.Features.Visual;

import mushroom.Features.Combat.AntiBot;
import mushroom.GUI.ClickGUI;
import mushroom.GUI.Configs;
import mushroom.Libs.ChatLib;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;

import static mushroom.Libs.PlayerLib.isNPC;
import static mushroom.Libs.PlayerLib.mc;
import static mushroom.mushroom.sillyfolderpath;

public class TargetHud {

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Configs.targethud && hudded != null && (hudded instanceof EntityPlayer && hudded.getDistanceToEntity(mc.thePlayer) < 6 && AntiBot.isValidEntity(hudded))) {
            if (Configs.targetESP && hudded != mc.thePlayer) drawTargetESP(hudded, new Color(200,40,190), event.partialTicks);
        }
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent m) {
        // probably not smart but oh well.
        if (drawing) {
            if (!did) {
                did = true;
                fadeStart = System.currentTimeMillis();
            }
            if (fadeprog < 1) {
                fadeprog += (System.currentTimeMillis() - fadeStart) * 0.001f;
            }
            else fadeprog = 1;
        }
        else {
            if (did) {
                did = false;
                fadeEnd = System.currentTimeMillis();
            }
            if (fadeprog > 0.01) {

                fadeprog -= (System.currentTimeMillis() - fadeEnd) * 0.001f;
            }
            else fadeprog = 0.01f;
        }
    }

    boolean did = false;

    float fadeprog = 0.01f;
    public static boolean drawing = false;
    long fadeStart = 0;
    long fadeEnd = 0;
    long lastFade = 0;

    EntityLivingBase hudded;

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRender(final RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        if (Configs.targethud && !Configs.notfollowTargetHud) {
            if (((event.entity instanceof EntityPlayer && event.entity.getDistanceToEntity(mc.thePlayer) < 6 && AntiBot.isValidEntity(event.entity)))) {
                hudded = event.entity;

                GlStateManager.alphaFunc(516, 0.1f);
                final double x = event.x;
                final double y = event.y;
                final double z = event.z;
                final float f = 0.9f;
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
                GlStateManager.disableTexture2D();


                drawing = true;
                drawHud(event.entity, 0, 0, false);


                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.popMatrix();


                return;
            }
            if (hudded != null && fadeprog != 0.01f && Configs.popUpAnimation == 2 && hudded instanceof AbstractClientPlayer) {
                GlStateManager.alphaFunc(516, 0.1f);
                final double x = event.x;
                final double y = event.y;
                final double z = event.z;
                final float f = 0.7f;
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
                GlStateManager.disableTexture2D();


                drawHud(hudded);


                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.popMatrix();
            }

            drawing = false;
        }
    }

    public static int targetHudX = 0;
    public static int targetHudY = 0;
    int targetHudW = 45;
    int targetHudH = 40;

    @SubscribeEvent
    public void renderString(TickEvent.RenderTickEvent tick) {

        if (Configs.targethud && PlayerLib.inGame()) {
            if (Configs.notfollowTargetHud) {
                for (final EntityPlayer entityPlayer : mc.theWorld.playerEntities) {

                    if (entityPlayer != mc.thePlayer && entityPlayer.getDistanceToEntity(mc.thePlayer) < 6 && AntiBot.isValidEntity(entityPlayer) && !PlayerLib.isTeam(entityPlayer) && (mc.currentScreen == null || mc.currentScreen instanceof GuiChat)) {
                        drawing = true;
                        drawHud(entityPlayer);
                        hudded = entityPlayer;
                        return;
                    }
                }


                if (showOwn && mc.currentScreen instanceof GuiChat) {
                    drawing = true;
                    hudded = mc.thePlayer;
                    drawHud(mc.thePlayer, true);
                    return;
                }

                if (fadeprog != 0.01f && hudded != null && Configs.popUpAnimation != 2) {
                    drawHud(hudded);
                }
            }

            drawing = false;
        }
    }

    public static void doTrans(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        GlStateManager.alphaFunc(516, 0.1f);
        final double x = event.x;
        final double y = event.y;
        final double z = event.z;
        final float f = 0.7f;
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
        GlStateManager.disableTexture2D();
    }

    public static void doUnTrans(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    public void drawHud(EntityLivingBase entity, boolean nwor) {
        drawHud(entity, targetHudX, targetHudY, nwor);
    }

    public void drawHud(EntityLivingBase entity) {
        drawHud(entity, targetHudX, targetHudY, false);
    }

    public void drawHud(EntityLivingBase entity, int x, int y, boolean fake) {
        if (!fake) lastFade = System.currentTimeMillis();

        ScaledResolution s = new ScaledResolution(mc);

        final String name = entity.getName();
        float textWidth = (float) FontUtil.font("productsans", 35).getStringWidth(name);

        float health = Float.parseFloat(new DecimalFormat("#.##").format(entity.getHealth()).replaceAll(",", "."));

        float multi = 1;
        fadeprog = Math.max(Math.min(1, fadeprog), 0);
        if (Configs.popUpAnimation == 1 && Configs.notfollowTargetHud) multi = Math.max(fadeprog, 0.1f);

        float op = 1;
        if (Configs.popUpAnimation == 0 && Configs.notfollowTargetHud) op = fadeprog;

        int newEgs = (int) (x + (((x/2) - ((x/2)*multi)) / 2));
        int newWhy = (int) (y + (((y/2) - ((y/2)*multi)) / 2));

        if (Configs.popUpAnimation == 3 && Configs.notfollowTargetHud) {
            if (drawing) newEgs = (int) (x * fadeprog);
            else newEgs = (int) (x + ((s.getScaledWidth()-x) * (1-fadeprog)));
        }

        float boxW = targetHudW*multi;
        float boxH = targetHudH*multi;

        Color col = ClickGUI.getColor(Configs.BackgroundColor, Configs.BackgroundColor[3]*op);
        if (Configs.ThudCustomColor) col = ClickGUI.getColor(Configs.thudbg1, Configs.thudbg1[3]*op);

        RenderLib.drawRoundedRect2(newEgs, newWhy - (int) (3*multi), (int) ((boxW + textWidth) * multi), (int) ((boxH + 2) * multi), 6, col.getRGB(), true);

        Color[] fadeHealthColors = RenderLib.getColorsFade(0, ((boxW+textWidth) * entity.getHealth() / entity.getMaxHealth()), 50, ClickGUI.getColor(Configs.healthBarColor1, Configs.healthBarColor1[3]*op), ClickGUI.getColor(Configs.healthBarColor2, Configs.healthBarColor2[3]*op), 0.01);
        if (Configs.ThudCustomColor)
            fadeHealthColors = RenderLib.getColorsFade(0, ((boxW+textWidth) * entity.getHealth() / entity.getMaxHealth()), 50, ClickGUI.getColor(Configs.thudhealth1, Configs.thudhealth1[3]*op), ClickGUI.getColor(Configs.thudhealth2, Configs.thudhealth2[3]*op), 0.01);

        RenderLib.drawFade(newEgs, (int) (newWhy + (boxH - 6)), (int) (((boxW+textWidth) * (entity.getHealth() / entity.getMaxHealth()))), (int) (5*multi), fadeHealthColors[0], fadeHealthColors[1]);

        RenderLib.drawHead((int) (newEgs + (4*multi)), (int) (newWhy + (1*multi)), (int) (32*multi), (int) (32*multi), (AbstractClientPlayer) entity, op);

        FontUtil.font("productsans", (int) (35 * multi)).drawString(name, newEgs + (41*multi), newWhy, new Color(255, 255, 255, (int) (op*255)).getRGB());
        FontUtil.font("productsans", (int) (33 * op)).drawString(health + "", newEgs + (41*multi), newWhy + (17*multi), new Color(255, 255, 255, (int) (op*255)).getRGB());
    }

    boolean showOwn = false;

    boolean dragging = false;

    int offsetX = 0;
    int offsetY = 0;

    @SubscribeEvent
    public void onChatEvent(final GuiChatEvent event) {
        if (!Configs.targethud || !Configs.notfollowTargetHud) {
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

            try {Files.write(Paths.get(sillyfolderpath + "/extras/sillys.scolio"), ("targetHudX:" + targetHudX + " \ntargetHudY:" + targetHudY + " \n").getBytes());
            } catch (IOException e) {e.printStackTrace();}

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
            targetHudW = 45;
            targetHudH = 40;
        }
    }


    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void drawTargetESP(final EntityLivingBase target, final Color color, final float partialTicks) {
        GL11.glPushMatrix();
        final float location = (float)((Math.sin(System.currentTimeMillis() * 0.005) + 1.0) * 0.5);
        GlStateManager.translate(target.lastTickPosX + (target.posX - target.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX, target.lastTickPosY + (target.posY - target.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY + target.height * location, target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ);
        enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glDisable(2884);
        GL11.glLineWidth(3.0f);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glBegin(3);
        final double cos = Math.cos(System.currentTimeMillis() * 0.005);
        for (int i = 0; i <= 120; ++i) {
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
            final double x = Math.cos(i * 3.141592653589793 / 60.0) * target.width;
            final double z = Math.sin(i * 3.141592653589793 / 60.0) * target.width;
            GL11.glVertex3d(x, 0.15000000596046448 * cos, z);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glBegin(5);
        for (int i = 0; i <= 120; ++i) {
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
            final double x = Math.cos(i * 3.141592653589793 / 60.0) * target.width;
            final double z = Math.sin(i * 3.141592653589793 / 60.0) * target.width;
            GL11.glVertex3d(x, 0.15000000596046448 * cos, z);
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.2f);
            GL11.glVertex3d(x, -0.15000000596046448 * cos, z);
        }
        GL11.glEnd();
        GL11.glShadeModel(7424);
        GL11.glEnable(2884);
        disableGL2D();
        GL11.glPopMatrix();
    }
}
