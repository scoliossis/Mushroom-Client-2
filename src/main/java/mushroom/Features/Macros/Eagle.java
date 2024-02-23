package mushroom.Features.Macros;

import mushroom.GUI.Configs;
import mushroom.Libs.ChatLib;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.Libs.PlayerLib;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static mushroom.Libs.PlayerLib.*;
import static mushroom.mushroom.mainssid;

public class Eagle {

    Boolean fastplac = false;
    boolean iscrouched = false;
    private Method rightClickMouse = null;

    @SubscribeEvent
    public void tick(TickEvent.PlayerTickEvent e) throws InvocationTargetException, IllegalAccessException {
        if (eagletriggered() && PlayerLib.isOverAir()) {
            if (Configs.togglefastplace && !Configs.fastplace) {
                Configs.fastplace = true;
                fastplac = true;
            }
            if (Configs.eaglemode == 0) {
                if (mc.thePlayer.onGround || !Configs.groundonly) {
                    setCrouched(true);
                    iscrouched = true;
                }
            }
            if (Configs.eaglemode == 2) {

                BlockPos lookingAtBlock = mc.objectMouseOver.getBlockPos();
                if (lookingAtBlock != null) {
                    Block stateBlock = mc.theWorld.getBlockState(lookingAtBlock).getBlock();
                    if ((stateBlock != Blocks.air && !(stateBlock instanceof BlockLiquid) && stateBlock instanceof Block)) {
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
                        if (this.rightClickMouse != null) {
                            this.rightClickMouse.setAccessible(true);
                            this.rightClickMouse.invoke(mc);
                        }
                    }
                }
            }
        }

        else if (iscrouched) {
            if (!Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
                setCrouched(false);
            }
            if (!Mouse.isButtonDown(1) && Configs.eaglemode == 2) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                setMouseButtonState(1, false);
            }
            iscrouched=false;
        }

        if (!eagletriggered() && fastplac) {
            Configs.fastplace = false;
            fastplac=false;
        }
    }

    @SubscribeEvent
    public void renderString(TickEvent.RenderTickEvent tick) {
        if (PlayerLib.inGame() && Configs.showifeagletriggered && eagletriggered()) {
            ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft());

            FontUtil.productsans19.drawStringWithShadow("Eagle triggered", ((double) s.getScaledWidth() / 2) - (FontUtil.productsans19.getStringWidth("Eagle triggered") / 2), ((float) s.getScaledHeight() / 2), -1);
        }
    }

    public static boolean eagletriggered() {
        if (Configs.eagle) {
            if ((mc.thePlayer.rotationPitch > Configs.lookingdownminyaw && mc.thePlayer.rotationPitch < Configs.lookingdownmaxyaw) || !Configs.onlylookingdown) {
                ItemStack i = mc.thePlayer.getHeldItem();
                if ((i != null && i.getItem() instanceof ItemBlock) || !Configs.blocksonly) {
                    BlockPos lookingAtBlock = mc.objectMouseOver.getBlockPos();
                    if (lookingAtBlock != null || !Configs.lookingatblockonly) {
                        Block stateBlock = null;
                        if (Configs.lookingatblockonly) stateBlock = mc.theWorld.getBlockState(lookingAtBlock).getBlock();
                        if (!Configs.lookingatblockonly || (stateBlock != Blocks.air && !(stateBlock instanceof BlockLiquid) && stateBlock instanceof Block)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
