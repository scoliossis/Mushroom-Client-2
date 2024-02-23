package mushroom.Features.Movement;

import java.util.concurrent.ConcurrentLinkedQueue;

import mushroom.GUI.Configs;
import mushroom.Libs.ChatLib;
import mushroom.Libs.PacketUtils;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.events.PacketSentEvent;
import mushroom.Libs.events.WorldJoinEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.Vec3;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.Queue;

import static mushroom.Libs.PlayerLib.mc;

public class AntiVoid {
    private static final Queue<C03PacketPlayer> packetQueue = new ConcurrentLinkedQueue<>();
    private Vec3 lastPos = new Vec3(0.0, 0.0, 0.0);
    private double motionY = 0.0;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacket(PacketSentEvent event) {
        // mode blink
        if (Configs.antivoid && event.packet instanceof C03PacketPlayer) {

            if (PlayerLib.isOverVoid()) {

                event.setCanceled(true);
                packetQueue.offer((C03PacketPlayer) event.packet);
                if (mc.thePlayer.fallDistance > 5) {
                    packetQueue.clear();
                    mc.thePlayer.fallDistance = 0.0f;
                    mc.thePlayer.setPosition(lastPos.xCoord, lastPos.yCoord, lastPos.zCoord);
                    mc.thePlayer.setVelocity(0.0, motionY, 0.0);
                }
            } else {
                lastPos = mc.thePlayer.getPositionVector();
                motionY = mc.thePlayer.motionY;
                while (!packetQueue.isEmpty()) {
                    PacketUtils.sendPacketNoEvent((Packet<?>) packetQueue.poll());
                }
            }
        }
    }

    public static boolean isBlinking() {
        return !packetQueue.isEmpty();
    }

    @SubscribeEvent
    public void onRespawn(WorldJoinEvent event) {
        packetQueue.clear();
    }
}