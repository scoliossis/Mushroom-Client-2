package mushroom.GUI.AltManager;

import com.google.gson.JsonObject;
import mushroom.Features.Visual.CoolMainMenu;
import mushroom.GUI.AltManager.Microsoft.Cookies;
import mushroom.GUI.AltManager.SessionLogin.APIUtils;
import mushroom.GUI.AltManager.SessionLogin.SessionChanger;
import mushroom.GUI.ClickGUI;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.mushroom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import okhttp3.OkHttpClient;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

import static mushroom.GUI.AltManager.Microsoft.Cookies.unpack;
import static mushroom.GUI.ClickGUI.burp;
import static mushroom.Libs.PlayerLib.mc;
import static mushroom.mushroom.sillyfolderpath;

public class AltManagerGUI extends GuiScreen {

    String[] buttons = {"session id", "cracked", "cookies"};
    String[] changers = {"change name", "change skin"};
    boolean sessionlogin = false;
    boolean microsoftLogin = false;
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

    String token = "";
    public static String specifictoken;

    boolean lastLeftClicked = false;

    public static String status = "";

    String prevstat = "";

    long lastTime = 0;
    @Override
    public void drawScreen(int mx, int my, float var3) {

        ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft());

        ClickGUI.drawTexture(new ResourceLocation("mushroom/altmanager/background.png"), 0, 0, s.getScaledWidth(), s.getScaledHeight());
        FontUtil.font("productsans", 35).drawCenteredStringWithShadow("alts", (float) (((s.getScaledWidth())/2)), 10, -1);
        if (!Objects.equals(status, "")) {
            if (!Objects.equals(prevstat, status)) {
                lastTime = System.currentTimeMillis();
            }

            FontUtil.font("productsans", 19).drawCenteredStringWithShadow(status, (float) (((s.getScaledWidth())/2)), 35, -1);
            if (System.currentTimeMillis() - lastTime > 1000) {
                status = "";
            }
        }
        prevstat = status;

        FontUtil.font("productsans", 35).drawCenteredStringWithShadow("login", x+(w/2f), 10, -1);
        FontUtil.font("productsans", 19).drawCenteredStringWithShadow("from clipboard", x+(w/2f), y/2f+15, -1);


        this.drawGradientRect(0, 0, (x*2)+w, s.getScaledHeight(), new Color(23,23,23, 50).getRGB(), new Color(23,23,23, 50).getRGB());

        newi = 0;

        for (int i = 0; i < buttons.length + changers.length; i++) {
            int thexinquestion = x;
            int theyinquestion = y + (h*i) + (gap*i);
            String draw;
            if (i >= buttons.length) {
                if (i == buttons.length)
                    FontUtil.font("productsans", 19).drawCenteredStringWithShadow("changer", x+(w/2f), theyinquestion + 5, -1);
                theyinquestion += 17;
                draw = changers[i-buttons.length];
            }
            else draw = buttons[i];


            if (mx >= thexinquestion && mx <= thexinquestion + w && my >= theyinquestion && my <= theyinquestion + h) {
                this.drawGradientRect(thexinquestion, theyinquestion, thexinquestion + w, theyinquestion + h, new Color(10,10,10).getRGB(), new Color(10,10,10).getRGB());

                if (i == 2) FontUtil.font("productsans", 19).drawString("copy txt / zip file path of cookie to clipboard", s.getScaledWidth() - FontUtil.font("productsans", 19).getStringWidth("copy txt / zip file path of cookie to clipboard") - 5, s.getScaledHeight() - FontUtil.font("productsans", 19).getHeight() - 5, -1);

                if (Mouse.isButtonDown(0) && !lastLeftClicked) {
                    switch (i) {
                        case 0:
                            sessionlogin = true;
                            break;
                        case 1:
                            crackedlogin = true;
                            break;
                        case 2:

                            status = "§alogging in with cookies!!";

                            String clipboardText;

                            try {
                                clipboardText = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                            } catch (IOException | UnsupportedFlavorException e) {
                                throw new RuntimeException(e);
                            }

                            if (clipboardText != null) {

                                clipboardText = clipboardText.replaceAll("\"", "");
                                if (new File(clipboardText).exists()) {

                                    Cookies.path = clipboardText;


                                    if (clipboardText.endsWith(".zip")) {
                                        try {
                                            String newFileLoc = clipboardText.substring(0, clipboardText.lastIndexOf("\\")) + "\\MushroomCookies";
                                            System.out.println(newFileLoc);
                                            unpack(clipboardText, new File(newFileLoc));

                                            if (Files.isDirectory(Paths.get(newFileLoc))) {

                                                for (File file : Objects.requireNonNull(Paths.get(newFileLoc).toFile().listFiles())) {
                                                    System.out.println(file.getPath());
                                                    Cookies.path = file.getPath();

                                                    Cookies.loginWithCookie();
                                                }
                                            }
                                        } catch (IOException e) {
                                            status = "§4failed to extract zip :(";
                                            throw new RuntimeException(e);
                                        }
                                    }

                                    Cookies.loginWithCookie();
                                }
                                else {
                                    AltManagerGUI.status = "§2epic fail :skull: (invalid file path)";
                                }
                            }
                            else {
                                status = "§4invalid clipboard >:(";
                            }

                            break;
                        case 3:
                            changename = true;
                            break;
                        case 4:
                            changeskin = true;
                    }

                }
            }
            else {
                this.drawGradientRect(thexinquestion, theyinquestion, thexinquestion + w, theyinquestion + h, new Color(23,23,23).getRGB(), new Color(23,23,23).getRGB());
            }
            FontUtil.font("productsans", 19).drawCenteredStringWithShadow(draw, thexinquestion + (w / 2f), theyinquestion + (h / 2f)-3, -1);

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
                            status = "§4nuh uh uh (token no valid)";
                        }
                    }
                    else {
                        token="";
                        System.out.println("nuh uh uh clipboard bad");
                        status = "§4nuh uh uh clipboard bad";
                    }
                } catch (Exception e) {
                    token="";
                    System.out.println("grrrrrrrrrrrr error");
                    status = "§4invalid token";
                    System.out.println(e);

                }
            }
            else {
                status = "§4clipboard is empty";
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
                    FontUtil.font("productsans", 35).drawCenteredStringWithShadow("yes", (float) s.getScaledWidth() / 2 - 47, (float) s.getScaledHeight() - 28, -1);

                    drawGradientRect(s.getScaledWidth() / 2 + 3, s.getScaledHeight() - 30, s.getScaledWidth() / 2 + 97,s.getScaledHeight() - 10, new Color(255, 0, 0).getRGB(), new Color(245, 11, 63).getRGB());
                    FontUtil.font("productsans", 35).drawCenteredStringWithShadow("no", (float) s.getScaledWidth() / 2 + 47, (float) s.getScaledHeight() - 28, -1);

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

                    int xCoord = (x * 2 + w) + 20 + (w * bp) + (5 * bp);
                    int many = (xCoord+(w/2)) / s.getScaledWidth();

                    xCoord -= (((w * bp) + (5 * bp))) * many;

                    int yCoord = ((many * (h+5)) + y);

                    drawRect(xCoord, yCoord, xCoord + w, yCoord + h, new Color(23, 23, 23, 100).getRGB());

                    ClickGUI.drawTexture(new ResourceLocation("mushroom/altmanager/remove.png"), xCoord + w - 15, yCoord + 2, 10, 10);

                    specifictoken = validsessplit[i].split("\\|")[1].replace("user:", "").replace(" ", "");
                    String specificusername = validsessplit[i].split("\\|")[0].replace(" ", "");

                    if (mx >= xCoord-15 && mx <= xCoord - 5 && my >= yCoord + 2 && my <= yCoord + 15) {
                        if (Mouse.isButtonDown(0)) {
                            if (Objects.equals(mc.getSession().getProfile().getName(), specificusername)) SessionChanger.setSession(new Session(mushroom.mainuser, mushroom.mainuuid, mushroom.mainssid, "mojang"));
                            System.out.println(specifictoken);
                            mushroom.validsessions = mushroom.validsessions.replace("user:" + specificusername + "|" + specifictoken, " ");

                            status = "§4removed " + specificusername + " as a session id";
                        }
                    }

                    if (mc.getSession().getProfile().getName().equals(specificusername)) FontUtil.font("productsans", 16).drawStringWithShadow("logged in", xCoord + w - FontUtil.font("productsans", 16).getStringWidth("logged in") - 5, yCoord + 16, new Color(175, 225, 175).getRGB());
                    FontUtil.font("productsans", 19).drawStringWithShadow(specificusername, xCoord + 5, yCoord + 5, new Color(255, 241, 250).getRGB());
                    FontUtil.font("productsans", 16).drawStringWithShadow("session", xCoord + 5, yCoord + 16, new Color(203, 1, 98).getRGB());


                    if (!Objects.equals(mc.getSession().getProfile().getName(), specificusername) && mx >= xCoord && mx <= xCoord + w && my >= yCoord && my <= yCoord + h) {
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

        FontUtil.font("productsans", 19).drawStringWithShadow(mushroom.mainuser, (x * 2 + w) + 20 + 5, y + 5, new Color(255, 241, 250).getRGB());
        FontUtil.font("productsans", 16).drawStringWithShadow("main", (x * 2 + w) + 20 + 5, y + 16, new Color(203, 1, 98).getRGB());

        if (mc.getSession().getProfile().getName().equals(mushroom.mainuser)) FontUtil.font("productsans", 16).drawStringWithShadow("logged in", (x * 2 + w) + 20 + w - FontUtil.font("productsans", 16).getStringWidth("logged in") - 5, y + 16, new Color(175, 225, 175).getRGB());

        lastLeftClicked = Mouse.isButtonDown(0);
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
