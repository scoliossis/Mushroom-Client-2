package mushroom.Features.Macros;

import mushroom.Features.Movement.AntiVoid;
import mushroom.Features.Movement.Nofall;
import mushroom.Features.Movement.Speed;
import mushroom.Features.Visual.Notifications;
import mushroom.GUI.Configs;
import mushroom.Libs.*;
import mushroom.Libs.events.MotionUpdateEvent;
import mushroom.Libs.events.MoveEvent;
import mushroom.mixins.PlayerSPAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;

import static mushroom.Libs.PlayerLib.inGame;
import static mushroom.Libs.PlayerLib.mc;

public class Scaffold {
    private int ticks = 0;
    boolean flag = false;

    MovingObjectPosition rayrace = null;

    boolean towering = false;
    int towerTicks = 0;

    int selectedSlot = -1;

    @SubscribeEvent
    public void onUpdate(final MotionUpdateEvent event) {

        if (Configs.scaffold && !AntiVoid.isBlinking() && !Nofall.noFallBlinking && PlayerLib.inGame()) {

            final BlockPos pos = this.getClosestBlock();

            Rotations rotation = new Rotations(MovementLib.getYaw()-180, 81);

            if (Configs.rotpos == 1 && pos != null) {
                rotation = RotationUtils.getRotations(RotationUtils.getClosestPointInAABB(mc.thePlayer.getPositionEyes(0f), mc.theWorld.getBlockState(pos).getBlock().getSelectedBoundingBox(mc.theWorld, pos)));
            }

            event.setYaw(rotation.getYaw());
            ((PlayerSPAccessor)mc.thePlayer).setLastReportedYaw(event.yaw);
            this.flag = true;
            for (int j = 81; j > 72; --j) {
                final MovingObjectPosition trace = rayTrace(event.yaw, (float)j);
                if (trace != null) {
                    this.flag = false;
                    event.setPitch((float)(j + MathLib.getRandomInRange(0.1, -0.1)));
                    ((PlayerSPAccessor)mc.thePlayer).setLastReportedPitch(event.pitch);
                    break;
                }
            }

            rayrace = rayTrace(event.getRotation());

            selectedSlot = this.getBlock();
            if (selectedSlot == -1) return;

            if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem()  instanceof ItemBlock)) {
                if (Configs.blockswap == 1)
                    mc.getNetHandler().getNetworkManager().sendPacket(new C09PacketHeldItemChange(selectedSlot));
                else PlayerLib.swapToSlot(selectedSlot);
            }


            if (this.ticks <= 0 && (mc.thePlayer.motionY <= Configs.maxYVeloc || !Configs.maxYVelo || Configs.tower)) {
                if (rayrace != null && rayrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && mc.theWorld.getBlockState(rayrace.getBlockPos()).getBlock().isFullBlock()) {
                    if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(selectedSlot), rayrace.getBlockPos(), rayrace.sideHit, rayrace.hitVec)) {
                        mc.thePlayer.swingItem();
                    }

                    if (Configs.placemaxdelay != 0) this.ticks = (int) ((Configs.placemindelay) + new Random().nextInt((int) ((Configs.placemaxdelay) - (Configs.placemindelay) + 1.0)));

                    if (mc.thePlayer.inventory.getStackInSlot(selectedSlot) != null && mc.thePlayer.inventory.getStackInSlot(selectedSlot).stackSize <= 0) {
                        mc.thePlayer.inventory.removeStackFromSlot(selectedSlot);
                    }
                }
            }
            --this.ticks;

        }
    }

    @SubscribeEvent
    public void onUpdatePre(final MotionUpdateEvent.Pre event) {
        if (Configs.scaffold && Configs.tower) {
            if (mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.onGround) {
                towering = true;
            }
            if (!mc.gameSettings.keyBindJump.isKeyDown() && towering) {
                towering = false;
            }
            if (towering) {
                if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
                    if (Configs.towermode == 0) {
                        towerTicks++;
                        if (mc.thePlayer.onGround) towerTicks = 0;
                        mc.thePlayer.motionY = 0.41965;
                        if (towerTicks == 1) mc.thePlayer.motionY = 0.30;
                        if (towerTicks == 2) mc.thePlayer.motionY = 0.9 - mc.thePlayer.posY % 1;
                        if (towerTicks == 3) {
                            towerTicks = 0;
                            towering = false;
                        }
                    }
                    else if (Configs.towermode == 1) {
                        MovementLib.setMotion(0.12875);
                        towerTicks++;
                        if (mc.thePlayer.onGround) towerTicks = 0;
                        mc.thePlayer.motionY = 0.41965;
                        if (towerTicks == 1) mc.thePlayer.motionY = 0.33;
                        else if (towerTicks == 2) mc.thePlayer.motionY = 1 - mc.thePlayer.posY % 1;
                        else if (towerTicks == 3) towerTicks = 0;
                    }
                } else {
                    towering = false;
                }
            }
        }
    }

    private int getBlock() {
        if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem()  instanceof ItemBlock && ((ItemBlock)mc.thePlayer.getHeldItem().getItem()).block.isFullBlock()) return mc.thePlayer.inventory.currentItem;
        int current = -1;
        int stackSize = 0;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stackSize < stack.stackSize && stack.getItem() instanceof ItemBlock && ((ItemBlock)stack.getItem()).block.isFullBlock()) {
                stackSize = stack.stackSize;
                current = i;
            }
        }
        return current;
    }

    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        // holy shit wtf switch statements exist
        if (Configs.scaffold && Configs.scafsprintmode != 0 && inGame()) {
            if (Configs.scafsprintmode != 1) mc.thePlayer.setSprinting(false);
            if (Configs.scafsprintmode == 3 || (Configs.scafsprintmode == 5 && mc.gameSettings.keyBindJump.isKeyDown())) return;
            double speed = 0.2575;
            if (Configs.scafsprintmode == 1) speed *= Configs.semisprintspeed;
            if (Configs.scafsprintmode == 4 || Configs.scafsprintmode == 5) speed *= 0.30; // number from my ass

            if (mc.thePlayer.onGround || !Configs.tower) {
                MovementLib.setMotion(speed, Configs.scaffoldJumpMutl, Configs.scaffoldJumpSpeed);
            }
        }
    }

    private BlockPos getClosestBlock() {
        final ArrayList<Vec3> posList = new ArrayList<Vec3>();
        int range = (int) Configs.scaffolddist;
        for (int x = -range; x <= range; x++) {
            for (int y = -3; y < -2; y++) {
                for (int z = -range; z <= range; z++) {
                    final Vec3 vec = new Vec3(x, y, z).addVector(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                    final BlockPos pos2 = new BlockPos(vec);
                    if (mc.theWorld.getBlockState(pos2).getBlock().isFullBlock() || !Configs.onlyFullBlocks) {
                        posList.add(vec);
                    }
                }
            }
        }
        if (posList.isEmpty()) {
            return null;
        }

        posList.sort(Comparator.comparingDouble(pos -> mc.thePlayer.getDistance(pos.xCoord, pos.yCoord, pos.zCoord)));
        return new BlockPos(posList.get(0));
    }

    private static MovingObjectPosition rayTrace(final Rotations rotation) {
        return rayTrace(rotation.getYaw(), rotation.getPitch());
    }

    private static MovingObjectPosition rayTrace(final float yaw, final float pitch) {
        final Vec3 vec3 = mc.thePlayer.getPositionEyes(1);
        final Vec3 vec4 = PlayerLib.getVectorForRotation(yaw, pitch);
        final Vec3 vec5 = vec3.addVector(vec4.xCoord * mc.playerController.getBlockReachDistance(), vec4.yCoord * mc.playerController.getBlockReachDistance(), vec4.zCoord * mc.playerController.getBlockReachDistance());
        return mc.theWorld.rayTraceBlocks(vec3, vec5);
    }

    private void placeBlock() {

        MovingObjectPosition rayrace = rayTrace(0, 90);
        if (rayrace != null) {
            final Vec3 hitVec = rayrace.hitVec;
            final BlockPos hitPos = rayrace.getBlockPos();
            final float f = (float)(hitVec.xCoord - hitPos.getX());
            final float f2 = (float)(hitVec.yCoord - hitPos.getY());
            final float f3 = (float)(hitVec.zCoord - hitPos.getZ());
            mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(rayrace.getBlockPos(), rayrace.sideHit.getIndex(), mc.thePlayer.getHeldItem(), f, f2, f3));
            mc.getNetHandler().getNetworkManager().sendPacket(new C0APacketAnimation());
            mc.thePlayer.getHeldItem().onItemUse(mc.thePlayer, mc.theWorld, hitPos, rayrace.sideHit, f, f2, f3);
        }
    }

}
