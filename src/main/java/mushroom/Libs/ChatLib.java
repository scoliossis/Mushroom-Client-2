package mushroom.Libs;

import mushroom.GUI.Setting;
import mushroom.mushroom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatLib {

    public static void addComponent(IChatComponent var0, boolean var1) {
        if (mushroom.getPlayer() != null) {
            try {
                ClientChatReceivedEvent var2 = new ClientChatReceivedEvent((byte) 0, var0);
                if (var1 && MinecraftForge.EVENT_BUS.post(var2)) {
                    return;
                }

                mushroom.getPlayer().addChatMessage(var2.message);
            } catch (Exception var3) {
                var3.printStackTrace();
            }

        }
    }

    public static void addComponent(IChatComponent var0) {
        addComponent(var0, true);
    }

    public static String addColor(String var0) {
        if (var0 == null) {
            return "";
        } else {
            Pattern var1 = Pattern.compile("((?<!\\\\))&(?![^0-9a-fklmnor]|$)");
            Matcher var2 = var1.matcher(var0);
            return var2.replaceAll("§");
        }
    }
    
    public static void chat(String var0, boolean var1) {
        if (var0 == null) {
            var0 = "null";
        }
        String[] var2 = var0.split("\n");
        int var4 = var2.length;

        for (String s : var2) {
            ChatComponentText var7 = new ChatComponentText(addColor(s));
            addComponent(var7, var1);
        }
    }

    public static void chat(String var0) {
        chat(var0, true);
    }

    public static void say(String var0) {
        if (var0 == null) {
            var0 = "null";
        }

        EntityPlayerSP var1 = mushroom.getPlayer();
        String[] var2 = var0.split("\n");
        int var4 = var2.length;

        for (String var6 : var2) {
            if (var1 != null) {
                var1.sendChatMessage(var6);
            }
        }

    }
}