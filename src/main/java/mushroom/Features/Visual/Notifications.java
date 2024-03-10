package mushroom.Features.Visual;

import mushroom.Libs.ChatLib;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.Libs.events.WorldJoinEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;

import static mushroom.Libs.PlayerLib.mc;
import static net.minecraftforge.fml.client.config.GuiUtils.drawGradientRect;

public class Notifications {

    static int ticksforpopup = 1;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent piss) {
        ticksforpopup++;
    }

    static ArrayList<String> popups = new ArrayList<>();
    public static void popupmessage(String text, String bottomtext) {
        popups.add(text);
        popups.add(bottomtext);
        popups.add(String.valueOf(ticksforpopup));
    }

    @SubscribeEvent
    public void onRenderEvent(TickEvent.RenderTickEvent tick) {
        try {
            if (popups.size() > 1) {
                for (int i = 0; i < popups.size(); i += 3) {
                    if (i >= 24) {
                        popups.remove(0);
                        popups.remove(0);
                        popups.remove(0);
                        return;
                    }
                    double width;
                    width = (((ticksforpopup - Integer.parseInt(popups.get(i + 2))) * 3.3) * 20);
                    if ((((ticksforpopup - Integer.parseInt(popups.get(i + 2))) * 3.3) * 20) > 200) width = 200;

                    // Q: "hey where did you get the number 0.01273074474 from?" A: my ass.
                    double minus = (0.01273074474 * (((ticksforpopup - Integer.parseInt(popups.get(i + 2))) * 3.3) * 20))*2;
                    if (minus > 200) {
                        popups.remove(i);
                        popups.remove(i);
                        popups.remove(i);
                        return;
                    };

                    ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft());

                    drawGradientRect(0, (int) ((s.getScaledWidth()) - width), (s.getScaledHeight()) - (30 * (i / 3 + 1)) - 20 - i / 3, (s.getScaledWidth()), ((s.getScaledHeight()) - (30 * (i / 3 + 1)) - 20 - i / 3) + 30, new Color(22, 0, 22, 150).getRGB(), new Color(22, 0, 22, 150).getRGB());
                    drawGradientRect(0, (int) ((int) ((s.getScaledWidth()) - width)+(200-minus)), ((s.getScaledHeight()) - (30 * (i / 3 + 1)) - 20 - i / 3) + 27, (s.getScaledWidth()), ((s.getScaledHeight()) - (30 * (i / 3 + 1)) - 20 - i / 3) + 30, new Color(150, 0, 255, 200).getRGB(), new Color(150, 0, 255, 200).getRGB());
                    FontUtil.font("productsans", 23).drawStringWithShadow(popups.get(i), (s.getScaledWidth()) - width + 5, (s.getScaledHeight()) - (30 * (i / 3f + 1) - 5) - 20 - i / 3f, new Color(255, 0, 120).getRGB());
                    if (popups.get(i + 1).contains("enabled")) FontUtil.font("productsans", 19).drawStringWithShadow(popups.get(i + 1), (s.getScaledWidth()) - width + 5, (s.getScaledHeight()) - (30 * (i / 3f + 1) - 15) - 20 - i / 3f, new Color(120, 255, 120).getRGB());
                    else if (popups.get(i + 1).contains("disabled")) FontUtil.font("productsans", 19).drawStringWithShadow(popups.get(i + 1), (s.getScaledWidth()) - width + 5, (s.getScaledHeight()) - (30 * (i / 3f + 1) - 15) - 20 - i / 3f, new Color(255, 120, 120).getRGB());
                    else FontUtil.font("productsans", 19).drawStringWithShadow(popups.get(i + 1), (s.getScaledWidth()) - width + 5, (s.getScaledHeight()) - (30 * (i / 3f + 1) - 15) - 20 - i / 3f, new Color(120, 120, 255).getRGB());
                }

            }
        }
        catch (Exception e) {
            // "errors" really often
            e.printStackTrace();
            //ChatLib.chat("renderfail :laugh: " + e);
        }
    }

    @SubscribeEvent
    public void onRespawn(WorldJoinEvent event) {
        popups.clear();
    }

}
