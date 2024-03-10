package mushroom.Features.Macros;

import mushroom.Features.Combat.Killaura;
import mushroom.Features.Visual.Notifications;
import mushroom.GUI.Configs;
import mushroom.Libs.*;
import mushroom.Libs.events.MotionUpdateEvent;
import mushroom.Libs.events.MoveEvent;
import mushroom.mixins.PlayerSPAccessor;
import net.minecraft.block.*;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tv.twitch.chat.Chat;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static mushroom.Libs.PlayerLib.inGame;
import static mushroom.Libs.PlayerLib.mc;

public class Fucker {

    ArrayList<BlockPos> blocksToMine = new ArrayList<>();

    public static Block blockAtPos(BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock();
    }

    int startSlot = -1;
    float yaw = 0;

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onMovePre(final MotionUpdateEvent.Pre event) {
        if (!Configs.fucker) {
            blockMining = null;
            blocksToMine.clear();
            mining = false;
        }
        if (blockMining != null && Configs.fuckerRotations && (!Configs.killaura || Killaura.target == null) && !Configs.scaffold) {
            Rotations rot = RotationUtils.getLimitedRotation(RotationUtils.getLastReportedRotation(), RotationUtils.getRotations(blockMining.getX(), blockMining.getY(), blockMining.getZ()), (Configs.fuckerRotationsSpeed-20) + Math.abs((Configs.fuckerRotationsSpeed+20) - (Configs.fuckerRotationsSpeed-20)) * new Random().nextFloat());
            event.setRotation(rot);

            event.setPitch(MathLib.clamp(event.pitch, 90.0f, -90.0f));
            ((PlayerSPAccessor)mc.thePlayer).setLastReportedYaw(event.yaw);
            ((PlayerSPAccessor)mc.thePlayer).setLastReportedPitch(event.pitch);
        }

        if (Configs.fucker && !mining) {
            int PlayerX = (int) mc.thePlayer.posX;
            int PlayerY = (int) mc.thePlayer.posY;
            int PlayerZ = (int) mc.thePlayer.posZ;

            int intReach = (int) Configs.fuckerReach;

            for (int x = PlayerX - intReach; x < PlayerX + intReach; x++) {
                for (int y = PlayerY - intReach; y < PlayerY + intReach; y++) {
                    for (int z = PlayerZ - intReach; z < PlayerZ + intReach; z++) {
                        BlockPos block = new BlockPos(x,y,z);
                        if (checkIfWeUp(block) && blockAtPos(block) instanceof BlockBed) blocksToMine.add(block);
                    }
                }
            }
        }


        if (Configs.fucker) {
            if ((!Configs.killaura || Killaura.target == null) && !Configs.scaffold) {
                if (!blocksToMine.isEmpty()) {
                    BlockPos block = blocksToMine.get(0);

                    if (checkIfWeUp(block)) {
                        yaw = event.yaw;
                        if (Configs.fuckerMode == 2) {
                            BlockPos blockAbove = new BlockPos(block.getX(), block.getY()+1, block.getZ());

                            if (checkIfWeUp(blockAbove)) mineBlockAt(blockAbove);
                            else if (mineBlockAt(block)) blocksToMine.clear();
                        }

                        else mineBlockAt(block);

                    } else blocksToMine.clear();
                }
            }
        }
        else {
            mining = false;
        }

        for (int i = 0; i < dontMinePos.size(); i+=2) {
            if (System.currentTimeMillis() - (long) dontMinePos.get(i+1) > 200) {
                dontMinePos.remove(i);
                dontMinePos.remove(i);
            }
        }
    }

    public boolean checkIfWeUp(BlockPos pos) {
        if (Configs.fucker && (!Configs.killaura || Killaura.target == null) && !Configs.scaffold) {
            return !(blockAtPos(pos) instanceof BlockAir) && !dontMinePos.contains(pos);
        }
        return false;
    }

    boolean mining = false;
    ArrayList dontMinePos = new ArrayList<>();

    public static BlockPos blockMining;
    float currentDamage = 0;
    public boolean mineBlockAt(BlockPos pos) {
        if (!mining) {
            if (!checkIfWeUp(pos) || PlayerLib.distanceToPlayer(pos.getX(), pos.getY(), pos.getZ()) >= Configs.fuckerReach) return blockAtPos(pos) instanceof BlockAir;

            startSlot = mc.thePlayer.inventory.currentItem;

            Notifications.popupmessage("Fucker", "§2breaking " + blockAtPos(pos).getLocalizedName());

            mining = true;

            blockMining = pos;

            currentDamage = 0;

            if (Configs.fuckerSwing == 1) mc.thePlayer.swingItem();
            else mc.getNetHandler().addToSendQueue(new C0APacketAnimation());

            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.fromAngle(yaw)));

            return blockAtPos(pos) instanceof BlockAir;
        }

        if (Configs.fuckerMode != 1) {
            float maxBreak = (2-Configs.fuckerBreakSpeed) * (((Configs.nofallmode == 0 || Configs.nofallmode == 4) && Configs.nofall) ? 4f : 1f);
            if (currentDamage <= maxBreak) {
                if (checkIfWeUp(pos) && mc.thePlayer.inventory.currentItem == startSlot && PlayerLib.distanceToPlayer(pos.getX(), pos.getY(), pos.getZ()) <= Configs.fuckerReach) {
                    currentDamage += mc.theWorld.getBlockState(pos).getBlock().getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, pos);
                    float damagePercent = Math.min(Math.max(currentDamage/(2-Configs.fuckerBreakSpeed) * (((Configs.nofallmode == 0 || Configs.nofallmode == 4) && Configs.nofall) ? 0.25f : 1f), 0), 1);
                    mc.theWorld.sendBlockBreakProgress(mc.thePlayer.getEntityId(), pos, (int) (damagePercent * 10F)-1);
                } else {
                    if (!(blockAtPos(pos) instanceof BlockAir)) {
                        if (PlayerLib.distanceToPlayer(pos.getX(), pos.getY(), pos.getZ()) > Configs.fuckerReach) Notifications.popupmessage("Fucker", "§cwent out of range of bed");
                        //mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, EnumFacing.fromAngle(yaw)));

                        // annoying ass notification if ur using auto tool, will fix one day
                        //else Notifications.popupmessage("Fucker", "§cswapped hotbar slots");
                    }

                    mining = false;
                    blockMining = null;
                    currentDamage = 0;

                }

                return blockAtPos(pos) instanceof BlockAir;
            }
        }

        if (checkIfWeUp(pos)) {
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.fromAngle(yaw)));
            addSurroundings(pos);

            if (blockAtPos(pos) instanceof BlockBed) {
                if (Configs.autoDisables) Configs.fucker = false;
            }

            Notifications.popupmessage("Fucker", "§abroke " + blockAtPos(pos).getLocalizedName());

            mining = false;
            blockMining = null;
            currentDamage = 0;

            return true;
        }

        return blockAtPos(pos) instanceof BlockAir;
    }

     private void addSurroundings(BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                dontMinePos.add(new BlockPos(pos.getX()+x, pos.getY(), pos.getZ()+z));
                dontMinePos.add(System.currentTimeMillis());
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (Configs.fucker) {
            float dam = Math.min(Math.max(currentDamage/(2-Configs.fuckerBreakSpeed) * (((Configs.nofallmode == 0 || Configs.nofallmode == 4) && Configs.nofall) ? 0.25f : 1f), 0), 1);

            int num = (int) (dam * 255);

            if (blockMining != null) RenderLib.renderBoundingBox(blockMining.getX(), blockMining.getY(), blockMining.getZ(), 1, 1-dam, new Color(255-num,num,50), event.partialTicks);
            //else for (BlockPos blockPos : blocksToMine) RenderLib.renderBoundingBox(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1, 1, new Color(83, 25, 101, 100), event.partialTicks);
        }
    }



    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (Configs.fucker && inGame() && mining && Configs.notAGoodMoveFixFucker) MovementLib.setMotion(0.07725);
    }
}
