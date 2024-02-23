package mushroom.Features.Visual;

import mushroom.GUI.AltManager.AltManagerGUI;
import mushroom.GUI.ClickGUI;
import mushroom.GUI.Configs;
import mushroom.GUI.Settings.SelectSetting;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.Libs.RenderLib;
import mushroom.mixins.AbstractClientPlayerMixin;
import mushroom.mushroom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static mushroom.Libs.PlayerLib.mc;

public class CoolMainMenu extends GuiScreen {

    public CoolMainMenu() {

    }

    int w = 250;
    int h = 25;
    int gap = 5;

    int p = 0;
    boolean godown = false;
    public static boolean incoolmainmenu = true;
    String[] buttons = {"singleplayer", "multiplayer", "minecraft settings", "alt manager", "mushroom settings"};

    // shoutout to https://www.cleverpdf.com/es/convertir-gif-a-png
    String[] folders = {"angy", "whatthesilly", "animegril", "spanish", "gril"};
    int[] frames = {20, 12, 12, 39, 7};

    boolean ran = false;

    long timeWhenStart = System.currentTimeMillis();
    long lastLoop = System.currentTimeMillis();

    @Override
    public void drawScreen(int mx, int my, float var3) {
        this.drawDefaultBackground();
        super.drawScreen(mx, my, var3);

        ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft());

        incoolmainmenu = true;


        if (!ran) {
            ran = true;
            timeWhenStart = System.currentTimeMillis();
            //AbstractClientPlayerMixin.loaded = true;
        }
        else if (System.currentTimeMillis()-timeWhenStart <= 2500 && mushroom.welcome) {
            ClickGUI.drawTexture(new ResourceLocation("mushroom/mainmenu/"+folders[Configs.mainmenugif]+"/"+folders[Configs.mainmenugif]+"-0.png"), 0, 0, s.getScaledWidth(), s.getScaledHeight());

            int typenum = (int) Math.floor((System.currentTimeMillis()-timeWhenStart) / 200f);
            if (typenum >= 6) typenum = 6;

            String pee = "";
            if (System.currentTimeMillis()-timeWhenStart < 400) {
                pee = "|";
            }
            if (System.currentTimeMillis()-timeWhenStart > 700) {
                pee = "|";
            }

            String welcomestring = "welcome".substring(0, typenum+1) + pee;
            if (System.currentTimeMillis()-timeWhenStart > 1700) {
                int h = (int) ((((1700 - (System.currentTimeMillis()-timeWhenStart))) / 100f) + 6);
                if (h <= 6 && h > 0) welcomestring = "welcome".substring(0, h) + pee;
                else welcomestring = "";
            }

            FontUtil.productsans40.drawString(welcomestring, (((double) s.getScaledWidth() / 2))-(FontUtil.productsans40.getStringWidth("welcome")/2), (((float) s.getScaledHeight() / 2))-3, -1);

        }

        else {
            mushroom.welcome = false;
            int thexinquestion = ((s.getScaledWidth() / 2)) - (w / 2);
            int theyinquestion = ((s.getScaledHeight() / 2)) - (h / 2) - 20;


            int c = (int) Math.floor(mushroom.animationframe) - 1;
            if (c > frames[Configs.mainmenugif]) {

                if (!Configs.loopgif) {
                    mushroom.animationframe=frames[Configs.mainmenugif];
                    c=frames[Configs.mainmenugif];
                }
                else {
                    mushroom.animationframe = 1;
                    c = 0;
                }
            }

            ClickGUI.drawTexture(new ResourceLocation("mushroom/mainmenu/" + folders[Configs.mainmenugif] + "/" + folders[Configs.mainmenugif] + "-" + c + ".png"), 0, 0, s.getScaledWidth(), s.getScaledHeight());

            mushroom.animationframe += (0.005f * (System.currentTimeMillis()-lastLoop) * Configs.gifSpeed);
            lastLoop = System.currentTimeMillis();

            for (int i = 0; i < buttons.length; i++) {

                if (mx >= thexinquestion && mx <= thexinquestion + w && my >= theyinquestion + (h * i) + (gap * i) && my <= theyinquestion + h + (h * i) + (gap * i)) {

                    Color[] fadeColors = RenderLib.getColorsFade(theyinquestion + (h * i) + (gap * i), h, 100, ClickGUI.getColor(Configs.mainMenuButtonHoverColors), ClickGUI.getColor(Configs.mainMenuButtonHoverColors2), 0.001 * Configs.butFadeSpeed);
                    //this.drawGradientRect(thexinquestion, theyinquestion + (h * i) + (gap * i), thexinquestion + w, theyinquestion + h + (h * i) + (gap * i), fadeColors[0].getRGB(), fadeColors[1].getRGB());
                    RenderLib.drawRoundedRect2(thexinquestion, theyinquestion + (h * i) + (gap * i), w, h, 5, fadeColors[0].getRGB(), false);

                    if (Mouse.isButtonDown(0)) {
                        if (i == 0) mc.displayGuiScreen(new GuiSelectWorld(this));
                        if (i == 1) mc.displayGuiScreen(new GuiMultiplayer(this));
                        if (i == 2) mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                        if (i == 3) mc.displayGuiScreen(new AltManagerGUI());
                        if (i == 4) {
                            // stores previous gui scale then changes scale to 2
                            ClickGUI.startGuiScale = mc.gameSettings.guiScale;

                            // the text in the gui looks hecka ugly on any other gui scale :(
                            mc.gameSettings.guiScale = 2;

                            // "burp" (just tells my code that the gui was opened from main menu and should return there when closed.)
                            ClickGUI.burp = true;

                            // shows of my beautiful gui
                            mc.displayGuiScreen(new ClickGUI());
                        }
                        incoolmainmenu = false;
                    }
                }
                else {
                    Color[] fadeColors = RenderLib.getColorsFade(theyinquestion + (h * i) + (gap * i), h, 100, ClickGUI.getColor(Configs.mainMenuButtonColors), ClickGUI.getColor(Configs.mainMenuButtonColors2), 0.001 * Configs.butFadeSpeed);
                    RenderLib.drawRoundedRect2(thexinquestion, theyinquestion + (h * i) + (gap * i), w, h, 5, fadeColors[0].getRGB(), false);

                    //this.drawGradientRect(thexinquestion, theyinquestion + (h * i) + (gap * i), thexinquestion + w, theyinquestion + h + (h * i) + (gap * i), fadeColors[0].getRGB(), fadeColors[1].getRGB());
                }

                FontUtil.comicsans19.drawCenteredStringWithShadow(buttons[i], thexinquestion + ((float) w / 2), theyinquestion + (h / 2) + (h * i) + (gap * i) - 3, -1);
            }
        }

        double xCoordOfText = (s.getScaledWidth() / 2d) - ((FontUtil.comicsans46.getStringWidth("mushroom client " + mushroom.VERSION))/2);
        float yCoordOfText = ((s.getScaledHeight() / 2f)) - h - FontUtil.comicsans46.getHeight() - 20;

        // seperate lines so i can choose better colors than mc color codes
        FontUtil.comicsans46.drawStringWithShadow("mushroom", xCoordOfText, yCoordOfText, new Color(203, 1, 98).getRGB());
        FontUtil.comicsans46.drawStringWithShadow("client", xCoordOfText + FontUtil.comicsans46.getStringWidth("mushroom "), yCoordOfText, new Color(255, 241, 250).getRGB());
        FontUtil.comicsans46.drawStringWithShadow(mushroom.VERSION, xCoordOfText + FontUtil.comicsans46.getStringWidth("mushroom client "), yCoordOfText, new Color(175, 225, 175).getRGB());

        if (Configs.showchangelog) {

            String[] changelog = mushroom.Changelog.split("\n");

            for (int i = 0; i < changelog.length; i++) {
                Color textColor = new Color(0, 221, 255);
                if (changelog[i].startsWith("#")) {
                    textColor = new Color(231, 184, 55);
                } else if (changelog[i].startsWith("+")) {
                    textColor = new Color(36, 222, 113);
                } else if (changelog[i].startsWith("-")) {
                    textColor = new Color(138, 9, 9);
                }

                FontUtil.productsans19.drawStringWithShadow(changelog[i], 5, 5 + ((FontUtil.productsans19.getHeight() + 5) * i), textColor.getRGB());
            }
        }

    }
}
