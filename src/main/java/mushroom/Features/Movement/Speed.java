package mushroom.Features.Movement;
import mushroom.Features.Visual.Notifications;
import mushroom.GUI.Configs;
import mushroom.Libs.FontLib.FontUtil;
import mushroom.Libs.MovementLib;
import mushroom.Libs.TimerLib;
import mushroom.Libs.events.*;
import mushroom.mixins.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.stats.StatList;
import net.minecraft.util.MathHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.Sys;
import scala.collection.parallel.ParIterableLike;

import java.awt.*;

import static mushroom.Libs.PlayerLib.inGame;
import static mushroom.Libs.PlayerLib.mc;

public class Speed {
    int airTicks;
    boolean canApplySpeed;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onUpdate(final MotionUpdateEvent.Pre event) {
        if (Configs.speed) {
            if (Configs.timeronspeed) ((MinecraftAccessor) Minecraft.getMinecraft()).getTimer().timerSpeed = (float) Configs.timerspeedonspeed / 10;
            if (MovementLib.isMoving()) {
                event.setYaw(MovementLib.getYaw());
            }
        }
    }

    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (Configs.speed && inGame()) {

            switch (Configs.speedmode) {
                case 0:
                    MovementLib.setMotion(Configs.speedspeed);

                case 1:
                    if (MovementLib.isMoving()) {
                        double multi = 1d;
                        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed) && this.canApplySpeed) {
                            multi += 0.015f * (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
                        }
                        if (Minecraft.getMinecraft().thePlayer.capabilities.getWalkSpeed() > 0.2f) {
                            multi = 0.8999999761581421;
                        }
                        Minecraft.getMinecraft().thePlayer.motionX *= multi;
                        Minecraft.getMinecraft().thePlayer.motionZ *= multi;
                    } else {
                        Minecraft.getMinecraft().thePlayer.motionX = 0.0;
                        Minecraft.getMinecraft().thePlayer.motionZ = 0.0;
                    }
                    event.setX(Minecraft.getMinecraft().thePlayer.motionX).setZ(Minecraft.getMinecraft().thePlayer.motionZ);

            }
        }
    }

    @SubscribeEvent
    public void onUpdateMove(final MoveStateUpdateEvent event) {
        if (Configs.speed) {
            event.setSneak(false);
        }
    }

    @SubscribeEvent
    public void onMoveFlying(final MoveHeadingEvent event) {
        if (Configs.speed && MovementLib.isMoving() && Configs.speedmode == 1) {

            if (Minecraft.getMinecraft().thePlayer.onGround) {
                this.jump();
                this.airTicks = 0;
            } else {
                ++this.airTicks;
                event.setOnGround(true);
                if (!Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
                    if (Minecraft.getMinecraft().thePlayer.fallDistance < 0.4 && Minecraft.getMinecraft().thePlayer.capabilities.getWalkSpeed() < 0.2f) {
                        event.setFriction2Multi(0.95f);
                    } else {
                        event.setFriction2Multi(0.87f);
                    }
                }
            }
        }
    }

    private String getBPS() {
        final double bps = Math.hypot(Minecraft.getMinecraft().thePlayer.posX - Minecraft.getMinecraft().thePlayer.prevPosX, Minecraft.getMinecraft().thePlayer.posZ - Minecraft.getMinecraft().thePlayer.prevPosZ) * TimerLib.getTimer().timerSpeed * 20.0;
        return String.format("%.2f", bps);
    }

    /*
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null || !Configs.speed) {
            return;
        }
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            FontUtil.comicsans19.drawStringWithShadow(getBPS(), 20, 40, new Color(0,0,0).getRGB());
        }
    }
     */

    private void jump() {
        Minecraft.getMinecraft().thePlayer.motionY = 0.41999998688697815;
        if (Minecraft.getMinecraft().thePlayer.isSprinting()) {
            final float f = MovementLib.getYaw() * 0.017453292f;
            final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
            thePlayer.motionX -= MathHelper.sin(f) * 0.2f;
            final EntityPlayerSP thePlayer2 = Minecraft.getMinecraft().thePlayer;
            thePlayer2.motionZ += MathHelper.cos(f) * 0.2f;
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
            if (Configs.speed) {
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