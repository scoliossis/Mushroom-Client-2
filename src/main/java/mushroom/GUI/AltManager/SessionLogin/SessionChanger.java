package mushroom.GUI.AltManager.SessionLogin;

import mushroom.GUI.AltManager.AltManagerGUI;
import mushroom.mushroom;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class SessionChanger {
    public static void setSession(Session session) {
        Field sessionField = ReflectionHelper.findField(Minecraft.class, "session", "field_71449_j");
        ReflectionHelper.setPrivateValue(Field.class, sessionField, sessionField.getModifiers() & ~Modifier.FINAL, "modifiers");
        ReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(), session, "session", "field_71449_j");
    }

    public static void loginWSession(String session){
        try {
            if (session != null) {
                if (APIUtils.validateSession(session)) {
                    String[] playerInfo = APIUtils.getProfileInfo(session);
                    SessionChanger.setSession(new Session(playerInfo[0], playerInfo[1], session, "mojang"));

                    AltManagerGUI.status = "§2logged in as " + playerInfo[0];
                    if (!mushroom.validsessions.contains(playerInfo[0]))
                        mushroom.validsessions += "user:" + playerInfo[0] + "|" + session;
                } else {
                    System.out.println("nuh uh uh (token no valid)");
                    AltManagerGUI.status = "§4nuh uh uh (token no valid)";
                }
            } else {
                System.out.println("nuh uh uh clipboard bad");
                AltManagerGUI.status = "§4nuh uh uh clipboard bad";
            }
        } catch (IOException e) {
            AltManagerGUI.status = "§4everything has gone horribly wrong";
            throw new RuntimeException(e);
        }
    }
}