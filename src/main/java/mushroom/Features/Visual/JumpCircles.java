package mushroom.Features.Visual;

import mushroom.Features.Combat.AntiBot;
import mushroom.GUI.ClickGUI;
import mushroom.GUI.Configs;
import mushroom.Libs.ChatLib;
import mushroom.Libs.RenderLib;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;

import static mushroom.Libs.PlayerLib.mc;

public class JumpCircles {

    ArrayList<double[]> drawLocs = new ArrayList<>();
    boolean lastg = false;
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRender(RenderWorldLastEvent event) {
        if (Configs.jumpCircles) {
            if (mc.thePlayer.onGround && !lastg && (Configs.jumpCirclesTime == 1 || Configs.jumpCirclesTime == 2)) {
                drawLocs.add(new double[]{mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, System.currentTimeMillis()});
            } else if (!mc.thePlayer.onGround && lastg && (Configs.jumpCirclesTime == 0 || Configs.jumpCirclesTime == 2)) {
                drawLocs.add(new double[]{mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, System.currentTimeMillis()});
            }

            for (double[] circ : drawLocs) {
                double timeWidth = (System.currentTimeMillis() - circ[3]) * 0.001f;

                Color[] colors = RenderLib.getColorsFade(circ[0]+circ[2], timeWidth, 1, ClickGUI.getColor(Configs.jumpCirclesColor), ClickGUI.getColor(Configs.jumpCirclesColor2), 0.0001);

                int opacity = Configs.jumpCirclesColor[3];
                if (timeWidth >= 0.7) {
                    opacity = (int) (opacity*(1.5-timeWidth));
                }

                if (opacity >= 1) RenderLib.draw3dCircle(circ[0], circ[1], circ[2], 0.05, timeWidth, new Color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), opacity));

                if (timeWidth >= 1.5) {
                    drawLocs.remove(circ);
                    return;
                }
            }

            lastg = mc.thePlayer.onGround;
        }
    }
}
