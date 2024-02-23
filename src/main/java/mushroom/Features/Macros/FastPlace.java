package mushroom.Features.Macros;

import mushroom.GUI.Config;
import mushroom.GUI.Configs;
import mushroom.Libs.ChatLib;
import mushroom.Libs.PlayerLib;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import scala.reflect.internal.Trees;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Date;

import static mushroom.Libs.PlayerLib.isOverAir;
import static mushroom.Libs.PlayerLib.mc;

public class FastPlace {
    public final static Field rightClickDelayTimerField;

    static {
        rightClickDelayTimerField = ReflectionHelper.findField(Minecraft.class, "field_71467_ac", "rightClickDelayTimer");

        if (rightClickDelayTimerField != null) {
            rightClickDelayTimerField.setAccessible(true);
        }
    }
    private Method rightClickMouse = null;
    boolean fastplacechanged = false;
    boolean readytogo = false;
    int i = 0;
    int datenow = 0;
    boolean go=true;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) throws IllegalAccessException, InvocationTargetException, InterruptedException {
        if (Configs.fastplace && PlayerLib.inGame()) {
            if (Configs.fastplacemode == 0) {
                if (event.phase == TickEvent.Phase.END) {
                    fastplacechanged = true;
                    if (PlayerLib.inGame() && mc.inGameHasFocus && rightClickDelayTimerField != null) {
                        if (rightClickDelayTimerField.getInt(mc) != 0) {
                            rightClickDelayTimerField.set(mc, 0);
                        }
                    }
                }
            }
            else {
                if (rightClickMouse == null) {
                    try {
                        try {
                            this.rightClickMouse = mc.getClass().getDeclaredMethod("func_147121_ag");
                        } catch (NoSuchMethodException var4) {
                            try {
                                this.rightClickMouse = mc.getClass().getDeclaredMethod("rightClickMouse");
                            } catch (NoSuchMethodException var3) {
                            }
                        }
                    } catch (NoClassDefFoundError varbruh) {
                        varbruh.printStackTrace();
                    }
                }
                ItemStack helditem = mc.thePlayer.getHeldItem();
                if (this.rightClickMouse != null && helditem != null && helditem.getItem() instanceof ItemBlock) {
                    this.rightClickMouse.setAccessible(true);

                    if (go && Mouse.isButtonDown(1) && Math.random() < (double) Configs.cpsoffastplace / 40 && i <= Configs.cpsoffastplace && go) {
                        this.rightClickMouse.invoke(mc);
                        i++;
                    }
                    if (datenow < new Date().getSeconds() || (datenow != new Date().getSeconds() && datenow + 1 != new Date().getSeconds())) {
                        i=0;
                    }
                    datenow = new Date().getSeconds();
                }
            }
        }
        else if (fastplacechanged) {
            fastplacechanged=false;
            rightClickDelayTimerField.set(mc, 4);
        }

    }

    boolean mousedown = false;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!Mouse.isButtonDown(1)) {
            go=false;
        }
        if (mousedown != Mouse.isButtonDown(1) && Mouse.isButtonDown(1)) {
            mousedown=Mouse.isButtonDown(1);
            new Thread(() -> {
                try {
                    Thread.sleep((long) Configs.delayfromstart);
                    if (Mouse.isButtonDown(1)) go=true;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
        mousedown = Mouse.isButtonDown(1);
    }
}
