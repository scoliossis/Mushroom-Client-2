package mushroom.Features.Movement;

import mushroom.Features.Visual.Notifications;
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
                        event.setOnGround(true);
                    case 2:
                        if (mc.thePlayer.fallDistance >= Configs.nofallheight) {
                            mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(true));
                            mc.thePlayer.fallDistance = 0;
                        }
                }
            }
        }
    }

    public static boolean noFallBlinking = false;
    boolean gogogo = false;
    int ticks = 0;

    private static final Queue<Packet> packetQueue = new ConcurrentLinkedQueue<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacket(PacketSentEvent event) {
        if (Configs.nofall && PlayerLib.inGame()) {
            // scaffold + nofall = BAN!
            if (Configs.nofallmode == 3 && !Configs.scaffold) {
                if (mc.thePlayer.onGround && PlayerLib.isOverAir() && !noFallBlinking && mc.thePlayer.motionY <= 0) {
                    Notifications.popupmessage("No Fall", "blinking");
                    noFallBlinking = true;
                }
                if (noFallBlinking) {
                    event.setCanceled(true);
                    if (event.packet instanceof C03PacketPlayer) ((C03Accessor)event.packet).setOnGround(true);
                    packetQueue.offer(event.packet);

                    if ((((mc.thePlayer.onGround && mc.thePlayer.fallDistance > 0 && packetQueue.size() > 5)) || !PlayerLib.isOverAir() || PlayerLib.isOverVoid())) {
                        mc.thePlayer.fallDistance = 0.0f;
                        gogogo = true;
                    }
                }
                if ((gogogo && noFallBlinking) || mc.thePlayer.motionY > 0) {
                    ticks++;

                    if (ticks > 10 || mc.thePlayer.motionY > 0) {
                        ticks = 0;
                        while (!packetQueue.isEmpty()) {
                            PacketUtils.sendPacketNoEvent(packetQueue.poll());
                        }
                        gogogo = false;
                        if (noFallBlinking) {
                            noFallBlinking = false;
                            Notifications.popupmessage("No Fall", "landed");
                            if (Configs.autoDisableNoFall) Configs.nofall = false;
                        }
                    }
                }
            }
            else if (Configs.nofallmode == 0) {
                ((C03Accessor)event.packet).setOnGround(false);
            }
            else if (Configs.nofallmode == 5 && event.packet instanceof C03PacketPlayer) {
                ((C03Accessor)event.packet).setOnGround(false);
                ((C03Accessor) event.packet).setY(((C03PacketPlayer) event.packet).getPositionY()+0.01d);
            }
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
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