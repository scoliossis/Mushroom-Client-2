package mushroom.Libs;

import mushroom.GUI.ClickGUI;
import mushroom.Libs.events.RenderLayersEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static mushroom.Libs.PlayerLib.mc;

public class RenderLib {
    public static void drawRect(float left, float top, float right, float bottom, final int color) {
        if (left < right) {
            final float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final float j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f4, f5, f6, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)left, (double)bottom, 0.0).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0).endVertex();
        worldrenderer.pos((double)right, (double)top, 0.0).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawHead(int x, int y, int width, int height, AbstractClientPlayer target) {
        final ResourceLocation skin = target.getLocationSkin();
        try {
            mc.getTextureManager().bindTexture(skin);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1, 1, 1, 1);
            Gui.drawScaledCustomSizeModalRect(x, y, 8f, 8f, 8, 8, width, height, 64f, 64f);
            GL11.glDisable(GL11.GL_BLEND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void outlineESP(final RenderLayersEvent event, final Color color) {
        final boolean fancyGraphics = mc.gameSettings.fancyGraphics;
        final float gamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.fancyGraphics = false;
        mc.gameSettings.gammaSetting = 100000.0f;
        GlStateManager.resetColor();

        GL11.glColor4d((double)(color.getRed() / 255.0f), (double)(color.getGreen() / 255.0f), (double)(color.getBlue() / 255.0f), (double)(color.getAlpha() / 255.0f));

        final Framebuffer fbo = mc.getFramebuffer();
        if (fbo != null && fbo.depthBuffer > -1) {

            //ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft());

            EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
            final int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
            EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
            EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
            EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
            EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);

            fbo.depthBuffer = -1;
        }

        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(2.0f);
        GL11.glEnable(2848);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);

        float two = event.p_77036_2_;
        float three = event.p_77036_3_;
        float foor = event.p_77036_4_;
        float five = event.p_77036_5_;
        float six = event.p_77036_6_;
        float scale = event.scaleFactor;

        event.modelBase.render((Entity)event.entity, two, three, foor, five, six, scale);

        GL11.glColor4d((double)(color.getRed() / 255.0f), (double)(color.getGreen() / 255.0f), (double)(color.getBlue() / 255.0f), (double)(color.getAlpha() / 255.0f));

        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);

        event.modelBase.render((Entity)event.entity, two, three, foor, five, six, scale);

        GL11.glColor4d((double)(color.getRed() / 255.0f), (double)(color.getGreen() / 255.0f), (double)(color.getBlue() / 255.0f), (double)(color.getAlpha() / 255.0f));

        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);

        event.modelBase.render((Entity)event.entity, two, three, foor, five, six, scale);


        GL11.glColor4d((double)(color.getRed() / 255.0f), (double)(color.getGreen() / 255.0f), (double)(color.getBlue() / 255.0f), (double)(color.getAlpha() / 255.0f));

        GL11.glColor4d((double)(color.getRed() / 255.0f), (double)(color.getGreen() / 255.0f), (double)(color.getBlue() / 255.0f), (double)(color.getAlpha() / 255.0f));
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1.0f, -2000000.0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);

        event.modelBase.render((Entity)event.entity, two, three, foor, five, six, scale);

        GL11.glColor4d((double)(color.getRed() / 255.0f), (double)(color.getGreen() / 255.0f), (double)(color.getBlue() / 255.0f), (double)(color.getAlpha() / 255.0f));

        GL11.glPolygonOffset(1.0f, 2000000.0f);
        GL11.glDisable(10754);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(2960);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glEnable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        GL11.glPopAttrib();

        GL11.glColor4d(0, 0, 0, 255);

        mc.gameSettings.fancyGraphics = fancyGraphics;
        mc.gameSettings.gammaSetting = gamma;
    }

    public static Matrix4f getMatrix(final int matrix) {
        final FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(matrix, floatBuffer);
        return (Matrix4f)new Matrix4f().load(floatBuffer);
    }

    public static Vector2f worldToScreen(final Vector3f pointInWorld, final int screenWidth, final int screenHeight) {
        return worldToScreen(pointInWorld, getMatrix(2982), getMatrix(2983), screenWidth, screenHeight);
    }

    public static Vector2f worldToScreen(final Vector3f pointInWorld, final Matrix4f view, final Matrix4f projection, final int screenWidth, final int screenHeight) {
        final Vector4f clipSpacePos = multiply(multiply(new Vector4f(pointInWorld.x, pointInWorld.y, pointInWorld.z, 1.0f), view), projection);
        final Vector3f ndcSpacePos = new Vector3f(clipSpacePos.x / clipSpacePos.w, clipSpacePos.y / clipSpacePos.w, clipSpacePos.z / clipSpacePos.w);
        final float screenX = (ndcSpacePos.x + 1.0f) / 2.0f * screenWidth;
        final float screenY = (1.0f - ndcSpacePos.y) / 2.0f * screenHeight;
        if (ndcSpacePos.z < -1.0 || ndcSpacePos.z > 1.0) {
            return null;
        }
        return new Vector2f(screenX, screenY);
    }

    public static Vector4f multiply(final Vector4f vec, final Matrix4f mat) {
        return new Vector4f(vec.x * mat.m00 + vec.y * mat.m10 + vec.z * mat.m20 + vec.w * mat.m30, vec.x * mat.m01 + vec.y * mat.m11 + vec.z * mat.m21 + vec.w * mat.m31, vec.x * mat.m02 + vec.y * mat.m12 + vec.z * mat.m22 + vec.w * mat.m32, vec.x * mat.m03 + vec.y * mat.m13 + vec.z * mat.m23 + vec.w * mat.m33);
    }
    public static void draw2D(final Entity entityLiving, final float partialTicks, final float lineWidth, final Color color) {
        ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft());

        final Matrix4f mvMatrix = getMatrix(2982);
        final Matrix4f projectionMatrix = getMatrix(2983);
        GL11.glPushAttrib(8192);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glMatrixMode(5889);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, s.getScaledWidth(), s.getScaledHeight(), 0.0, -1.0, 1.0);
        GL11.glMatrixMode(5888);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glDisable(2929);
        GL11.glBlendFunc(770, 771);
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GL11.glLineWidth(lineWidth);
        final RenderManager renderManager = mc.getRenderManager();
        final AxisAlignedBB bb = entityLiving.getEntityBoundingBox().offset(-entityLiving.posX, -entityLiving.posY, -entityLiving.posZ).offset(entityLiving.lastTickPosX + (entityLiving.posX - entityLiving.lastTickPosX) * partialTicks, entityLiving.lastTickPosY + (entityLiving.posY - entityLiving.lastTickPosY) * partialTicks, entityLiving.lastTickPosZ + (entityLiving.posZ - entityLiving.lastTickPosZ) * partialTicks).offset(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ);
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
        final double[][] boxVertices = { { bb.minX, bb.minY, bb.minZ }, { bb.minX, bb.maxY, bb.minZ }, { bb.maxX, bb.maxY, bb.minZ }, { bb.maxX, bb.minY, bb.minZ }, { bb.minX, bb.minY, bb.maxZ }, { bb.minX, bb.maxY, bb.maxZ }, { bb.maxX, bb.maxY, bb.maxZ }, { bb.maxX, bb.minY, bb.maxZ } };
        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxX = -1.0f;
        float maxY = -1.0f;

        for (final double[] boxVertex : boxVertices) {
            final Vector2f screenPos = worldToScreen(new Vector3f((float)boxVertex[0], (float)boxVertex[1], (float)boxVertex[2]), mvMatrix, projectionMatrix, s.getScaledWidth(), s.getScaledHeight());
            if (screenPos != null) {
                minX = Math.min(screenPos.x, minX);
                minY = Math.min(screenPos.y, minY);
                maxX = Math.max(screenPos.x, maxX);
                maxY = Math.max(screenPos.y, maxY);
            }
        }
        if (minX > 0.0f || minY > 0.0f || maxX <= s.getScaledWidth() || maxY <= s.getScaledWidth()) {
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
            GL11.glBegin(2);
            GL11.glVertex2f(minX, minY);
            GL11.glVertex2f(minX, maxY);
            GL11.glVertex2f(maxX, maxY);
            GL11.glVertex2f(maxX, minY);
            GL11.glEnd();
        }
        GL11.glEnable(2929);
        GL11.glMatrixMode(5889);
        GL11.glPopMatrix();
        GL11.glMatrixMode(5888);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    public static void drawRoundedRect2(double x, double y, final double width, final double height, final double radius, final int color, final boolean half) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double x2 = x + width;
        double y2 = y + height;
        final float f = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2.0;
        y *= 2.0;
        x2 *= 2.0;
        y2 *= 2.0;
        GL11.glDisable(3553);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glEnable(2848);
        GL11.glBegin(9);

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * (radius * -1.0), y + radius + Math.cos(i * 3.141592653589793 / 180.0) * (radius * -1.0));
        }
        if (!half) {
            for (int i = 90; i <= 180; i += 3) {
                GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * (radius * -1.0), y2 - radius + Math.cos(i * 3.141592653589793 / 180.0) * (radius * -1.0));
            }

            for (int i = 0; i <= 90; i += 3) {
                GL11.glVertex2d(x2 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y2 - radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
            }
        }


        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x2 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y + radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }

        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glPopAttrib();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

        if (half) {
            drawRect((float) x/2, (float) (y + radius)/2, (float) x2/2, (float) y2/2, color);
        }
    }

    public static void renderBoundingBox(double x, double y, double z, double w, double h, Color col, float partialTicks) {
        double playX = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * partialTicks;
        double playY = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * partialTicks;
        double playZ = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * partialTicks;


        AxisAlignedBB bb = new AxisAlignedBB(x - playX, y - playY, z - playZ, x + w - playX, y + h - playY, z + w - playZ);

        GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GlStateManager.disableTexture2D();
        drawFilledBoundingBox(bb, 1f, col);
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    public static void renderBoundingBox(double x, double y, double z, double xw, double h, double zw, Color col, float partialTicks) {
        double playX = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * partialTicks;
        double playY = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * partialTicks;
        double playZ = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * partialTicks;


        AxisAlignedBB bb = new AxisAlignedBB(x - playX, y - playY, z - playZ, x + xw - playX, y + h - playY, z + zw - playZ);

        GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GlStateManager.disableTexture2D();
        drawFilledBoundingBox(bb, 1f, col);
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    public static void drawFilledBoundingBox(AxisAlignedBB p_181561_0_, float alpha, Color c) {

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f * alpha);

        //vertical
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        tessellator.draw();

        GlStateManager.color(
                c.getRed() / 255f * 0.8f,
                c.getGreen() / 255f * 0.8f,
                c.getBlue() / 255f * 0.8f,
                c.getAlpha() / 255f * alpha
        );

        //x
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        tessellator.draw();

        GlStateManager.color(
                c.getRed() / 255f * 0.9f,
                c.getGreen() / 255f * 0.9f,
                c.getBlue() / 255f * 0.9f,
                c.getAlpha() / 255f * alpha
        );
        //z
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
        tessellator.draw();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
        tessellator.draw();
    }


    public static Color[] getColorsFade(double y, double h, double fadeCentre, Color minColor, Color maxColor, double fadeSpeed) {

        y += System.currentTimeMillis() * fadeSpeed;

        // get colors
        int colorRedStart = minColor.getRed();
        int colorGreenStart = minColor.getGreen();
        int colorBlueStart = minColor.getBlue();
        int colorOpacityStart = minColor.getAlpha();

        int colorRedEnd = maxColor.getRed();
        int colorGreenEnd = maxColor.getGreen();
        int colorBlueEnd = maxColor.getBlue();
        int colorOpacityEnd = maxColor.getAlpha();

        // get dif from start to end
        int redDif = colorRedEnd - colorRedStart;
        int greenDif = colorGreenEnd - colorGreenStart;
        int blueDif = colorBlueEnd - colorBlueStart;
        int opacityDif = colorOpacityEnd - colorOpacityStart;

        // find how many times over the centre point the y is, if under it this is 0
        double amountOverCentreINT = Math.floor(y / fadeCentre);
        // math
        double amountDown = (y - (amountOverCentreINT * fadeCentre)) / fadeCentre;

        // if the number isnt even then make it fade down instead of up
        if (amountOverCentreINT % 2 != 1) amountDown = 1-amountDown;


        // height at bottom
        double y2 = y + h;

        // same as above but w the new height
        double amountOverCentreINT2 = Math.floor(y2 / fadeCentre);
        double amountDown2 = (y2 - (amountOverCentreINT2 * fadeCentre)) / fadeCentre;

        if (amountOverCentreINT2 % 2 != 1) amountDown2 = 1-amountDown2;


        // start color + (difference between start n end · percent down screen)
        int redNum1 = (int) (colorRedStart + (redDif * amountDown));
        int redNum2 = (int) (colorRedStart + (redDif * amountDown2));

        int greenNum1 = (int) (colorGreenStart + (greenDif * amountDown));
        int greenNum2 = (int) (colorGreenStart + (greenDif * amountDown2));

        int blueNum1 = (int) (colorBlueStart + (blueDif * amountDown));
        int blueNum2 = (int) (colorBlueStart + (blueDif * amountDown2));

        int opacityNum1 = (int) (colorOpacityStart + (opacityDif * amountDown));
        int opacityNum2 = (int) (colorOpacityStart + (opacityDif * amountDown2));

        return new Color[] {new Color(redNum1, greenNum1, blueNum1, opacityNum1), new Color(redNum2, greenNum2, blueNum2, opacityNum2)};
    }


    public static void drawFade(int x, int y, int w, int h, Color Left, Color Right) {
        int startRed = Left.getRed();
        int startGreen = Left.getGreen();
        int startBlue = Left.getBlue();
        int startOpacity = Left.getAlpha();

        int redDif = Right.getRed() - Left.getRed();
        int greenDif = Right.getGreen() - Left.getGreen();
        int blueDif = Right.getBlue() - Left.getBlue();
        int opacityDif = Right.getAlpha() - Left.getAlpha();

        double redEach = (double) redDif / w;
        double greenEach = (double) greenDif / w;
        double blueEach = (double) blueDif / w;
        double opacityEach = (double) opacityDif / w;

        for (int i = 0; i < w; i++) {
            int red = (int) (startRed + (redEach * i));
            int green = (int) (startGreen + (greenEach * i));
            int blue = (int) (startBlue + (blueEach * i));
            int opacity = (int) (startOpacity + (opacityEach * i));

            drawRect(x + i, y, x + i + 1, y + h, new Color(red, green, blue, opacity).getRGB());
        }

    }

    public static void idiotFadeDumbStupid(int x, int y, int w, int h, Color Left, Color Right) {
        int startRed = Left.getRed();
        int startGreen = Left.getGreen();
        int startBlue = Left.getBlue();
        int startOpacity = Left.getAlpha();

        int redDif = Right.getRed() - Left.getRed();
        int greenDif = Right.getGreen() - Left.getGreen();
        int blueDif = Right.getBlue() - Left.getBlue();
        int opacityDif = Right.getAlpha() - Left.getAlpha();


        w = w-x;
        h = y-h;

        double redEach = (double) redDif / w;
        double greenEach = (double) greenDif / w;
        double blueEach = (double) blueDif / w;
        double opacityEach = (double) opacityDif / w;

        for (int i = 0; i < w; i++) {
            int red = (int) (startRed + (redEach * i));
            int green = (int) (startGreen + (greenEach * i));
            int blue = (int) (startBlue + (blueEach * i));
            int opacity = (int) (startOpacity + (opacityEach * i));

            drawRect(x + i, y, x + i + 1, y + h, new Color(red, green, blue, opacity).getRGB());
        }

    }

    public static void drawDownFade(int x, int y, int w, int h, Color Top, Color Bottom) {
        int startRed = Top.getRed();
        int startGreen = Top.getGreen();
        int startBlue = Top.getBlue();
        int startOpacity = Top.getAlpha();

        int redDif = Bottom.getRed() - Top.getRed();
        int greenDif = Bottom.getGreen() - Top.getGreen();
        int blueDif = Bottom.getBlue() - Top.getBlue();
        int opacityDif = Bottom.getAlpha() - Top.getAlpha();

        double redEach = (double) redDif / h;
        double greenEach = (double) greenDif / h;
        double blueEach = (double) blueDif / h;
        double opacityEach = (double) opacityDif / h;

        for (int i = 0; i < h; i++) {
            int red = (int) (startRed + (redEach * i));
            int green = (int) (startGreen + (greenEach * i));
            int blue = (int) (startBlue + (blueEach * i));
            int opacity = (int) (startOpacity + (opacityEach * i));

            drawRect(x, y + i, x + w, y + i + 1, new Color(red, green, blue, opacity).getRGB());
        }

    }




    public static void drawOutlinedRoundedRect(float x, float y, final float width, final float height, final float radius, final float linewidth, final int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double x2 = x + width;
        double y2 = y + height;
        final float f = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2.0f;
        y *= 2.0f;
        x2 *= 2.0;
        y2 *= 2.0;
        GL11.glLineWidth(linewidth);
        GL11.glDisable(3553);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glEnable(2848);
        GL11.glBegin(2);
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * (radius * -1.0f), y + radius + Math.cos(i * 3.141592653589793 / 180.0) * (radius * -1.0f));
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + Math.sin(i * 3.141592653589793 / 180.0) * (radius * -1.0f), y2 - radius + Math.cos(i * 3.141592653589793 / 180.0) * (radius * -1.0f));
        }
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x2 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y2 - radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x2 - radius + Math.sin(i * 3.141592653589793 / 180.0) * radius, y + radius + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glPopAttrib();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
