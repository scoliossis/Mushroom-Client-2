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
        if (Configs.criticals && Configs.critMode != 0 && event.packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.packet).getAction() == C02PacketUseEntity.Action.ATTACK && ((C02PacketUseEntity)event.packet).getEntityFromWorld(mc.theWorld) instanceof EntityLivingBase && ((EntityLivingBase)((C02PacketUseEntity)event.packet).getEntityFromWorld(mc.theWorld)).hurtTime <= 1) {
            this.attack = (C02PacketUseEntity)event.packet;
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
                    Speed.jump();
                }

                prevY = mc.thePlayer.posY;
            }
        }
    }



    private int ticks;

    @SubscribeEvent
    public void onUpdate(final MotionUpdateEvent.Pre event) {
        if (Configs.criticals && this.attack != null) {
            switch (Configs.critMode) {
                case 1: {
                    if (mc.thePlayer.onGround && event.onGround && this.attack.getEntityFromWorld(mc.theWorld) instanceof EntityLivingBase && ((EntityLivingBase)this.attack.getEntityFromWorld(mc.theWorld)).hurtTime <= 1) {
                        mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(((PlayerSPAccessor)mc.thePlayer).getLastReportedPosX(), ((PlayerSPAccessor)mc.thePlayer).getLastReportedPosY() +  0.0625f + MathLib.getRandomInRange(0.0, 0.0010000000474974513), ((PlayerSPAccessor)mc.thePlayer).getLastReportedPosZ(), false));
                        mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(((PlayerSPAccessor)mc.thePlayer).getLastReportedPosX(), ((PlayerSPAccessor)mc.thePlayer).getLastReportedPosY() + 0.03125f + MathLib.getRandomInRange(0.0, 0.0010000000474974513), ((PlayerSPAccessor)mc.thePlayer).getLastReportedPosZ(), false));
                        PacketUtils.sendPacketNoEvent(this.attack);
                        this.attack = null;
                        break;
                    }
                    this.attack = null;
                    break;
                }
                case 2: {
                    if (mc.thePlayer.onGround && event.onGround && this.attack.getEntityFromWorld(mc.theWorld) instanceof EntityLivingBase && ((EntityLivingBase)this.attack.getEntityFromWorld(mc.theWorld)).hurtTime <= 1) {
                        switch (this.ticks++) {
                            case 0:
                            case 1: {
                                event.y += 0.0625f + MathLib.getRandomInRange(0.0, 0.0010000000474974513);
                                event.setOnGround(false);
                                break;
                            }
                            case 2: {
                                PacketUtils.sendPacketNoEvent(this.attack);
                                this.ticks = 0;
                                this.attack = null;
                                break;
                            }
                        }
                        break;
                    }
                    this.ticks = 0;
                    this.attack = null;
                    break;
                }
            }
        }
    }
}
