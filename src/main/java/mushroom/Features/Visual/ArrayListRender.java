package mushroom.Features.Visual;

import mushroom.GUI.ClickGUI;
import mushroom.GUI.Configs;
import mushroom.GUI.Setting;
import mushroom.GUI.Settings.BooleanSetting;
import mushroom.GUI.Settings.SelectSetting;
import mushroom.Libs.ChatLib;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.RenderLib;
import mushroom.mushroom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.*;

import static net.minecraftforge.fml.client.config.GuiUtils.drawGradientRect;

public class ArrayListRender {

    @SubscribeEvent
    public void renderString(TickEvent.RenderTickEvent tick) {
        if ((Configs.arraylist && PlayerLib.inGame()) || (CoolMainMenu.incoolmainmenu && Configs.showarraylistinmainmenu && !PlayerLib.inGame())) {
            if (mushroom.arraycolor > 254) mushroom.godown=true;
            else if (mushroom.arraycolor < 1) mushroom.godown=false;
            if (mushroom.godown) mushroom.arraycolor--;
            else mushroom.arraycolor++;

            String settings = "";
            Iterator allsettings = mushroom.settings.iterator();
            while (allsettings.hasNext()) {
                Setting setting = (Setting) allsettings.next();
                if (setting.parent != null && setting.parent.parent == null && setting instanceof BooleanSetting && (Boolean) setting.get(Boolean.class) && (!Objects.equals(setting.parent.name, "Visual") || Configs.showVisuals)) {
                    Iterator allsettings2 = setting.getSons().iterator();
                    String mode = "";
                    if (Configs.showmode) {
                        while (allsettings2.hasNext()) {
                            Setting setting2 = (Setting) allsettings2.next();
                            if (setting2 instanceof SelectSetting) {
                                mode = " " + ((SelectSetting) setting2).options[(Integer) setting2.get(Integer.class)];
                                while (allsettings2.hasNext()) {
                                    allsettings2.next();
                                }
                            }
                        }
                    }

                    String setName = setting.name;
                    if (Configs.nospacesarraylist) setName = setName.replace(" ", "");
                    if (Configs.nocapitalssarraylist) setName = setName.toLowerCase();

                    settings+= setName + "\u00A77"+mode + ",";
                }
            }
            String[] EachSettingSplit = settings.split(",");
            if (Configs.arraysort == 0) sort2(EachSettingSplit);
            else if (Configs.arraysort == 1) sort(EachSettingSplit);
            int i = 0;
            int p = 0;

            ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft());

            if (EachSettingSplit.length > 0) {
                for (p = 0; p < EachSettingSplit.length; p++) {

                    Color[] colors = getColors();

                    Color[] colorOfText = RenderLib.getColorsFade(2 + (10 * i), 10, Configs.arrayfadecentre, colors[0], colors[1], 0.05 * Configs.arraylistspeed);

                    if (Configs.arraylistpos == 0) {
                        if (Configs.showBackgrounds)
                        drawGradientRect(0,1, 1 + (10*i), (int) (2 + FontUtil.productsans19.getStringWidth(EachSettingSplit[p])+3), 1 + (10 * i) + 10, new Color(23,23,23,100).getRGB(), new Color(23,23,23,100).getRGB());
                        if (Configs.sideline == 1) drawGradientRect(0, 0, 2 + (10 * i), 1, 1 + (10 * i) + 8, colorOfText[0].getRGB(), colorOfText[1].getRGB());
                        else if (Configs.sideline == 2) drawGradientRect(0, 0, 1 + (10 * i), 1, 1 + (10 * i) + 10, colorOfText[0].getRGB(), colorOfText[1].getRGB());

                        FontUtil.productsans19.drawStringWithShadow(EachSettingSplit[p], 2, 1 + (10 * i)+1, colorOfText[0].getRGB());
                    } else if (Configs.arraylistpos == 1) {
                        if (Configs.showBackgrounds)
                        drawGradientRect(0, (int) ((s.getScaledWidth()) - FontUtil.productsans19.getStringWidth(EachSettingSplit[p]) - 4), 1 + (10 * i), s.getScaledWidth(), 1 + (10 * i) + 10, new Color(23,23,23,100).getRGB(), new Color(23,23,23,100).getRGB());
                        if (Configs.sideline == 1) drawGradientRect(0, s.getScaledWidth() - 1, 2 + (10 * i), s.getScaledWidth(), 1 + (10 * i) + 8, colorOfText[0].getRGB(), colorOfText[1].getRGB());
                        else if (Configs.sideline == 2) drawGradientRect(0, s.getScaledWidth() - 1, 1 + (10 * i), s.getScaledWidth(), 1 + (10 * i) + 10, colorOfText[0].getRGB(), colorOfText[1].getRGB());
                        else if (Configs.sideline == 3)  {
                            drawGradientRect(0, s.getScaledWidth() - 1, 1 + (10 * i), s.getScaledWidth(), 1 + (10 * i) + 10, colorOfText[0].getRGB(), colorOfText[1].getRGB());
                            drawGradientRect(0, (int) ((s.getScaledWidth()) - FontUtil.productsans19.getStringWidth(EachSettingSplit[p]) - 5), 1 + (10 * i), (int) ((s.getScaledWidth()) - FontUtil.productsans19.getStringWidth(EachSettingSplit[p]) - 4), 1 + (10 * i) + 10, colorOfText[0].getRGB(), colorOfText[1].getRGB());
                            if (p == 0) {
                                drawGradientRect(0, (int) ((s.getScaledWidth()) - FontUtil.productsans19.getStringWidth(EachSettingSplit[p]) - 5), 0, (s.getScaledWidth()), 1, colorOfText[0].getRGB(), colorOfText[1].getRGB());
                            }
                            if (p == EachSettingSplit.length-1) {
                                drawGradientRect(0, (int) ((s.getScaledWidth()) - FontUtil.productsans19.getStringWidth(EachSettingSplit[p]) - 5), 1 + (10 * i) + 10, (s.getScaledWidth()), 1 + (10 * i)+11, colorOfText[0].getRGB(), colorOfText[1].getRGB());
                            }
                            else {
                                drawGradientRect(0, (int) ((s.getScaledWidth()) - FontUtil.productsans19.getStringWidth(EachSettingSplit[p]) - 5), 1 + (10 * i) + 10, (int) ((s.getScaledWidth()) - FontUtil.productsans19.getStringWidth(EachSettingSplit[p+1]) - 4), 1 + (10 * i)+11, colorOfText[0].getRGB(), colorOfText[1].getRGB());
                            }
                        }

                        FontUtil.productsans19.drawStringWithShadow(EachSettingSplit[p], ((double) s.getScaledWidth()) - FontUtil.productsans19.getStringWidth(EachSettingSplit[p]) - 3, 1 + (10 * i)+1, colorOfText[0].getRGB());
                    }
                    i++;
                }
            }
        }
    }

    static void sort(String []s) {
        for (int i=1 ;i<s.length; i++) {
            String temp = s[i];

            int j = i - 1;
            while (j >= 0 && FontUtil.productsans19.getStringWidth(s[j]) < FontUtil.productsans19.getStringWidth(temp)) {
                s[j+1] = s[j];
                j--;
            }
            s[j+1] = temp;
        }
    }

    static void sort2(String []s) {
        for (int i=1 ;i<s.length; i++) {
            String temp = s[i];

            int j = i - 1;
            while (j >= 0 && s[j].compareToIgnoreCase(temp) > 0) {
                s[j+1] = s[j];
                j--;
            }
            s[j+1] = temp;
        }
    }


    public static Color[] getColors() {
        int col = Configs.arraycolor;
        Color surroundStartColor = new Color(13, 99, 236, 255);
        Color surroundEndColor = new Color(244, 13, 253, 255);

        if (Configs.arraycolor == 0) {
            Color[] colsGUI = ClickGUI.getColors();
            surroundStartColor = colsGUI[2];
            surroundEndColor = colsGUI[3];
        }

        if (col == 2) {
            surroundStartColor = new Color(151, 210, 168, 255);
            surroundEndColor = new Color(15, 73, 3, 255);
        }
        else if (col == 3) {
            surroundStartColor = new Color(213, 21, 21, 255);
            surroundEndColor = new Color(253, 208, 252, 255);
        }
        else if (col == 4) {
            surroundStartColor = new Color(199, 167, 232, 255);
            surroundEndColor = new Color(175, 16, 231, 255);
        }
        else if (col == 5) {
            surroundStartColor = new Color(Configs.arraylistCustCol[0], Configs.arraylistCustCol[1], Configs.arraylistCustCol[2], Configs.arraylistCustCol[3]);
            surroundEndColor = new Color(Configs.arraylistCustCol2[0], Configs.arraylistCustCol2[1], Configs.arraylistCustCol2[2], Configs.arraylistCustCol2[3]);
        }

        return new Color[] {surroundStartColor, surroundEndColor};
    }
}