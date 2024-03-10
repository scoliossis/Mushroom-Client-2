package mushroom.mixins;

import mushroom.Features.Combat.Killaura;
import mushroom.GUI.Configs;
import mushroom.Libs.MovementLib;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.RotationUtils;
import mushroom.Libs.events.MotionUpdateEvent;
import mushroom.Libs.events.MoveFlyingEvent;
import mushroom.Libs.events.MoveHeadingEvent;
import mushroom.Libs.events.PlayerUpdateEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static mushroom.Libs.PlayerLib.mc;

@Mixin(value = { EntityPlayerSP.class }, priority = 1)
public class PlayerSPMixin {
    private boolean serverSprintState;
    private boolean serverSneakState;
    private float lastReportedPitch;
    private double lastReportedPosX;
    private float lastReportedYaw;
    private double lastReportedPosY;
    private double lastReportedPosZ;
    private int positionUpdateTicks;

    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isUsingItem()Z"))
    public boolean isUsingItem(final EntityPlayerSP instance) {
        return !Configs.noslow && instance.isUsingItem();
    }

    boolean wasdown = false;

    @Inject(method = { "onLivingUpdate" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;onLivingUpdate()V") }, cancellable = true)
    public void onLivingUpdate(final CallbackInfo ci) {

        if (((Configs.sprint && (Configs.sprintmode == 1)) || (Configs.scaffold && (Configs.scafsprintmode == 4 || Configs.scafsprintmode == 5))) && !mc.thePlayer.isUsingItem()) {
            if (!MovementLib.isMoving() || mc.thePlayer.isSneaking() || (mc.thePlayer.getFoodStats().getFoodLevel() <= 6.0f && !mc.thePlayer.capabilities.allowFlying)) {
                if (mc.thePlayer.isSprinting()) {
                    mc.thePlayer.setSprinting(false);
                }
            }

            else if (!mc.thePlayer.isSprinting()) {
                mc.thePlayer.setSprinting(true);
            }

        }

        if (Configs.speed && mc.thePlayer.getFoodStats().getFoodLevel() > 6.0f && !mc.thePlayer.isSprinting() && MovementLib.isMoving()) {
            mc.thePlayer.setSprinting(true);
        }

        if ((Configs.noslow && Minecraft.getMinecraft().thePlayer.isUsingItem()) || (Configs.killaura && Killaura.target != null && Killaura.isBlocking && Configs.autoblockSlowDown && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
            EnumAction action;
            if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) action = EnumAction.BLOCK;
            else action = Minecraft.getMinecraft().thePlayer.getHeldItem().getItem().getItemUseAction(Minecraft.getMinecraft().thePlayer.getHeldItem());

            float sworrdSped = Configs.noslowswordspeed;
            if ((Configs.killaura && Killaura.target != null && (Killaura.isBlocking || Configs.autoblockmode == 5 || Configs.autoblockmode == 6) && Configs.autoblockSlowDown && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) sworrdSped = 0.3f;


            float bowSped = Configs.noslowbowspeed;
            float drrinkSped = Configs.nosloweatspeed;

            if (!Configs.Swordnoslow) sworrdSped = 0.3f;
            if (!Configs.Bownoslow) bowSped = 0.3f;
            if (!Configs.Foodnoslow) drrinkSped = 0.3f;

            if (action == EnumAction.BLOCK) {
                Minecraft.getMinecraft().thePlayer.movementInput.moveForward *= sworrdSped;
                Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe *= sworrdSped;
            }
            else if (action == EnumAction.BOW) {
                Minecraft.getMinecraft().thePlayer.movementInput.moveForward *= bowSped;
                Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe *= bowSped;
            }

            else if (action != EnumAction.NONE) {
                Minecraft.getMinecraft().thePlayer.movementInput.moveForward *= drrinkSped;
                Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe *= drrinkSped;
            }
        }
    }

    @Inject(method = { "onUpdate" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isRiding()Z") }, cancellable = true)
    private void onUpdate(final CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post((Event)new PlayerUpdateEvent())) {
            ci.cancel();
        }
    }


    /**
     * @author shoutout to notch
     * @reason he an og fr (why does intellij make me add an author and a reason)
     */
    @Overwrite
    public void onUpdateWalkingPlayer() {
        final MotionUpdateEvent event = new MotionUpdateEvent.Pre(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround, mc.thePlayer.isSprinting(), mc.thePlayer.isSneaking());
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return;
        }
        final boolean flag = event.sprinting;
        if (flag != this.serverSprintState) {
            if (flag) {
                mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            }
            else {
                mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            this.serverSprintState = flag;
        }
        final boolean flag2 = event.sneaking;
        if (flag2 != this.serverSneakState) {
            if (flag2) {
                mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            else {
                mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
            this.serverSneakState = flag2;
        }
        final double d0 = event.x - this.lastReportedPosX;
        final double d2 = event.y - this.lastReportedPosY;
        final double d3 = event.z - this.lastReportedPosZ;
        final double d4 = event.yaw - this.lastReportedYaw;
        final double d5 = event.pitch - this.lastReportedPitch;
        boolean flag3 = d0 * d0 + d2 * d2 + d3 * d3 > 9.0E-4 || this.positionUpdateTicks >= 20;
        final boolean flag4 = d4 != 0.0 || d5 != 0.0;
        if (mc.thePlayer.ridingEntity == null) {
            if (flag3 && flag4) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(event.x, event.y, event.z, event.yaw, event.pitch, event.onGround));
            }
            else if (flag3) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(event.x, event.y, event.z, event.onGround));
            }
            else if (flag4) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(event.yaw, event.pitch, event.onGround));
            }
            else {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(event.onGround));
            }
        }
        else {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.motionX, -999.0, mc.thePlayer.motionZ, event.yaw, event.pitch, event.onGround));
            flag3 = false;
        }
        ++this.positionUpdateTicks;
        if (flag3) {
            this.lastReportedPosX = event.x;
            this.lastReportedPosY = event.y;
            this.lastReportedPosZ = event.z;
            this.positionUpdateTicks = 0;
        }
        PlayerLib.lastGround = event.onGround;
        RotationUtils.lastLastReportedPitch = this.lastReportedPitch;
        if (flag4) {
            this.lastReportedYaw = event.yaw;
            this.lastReportedPitch = event.pitch;
        }
        MinecraftForge.EVENT_BUS.post(new MotionUpdateEvent.Post(event));
    }
}