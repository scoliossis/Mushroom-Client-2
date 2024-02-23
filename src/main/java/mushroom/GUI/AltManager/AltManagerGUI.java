package mushroom.GUI.AltManager;

import mushroom.Features.Visual.CoolMainMenu;
import mushroom.GUI.AltManager.SessionLogin.APIUtils;
import mushroom.GUI.AltManager.SessionLogin.SessionChanger;
import mushroom.GUI.ClickGUI;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.mushroom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import static mushroom.GUI.ClickGUI.burp;
import static mushroom.Libs.PlayerLib.mc;
import static mushroom.mushroom.sillyfolderpath;

public class AltManagerGUI extends GuiScreen {

    String[] buttons = {"session id", "cracked", "change name", "change skin"};
    boolean sessionlogin = false;
    boolean crackedlogin = false;
    boolean changename = false;
    boolean changeskin = false;
    int newi = 0;

    public AltManagerGUI() {

    }

    int x = 10;
    int y = 50;
    int w = 150;
    int h = 25;
    int gap = 5;

    public int pp = 0;

    String stat = "";
    String token = "";
    public static String specifictoken;

    public static String status = "";
    @Override
    public void drawScreen(int mx, int my, float var3) {

        ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft());

        ClickGUI.drawTexture(new ResourceLocation("mushroom/altmanager/background.png"), 0, 0, s.getScaledWidth(), s.getScaledHeight());
        FontUtil.comicsans35.drawCenteredStringWithShadow("alts", (float) (((s.getScaledWidth())/2)), 10, -1);
        if (status != "") {
            if (stat != status) pp=0;
            stat = status;
            FontUtil.comicsans19.drawCenteredStringWithShadow(status, (float) (((s.getScaledWidth())/2)), 35, -1);
            pp++;
            if (pp > 75) {
                pp = 0;
                status = "";
            }
        }

        FontUtil.productsans35.drawCenteredStringWithShadow("login", x+(w/2f), 10, -1);
        FontUtil.productsans19.drawCenteredStringWithShadow("from clipboard", x+(w/2f), y/2f+15, -1);
        FontUtil.productsans19.drawCenteredStringWithShadow("changer", x+(w/2f), (y + (h*2) + (gap*2))+7, -1);


        this.drawGradientRect(0, 0, (x*2)+w, s.getScaledHeight(), new Color(23,23,23, 50).getRGB(), new Color(23,23,23, 50).getRGB());

        newi = 0;

        for (int i = 0; i < buttons.length; i++) {
            int thexinquestion = x;
            int theyinquestion = y + (h*i) + (gap*i);
            if (i > 1) theyinquestion+=17;


            if (mx >= thexinquestion && mx <= thexinquestion + w && my >= theyinquestion && my <= theyinquestion + h) {
                this.drawGradientRect(thexinquestion, theyinquestion, thexinquestion + w, theyinquestion + h, new Color(10,10,10).getRGB(), new Color(10,10,10).getRGB());
                if (Mouse.isButtonDown(0)) {
                    if (i == 0) sessionlogin = true;
                    if (i == 1) crackedlogin = true;

                    if (i == 2) changename = true;
                    if (i == 3) changeskin = true;
                }
            }
            else {
                this.drawGradientRect(thexinquestion, theyinquestion, thexinquestion + w, theyinquestion + h, new Color(23,23,23).getRGB(), new Color(23,23,23).getRGB());
            }
            FontUtil.productsans19.drawCenteredStringWithShadow(buttons[i], thexinquestion + (w / 2), theyinquestion + (h / 2)-3, -1);

        }

        if (sessionlogin) {

            sessionlogin = false;

            token="";
            try {
                status = "§2attempting to login to token";
                token = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                status = "§4clipboard is invalid";
                throw new RuntimeException(e);
            }

            if (!Objects.equals(token, "")) {
                try {

                    if (token != null) {
                        if (APIUtils.validateSession(token)) {
                            String[] playerInfo = APIUtils.getProfileInfo(token);
                            SessionChanger.setSession(new Session(playerInfo[0], playerInfo[1], token, "mojang"));
                            status = "§2logged in as " + playerInfo[0];
                            if (!mushroom.validsessions.contains(playerInfo[0])) mushroom.validsessions += "user:" + playerInfo[0] + "|" + token;
                            token = "";
                        }
                        else {
                            token="";
                            System.out.println("nuh uh uh (token no valid)");
                            status = "§4invalid token";
                        }
                    }
                    else {
                        token="";
                        System.out.println("nuh uh uh clipboard bad");
                        status = "§4invalid token";
                    }
                } catch (Exception e) {
                    token="";
                    System.out.println("grrrrrrrrrrrr error");
                    status = "§4invalid token";
                    System.out.println(e);

                }
            }
        }


        if (crackedlogin) {
            String username;
            try {
                username = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                if (username != null) {
                    String newwu = username.replace(" ", "");
                    if (newwu.length() > 16) newwu = newwu.substring(0, 16);
                    status = "§2logged in as " + newwu;
                    crackedlogin = false;
                    SessionChanger.setSession(new Session(newwu, "7", "7", "mojang"));
                }
                else {
                    status = "§4clipboard is invalid";
                }
            } catch (UnsupportedFlavorException | IOException e) {
                status = "§4clipboard is invalid";
                crackedlogin = false;
                throw new RuntimeException(e);
            }
        }

        if (changename) {
            try {
                String newusername = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                if (newusername != null) {
                    newusername = newusername.replace(" ", "");
                    if (newusername.length() > 16) newusername = newusername.substring(0, 16);
                    String nametochange = mc.getSession().getProfile().getName();
                    status = "§4are you SURE you want to change username of " + nametochange + " to " + newusername + "?";

                    drawGradientRect(s.getScaledWidth() / 2 - 97, s.getScaledHeight() - 30, s.getScaledWidth() / 2 - 3,s.getScaledHeight() - 10, new Color(103, 248, 0).getRGB(), new Color(194, 231, 27).getRGB());
                    FontUtil.productsans35.drawCenteredStringWithShadow("yes", (float) s.getScaledWidth() / 2 - 47, (float) s.getScaledHeight() - 28, -1);

                    drawGradientRect(s.getScaledWidth() / 2 + 3, s.getScaledHeight() - 30, s.getScaledWidth() / 2 + 97,s.getScaledHeight() - 10, new Color(255, 0, 0).getRGB(), new Color(245, 11, 63).getRGB());
                    FontUtil.productsans35.drawCenteredStringWithShadow("no", (float) s.getScaledWidth() / 2 + 47, (float) s.getScaledHeight() - 28, -1);

                    if (mx >= s.getScaledWidth() / 2 - 97 && mx <= s.getScaledWidth() / 2 - 3 && my >= s.getScaledHeight() - 30 && my <= s.getScaledHeight() - 10) {
                        if (Mouse.isButtonDown(0)) {
                            changename = false;
                            String finalNewusername = newusername;
                            new Thread(() -> {
                                try {
                                    int statusCode = APIUtils.changeName(finalNewusername, mc.getSession().getToken());
                                    if (statusCode == 200) {
                                        status = "§2Successfully changed name!";
                                        SessionChanger.setSession(new Session(finalNewusername, mc.getSession().getPlayerID(), mc.getSession().getToken(), "mojang"));
                                        if (mushroom.validsessions.contains(nametochange)) {
                                            mushroom.validsessions = mushroom.validsessions.replace(nametochange, finalNewusername);
                                        }
                                    } else if (statusCode == 429) {
                                        status = "§4Error: Too many requests!";
                                    } else if (statusCode == 400) {
                                        status = "§4Error: Invalid name!";
                                    } else if (statusCode == 401) {
                                        status = "§4Error: Invalid token!";
                                    } else if (statusCode == 403) {
                                        status = "§4Error: Name is unavailable/Player already changed name in the last 35 days";
                                    } else {
                                        status = "§4An unknown error occurred!";
                                    }
                                }
                                catch (Exception e) {
                                    status = "§4An unknown error occurred!";
                                    e.printStackTrace();
                                }
                            }).start();
                        }
                    }
                    else if (mx >= s.getScaledWidth() / 2 + 3 && mx <= s.getScaledWidth() / 2 + 97 && my >= s.getScaledHeight() - 30 && my <= s.getScaledHeight() - 10) {
                        if (Mouse.isButtonDown(0)) {
                            changename = false;
                            status = "§4"+nametochange + "'s name was not changed.";
                        }
                    }

                }
                else {
                    status = "§4clipboard is invalid";
                }
            } catch (UnsupportedFlavorException | IOException e) {
                status = "§4clipboard is invalid";
                changename = false;
                throw new RuntimeException(e);
            }
        }

        if (changeskin) {
            changeskin=false;
            try {
                String newskinlink = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                new Thread(() -> {
                    try {
                        int statusCode = APIUtils.changeSkin(newskinlink, mc.getSession().getToken());
                        if (statusCode == 200) {
                            status = "§2Successfully changed skin!";
                        } else if (statusCode == 429) {
                            status = "§4Error: Too many requests!";
                        } else if (statusCode == 401) {
                            status = "§4Error: Invalid token!";
                        } else {
                            status = "§4Error: Invalid Skin";
                        }
                    }
                    catch (Exception e) {
                        status = "§4An unknown error occurred!";
                        e.printStackTrace();
                    }
                }).start();
            } catch (UnsupportedFlavorException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (mx >= (x*2+w)+20 && mx <= (x*2+w)+20 + w && my >= y && my <= y + h) {
            if (Mouse.isButtonDown(0)) {
                if (!Objects.equals(mc.getSession().getProfile().getName(), mushroom.mainuser)) {
                    SessionChanger.setSession(new Session(mushroom.mainuser, mushroom.mainuuid, mushroom.mainssid, "mojang"));
                    status = "§2logged in as " + mushroom.mainuser;
                }
            }
        }

        if (mushroom.validsessions.contains("user:")) {
            String[] validsessplit = mushroom.validsessions.split("user:");

            for (int i = 0; i < validsessplit.length; i++) {

                if (validsessplit[i].contains("|")) {

                    newi++;
                    int bp = newi;

                    drawRect((x * 2 + w) + 20 + (w * bp) + (5 * bp), y, (x * 2 + w) + 20 + w + (w * bp) + (5 * bp), y + h, new Color(23, 23, 23, 100).getRGB());

                    ClickGUI.drawTexture(new ResourceLocation("mushroom/altmanager/remove.png"), (x * 2 + w) + 20 + w + (w * bp) + (5 * bp)-15, y + 2, 10, 10);

                    specifictoken = validsessplit[i].split("\\|")[1].replace("user:", "").replace(" ", "");
                    String specificusername = validsessplit[i].split("\\|")[0].replace(" ", "");

                    if (mx >= (x * 2 + w) + 20 + w + (w * bp) + (5 * bp)-15 && mx <= (x * 2 + w) + 20 + w + (w * bp) + (5 * bp)-15 + 10 && my >= y + 2 && my <= y + 15) {
                        if (Mouse.isButtonDown(0)) {
                            if (Objects.equals(mc.getSession().getProfile().getName(), specificusername)) SessionChanger.setSession(new Session(mushroom.mainuser, mushroom.mainuuid, mushroom.mainssid, "mojang"));
                            mushroom.validsessions = mushroom.validsessions.replace("user:" + specificusername + "|" + specifictoken, "");
                            status = "§4removed " + specificusername + " as a session id";
                        }
                    }

                    if (mc.getSession().getProfile().getName().equals(specificusername)) FontUtil.comicsans10.drawStringWithShadow("logged in", (x * 2 + w) + 20 + (w * bp) + (5 * bp) + w - FontUtil.comicsans10.getStringWidth("logged in") - 5, y + 16, new Color(175, 225, 175).getRGB());
                    FontUtil.comicsans19.drawStringWithShadow(specificusername, (x * 2 + w) + 20 + 5 + (w * bp) + (5 * bp), y + 5, new Color(255, 241, 250).getRGB());
                    FontUtil.comicsans10.drawStringWithShadow("session", (x * 2 + w) + 20 + 5 + (w * bp) + (5 * bp), y + 16, new Color(203, 1, 98).getRGB());


                    if (!Objects.equals(mc.getSession().getProfile().getName(), specificusername) && mx >= (x * 2 + w) + 20 + (w * i) + (5 * i) && mx <= (x * 2 + w) + 20 + w + (w * bp) + (5 * bp) && my >= y && my <= y + h) {
                        if (Mouse.isButtonDown(0)) {
                            if (mushroom.validsessions.contains(specifictoken)) {

                                sessionlogin = false;
                                new Thread(() -> {
                                    try {
                                        String[] playerInfo = APIUtils.getProfileInfo(specifictoken);
                                        SessionChanger.setSession(new Session(playerInfo[0], playerInfo[1], specifictoken, "mojang"));
                                        status = "§2logged in as " + playerInfo[0];
                                    } catch (Exception e) {
                                        status = "§4invalid token";
                                        token = "";
                                        e.printStackTrace();
                                    }
                                }).start();
                            }
                        }
                    }
                }
            }
        }

        drawRect((x*2+w)+20, y, (x*2+w)+20+w, y+h, new Color(23,23,23,100).getRGB());

        FontUtil.comicsans19.drawStringWithShadow(mushroom.mainuser, (x * 2 + w) + 20 + 5, y + 5, new Color(255, 241, 250).getRGB());
        FontUtil.comicsans10.drawStringWithShadow("main", (x * 2 + w) + 20 + 5, y + 16, new Color(203, 1, 98).getRGB());

        if (mc.getSession().getProfile().getName().equals(mushroom.mainuser)) FontUtil.comicsans10.drawStringWithShadow("logged in", (x * 2 + w) + 20 + w - FontUtil.comicsans10.getStringWidth("logged in") - 5, y + 16, new Color(175, 225, 175).getRGB());

    }

    char char1 = '\u0000';
    int int2 = 999;

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        char1 = typedChar;
        int2 = keyCode;

        if (keyCode == 1) {
            burp = false;
            mc.displayGuiScreen(new CoolMainMenu());
            Files.write(Paths.get(sillyfolderpath + "/accounts/sessionids.cfg"), mushroom.validsessions.getBytes());

        }
    }
}
