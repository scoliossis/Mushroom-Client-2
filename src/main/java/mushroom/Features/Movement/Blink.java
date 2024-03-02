package mushroom.Features.Movement;

import mushroom.GUI.Configs;
import mushroom.Libs.PacketUtils;
import mushroom.Libs.TickTimer;
import mushroom.Libs.events.PacketSentEvent;
import mushroom.Libs.events.PlayerUpdateEvent;
import mushroom.Libs.events.WorldJoinEvent;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.network.Packet;
import java.util.Queue;

import static mushroom.Libs.PlayerLib.mc;

public class Blink {
    private final Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
    private final TickTimer timer = new TickTimer();

    @SubscribeEvent
    public void onDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        if (Configs.blink) {
            this.packets.clear();
            Configs.blink = false;
        }
    }

    @SubscribeEvent
    public void onUpdate(PlayerUpdateEvent event) {
        if (Configs.blink && Configs.pulse) {
            timer.updateTicks();
            if (timer.passed((int) Configs.pulseticks) && Configs.pulse) {
                sendPackets();
                timer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onPacket(PacketSentEvent event) {
        if (Configs.blink && mc.thePlayer == null) {
            packets.clear();
            Configs.blink = false;
            return;
        }
        if (Configs.blink && (event.packet instanceof C03PacketPlayer || !Configs.onlypos)) {
            event.setCanceled(true);
            packets.offer(event.packet);
        }
    }

    @SubscribeEvent
    public void onWorld(final WorldJoinEvent event) {
        packets.clear();
        Configs.blink = false;
    }

    private void sendPackets() {
        if (mc.getNetHandler() != null) {
            while (!packets.isEmpty()) {
                PacketUtils.sendPacketNoEvent(packets.poll());
            }
        }
    }
}