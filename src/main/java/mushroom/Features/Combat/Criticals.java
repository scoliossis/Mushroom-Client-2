package mushroom.Features.Combat;

import mushroom.Features.Movement.Speed;
import mushroom.GUI.Configs;
import mushroom.Libs.ChatLib;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.Libs.MathLib;
import mushroom.Libs.PacketUtils;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.events.MotionUpdateEvent;
import mushroom.Libs.events.PacketSentEvent;
import mushroom.mixins.C03Accessor;
import mushroom.mixins.PlayerSPAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static mushroom.Libs.PlayerLib.mc;


public class Criticals {

    public static boolean trySwing = false;
    public static boolean canSwing = false;
    public static int offground = 0;

    private C02PacketUseEntity attack;

    double prevY = 0;
    double fallDist = 0;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacket(PacketSentEvent event) {
        if (Configs.criticals && Configs.critMode != 0 && event.packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.packet).getAction() == C02PacketUseEntity.Action.ATTACK && ((C02PacketUseEntity)event.packet).getEntityFromWorld(mc.theWorld) instanceof EntityLivingBase && ((EntityLivingBase)((C02PacketUseEntity)event.packet).getEntityFromWorld(mc.theWorld)).hurtTime <= 1 && mc.thePlayer.onGround) {
            attack = (C02PacketUseEntity)event.packet;
            event.setCanceled(true);
            return;
        }
        if (Configs.criticals && Configs.critMode == 0) {
            if (event.packet instanceof C02PacketUseEntity && ((C02PacketUseEntity) event.packet).getAction() == C02PacketUseEntity.Action.ATTACK) {
                trySwing = true;
                canSwing = false;
            }
            if (event.packet instanceof C03PacketPlayer && (trySwing || canSwing)) {

                offground++;
                ((C03Accessor) event.packet).setOnGround(false);
                if (offground > Configs.offGroundTicks) {
                    canSwing = true;
                    trySwing = false;
                    offground = 0;
                }

                if (mc.thePlayer.posY < prevY) {
                    fallDist += prevY - mc.thePlayer.posY;
                }

                if (mc.thePlayer.onGround) {
                    canSwing = false;
                    if (fallDist >= Configs.noFallJumps) {
                        ((C03Accessor) event.packet).setOnGround(true);
                        fallDist = 0;
                    }
                    Speed.jump(0.2f);
                }

                prevY = mc.thePlayer.posY;
            }
        }
    }



    private int ticks;

    @SubscribeEvent
    public void onUpdate(final MotionUpdateEvent.Pre event) {
        if (Configs.criticals && attack != null) {
            if (Configs.critMode == 1) {
                if (event.onGround) {
                    ticks++;
                    if (ticks == 1) event.y += 0.0625f + MathLib.getRandomInRange(0.0, 0.0010000000474974513);
                    event.setOnGround(false);
                    mc.thePlayer.onGround = false;
                    if (ticks > 2) {
                        PacketUtils.sendPacketNoEvent(attack);
                        attack = null;
                        ticks = 0;
                    }
                }
                else {
                    PacketUtils.sendPacketNoEvent(attack);
                    attack = null;
                    ticks = 0;
                }
            }
        }
    }
}
