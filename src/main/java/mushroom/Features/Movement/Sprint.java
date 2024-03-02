package mushroom.Features.Movement;

import mushroom.GUI.Configs;
import mushroom.Libs.events.PlayerUpdateEvent;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static mushroom.Libs.PlayerLib.mc;

public class Sprint {

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent e) {
        if (Configs.sprint && Configs.sprintmode == 0 && mc.thePlayer.moveForward > 0) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
