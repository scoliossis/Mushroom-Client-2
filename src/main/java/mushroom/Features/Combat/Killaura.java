package mushroom.Features.Combat;

import mushroom.Features.Movement.AntiVoid;
import mushroom.Features.Movement.Nofall;
import mushroom.GUI.Configs;
import mushroom.Libs.*;
import mushroom.Libs.events.MotionUpdateEvent;
import mushroom.Libs.events.MoveFlyingEvent;
import mushroom.Libs.events.PacketReceivedEvent;
import mushroom.mixins.PlayerSPAccessor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.util.BlockPos;
import org.lwjgl.Sys;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static mushroom.Libs.PlayerLib.*;

public class Killaura {

    boolean killauraon = false;
    public static final MillisTimer DISABLE = new MillisTimer();
    private final MillisTimer lastAttack = new MillisTimer();
    private final MillisTimer switchDelayTimer = new MillisTimer();
    private final MillisTimer blockDelay = new MillisTimer();
    private int nextCps = 10;

    int attacks = 0;
    public static EntityLivingBase target = null;
    public static boolean isBlocking = false;

    int targetIndex = 0;

    double motionX = 0;
    double motionY = 0;
    double motionZ = 0;

    float yaw = 0;
    float pitch = 0;

    private EntityLivingBase getTarget() {
        if ((mc.currentScreen instanceof GuiContainer) || mc.theWorld == null) {
            return null;
        }
        final List<Entity> validTargets = mc.theWorld.getLoadedEntityList().stream().filter(entity -> entity instanceof EntityLivingBase).filter(entity -> isValid((EntityLivingBase) entity)).sorted(Comparator.comparingDouble(e -> e.getDistanceToEntity(mc.thePlayer))).collect(Collectors.toList());
        switch (Configs.killaurasorting) {
            case 0: {
                validTargets.sort(Comparator.comparingDouble(e -> e.getDistanceToEntity(mc.thePlayer)));
                break;
            }
            case 1: {
                validTargets.sort(Comparator.comparing(e -> e.hurtResistantTime));
                break;
            }
        }
        if (!validTargets.isEmpty()) {
            if (targetIndex >= validTargets.size()) {
                targetIndex = 0;
            }
            switch (Configs.killauramode) {
                case 0: {
                    return (EntityLivingBase)validTargets.get(targetIndex);
                }
                case 1: {
                    return (EntityLivingBase)validTargets.get(0);
                }
            }
        }
        return null;
    }

    private boolean isValid(final EntityLivingBase entity) {
        if (entity == mc.thePlayer || !AntiBot.isValidEntity(entity) || (!Configs.invisibles && entity.isInvisible()) || entity instanceof EntityArmorStand || (!mc.thePlayer.canEntityBeSeen(entity) && Configs.raytrace) || entity.getHealth() <= 0.0f || entity.getDistanceToEntity(mc.thePlayer) > ((target != null && target != entity) ? ((float) Configs.aurareach) : Math.max((Configs.rotationrange), (Configs.aurareach))) || RotationUtils.getRotationDifference(RotationUtils.getRotations(entity), RotationUtils.getPlayerRotation()) > Configs.aurafov) {
            return false;
        }
        return ((!(entity instanceof EntityMob) && !(entity instanceof EntityAmbientCreature) && !(entity instanceof EntityWaterMob) && !(entity instanceof EntityAnimal) && !(entity instanceof EntitySlime)) || Configs.mobs) && (!(entity instanceof EntityPlayer) || ((!PlayerLib.isTeam(entity) || !Configs.teams) && Configs.people)) && !(entity instanceof EntityVillager);
    }

    @SubscribeEvent
    public void tick(TickEvent.PlayerTickEvent e) {
        if (killauraon != Configs.killaura) {
            if (Configs.killaura) {
                attacks = 0;
            }
            else {
                if (isBlocking) stopBlocking();

                target = null;
                isBlocking = false;
            }
        }
        killauraon = Configs.killaura;
    }

    long stoppedTime = System.currentTimeMillis();

    long lastSwung = 0;
    long unblockTime = 0;

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onMovePre(MotionUpdateEvent.Pre event) {
        if (AntiVoid.isBlinking() || Nofall.noFallBlinking || Configs.scaffold || !DISABLE.hasTimePassed(100L) || (!Configs.killaura || (Configs.swordonly && (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword))))) {
            target = null;
            return;
        }

        target = getTarget();
        if (Configs.mousedown && !mc.gameSettings.keyBindAttack.isKeyDown()) {
            return;
        }

        if (Configs.autoblockmode == 4 && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            boolean block;


            if (target == null) block = false;
            else if (System.currentTimeMillis() - lastSwung > Configs.maxHitDelay && Configs.maxHitDelayB) block = false;
            else if (System.currentTimeMillis() - lastSwung < Configs.reblockTimeMax && System.currentTimeMillis() - Configs.reblockTimeMin > 10) block = true;
            else if (mc.thePlayer.hurtResistantTime <= Configs.hurtTimeFrames && Configs.playerHurtTimeCheck) block = false;
            else if (target.hurtResistantTime <= Configs.enemyTimeFrames && Configs.enemyHurtTimeCheck) block = true;
            else if (mc.thePlayer.swingProgress <= Configs.swingCheckFrames && Configs.swingProgressCheck) block = false;
            else if (target.getDistanceToEntity(mc.thePlayer) > Configs.aurareach) block = false;
            else block = true;

            if (block && !isBlocking) {
                //System.out.println("blocked");
                if (!mc.thePlayer.isBlocking()) startBlocking();
            }
            else if (isBlocking && !block) {
                //System.out.println("unblocked");
                stopBlocking();
                unblockTime = System.currentTimeMillis();
            }

            isBlocking = block;
        }


        if (target != null) {

            final Rotations angles;
            angles = RotationUtils.getRotations(target, 0.2f);
            switch (Configs.killaurarotmod) {
                case 0:
                    event.setRotation(RotationUtils.getSmoothRotation(RotationUtils.getLastReportedRotation(), angles, Configs.smoothspeed));
                    break;
                case 1:
                    event.setRotation(RotationUtils.getLimitedRotation(RotationUtils.getLastReportedRotation(), angles, Configs.minrotation + Math.abs(Configs.maxrotation - Configs.minrotation) * new Random().nextFloat()));
                    break;
                case 2:
                    //event.setRotation(RotationUtils.getLimitedRotation(RotationUtils.getLastReportedRotation(), angles, Configs.minrotation + Math.abs(Configs.maxrotation - Configs.minrotation) * new Random().nextFloat()));
                    //break;
            }

            event.setPitch(MathLib.clamp(event.pitch, 90.0f, -90.0f));
            ((PlayerSPAccessor)mc.thePlayer).setLastReportedYaw(event.yaw);
            ((PlayerSPAccessor)mc.thePlayer).setLastReportedPitch(event.pitch);
        }

        if (Configs.mousedown && !mc.gameSettings.keyBindAttack.isKeyDown()) {
            attacks = 0;
            return;
        }
        if (target != null && (mc.thePlayer.getDistanceToEntity(target) < Math.max(Configs.rotationrange, Configs.aurareach)) && attacks > 0) {
            if (Configs.autoblockmode == 2) {
                stopBlocking();
            }
            while (attacks > 0) {

                if (Configs.killaurarotmod == 2) {
                    final Rotations angles;
                    angles = RotationUtils.getRotations(target, 0.2f);

                    event.setRotation(RotationUtils.getLimitedRotation(RotationUtils.getLastReportedRotation(), angles, 360));

                    motionX = mc.thePlayer.motionX;
                    motionY = mc.thePlayer.motionY;
                    motionZ = mc.thePlayer.motionZ;

                    yaw = mc.thePlayer.rotationYaw;
                    pitch = mc.thePlayer.rotationPitch;

                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionY = 0;
                    mc.thePlayer.motionZ = 0;

                    event.setPitch(MathLib.clamp(event.pitch, 90.0f, -90.0f));
                    ((PlayerSPAccessor)mc.thePlayer).setLastReportedYaw(event.yaw);
                    ((PlayerSPAccessor)mc.thePlayer).setLastReportedPitch(event.pitch);

                    mc.thePlayer.swingItem();

                    if (mc.thePlayer.getDistanceToEntity(target) < (Configs.aurareach)) {
                        if ((RotationUtils.getRotationDifference(RotationUtils.getRotations(target), RotationUtils.getLastReportedRotation()) < Configs.auraaccuracy)) {
                            mc.playerController.attackEntity(mc.thePlayer, target);
                            if (switchDelayTimer.hasTimePassed((long) Configs.switchdelay)) {
                                ++targetIndex;
                                switchDelayTimer.reset();
                            }
                        }
                    }
                    --attacks;


                    event.setPitch(MathLib.clamp(pitch, 90.0f, -90.0f));
                    ((PlayerSPAccessor)mc.thePlayer).setLastReportedYaw(yaw);
                    ((PlayerSPAccessor)mc.thePlayer).setLastReportedPitch(pitch);

                    mc.thePlayer.motionX = motionX;
                    mc.thePlayer.motionY = motionY;
                    mc.thePlayer.motionZ = motionZ;
                }

                else {
                    if ((Configs.autoblockmode != 4 || (!isBlocking && System.currentTimeMillis() - unblockTime > Configs.swingCooldown))) {

                        if (Configs.autoblockmode == 5 || Configs.autoblockmode == 6 || Configs.autoblockmode == 7) {
                            stopBlocking();
                            stoppedTime = System.currentTimeMillis();
                            isBlocking = false;
                        }

                        mc.thePlayer.swingItem();

                        if (Configs.autoblockmode == 4 && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                            lastSwung = System.currentTimeMillis();
                        }

                        if (mc.thePlayer.getDistanceToEntity(target) < (Configs.aurareach)) {
                            if ((RotationUtils.getRotationDifference(RotationUtils.getRotations(target), RotationUtils.getLastReportedRotation()) < Configs.auraaccuracy)) {
                                if (Configs.packetAttack) PacketUtils.sendPacketNoEvent(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                                else mc.playerController.attackEntity(mc.thePlayer, target);

                                if (switchDelayTimer.hasTimePassed((long) Configs.switchdelay)) {
                                    targetIndex++;
                                    switchDelayTimer.reset();
                                }
                            }
                        }
                    }
                    --attacks;

                }
            }
            if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                switch (Configs.autoblockmode) {
                    case 2: {
                        if (isBlocking) {
                            startBlocking();
                            break;
                        }
                        break;
                    }
                    case 3: {
                        if (blockDelay.hasTimePassed(250L)) {
                            startBlocking();
                            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                            blockDelay.reset();
                            break;
                        }
                        break;
                    }
                }
            }

            if (Configs.killaurarotmod == 2) {
                target = null;
            }

        }
        else {
            attacks = 0;
        }
    }

    @SubscribeEvent // AHHH POST SCARY
    public void onMovePOST(MotionUpdateEvent.Post event) {
        if (Configs.killaura && Configs.autoblockmode == 5 && target != null) {
            if (!isBlocking) {
                isBlocking = true;
                startBlocking();
            }
        }
    }

    @SubscribeEvent
    public void onMoveMix(MotionUpdateEvent event) {
        if (Configs.killaura && Configs.autoblockmode == 6 && target != null) {
            if (!isBlocking) {
                isBlocking = true;
                startBlocking();
            }
        }
    }

    @SubscribeEvent
    public void onMovePr(MotionUpdateEvent.Pre event) {
        if (Configs.killaura && Configs.autoblockmode == 7 && target != null) {
            if (!isBlocking && System.currentTimeMillis() - stoppedTime > Configs.reblockDelay) {
                isBlocking = true;
                startBlocking();
            }
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onPacket(final PacketReceivedEvent event) {
        if (event.packet instanceof S08PacketPlayerPosLook) {
            DISABLE.reset();
        }
    }

    @SubscribeEvent
    public void onRender(final RenderWorldLastEvent event) {
        if (Configs.killaura && target != null && lastAttack.hasTimePassed(1000 / nextCps) && (mc.thePlayer.getDistanceToEntity(target) < (Configs.swingonrot ? ((float) Configs.rotationrange) : ((float) Configs.aurareach)))) {
            nextCps = (int)((Configs.averagecps-3) + Math.abs((Configs.averagecps+3) - (Configs.averagecps-3)) * new Random().nextFloat());
            lastAttack.reset();
            attacks++;
        }
    }

    private void startBlocking() {
        PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
        isBlocking = true;
    }

    private void stopBlocking() {
        if (isBlocking) {
            mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            isBlocking = false;
        }
    }


    @SubscribeEvent
    public void onMoveFlying(MoveFlyingEvent event) {
        if (Configs.killaura && target != null && Configs.moveFixAura) {
            event.setYaw(((PlayerSPAccessor)mc.thePlayer).getLastReportedYaw());
        }
    }
}