package mushroom.Features.Combat;

import com.jcraft.jogg.Packet;
import mushroom.GUI.Configs;
import mushroom.Libs.ChatLib;
import net.minecraft.client.Minecraft;
import net.minecraft.item.EnumAction;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;
import net.minecraft.util.BlockPos;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static mushroom.Libs.PlayerLib.mc;

public class BlockHit {

    private Method leftClickMouse = null;

    public boolean swing = true;

    @SubscribeEvent
    public void onPlayertick(TickEvent.PlayerTickEvent e) throws InvocationTargetException, IllegalAccessException {
        if (Configs.blockhit && Minecraft.getMinecraft().thePlayer.isUsingItem() && Minecraft.getMinecraft().thePlayer.getHeldItem() != null) {
            final EnumAction action = Minecraft.getMinecraft().thePlayer.getHeldItem().getItem().getItemUseAction(Minecraft.getMinecraft().thePlayer.getHeldItem());
            if (action == EnumAction.BLOCK) {
                if (Mouse.isButtonDown(0) && swing) {
                    //if (leftClickMouse == null) {
                    //    try {
                    //        leftClickMouse = mc.getClass().getDeclaredMethod("func_147116_af");
                    //        leftClickMouse.setAccessible(true);
                    //    } catch (NoSuchMethodException ex) {
                    //        ex.printStackTrace();
                    //    }
                    //}
                    mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));

                    swing=false;
                }
            }
        }
        if (!Mouse.isButtonDown(0) && !swing) {
            // ik swingitem exists but in og mushroom client i used leftclick.invoke so i keep it here too
            //leftClickMouse.invoke(mc); // nvm it crashed my game

            mc.thePlayer.swingItem();
            swing=true;
            mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }

}
