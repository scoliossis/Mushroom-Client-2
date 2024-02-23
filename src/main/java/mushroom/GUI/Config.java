package mushroom.GUI;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import mushroom.Features.Visual.Notifications;
import mushroom.GUI.Settings.*;
import mushroom.GUI.Property;
import mushroom.GUI.Setting;
import mushroom.Libs.ChatLib;
import mushroom.mushroom;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import org.lwjgl.Sys;
import tv.twitch.chat.Chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static mushroom.mushroom.settings;
import static mushroom.mushroom.sillyfolderpath;

public class Config extends CommandBase {

    private static final String fileName = "config/mushroomclient/configs/";
    static String keybindspath = "config/mushroomclient/configs/keybinds.cfg";

    public static void save(String configname) {
        try {
            HashMap map = new HashMap();
            Iterator settingsit = mushroom.settings.iterator();

            while (settingsit.hasNext()) {
                Setting set = (Setting) settingsit.next();
                map.put(set.name, set.get(Object.class));
            }

            String settingsAll = (new Gson()).toJson(map);
            Files.write(Paths.get(fileName + configname), settingsAll.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.out.println("Error while saving config file");
            e.printStackTrace();
        }

    }

    public static ArrayList collect(Class configs) {
        Field[] fields = configs.getDeclaredFields();
        ArrayList settings = new ArrayList();

        for (Field settingNow : fields) {
            Property settingType = settingNow.getAnnotation(Property.class);
            if (settingType != null) {
                switch (settingType.type()) {
                    case BOOLEAN:
                        settings.add(new BooleanSetting(settingType, settingNow, settingType.type()));
                        break;
                    case NUMBER:
                        settings.add(new SliderSetting(settingType, settingNow));
                        break;
                    case SELECT:
                        settings.add(new SelectSetting(settingType, settingNow));
                        break;
                    case TEXT:
                        settings.add(new TextSetting(settingType, settingNow));
                        break;
                    case COLOR:
                        settings.add(new ColorSetting(settingType, settingNow));
                }
            }
        }

        for (Object setting : settings) {
            Setting setingnew = (Setting) setting;
            if (!setingnew.annotation.parent().isEmpty()) {
                setingnew.parent = getSetting(setingnew.annotation.parent(), settings);
                if (setingnew.parent != null) {
                    setingnew.parent.sons.add(setingnew);
                }
            }
        }

        ArrayList endSettings = new ArrayList();
        Iterator copa = settings.iterator();

        while (copa.hasNext()) {
            Setting setingnew = (Setting) copa.next();
            if (setingnew.parent == null) {
                dfs(endSettings, setingnew);
            }
        }

        return endSettings;
    }

    public static void load(String configname) {
        try {
            File var0 = new File(fileName + configname);
            if (var0.exists()) {
                BufferedReader var1 = Files.newBufferedReader(Paths.get(fileName + configname));
                Set<Map.Entry<String, JsonElement>> set = new JsonParser().parse(var1).getAsJsonObject().entrySet();
                for (Map.Entry<String, JsonElement> entry : set) {
                    Setting setting = Config.getSetting(entry.getKey(), mushroom.settings);
                    if (setting != null) {
                        if (setting instanceof SliderSetting) {
                            setting.set(entry.getValue().getAsFloat());
                        }
                        else if (setting instanceof SelectSetting) {
                            setting.set(entry.getValue().getAsInt());
                        }
                        else if (setting instanceof ColorSetting) {
                            //setting.set(entry.getValue().getAsJsonArray());
                            // ^^^^^ THIS DOESNT FUCKING WORK AND I WAS CONFUSED FO LIKE 10 MINUTES

                            //maybe its like entry.getValue().getAsJsonObject() or smth, idk ill fix later
                            int[] fuckoff = new int[] {entry.getValue().getAsJsonArray().get(0).getAsInt(),
                                    entry.getValue().getAsJsonArray().get(1).getAsInt(),
                                    entry.getValue().getAsJsonArray().get(2).getAsInt(),
                                    entry.getValue().getAsJsonArray().get(3).getAsInt(),
                                    entry.getValue().getAsJsonArray().get(4).getAsInt(),
                                    entry.getValue().getAsJsonArray().get(5).getAsInt(),
                                    entry.getValue().getAsJsonArray().get(6).getAsInt(),
                                    entry.getValue().getAsJsonArray().get(7).getAsInt()};

                            setting.set(fuckoff);
                        }
                        else if (setting instanceof BooleanSetting) {
                            setting.set(entry.getValue().getAsBoolean());
                        } else {
                            setting.set(entry.getValue().getAsString());
                        }
                    }
                }
            }
        } catch (Exception var7) {
            System.out.println("Error while loading config file");
            var7.printStackTrace();
        }

    }

    public static Setting getSetting(String configName, ArrayList Configs) {
        Iterator configsIt = Configs.iterator();

        Setting set;
        do {
            if (!configsIt.hasNext()) {
                return null;
            }

            set = (Setting) configsIt.next();
        } while (!set.name.equals(configName));

        return set;
    }

    private static void dfs(ArrayList list, Setting setting) {
        list.add(setting);
        Iterator settingsit = setting.sons.iterator();

        Setting nextSet;
        while (settingsit.hasNext()) {
            nextSet = (Setting) settingsit.next();
            dfs(list, nextSet);
        }
    }

    public static void getkeybinds() {
        Iterator allsettings = mushroom.settings.iterator();
        if (Files.exists(Paths.get(keybindspath))) {
            String readfile = mushroom.ReadFile(keybindspath);
            System.out.println(readfile);
            while (allsettings.hasNext()) {
                Setting setting = (Setting) allsettings.next();
                if (setting instanceof BooleanSetting && setting.parent != null && setting.parent.parent == null && readfile.contains(setting.name)) {
                    // so i can launch my game w intellij :)
                    if (readfile.contains("Â")) {
                        setting.keybindint = Integer.parseInt(readfile.split(setting.name + ":b]")[1].split(",hÂ£")[0]);
                        setting.keybindchar = readfile.split(setting.name + ":b]")[1].split(",hÂ£")[1].split("!89")[0];
                    }
                    else {
                        setting.keybindint = Integer.parseInt(readfile.split(setting.name + ":b]")[1].split(",h£")[0]);
                        setting.keybindchar = readfile.split(setting.name + ":b]")[1].split(",h£")[1].split("!89")[0];
                    }
                }
            }
        }
    }

    public static void savekeybinds() throws IOException {
        Iterator allsettings = mushroom.settings.iterator();
        String binds = "";
        System.out.println(sillyfolderpath);
        while (allsettings.hasNext()) {
            Setting setting = (Setting) allsettings.next();
            if (setting instanceof BooleanSetting && setting.parent != null && setting.parent.parent == null) {
                binds+=setting.name + ":b]" + setting.keybindint + ",h£" + setting.keybindchar + "!89\n";
            }
        }
        Files.write(Paths.get(keybindspath), binds.getBytes(StandardCharsets.UTF_8));
    }


    @Override
    public String getCommandName() {
        return "m";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        ChatLib.chat("use /m save [configname] to save your current config");
        return "";
    }
    public ArrayList getCommandAliases() {
        ArrayList<String> var1 = new ArrayList<String>();
        var1.add("m");
        var1.add("M");
        return var1;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) {
        if (strings[0] == null) {
            ChatLib.chat("use /m save [configname] or /m delete [configname]");
        }
        else if (strings[0].equalsIgnoreCase("save") && strings[1] == null) {
            ChatLib.chat("use /m save [configname] to save your current config");
        }
        else if (strings[0].equalsIgnoreCase("delete") && strings[1] == null) {
            ChatLib.chat("use /m delete [configname] to delete your current config");
        }
        else {
            if (strings[0].equalsIgnoreCase("save")) {
                mushroom.loadedconfig = strings[1];
                Notifications.popupmessage("config saved: ", strings[1]);
                Config.save(strings[1] + ".cfg");
            }
            if (strings[0].equalsIgnoreCase("delete")) {
                mushroom.loadedconfig = "current";
                try {
                    Files.delete(Paths.get("config/mushroomclient/configs/" + strings[1] + ".cfg"));
                    Notifications.popupmessage("config deleted: ", strings[1]);
                } catch (IOException e) {
                    Notifications.popupmessage("ERROR", "config " + strings[1] + " does not exist");
                }
            }
        }
    }
}
