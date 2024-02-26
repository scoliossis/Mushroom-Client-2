package mushroom.GUI;

import mushroom.Features.Visual.CoolMainMenu;
import mushroom.Features.Visual.Notifications;
import mushroom.GUI.Settings.*;
import mushroom.Libs.ChatLib;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.RenderLib;
import mushroom.mushroom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Objects;

import static mushroom.Libs.PlayerLib.mc;


public class ClickGUI extends GuiScreen {

    public static int startGuiScale = 0;
    public int oldx = 10;
    public int oldy = 10;
    public int oldw = 150;
    public int oldh = 15;
    public int distbetweencat = 20;

    char char1 = '\u0000';
    int int2 = 999;

    int mousex = 0;
    int mousey = 0;
    int mousebutton = 10;
    boolean mousedown = false;
    int scroll = 0;
    float coloar = 0;
    boolean godown = false;

    int xofscroll = 0;
    int amountscrolled = 0;

    public ClickGUI() {

    }

    int mouseX = 0;
    int mouseY = 0;


    // for the cool color box selector
    int widfOfSquare = 90;
    int heightSquare = 100;

    int heightSlider = 7;

    Color[] baseColors;
    Color[] toggleColors;
    Color[] surroundColors;

    int[] searchForRGB = new int[] {0,0,0};

    String[] animeGirls = {"purpleHair", "redHair", "redHair2", "bread", "pinkHair", "catGirl"};
    public void drawScreen(int mx, int my, float var3) {
        // draws grey background when in game and the default minecraft background if in main menu
        if (!Configs.noBackground) this.drawDefaultBackground();
        super.drawScreen(mx, my, var3);

        mouseX = mx;
        mouseY = my;

        Iterator allsettings = mushroom.settings.iterator();

        ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft());

        // draws black background if in main menu
        if (burp) drawRect(0, 0, s.getScaledWidth(), s.getScaledWidth(), new Color(0,0,0).getRGB());

        // draw kawaii anime girl ;3 in gui
        if (Configs.animegirlsinGUi) ClickGUI.drawTexture(new ResourceLocation("mushroom/clickgui/"+animeGirls[Configs.animegirl]+".png"), s.getScaledWidth() - (s.getScaledWidth() / 3), (int) (s.getScaledHeight() / 2.5f), s.getScaledWidth() / 5, (int) (s.getScaledHeight() - (s.getScaledHeight() / 2.5f)));

        // rescale the x,y,w,h to fit on any screen
        int x = (int) (s.getScaledWidth()/(960f/oldx));
        int y = (int) (s.getScaledHeight()/(495.5f/oldy));
        int w = (int) (s.getScaledWidth()/(960f/oldw));
        int h = (int) (s.getScaledHeight()/(495.5f/oldh));

        int i = 0;
        int numofparents = 0;

        // heck yeah
        int theyinquestion = 0;
        int thexinquestion = 0;

        // needed stuff for my custom color setting
        int[] dontDoSquare = new int[4];
        int[] neededCoords = new int[4];
        Color[] neededColors = new Color[4];
        boolean drosmth = false;

        // start looping through settings
        while (allsettings.hasNext()) {

            // select next setting
            Setting setting = (Setting) allsettings.next();

            String settingName = setting.name;

            if (Configs.nocapitalscgui) settingName = settingName.toLowerCase();
            if (Configs.nospacescgui) settingName = settingName.replaceAll(" ", "");

            // if this is the top setting (category)
            if (setting.parent == null && i != 0) {

                // extends the background color by 2 pixels at the bottom of prev category
                drawRect(thexinquestion, theyinquestion + h, thexinquestion + w, theyinquestion + h + 4, baseColors[1].getRGB());

                // extends the sides of it
                drawRect(thexinquestion - 2, theyinquestion + h, thexinquestion, theyinquestion + h + 4, surroundColors[1].getRGB());
                drawRect(thexinquestion + w, theyinquestion + h, thexinquestion + w + 2, theyinquestion + h + 4, surroundColors[1].getRGB());

                // draws the bottom line
                drawRect(thexinquestion, theyinquestion + h + 2, thexinquestion + w, theyinquestion + h + 4, surroundColors[1].getRGB());

                // smth for scrolling i deadass fogor what this is doing
                if (theyinquestion-h < y) {
                    amountscrolled+=2;
                }
            }

            if (shouldshowwsetting(setting)) {
                // if this is a category thing
                if (setting.parent == null) {
                    numofparents+=1;
                    i=0;
                }


                // sets the y to draw at, also used for getting the previous y coord sometimes
                theyinquestion = y + (h * i);
                thexinquestion = (x + (w * numofparents) + (distbetweencat * numofparents))-w;

                // scrolling yayy, the scrolling feels hecka wierd i need some sort of smoothing, rn it just scrolls 2 settings up at a time
                if (xofscroll == thexinquestion && setting.parent != null) {

                    // adds scrolled amount to y
                    theyinquestion+=amountscrolled*h;
                }

                // getting colors
                Color[] colors = getColors();

                baseColors = RenderLib.getColorsFade(theyinquestion, h, Configs.guifadecent, colors[0], colors[1], 0.05 * Configs.guifadespeed);
                toggleColors = RenderLib.getColorsFade(theyinquestion, h, Configs.guifadecent, colors[2], colors[3], 0.05 * Configs.guifadespeed);
                surroundColors = RenderLib.getColorsFade(theyinquestion, h, Configs.guifadecent, colors[4], colors[5], 0.05 * Configs.guifadespeed);

                // holy shit fade sideways needs to get, it doesnt really work
                if (Configs.fadesideways) {
                    baseColors = RenderLib.getColorsFade(thexinquestion, w, Configs.guifadecent, colors[0], colors[1], 0.05 * Configs.guifadespeed);
                    toggleColors = RenderLib.getColorsFade(thexinquestion, w, Configs.guifadecent, colors[2], colors[3], 0.05 * Configs.guifadespeed);
                    surroundColors = RenderLib.getColorsFade(thexinquestion, w, Configs.guifadecent, colors[4], colors[5], 0.05 * Configs.guifadespeed);
                }

                // check if hovering over button rn
                if (mx >= thexinquestion && mx <= thexinquestion + w && my >= theyinquestion && my <= theyinquestion + h && !(mx >= dontDoSquare[0] && mx <= dontDoSquare[0] + dontDoSquare[2] && my >= dontDoSquare[1] && my <= dontDoSquare[1] + dontDoSquare[3])) {

                    if (setting instanceof BooleanSetting) {
                        // if not a category and also not a subsetting, smth like killaura or scaffold
                        if (setting.parent != null && setting.parent.parent == null) {

                            // if the last key pressed isnt null
                            if (char1 != '\u0000') {

                                // if space is pressed
                                if (int2 == 57) {

                                    // resetting keybind
                                    Notifications.popupmessage(setting.name + "'s", "keybind was reset");
                                    setting.keybindchar = " ";
                                    setting.keybindint = 999;
                                }

                                // if any other key is pressedd
                                else {

                                    // shit for drawing better, doesnt ac do anything rn because the keybinds sorta fucked
                                    if (int2 == 14) setting.keybindchar = "back";
                                    else if (int2 == 15) setting.keybindchar = "tab";
                                    else if (int2 == 28) setting.keybindchar = "enter";

                                    // draws it normally if its a letter (works!!!)
                                    else setting.keybindchar = String.valueOf(char1).toLowerCase();

                                    // sets keybind
                                    setting.keybindint = int2;
                                    if (int2 == 14) Notifications.popupmessage(setting.name, "binded to key §3back");
                                    else if (int2 == 15) Notifications.popupmessage(setting.name, "binded to key §3tab");
                                    else if (int2 == 28) Notifications.popupmessage(setting.name, "binded to key §3enter");
                                    else Notifications.popupmessage(setting.name, "binded to key §3" + String.valueOf(char1).toLowerCase());
                                }

                                // sets last key pressed to null
                                char1 = '\u0000';

                                // useless resetting i think
                                int2 = 999;
                            }
                        }
                    }

                    // if scrolling
                    if (mouseinput) {
                        // sets amountscrolled to 0, which is COMPLETELY USELESS AS IT IS SET NEXT LINE
                        if (xofscroll != thexinquestion) amountscrolled=0;

                        // ???????????????? (legit useless idk why this is here.
                        xofscroll = thexinquestion;

                        // checks if scrolling down, if not adds scroll to y coord
                        if (amountscrolled < 0 || scroll < 0) amountscrolled+=scroll;

                        // sets scrolling to false
                        mouseinput=false;
                    }

                    // if not hovering over a text setting
                    if (!(setting instanceof TextSetting)) {
                        // this draws a description of what the setting hovered over does in the bottom right, might make it at cursor instead
                        this.drawGradientRect((int) (s.getScaledWidth() - FontUtil.productsans19.getStringWidth(setting.description) - 5), s.getScaledHeight() - 10, s.getScaledWidth(), s.getScaledHeight(), new Color(100, 100, 100, 100).getRGB(), new Color(100, 100, 100, 25).getRGB());
                        FontUtil.productsans19.drawStringWithShadow(setting.description, (double) s.getScaledWidth() - FontUtil.productsans19.getStringWidth(setting.description) - 3, (float) s.getScaledHeight() - 9, -1);
                    }

                    // if left click
                    if (mousebutton == 0) {
                        if (setting instanceof BooleanSetting) {
                            // click gui and ghost blocks always set to false
                            if (!Objects.equals(setting.name, "Click GUI") && !Objects.equals(setting.name, "Ghost Block"))
                                setting.set(!(Boolean) setting.get(Boolean.class));

                            // dont allow ghost blocks to be turned on (they arnt in the client anymore :( i will readd)
                            if (Objects.equals(setting.name, "Ghost Block")) Notifications.popupmessage("nuh uh!", "bind this to a keybind then use that please :)");

                        }

                        else if (setting instanceof SelectSetting) {
                            int num;

                            // if the setting is already the max one loop around
                            if ((Integer) setting.get(Integer.class) == ((SelectSetting) setting).options.length-1) num = 0;

                            // if its not max setting add 1 to it
                            else num = (Integer) setting.get(Integer.class)+1;
                            setting.set(num);
                        }
                    }


                    // on right click
                    else if (mousebutton == 1) {
                        // settings like killaura / scaffold, no categorys or subsettings
                        if (setting instanceof BooleanSetting && setting.parent != null && setting.parent.parent == null) {

                            // not good for fps may fix later idrk
                            // if my "rightclicked" string already contains setting name removes it
                            if (mushroom.rightclicked.contains(","+setting.name+",")) mushroom.rightclicked = mushroom.rightclicked.replace(","+setting.name+",", "");

                            // if not the add it to my rightclicked string
                            else mushroom.rightclicked += ","+setting.name+",";

                            // if subsettings parents name is in rightclicked string then it will show
                        }

                        // right clicked select setting does the opposite of left clicking (goes backward)
                        else if (setting instanceof SelectSetting) {
                            int num = 0;
                            if ((Integer) setting.get(Integer.class) == 0) num = ((SelectSetting) setting).options.length-1;
                            else num = (Integer) setting.get(Integer.class)-1;
                            setting.set(num);
                        }
                    }

                    // if mouse down
                    else if (mousedown) {
                        if (setting instanceof SliderSetting) {

                            // still fucking hate maths :yawning:
                            float max = ((SliderSetting) setting).max;
                            float min = ((SliderSetting) setting).min;

                            // gets gets distance between max and minium, then the width and divides them
                            // this gives an increment per pixel
                            float maxdivlen = (max-min)/(w-2);

                            // gets mousex, subtracts x of button from it, times it by the increment per pixel
                            // then adds the minimum number
                            float num = ((mx - thexinquestion) * maxdivlen) + min;

                            // really dumb rounding method (heck yeah)
                            // .replaceAll(",", ".") because float.parsefloat doesnt recognise , as a decimal point.
                            // if your computers lang is spanish or any other lang that uses , instead of . ur game would crash
                            num = Float.parseFloat(new DecimalFormat("#.##").format(num).replaceAll(",", "."));
                            setting.set(num);
                        }
                    }
                }

                // fuck this setting, i hate colorsetting
                // i have a few bugs w this setting,
                // the opacity slider isnt stored for example
                if (setting instanceof ColorSetting) {
                    int[] currentsetting = (int[]) setting.get(int[].class);

                    // if the color setting is being shown
                    if (currentsetting[7] == 1) {
                        int xToDraw = (int) (thexinquestion + (w * 0.8));
                        int yToDraw = (int) (theyinquestion + (h * 0.8));

                        // doesnt allow clicking settings behind the color setting
                        dontDoSquare = new int[]{xToDraw - 10, yToDraw - 10, xToDraw + widfOfSquare + 20, yToDraw + heightSquare + 22};
                    }


                    // if not hovering over another color setting and the color setting is being shown
                    if (!(mx >= dontDoSquare[0] && mx <= dontDoSquare[0] + dontDoSquare[2] && my >= dontDoSquare[1] && my <= dontDoSquare[1] + dontDoSquare[3]) && mousebutton == 0 && currentsetting[7] == 1) {
                        // stops showing the setting
                        currentsetting[7] = 0;

                        // stores everything about the setting
                        setting.set(currentsetting);

                        // shouldnt store this i think idk
                        HSLover = new Color(currentsetting[4], currentsetting[5], currentsetting[6]);
                    }

                    // if the setting not being shown and hovering over button, OR hovering over the left 70% of the button where the box isnt overlapping
                    else if (currentsetting[7] == 0 || (mx >= thexinquestion && mx <= thexinquestion + (w*0.7) && my >= theyinquestion && my <= theyinquestion + h)) {
                        if (mx >= thexinquestion && mx <= thexinquestion + w && my >= theyinquestion && my <= theyinquestion + h) {
                            // if left clicked
                            if (mousebutton == 0) {

                                // if not showing setting
                                if (currentsetting[7] == 0) {

                                    // if not hovering over another color box
                                    if (!(mx >= dontDoSquare[0] && mx <= dontDoSquare[0] + dontDoSquare[2] && my >= dontDoSquare[1] && my <= dontDoSquare[1] + dontDoSquare[3])) {
                                        currentsetting[7] = 1;
                                        setting.set(currentsetting);

                                        rgbOver = new Color(currentsetting[0], currentsetting[1], currentsetting[2]);
                                        HSLover = new Color(currentsetting[4], currentsetting[5], currentsetting[6]);
                                    }

                                    // if its showing
                                } else {

                                    // stops showing it and saves setting
                                    currentsetting[7] = 0;
                                    setting.set(currentsetting);
                                    HSLover = new Color(currentsetting[4], currentsetting[5], currentsetting[6]);
                                }
                            }
                        }
                    }

                    else if (mousedown) {
                        int xToDraw = (int) (thexinquestion + (w * 0.8));
                        int yToDraw = (int) (theyinquestion + (h * 0.8));

                        dontDoSquare = new int[]{xToDraw - 10, yToDraw - 10, xToDraw + widfOfSquare + 20, yToDraw + heightSquare + 22};

                        // sets setting to color being hovered over
                        // the color being hovered over is set in the drawcolor functions at the bottom
                        if (mx >= dontDoSquare[0] && mx <= dontDoSquare[0] + dontDoSquare[2] && my >= dontDoSquare[1] && my <= dontDoSquare[1] + dontDoSquare[3]) {
                            setting.set(new int[]{rgbOver.getRed(), rgbOver.getGreen(), rgbOver.getBlue(), OpacityOver.getAlpha(), HSLover.getRed(), HSLover.getGreen(), HSLover.getBlue(), 1});
                        }
                    }

                }

                // sets default x of text
                double textXcat = thexinquestion + 5;
                double textXnor = thexinquestion + 5;
                double textXext = thexinquestion + 5;

                // if the text is centered differently
                if (Configs.categorystextcenter == 1) textXcat = (thexinquestion + (w / 2d)) - (FontUtil.productsans19.getStringWidth(settingName) / 2);
                else if (Configs.categorystextcenter == 2) textXcat = (thexinquestion + w) - FontUtil.productsans19.getStringWidth(settingName) - 5;

                if (Configs.textcenter == 1) textXnor = (thexinquestion + (w / 2d)) - (FontUtil.productsans19.getStringWidth(settingName) / 2);
                else if (Configs.textcenter == 2) textXnor = (thexinquestion + w) - FontUtil.productsans19.getStringWidth(settingName) - 5;

                if (Configs.extrastextcenter == 1) textXext = (thexinquestion + (w / 2d)) - (FontUtil.productsans19.getStringWidth(settingName) / 2);
                else if (Configs.extrastextcenter == 2) textXext = (thexinquestion + w) - FontUtil.productsans19.getStringWidth(settingName) - 5;

                // checks if setting is visible
                if (theyinquestion > y || setting.parent == null) {

                    if (Configs.sidescgui) {
                        if (setting.parent != null) {

                            // ugly ahh sideways fade code
                            if (Configs.fadesideways) {
                                RenderLib.drawFade((thexinquestion - 2), theyinquestion-2, 2, h, surroundColors[0], surroundColors[1]);
                                RenderLib.drawFade((thexinquestion + w), theyinquestion-2, 2, h, surroundColors[0], surroundColors[1]);
                            } else {
                                this.drawGradientRect(thexinquestion - 2, theyinquestion, thexinquestion, theyinquestion + h, surroundColors[0].getRGB(), surroundColors[1].getRGB());
                                this.drawGradientRect(thexinquestion + w, theyinquestion, thexinquestion + w + 2, theyinquestion + h, surroundColors[0].getRGB(), surroundColors[1].getRGB());
                            }
                        }

                        else {
                            RenderLib.drawRoundedRect2(thexinquestion - 2, theyinquestion - 2, w + 4, h + 2, 5, surroundColors[1].getRGB(), true);
                        }
                    }

                    // draws category
                    if (setting.parent == null && setting instanceof BooleanSetting) {
                        //this.drawGradientRect(thexinquestion, theyinquestion, thexinquestion + w, theyinquestion + h, new Color(27, 27, 27).getRGB(), new Color(23, 23, 23).getRGB());

                        //if ((Boolean) setting.get(Boolean.class))
                        if (!Configs.sidescgui) RenderLib.drawRoundedRect2(thexinquestion, theyinquestion, w, h, 5, surroundColors[1].getRGB(), true);
                        else RenderLib.drawRoundedRect2(thexinquestion, theyinquestion, w, h, 5, toggleColors[1].getRGB(), true);
                        //this.drawGradientRect(thexinquestion, theyinquestion, thexinquestion + w, theyinquestion + h, surroundColors[0].getRGB(), surroundColors[1].getRGB());

                        FontUtil.productsans19.drawStringWithShadow(settingName, textXcat, theyinquestion + (h / 2f) - 3, -1);

                    }

                    // draws not categorys or subsettings, killaura/scaffold n shit
                    else if (setting.parent != null && setting.parent.parent == null && setting instanceof BooleanSetting) {

                        // draws square behind text on setting, this is covered up if the setting is on
                        if (Configs.fadesideways) RenderLib.drawFade(thexinquestion, theyinquestion, w, h, baseColors[0], baseColors[1]);
                        else this.drawGradientRect(thexinquestion, theyinquestion, thexinquestion + w, theyinquestion + h, baseColors[0].getRGB(), baseColors[1].getRGB());



                        // if the setting is on, or is the click gui setting draws them differently to show they on
                        if ((Boolean) setting.get(Boolean.class) || Objects.equals(setting.name, "Click GUI")) {
                            if (!Configs.fadesideways) {
                                if (!Configs.sidescgui)
                                    this.drawGradientRect(thexinquestion, theyinquestion, thexinquestion + w, theyinquestion + h, toggleColors[0].getRGB(), toggleColors[1].getRGB());
                                else
                                    this.drawGradientRect(thexinquestion + 2, theyinquestion, thexinquestion + w - 2, theyinquestion + h, toggleColors[0].getRGB(), toggleColors[1].getRGB());
                            }
                            else {
                                if (!Configs.sidescgui)
                                    RenderLib.drawFade(thexinquestion, theyinquestion, w, h, toggleColors[0], toggleColors[1]);
                                else
                                    RenderLib.drawFade(thexinquestion + 2, theyinquestion, w - 4, h, toggleColors[0], toggleColors[1]);

                            }
                        }


                        // draw setting name
                        FontUtil.productsans19.drawStringWithShadow(settingName, textXnor, theyinquestion + (h / 2f) - 3, -1);

                        // draws the keybind
                        if (!Objects.equals(setting.keybindchar, " ")) {
                            if (Configs.textcenter != 0) FontUtil.productsans19.drawStringWithShadow(String.valueOf(setting.keybindchar), thexinquestion + 5, theyinquestion + (h / 2) - 3, -1);
                            else FontUtil.productsans19.drawStringWithShadow(String.valueOf(setting.keybindchar), thexinquestion + w - 5 - FontUtil.productsans19.getStringWidth(setting.keybindchar), theyinquestion + (h / 2) - 3, -1);
                        }

                    }




                    // drawing subsettings
                    else {

                        // my little boolean subsetting
                        if (setting instanceof BooleanSetting) {

                            // draws background behind text
                            if (Configs.fadesideways) RenderLib.drawFade(thexinquestion, theyinquestion, w, h, baseColors[0], baseColors[1]);
                            else this.drawGradientRect(thexinquestion, theyinquestion, thexinquestion + w, theyinquestion + h, baseColors[0].getRGB(), baseColors[1].getRGB());

                            if (!Configs.fadesideways) {

                                // draws a white box towards the right side of the square
                                if (Configs.extrastextcenter != 0)
                                    this.drawGradientRect((int) (thexinquestion + (w * 0.025)), (int) ((theyinquestion + (h * 0.5)) - w * 0.025), (int) (thexinquestion + (w * 0.075)), (int) ((theyinquestion + (h * 0.5)) + w * 0.025), new Color(255, 255, 255, 255).getRGB(), new Color(200, 200, 200, 255).getRGB());
                                else
                                    this.drawGradientRect((int) (thexinquestion + (w * 0.8)), (int) ((theyinquestion + (h * 0.5)) - w * 0.025), (int) (thexinquestion + (w * 0.855)), (int) ((theyinquestion + (h * 0.5)) + w * 0.025), new Color(255, 255, 255, 255).getRGB(), new Color(200, 200, 200, 255).getRGB());

                                // draws a colored box where the white box was if the setting is on
                                if ((Boolean) setting.get(Boolean.class)) {
                                    if (Configs.extrastextcenter != 0)
                                        this.drawGradientRect((int) (thexinquestion + (w * 0.025)), (int) ((theyinquestion + (h * 0.5)) - w * 0.025), (int) (thexinquestion + (w * 0.075)), (int) ((theyinquestion + (h * 0.5)) + w * 0.025), toggleColors[0].getRGB(), toggleColors[1].getRGB());
                                    else
                                        this.drawGradientRect((int) (thexinquestion + (w * 0.8)), (int) ((theyinquestion + (h * 0.5)) - w * 0.025), (int) (thexinquestion + (w * 0.855)), (int) ((theyinquestion + (h * 0.5)) + w * 0.025), toggleColors[0].getRGB(), toggleColors[1].getRGB());
                                }
                            }
                            else {

                                // draws sideways fading small box
                                if (Configs.extrastextcenter != 0)
                                    RenderLib.idiotFadeDumbStupid((int) (thexinquestion + (w * 0.025)), (int) (((theyinquestion + (h * 0.5)) + w * 0.025) - w * 0.0075), (int) (thexinquestion + (w * 0.07)), (int) (((theyinquestion + (h * 0.5)) + w * 0.05) + w * 0.0075), new Color(255, 255, 255, 255), new Color(200, 200, 200, 255));
                                else
                                    RenderLib.idiotFadeDumbStupid((int) (thexinquestion + (w * 0.8)), (int) (((theyinquestion + (h * 0.5)) + w * 0.025) - w * 0.0075), (int) (thexinquestion + (w * 0.85)), (int) (((theyinquestion + (h * 0.5)) + w * 0.05) + w * 0.0075), new Color(255, 255, 255, 255), new Color(200, 200, 200, 255));

                                if ((Boolean) setting.get(Boolean.class)) {
                                    if (Configs.extrastextcenter != 0)
                                        RenderLib.idiotFadeDumbStupid((int) (thexinquestion + (w * 0.025)), (int) (((theyinquestion + (h * 0.5))) + w * 0.025 - w * 0.0075), (int) (thexinquestion + (w * 0.07)), (int) (((theyinquestion + (h * 0.5)) + w * 0.05) + w * 0.0075), toggleColors[0], toggleColors[1]);
                                    else
                                        RenderLib.idiotFadeDumbStupid((int) (thexinquestion + (w * 0.8)), (int) (((theyinquestion + (h * 0.5))) + w * 0.025 - w * 0.0075), (int) (thexinquestion + (w * 0.85)), (int) (((theyinquestion + (h * 0.5)) + w * 0.05) + w * 0.0075), toggleColors[0], toggleColors[1]);
                                }
                            }


                            FontUtil.productsans19.drawStringWithShadow(settingName, textXext, theyinquestion + (h / 2f) - 3, -1);

                        } else if (setting instanceof SelectSetting) {
                            // background
                            if (Configs.fadesideways) RenderLib.drawFade(thexinquestion, theyinquestion, w, h, baseColors[0], baseColors[1]);
                            else this.drawGradientRect(thexinquestion, theyinquestion, thexinquestion + w, theyinquestion + h, baseColors[0].getRGB(), baseColors[1].getRGB());

                            // setting name
                            FontUtil.productsans19.drawStringWithShadow(settingName, textXext, theyinquestion + (h / 2f) - 3, -1);

                            // setting set
                            if (Configs.extrastextcenter != 0) FontUtil.productsans19.drawStringWithShadow(((SelectSetting) setting).options[(Integer) setting.get(Integer.class)], thexinquestion + 5, theyinquestion + (h / 2) - 3, -1);
                            else FontUtil.productsans19.drawStringWithShadow(((SelectSetting) setting).options[(Integer) setting.get(Integer.class)], thexinquestion + w - FontUtil.productsans19.getStringWidth(((SelectSetting) setting).options[(Integer) setting.get(Integer.class)]) - 5, theyinquestion + (h / 2) - 3, -1);

                        }

                        // slider, yay
                        else if (setting instanceof SliderSetting) {

                            if (Configs.fadesideways) RenderLib.drawFade(thexinquestion, theyinquestion, w, h, baseColors[0], baseColors[1]);
                            else this.drawGradientRect(thexinquestion, theyinquestion, thexinquestion + w, theyinquestion + h, baseColors[0].getRGB(), baseColors[1].getRGB());

                            float currentsetting = (float) setting.get(Float.class);

                            // hell yeah, maths
                            float max = ((SliderSetting) setting).max;
                            float min = ((SliderSetting) setting).min;

                            // i think i used a similar math think earlier, this is just a worse version of that
                            // it only gets to the current setting instead of the whole widdth
                            float widthe = (float) w / ((max - min) / (currentsetting-min));
                            widthe = Math.max(0, Math.min(widthe, w-4));

                            if (!Configs.sidescgui) {
                                if (Configs.fadesideways)
                                    RenderLib.drawFade(thexinquestion, theyinquestion, (int) (widthe), h, toggleColors[0], toggleColors[1]);
                                else
                                    this.drawGradientRect(thexinquestion, theyinquestion, (int) (thexinquestion + widthe), theyinquestion + h, toggleColors[0].getRGB(), toggleColors[1].getRGB());
                            }
                            else {
                                if (Configs.fadesideways)
                                    RenderLib.drawFade(thexinquestion + 2, theyinquestion, (int) (4 + widthe), h, toggleColors[0], toggleColors[1]);
                                else
                                    this.drawGradientRect(thexinquestion + 2, theyinquestion, (int) (thexinquestion + 2 + widthe), theyinquestion + h, toggleColors[0].getRGB(), toggleColors[1].getRGB());
                            }
                            FontUtil.productsans19.drawStringWithShadow(settingName, textXext, theyinquestion + (h / 2f) - 3, -1);

                            // draws setting of slider
                            if (Configs.extrastextcenter != 0) FontUtil.productsans19.drawStringWithShadow(String.valueOf(setting.get(Float.class)), thexinquestion + 5, theyinquestion + (h / 2f) - 3, -1);
                            else FontUtil.productsans19.drawStringWithShadow(String.valueOf(setting.get(Float.class)), thexinquestion + w - FontUtil.productsans19.getStringWidth(String.valueOf(setting.get(Float.class))) - 5, theyinquestion + (h / 2f) - 3, -1);

                        }

                        // FUCKING COLOR SETTING
                        else if (setting instanceof ColorSetting) {

                            // background :yawn:
                            if (Configs.fadesideways) {
                                RenderLib.drawFade(thexinquestion, theyinquestion, w, h, baseColors[0], baseColors[1]);
                            }
                            else {
                                this.drawGradientRect(thexinquestion, theyinquestion, thexinquestion + w, theyinquestion + h, baseColors[0].getRGB(), baseColors[1].getRGB());
                            }

                            int[] currentsetting = (int[]) setting.get(int[].class);

                            // if not showing the box
                            if (currentsetting[7] == 0) {
                                //drawRect((int) (thexinquestion + (w * 0.85))-1, (int) ((theyinquestion + (h * 0.5)) - w * 0.025)-1, (int) (thexinquestion + (w * 0.905))+1, (int) ((theyinquestion + (h * 0.5)) + w * 0.025)+1, new Color(255-currentsetting[0], 255-currentsetting[1], 255-currentsetting[2], 255).getRGB());

                                drawRect((int) (thexinquestion + (w * 0.85)), (int) ((theyinquestion + (h * 0.5)) - w * 0.025), (int) (thexinquestion + (w * 0.905)), (int) ((theyinquestion + (h * 0.5)) + w * 0.025), new Color(currentsetting[0], currentsetting[1], currentsetting[2], currentsetting[3]).getRGB());
                            }

                            // if showing the color slider thingy
                            else {
                                drosmth = true;

                                searchForRGB = currentsetting;

                                int xToDraw = (int) (thexinquestion + (w * 0.7));
                                int yToDraw = (int) (theyinquestion + (h*0.8));

                                // store some shit to avoid / use, later
                                dontDoSquare = new int[] {xToDraw - 10, yToDraw - 10, xToDraw + widfOfSquare + 20, yToDraw + heightSquare + 22};

                                neededCoords = new int[] {xToDraw, yToDraw, widfOfSquare, heightSquare};
                                neededColors = new Color[] {baseColors[0], baseColors[1], surroundColors[0], surroundColors[1]};

                            }

                            FontUtil.productsans19.drawStringWithShadow(settingName, textXext, theyinquestion + (h / 2f) - 3, -1);

                        }

                        // this text setting sucks, the default minecraft textboxes are ugly and poop
                        // i made my own text box in java before ill add it here later
                        else if (setting instanceof TextSetting) {

                            // make mc textbox
                            GuiTextField textField = new GuiTextField(0, this.mc.fontRendererObj, thexinquestion, theyinquestion, w, h);
                            textField.setEnableBackgroundDrawing(false);
                            textField.setMaxStringLength(1000);
                            textField.setFocused(false);
                            textField.drawTextBox();

                            // add text only if hovering over the button
                            if (mx >= thexinquestion && mx <= thexinquestion + w && my >= theyinquestion && my <= theyinquestion + h && char1 != '\u0000') {
                                textField.setFocused(true);
                                if (int2 == 14 && !setting.get(String.class).toString().isEmpty()) {
                                    setting.set(setting.get(String.class).toString().substring(0, setting.get(String.class).toString().length() - 1));
                                } else {
                                    textField.textboxKeyTyped(char1, int2);
                                    setting.set(setting.get(String.class) + textField.getText());
                                }
                            }

                            // stop making letters add to the textbox
                            textField.setFocused(false);

                            // blehh
                            textField.setText((String) setting.get(String.class));

                            // draw background
                            if (Configs.fadesideways) RenderLib.drawFade(thexinquestion, theyinquestion, w, h, baseColors[0], baseColors[1]);
                            else this.drawGradientRect(thexinquestion, theyinquestion, thexinquestion + w, theyinquestion + h, baseColors[0].getRGB(), baseColors[1].getRGB());

                            // if the text is too long, cut of part of the start, will be fixed later maybe
                            String renderstring = String.valueOf(setting.get(String.class));
                            if (!Objects.equals(String.valueOf(setting.get(String.class)), "") && !setting.get(String.class).toString().isEmpty()) {
                                if (FontUtil.productsans19.getStringWidth(renderstring) > w - 5) {
                                    while (FontUtil.productsans19.getStringWidth(renderstring) > w - 5) {
                                        renderstring = renderstring.substring(1);
                                    }
                                }

                                FontUtil.productsans19.drawString(renderstring, thexinquestion + 5, theyinquestion + (h / 2) - 3, -1);

                                if (mx >= thexinquestion && mx <= thexinquestion + w && my >= theyinquestion && my <= theyinquestion + h) {
                                    this.drawGradientRect((int) (s.getScaledWidth() - FontUtil.productsans19.getStringWidth(settingName) - 5), s.getScaledHeight() - 10, s.getScaledWidth(), s.getScaledHeight(), new Color(100, 100, 100, 100).getRGB(), new Color(100, 100, 100, 25).getRGB());
                                    FontUtil.productsans19.drawStringWithShadow(settingName, (double) s.getScaledWidth() - FontUtil.productsans19.getStringWidth(settingName) - 3, (float) s.getScaledHeight() - 9, -1);
                                }
                            } else {
                                FontUtil.productsans19.drawStringWithShadow(settingName, thexinquestion + 5, theyinquestion + (h / 2) - 3, -1);
                            }
                        }

                        /* WARNING !!!
                        Congratulations, you got a reaction from users. That's what you wanted right?
                        Well, I've decided I don't like people like you.
                        You've messed with the community of the wrong psychopath.
                        Before you get excited, you haven't even made me angry.
                        I am a hard person to make angry. However, I despise people like you.
                        Your pitiful hacking skills are hilarious.
                        Hacking accounts and putting up proxies are level 1.
                        Can you hack encrypted files? Can you tear through firewalls without leaving a mark?
                        Your silly little proxy won't protect you.
                        I have hacked into many computers and spied on the users.
                        I've hacked into games.
                        I've been hacking since I had a computer.
                        It's what I was raised to do.
                        You have no idea to the extent of fear which you should be feeling.
                        All you are is just a community of internet creeps.
                        Have you ever murdered anyone?
                        I have no empathy and I will probably feel joy peeling your skin off your face.
                        You think I'm giving you an empty threat? Believe that.
                        I have contacts in dark places that you don't want to know about.
                        If you live even close to me you better fear for your life.
                        Track my IP if you want to, but I am smart enough to use a library computer.
                        Hack into my account if you want, but it'll just make it easier for me to track you.
                         */

                        else {
                            // incase u mess up
                            this.drawGradientRect(thexinquestion, theyinquestion, thexinquestion + w, theyinquestion + h, baseColors[0].getRGB(), baseColors[1].getRGB());
                            ChatLib.chat(setting.name + " IS NOT A BOOLEAN AND HAS BREACHED CONTAINENT!!!!! (please put it inside of a parent because i didnt prepare for silly buttons to be without 2 parents");
                        }
                    }
                }
                i++;
            }
        }

        // oh but we arnt done yet ;3

        // draws bottom line of last category before configs
        drawRect(thexinquestion, theyinquestion + h, thexinquestion + w, theyinquestion + h + 4, baseColors[1].getRGB());
        drawRect(thexinquestion - 2, theyinquestion + h, thexinquestion, theyinquestion + h + 4, surroundColors[1].getRGB());
        drawRect(thexinquestion + w, theyinquestion + h, thexinquestion + w + 2, theyinquestion + h + 4, surroundColors[1].getRGB());
        drawRect(thexinquestion, theyinquestion + h + 2, thexinquestion + w, theyinquestion + h + 4, surroundColors[1].getRGB());


        // draws color setting thingy above all
        if (drosmth) {
            if (neededCoords[0] != 0) {
                // outline
                drawGradientRect(neededCoords[0] - 11, neededCoords[1] - 11, neededCoords[0] + widfOfSquare + 21, neededCoords[1] + heightSquare + 23, neededColors[2].getRGB(), neededColors[3].getRGB());

                // background (big ahh square 100x100)
                drawGradientRect(neededCoords[0] - 10, neededCoords[1] - 10, neededCoords[0] + widfOfSquare + 20, neededCoords[1] + heightSquare + 22, neededColors[0].getRGB(), neededColors[1].getRGB());

                // draws coolio HSL slider (hue saturation light)
                for (int o = 0; o < HSLcolors.length - 1; o++) {
                    int egx = (o * (widfOfSquare / (HSLcolors.length - 1))) + 2 + neededCoords[0];
                    int wid = (widfOfSquare / (HSLcolors.length - 1));
                    drawHSLSlider(egx, heightSquare + neededCoords[1] + 5, wid, heightSlider, HSLcolors[o], HSLcolors[o + 1]);
                }

                // white rectangle behind opacity slider
                drawRect(neededCoords[0] + 2, heightSquare + neededCoords[1] + 5 + heightSlider + 2, neededCoords[0] + widfOfSquare - 2, heightSquare + neededCoords[1] + 5 + heightSlider + 2 + heightSlider, new Color(255, 255, 255).getRGB());
                // opacity slider :sunglasses:
                opacitySlider(neededCoords[0] + 2, heightSquare + neededCoords[1] + 5 + heightSlider + 2, widfOfSquare, heightSlider, new Color(HSLover.getRed(), HSLover.getGreen(), HSLover.getBlue(), 0), new Color(HSLover.getRed(), HSLover.getGreen(), HSLover.getBlue(), 255));


                // cool ahh threewayfade code that took me like 10 minutes :cool: (and is slightly poop maybe idk)
                drawThreeWayFade(neededCoords[0], neededCoords[1], widfOfSquare, heightSquare, HSLover, new Color(0, 0, 0), new Color(255, 255, 255));

                // draws a square on the currently hovered over color
                drawRect(xOfColor - 2, yOfColor - 2, xOfColor + 3, yOfColor + 3, new Color(20, 20, 20, 150).getRGB());
            }
        }

        // draw bottom line of last collumn
        if (theyinquestion-h < y) {
            // scrolling for the configs i think?
            // idk prob does nothing
            amountscrolled+=1;
        }

        // new x, heck yeah
        int newx = thexinquestion + w + distbetweencat;
        int newi = 0;
        int newy = 0;

        // for every config file, excluding keybinds
        for (File file : Objects.requireNonNull(Paths.get("config/mushroomclient/configs").toFile().listFiles())) {
            if (!file.getName().contains("keybinds")) {
                newi++;

                // sets new y coord every time
                newy = y + (h * newi);

                // gets colors again yayyyy!
                Color[] colors = getColors();

                baseColors = RenderLib.getColorsFade(newy, h, Configs.guifadecent, colors[0], colors[1], 0.05 * Configs.guifadespeed);
                toggleColors = RenderLib.getColorsFade(newy, h, Configs.guifadecent, colors[2], colors[3], 0.05 * Configs.guifadespeed);
                surroundColors = RenderLib.getColorsFade(newy, h, Configs.guifadecent, colors[4], colors[5], 0.05 * Configs.guifadespeed);

                // fuck this :yawn:
                if (Configs.fadesideways) {
                    baseColors = RenderLib.getColorsFade(newx, w, Configs.guifadecent, colors[0], colors[1], 0.05 * Configs.guifadespeed);
                    toggleColors = RenderLib.getColorsFade(newx, w, Configs.guifadecent, colors[2], colors[3], 0.05 * Configs.guifadespeed);
                    surroundColors = RenderLib.getColorsFade(newx, w, Configs.guifadecent, colors[4], colors[5], 0.05 * Configs.guifadespeed);
                }


                // x of text
                double textXcat = newx + 5;

                // different text centers
                if (Configs.categorystextcenter == 1) textXcat = (newx + (w / 2d)) - (FontUtil.productsans19.getStringWidth("configs") / 2);
                else if (Configs.categorystextcenter == 2) textXcat = (newx + w) - FontUtil.productsans19.getStringWidth("configs") - 5;


                // if this is the first config
                if (newi == 1) {
                    // draws cool top thingy
                    if (Configs.sidescgui) {
                        RenderLib.drawRoundedRect2(newx - 2, y - 2, w + (4), h + 2, 5, surroundColors[1].getRGB(), true);
                    }

                    // yay
                    RenderLib.drawRoundedRect2(newx, y, w, h, 5, toggleColors[1].getRGB(), true);

                    FontUtil.productsans19.drawStringWithShadow("configs", textXcat, y + (h / 2f) - 3, -1);
                }

                // draws sidelines
                if (Configs.sidescgui) {
                    this.drawGradientRect((newx - 2), newy, (newx), newy + h, surroundColors[0].getRGB(), surroundColors[1].getRGB());
                    this.drawGradientRect((newx + w), newy, (newx + w + 2), newy + h, surroundColors[0].getRGB(), surroundColors[1].getRGB());
                }
                String configname = file.getName().replace(".cfg", "");


                // if this config is loaded draw it dif
                if (configname.equals(mushroom.loadedconfig)) drawGradientRect(newx, newy, newx + w, newy + h, toggleColors[0].getRGB(), toggleColors[1].getRGB());
                else drawGradientRect(newx, newy, newx + w, newy + h, baseColors[0].getRGB(), baseColors[1].getRGB());

                // text center !!
                double textXcon = newx + 5;

                if (Configs.configstextcenter == 1) textXcon = (newx + (w / 2d)) - (FontUtil.productsans19.getStringWidth(configname) / 2);
                else if (Configs.configstextcenter == 2) textXcon = (newx + w) - FontUtil.productsans19.getStringWidth(configname) - 5;

                FontUtil.productsans19.drawStringWithShadow(configname, textXcon, newy + (h / 2f) - 3, -1);

                // hovering over button
                if (mx >= newx && mx <= newx + w && my >= newy && my <= newy + h) {
                    // help message in bottom right
                    FontUtil.productsans35.drawStringWithShadow("use /m save [configname] to save config", 10, (s.getScaledHeight())-35, -1);

                    // if left click set current config to this one
                    if (mousebutton == 0) {
                        Config.save(mushroom.loadedconfig + ".cfg");
                        mushroom.loadedconfig = configname;
                        Config.load(file.getName());
                        Notifications.popupmessage("config loaded: ", configname);
                    }
                }
            }
        }

        // draws bottom shit at the end
        drawRect(newx, newy + h, newx + w, newy + h + 4, baseColors[1].getRGB());
        drawRect(newx - 2, newy + h, newx, newy + h + 4, surroundColors[1].getRGB());
        drawRect(newx + w, newy + h, newx + w + 2, newy + h + 4, surroundColors[1].getRGB());
        drawRect(newx, newy + h + 2, newx + w, newy + h + 4, surroundColors[1].getRGB());

        //this.drawGradientRect(newx-1, newy+h, newx + w+1, newy + h + 2, surroundstartcolour.getRGB(), surroundendcolour.getRGB());
        //this.drawGradientRect(newx, newy+h+2, newx + w, newy+ + h + 3, surroundstartcolour.getRGB(), surroundendcolour.getRGB());

        //theyinquestion = y + (h * i);

        mousex = 0;
        mousey = 0;

        // sets last mouse click to random, so mousebutton is only set for 1 tick
        mousebutton = 10;

        // sets last key pressed to null
        char1 = '\u0000';
    }
    public void onGuiClosed() {
        try {
            Config.save("current.cfg");
            Config.savekeybinds();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);
        mousex = x;
        mousey = y;
        mousebutton = button;
        mousedown = true;
    }

    @Override
    protected void mouseReleased(int x, int y, int button) {
        super.mouseReleased(x, y, button);
        mousedown = false;
    }

    public static boolean burp = false;
    public void keyTyped(char uno, int dos) {
        char1 = uno;
        int2 = dos;
        if (dos == 1) {
            // idfk i messed up smth in the textbox shit so...
            // ^^^^^^^^^^ ???? idfk what this note is about, i removed some code here ig and just left the note


            mc.gameSettings.guiScale = startGuiScale;

            if (burp) {
                burp=false;
                mc.displayGuiScreen(new CoolMainMenu());
            }
            else mc.thePlayer.closeScreen();
        }
    }

    public static void drawTexture(ResourceLocation resourceLocation, int x, int y, int w1, int h1, int w2, int h2, int u, int v) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        GlStateManager.color(255.0F, 255.0F, 255.0F);
        Gui.drawModalRectWithCustomSizedTexture(x, y, (float) u, (float) v, w1, h1, (float) w2, (float) h2);
    }

    public static void drawTexture(ResourceLocation resourceLocation, int x, int y, int w, int h) {
        drawTexture(resourceLocation, x, y, w, h, w, h, 0, 0);
    }

    boolean mouseinput = false;
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            if (scroll > 1) {
                scroll = 1;
            }
            if (scroll < -1) {
                scroll = -1;
            }
        }
        mouseinput=true;
    }


    public boolean shouldshowwsetting(Setting setting) {
        return setting.parent == null || setting.parent.parent == null || setting.parent.parent.parent == null && mushroom.rightclicked.contains("," + setting.parent.name + ",") || Objects.requireNonNull(Objects.requireNonNull(setting.parent).parent).parent != null && setting.parent instanceof BooleanSetting && (Boolean) setting.parent.get(Boolean.class) && mushroom.rightclicked.contains("," + setting.parent.parent.name + ",") || Objects.requireNonNull(setting.parent.parent).parent != null && setting.parent instanceof SelectSetting && (int) setting.parent.get(Integer.class) == setting.modereq && mushroom.rightclicked.contains("," + setting.parent.parent.name + ",") || (Objects.requireNonNull(Objects.requireNonNull(setting.parent).parent).parent != null && setting.parent instanceof BooleanSetting && (Boolean) setting.parent.get(Boolean.class) && shouldshowwsetting(setting.parent));
    }

    public static void handleKeypress(final int key) {
        try {
            if (key == 0 || key == 999) {
                return;
            }
            Iterator allsettings = mushroom.settings.iterator();
            while (allsettings.hasNext()) {
                Setting setting = (Setting) allsettings.next();
                if (setting.keybindint == key) {
                    if (!(setting instanceof BooleanSetting)) return;

                    if (Objects.equals(setting.name, "Click GUI")) {
                        // stores previous gui scale then changes scale to 2
                        ClickGUI.startGuiScale = PlayerLib.mc.gameSettings.guiScale;

                        // the text in the gui looks hecka ugly on any other gui scale :(
                        PlayerLib.mc.gameSettings.guiScale = 2;

                        mushroom.guiToOpen = new ClickGUI();
                    }
                    else {
                        setting.set(!(Boolean) setting.get(Boolean.class));
                        Notifications.popupmessage(setting.name, ((Boolean) setting.get(Boolean.class) ? " enabled" : " disabled"));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void updateScreen() {
        super.updateScreen();
    }


    public static Color[] getColors() {
        Color startColor = new Color(44, 45, 51, 255);
        Color endColor = new Color(44, 45, 51, 255);

        Color toggleStartColor = new Color(244, 13, 253, 255);
        Color toggleEndColor = new Color(13, 99, 236, 255);

        Color surroundStartColor = new Color(13, 99, 236, 255);
        Color surroundEndColor = new Color(244, 13, 253, 255);

        if (Configs.cguicolor == 1) {
            startColor = new Color(44, 45, 51, 255);
            endColor = new Color(44, 45, 51, 255);

            toggleStartColor =  new Color(15, 73, 3, 255);
            toggleEndColor =  new Color(151, 210, 168, 255);

            surroundStartColor = new Color(151, 210, 168, 255);
            surroundEndColor = new Color(15, 73, 3, 255);
        }
        else if (Configs.cguicolor == 2) {
            startColor = new Color(44, 45, 51, 255);
            endColor = new Color(44, 45, 51, 255);

            toggleStartColor = new Color(213, 21, 21, 255);
            toggleEndColor = new Color(231, 207, 207, 255);

            surroundStartColor = new Color(213, 21, 21, 255);
            surroundEndColor = new Color(253, 208, 252, 255);
        }
        else if (Configs.cguicolor == 3) {
            startColor = new Color(44, 45, 51, 255);
            endColor = new Color(44, 45, 51, 255);

            toggleStartColor = new Color(236, 85, 224, 255);
            toggleEndColor = new Color(148, 96, 173, 255);

            surroundStartColor = new Color(199, 167, 232, 255);
            surroundEndColor = new Color(175, 16, 231, 255);
        }
        else if (Configs.cguicolor == 4) {
            startColor = new Color(Configs.clickguiCustomColorBack[0], Configs.clickguiCustomColorBack[1], Configs.clickguiCustomColorBack[2], Configs.clickguiCustomColorBack[3]);
            endColor = new Color(Configs.clickguiCustomColorBack2[0], Configs.clickguiCustomColorBack2[1], Configs.clickguiCustomColorBack2[2], Configs.clickguiCustomColorBack2[3]);

            toggleStartColor = new Color(Configs.clickguiCustomColorTog[0], Configs.clickguiCustomColorTog[1], Configs.clickguiCustomColorTog[2], Configs.clickguiCustomColorTog[3]);
            toggleEndColor = new Color(Configs.clickguiCustomColorTog2[0], Configs.clickguiCustomColorTog2[1], Configs.clickguiCustomColorTog2[2], Configs.clickguiCustomColorTog2[3]);

            surroundStartColor = new Color(Configs.clickguiCustomColorSur[0], Configs.clickguiCustomColorSur[1], Configs.clickguiCustomColorSur[2], Configs.clickguiCustomColorSur[3]);
            surroundEndColor = new Color(Configs.clickguiCustomColorSur2[0], Configs.clickguiCustomColorSur2[1], Configs.clickguiCustomColorSur2[2], Configs.clickguiCustomColorSur2[3]);
        }

        return new Color[] {startColor, endColor, toggleStartColor, toggleEndColor, surroundStartColor, surroundEndColor};
    }


    Color rgbOver = new Color(255,0,0);

    Color HSLover = new Color(255,0,0);
    Color OpacityOver = new Color(0,0,0, 255);

    boolean setRGB = false;

    public void opacitySlider(int x, int y, int w, int h, Color Left, Color Right) {
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

            if (mousedown && mouseX == x + i && mouseY >= y && mouseY <= y + h) {
                OpacityOver = new Color(red,green,blue,opacity);

                setRGB = true;
            }

            drawRect(x + i, y, x + i + 1, y + h, new Color(red, green, blue, opacity).getRGB());
        }

    }


    public void drawHSLSlider(int x, int y, int w, int h, Color Left, Color Right) {
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

            if (mousedown && mouseX == x + i && mouseY >= y && mouseY <= y + h) {
                HSLover = new Color(red,green,blue);

                setRGB = true;
            }

            drawRect(x + i, y, x + i + 1, y + h, new Color(red, green, blue, opacity).getRGB());
        }

    }

    public void drawDownFade(int x, int y, int w, int h, Color Top, Color Bottom) {
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


    int xOfColor = 0;
    int yOfColor = 0;

    public void drawThreeWayFade(int x, int y, int w, int h, Color Right, Color Top, Color Bottom) {

        boolean drow = false;

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

            int redDif2 = Right.getRed() - red;
            int greenDif2 = Right.getGreen() - green;
            int blueDif2 = Right.getBlue() - blue;
            int opacityDif2 = Right.getAlpha() - opacity;

            double redEach2 = (double) redDif2 / w;
            double greenEach2 = (double) greenDif2 / w;
            double blueEach2 = (double) blueDif2 / w;
            double opacityEach2 = (double) opacityDif2 / w;

            for (int k = 0; k < w; k++) {
                int red2 = (int) (red + (redEach2 * k));
                int green2 = (int) (green + (greenEach2 * k));
                int blue2 = (int) (blue + (blueEach2 * k));
                int opacity2 = (int) (opacity + (opacityEach2 * k));

                if (x+i == xOfColor && y + k == yOfColor) {
                    rgbOver = new Color(red2,green2,blue2);

                    setRGB = false;
                }

                if (red2 == searchForRGB[0] && green2 == searchForRGB[1] && blue2 == searchForRGB[2] && !drow) {
                    drow = true;

                    xOfColor = x + i;
                    yOfColor = y + k;
                }

                if (mouseX == x + i && mouseY >= y+k && mouseY <= y + k+1) {
                    rgbOver = new Color(red2,green2,blue2);

                    if (mousedown) {
                        xOfColor = x + i;
                        yOfColor = y + k;
                    }
                }

                drawRect(x + i, y + k, x + i + 1, y + k + 1, new Color(red2, green2, blue2, opacity2).getRGB());
            }

        }
    }


    static Color[] HSLcolors = {
            new Color(255, 0, 0),
            new Color(255, 255, 0),
            new Color(0, 255, 0),
            new Color(0, 255, 255),
            new Color(0, 0, 255),
            new Color(255, 0, 255),
            new Color(255, 0, 0),
    };


    public static Color getColor(int[] color) {
        return new Color(color[0], color[1], color[2], color[3]);
    }
}