package mushroom.Features.Movement;
import mushroom.Features.Visual.Notifications;
import mushroom.GUI.Configs;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.Libs.MovementLib;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.TimerLib;
import mushroom.Libs.events.*;
import mushroom.mixins.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.stats.StatList;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.Sys;
import scala.collection.parallel.ParIterableLike;

import java.awt.*;

import static mushroom.Libs.PlayerLib.inGame;
import static mushroom.Libs.PlayerLib.mc;

public class Speed {
    boolean canApplySpeed;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onUpdate(final MotionUpdateEvent.Pre event) {
        if (Configs.speed && (!Configs.scaffold || !Configs.toggleSpeed)) {

            if (Configs.timeronspeed)
                ((MinecraftAccessor) Minecraft.getMinecraft()).getTimer().timerSpeed = Configs.timerspeedonspeed;

            if (Configs.speedmode == 0) {
                if (MovementLib.isMoving()) {
                    if (Configs.autoJumpSpeed) jump(0.2f);
                    event.setYaw(MovementLib.getYaw());
                }
            }
            else if (Configs.speedmode == 1 && MovementLib.isMoving()) {
                if (Minecraft.getMinecraft().thePlayer.onGround && mc.gameSettings.keyBindJump.isKeyDown()) {
                    MovementLib.setMotion(0.18025 * Configs.speedspeed);
                    jump(0.2f);
                }
            }
            else if (Configs.speedmode == 2 && MovementLib.isMoving()) {
                if (Minecraft.getMinecraft().thePlayer.onGround) {
                    jump(0.2f);
                }
                MovementLib.setMotion(0.07725f, true, Configs.speedspeed);

            }
        }
    }


    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (Configs.speed && (!Configs.scaffold || !Configs.toggleSpeed)) {
            if (Configs.speedmode == 0) MovementLib.setMotion(Configs.speedspeed);
        }
    }

    private String getBPS() {
        final double bps = Math.hypot(Minecraft.getMinecraft().thePlayer.posX - Minecraft.getMinecraft().thePlayer.prevPosX, Minecraft.getMinecraft().thePlayer.posZ - Minecraft.getMinecraft().thePlayer.prevPosZ) * TimerLib.getTimer().timerSpeed * 20.0;
        return String.format("%.2f", bps);
    }

    public static void jump(float speedMulti, float yaw) {
        Minecraft.getMinecraft().thePlayer.motionY = 0.41999998688697815;
        if (Minecraft.getMinecraft().thePlayer.isSprinting()) {
            final float f = yaw * 0.017453292f;
            mc.thePlayer.motionX -= MathHelper.sin(f) * speedMulti;
            mc.thePlayer.motionZ += MathHelper.cos(f) * speedMulti;
        }
        Minecraft.getMinecraft().thePlayer.isAirBorne = true;
        Minecraft.getMinecraft().thePlayer.triggerAchievement(StatList.jumpStat);
        if (Minecraft.getMinecraft().thePlayer.isSprinting()) {
            Minecraft.getMinecraft().thePlayer.addExhaustion(0.8f);
        } else {
            Minecraft.getMinecraft().thePlayer.addExhaustion(0.2f);
        }
    }

    public static void jump(float speedMulti) {
        Minecraft.getMinecraft().thePlayer.motionY = 0.41999998688697815;
        if (Minecraft.getMinecraft().thePlayer.isSprinting()) {
            final float f = MovementLib.getYaw() * 0.017453292f;
            mc.thePlayer.motionX -= MathHelper.sin(f) * speedMulti;
            mc.thePlayer.motionZ += MathHelper.cos(f) * speedMulti;
        }
        Minecraft.getMinecraft().thePlayer.isAirBorne = true;
        Minecraft.getMinecraft().thePlayer.triggerAchievement(StatList.jumpStat);
        if (Minecraft.getMinecraft().thePlayer.isSprinting()) {
            Minecraft.getMinecraft().thePlayer.addExhaustion(0.8f);
        } else {
            Minecraft.getMinecraft().thePlayer.addExhaustion(0.2f);
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onPacket(final PacketReceivedEvent event) {
        if (event.packet instanceof S08PacketPlayerPosLook && Configs.disablespeedonflag) {
            if (Configs.speed && (!Configs.scaffold || !Configs.toggleSpeed)) {
                Notifications.popupmessage("speed", "disabled");
                ((MinecraftAccessor) Minecraft.getMinecraft()).getTimer().timerSpeed = 1.0f;
                this.canApplySpeed = false;
                Configs.speed = false;
                if (Minecraft.getMinecraft().thePlayer != null) {
                    Minecraft.getMinecraft().thePlayer.motionX = 0.0;
                    Minecraft.getMinecraft().thePlayer.motionZ = 0.0;
                }
            }
        }
    }

}