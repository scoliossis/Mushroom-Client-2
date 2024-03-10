package mushroom.Features.Movement;

import mushroom.Features.Combat.Killaura;
import mushroom.GUI.Configs;
import mushroom.Libs.ChatLib;
import mushroom.Libs.PacketUtils;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.events.MotionUpdateEvent;
import mushroom.Libs.events.PacketSentEvent;
import mushroom.Libs.events.WorldJoinEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
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
        if (Configs.noslow && event.packet instanceof C03PacketPlayer) {

            if ((mc.thePlayer.isUsingItem() || Killaura.isBlocking) && mc.thePlayer.getHeldItem() != null &&
                    ((Configs.Swordnoslow && Configs.Swordnoslowmode == 1 && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) ||
                    (Configs.Bownoslow && Configs.Bownoslowmode == 1 && mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) ||
                    (Configs.Foodnoslow && Configs.Foodnoslowmode == 1 && (mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion)))) {

                if (Minecraft.getMinecraft().thePlayer.isUsingItem() || (Configs.killaura && Killaura.target != null)) {

                    event.setCanceled(true);
                    packetQueue.offer((C03PacketPlayer) event.packet);

                    if (packetQueue.size() > Configs.packetspulsenoslow) {
                        if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                            if (Configs.unblocknoslow)
                                mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            if (Configs.swingnoslow) Minecraft.getMinecraft().thePlayer.swingItem();
                        }
                        while (!packetQueue.isEmpty()) {
                            PacketUtils.sendPacketNoEvent(packetQueue.poll());
                        }

                        if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && Configs.unblocknoslow)
                            mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                    }
                } else {
                    while (!packetQueue.isEmpty()) {
                        PacketUtils.sendPacketNoEvent(packetQueue.poll());
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public void onMotionPre(MotionUpdateEvent.Pre event) {
        if ((PlayerLib.inGame() && Configs.noslow && (mc.thePlayer.isUsingItem() || Killaura.isBlocking) && mc.thePlayer.getHeldItem() != null) &&
                ((Configs.Swordnoslow && Configs.Swordnoslowmode == 2 && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) ||
                (Configs.Bownoslow && Configs.Bownoslowmode == 2 && mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) ||
                (Configs.Foodnoslow && Configs.Foodnoslowmode == 2 && (mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion)))) {
            PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(null));
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
