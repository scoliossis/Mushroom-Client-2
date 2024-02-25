package mushroom.Features.Movement;

import mushroom.GUI.Configs;
import mushroom.Libs.ChatLib;
import mushroom.Libs.PacketUtils;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.events.MotionUpdateEvent;
import mushroom.Libs.events.PacketSentEvent;
import mushroom.Libs.events.WorldJoinEvent;
import mushroom.mixins.C03Accessor;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static mushroom.Libs.PlayerLib.mc;

public class Nofall {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onUpdate(final MotionUpdateEvent event) {
        if (Configs.nofall) {
            if (!PlayerLib.isOverVoid()) {
                switch(Configs.nofallmode){
                    case 1:
                        if (mc.thePlayer.fallDistance > Configs.nofallheight) {
                            event.setOnGround(true);
                        }
                    case 2:
                        if (mc.thePlayer.fallDistance > Configs.nofallheight) {
                            mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(true));
                            mc.thePlayer.fallDistance = 0;
                        }
                }
            }
        }
    }

    int minus = 0;
    private static final Queue<C03PacketPlayer> packetQueue = new ConcurrentLinkedQueue<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacket(PacketSentEvent event) {
        // mode blink
        if (Configs.nofall && event.packet instanceof C03PacketPlayer) {

            if (Configs.nofallmode == 3) {
                if (mc.thePlayer.fallDistance > (float) Configs.nofallheight / 10) {
                    if (mc.thePlayer.ticksExisted % 2 == 0) ((C03Accessor)event.packet).setOnGround(true);
                    event.setCanceled(true);

                    packetQueue.offer((C03PacketPlayer) event.packet);

                    if (mc.thePlayer.onGround) {
                        packetQueue.clear();
                        mc.thePlayer.fallDistance = 0.0f;
                    }
                } else {
                    while (!packetQueue.isEmpty()) {
                        PacketUtils.sendPacketNoEvent(packetQueue.poll());
                    }
                    minus = 0;
                }
            }
            else if (Configs.nofallmode == 0) {
                ((C03Accessor)event.packet).setOnGround(false);
            }
        }
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPack(PacketSentEvent.Post event) {
        if (Configs.nofall && event.packet instanceof C03PacketPlayer) {
            if (Configs.nofallmode == 4) {
                ((C03Accessor)event.packet).setOnGround(false);
            }
        }
    }


    @SubscribeEvent
    public void onRespawn(WorldJoinEvent event) {
        packetQueue.clear();
    }
}
