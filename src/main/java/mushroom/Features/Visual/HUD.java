package mushroom.Features.Visual;

import mushroom.GUI.Configs;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.RenderLib;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

public class HUD {
    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent tick) {
        if (PlayerLib.inGame() && Minecraft.getMinecraft().currentScreen == null && Configs.Watermark && !Configs.watermarkText.isEmpty()) {
            FontUtil.font("TahomaB", 40).drawStringWithShadow(String.valueOf(Configs.watermarkText.charAt(0)), 5, 5, new Color(7, 180, 180).getRGB());
            for (int i = 1; i < Configs.watermarkText.length(); i++) {
                Color[] color = RenderLib.getColorsFade(5 + FontUtil.font("TahomaB", 40).getStringWidth(Configs.watermarkText.substring(0, i)), 5, 60, new Color(134, 18, 178), new Color(224, 12, 221), 0.1);
                FontUtil.font("TahomaB", 40).drawStringWithShadow(String.valueOf(Configs.watermarkText.charAt(i)), 5 + FontUtil.font("TahomaB", 40).getStringWidth(Configs.watermarkText.substring(0, i)), 5, color[0].getRGB());
            }
        }
    }
}
