package mushroom.Features.Movement;

import mushroom.GUI.Configs;
import mushroom.Libs.events.MotionUpdateEvent;
import mushroom.Libs.events.PlayerUpdateEvent;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static mushroom.Libs.PlayerLib.mc;

public class Sprint {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onUpdate(final PlayerUpdateEvent e) {
        if ((Configs.sprint && Configs.sprintmode == 2) || (Configs.scaffold && Configs.scafsprintmode == 4)) {
            // oringo omnisprint disabler from 2 years ago (idk if still works maybe they updated disabler)
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }

        if (Configs.sprint && Configs.sprintmode == 0) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
