package mushroom.Features.Visual;

import java.util.ArrayList;
import java.awt.Color;

import mushroom.GUI.ClickGUI;
import mushroom.GUI.Configs;
import mushroom.Libs.RenderLib;
import mushroom.Libs.events.PlayerUpdateEvent;
import mushroom.Libs.events.WorldJoinEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.Vec3;
import java.util.List;

import static mushroom.Libs.PlayerLib.mc;

public class Trail {
    private static final List<Vec3> vecs = new ArrayList<>();

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        if (Configs.trail) {
            vecs.add(new Vec3(mc.thePlayer.prevPosX, mc.thePlayer.prevPosY + 0.1, mc.thePlayer.prevPosZ));
            while (vecs.size() > Configs.trailLength) {
                vecs.remove(0);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderWorld(final RenderWorldLastEvent event) {
        if (Configs.trail && !vecs.isEmpty() && mc.thePlayer != null && mc.getRenderManager() != null) {
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glLineWidth(2.5f);
            GL11.glDisable(3553);
            GL11.glDisable(2884);
            GL11.glShadeModel(7425);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glBegin(3);
            int index = 0;
            for (final Vec3 vec : vecs) {
                final boolean isFirst = index == 0;
                index++;
                Color[] color = RenderLib.getColorsFade(index*10, 10, 20, ClickGUI.getColor(Configs.trailColor1), ClickGUI.getColor(Configs.trailColor2), 0.1);
                GL11.glColor3f(color[0].getRed() / 255.0f, color[0].getGreen() / 255.0f, color[0].getBlue() / 255.0f);
                if (isFirst && vecs.size() > 2) {
                    final Vec3 newVec = vecs.get(1);
                    GL11.glVertex3d(this.interpolate(vec.xCoord, newVec.xCoord, event.partialTicks) - mc.getRenderManager().viewerPosX, this.interpolate(vec.yCoord, newVec.yCoord, event.partialTicks) - mc.getRenderManager().viewerPosY, this.interpolate(vec.zCoord, newVec.zCoord, event.partialTicks) - mc.getRenderManager().viewerPosZ);
                }
                else {
                    GL11.glVertex3d(vec.xCoord - mc.getRenderManager().viewerPosX, vec.yCoord - mc.getRenderManager().viewerPosY, vec.zCoord - mc.getRenderManager().viewerPosZ);
                }
            }
            Color[] color = RenderLib.getColorsFade(index*10, 10, 20, ClickGUI.getColor(Configs.trailColor1), ClickGUI.getColor(Configs.trailColor2), 0.1);

            GL11.glColor3f(color[0].getRed() / 255.0f, color[0].getGreen() / 255.0f, color[0].getBlue() / 255.0f);
            GL11.glVertex3d(mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX) * event.partialTicks - mc.getRenderManager().viewerPosX, mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY) * event.partialTicks - mc.getRenderManager().viewerPosY + 0.1, mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * event.partialTicks - mc.getRenderManager().viewerPosZ);
            GL11.glEnd();
            GL11.glEnable(3553);
            GL11.glShadeModel(7424);
            GL11.glEnable(2884);
            GL11.glDisable(2848);
            GL11.glDisable(2881);
            GL11.glDisable(3042);
        }
    }

    @SubscribeEvent
    public void onWorldJoin(final WorldJoinEvent event) {
        vecs.clear();
    }

    private double interpolate(final double prev, final double newPos, final float partialTicks) {
        return prev + (newPos - prev) * partialTicks;
    }
}