package mushroom.Features.Movement;

import mushroom.Features.Combat.Killaura;
import mushroom.GUI.Configs;
import mushroom.Libs.ChatLib;
import mushroom.Libs.PacketUtils;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.events.PacketSentEvent;
import mushroom.Libs.events.WorldJoinEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static mushroom.Libs.PlayerLib.mc;

public class NoSlow {
    private static final Queue<C03PacketPlayer> packetQueue = new ConcurrentLinkedQueue<>();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacket(PacketSentEvent event) {
        if (Configs.noslow && Configs.noslowmode == 2 && event.packet instanceof C03PacketPlayer) {

            if (Minecraft.getMinecraft().thePlayer.isUsingItem() || (Configs.killaura && Killaura.target != null)) {

                event.setCanceled(true);
                packetQueue.offer((C03PacketPlayer) event.packet);

                if (packetQueue.size() > Configs.packetspulsenoslow) {
                    if (Configs.unblocknoslow) mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    if (Configs.swingnoslow) Minecraft.getMinecraft().thePlayer.swingItem();
                    while (!packetQueue.isEmpty()) {
                        PacketUtils.sendPacketNoEvent((Packet<?>) packetQueue.poll());
                    }

                    if (Configs.unblocknoslow) mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                }
            } else {
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
