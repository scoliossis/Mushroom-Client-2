package mushroom.Libs;

import mushroom.mixins.PlayerSPAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.EntityLivingBase;

import static mushroom.Libs.PlayerLib.mc;

public class RotationUtils {
    public static float lastLastReportedPitch;

    private RotationUtils() {
    }

    public static Rotations getClosestRotation(final AxisAlignedBB aabb) {
        return getRotations(getClosestPointInAABB(mc.thePlayer.getPositionEyes(1.0f), aabb));
    }

    public static Rotations getClosestRotation(final AxisAlignedBB aabb, final float offset) {
        return getClosestRotation(aabb.expand((double)(-offset), (double)(-offset), (double)(-offset)));
    }

    public static Rotations getRotations(final EntityLivingBase target) {
        return getRotations(target.posX, target.posY + target.getEyeHeight() / 2.0, target.posZ);
    }

    public static Rotations getRotations(final EntityLivingBase target, final float random) {
        return getRotations(target.posX + (new Random().nextInt(3) - 1) * random * new Random().nextFloat(), target.posY + target.getEyeHeight() / 2.0 + (new Random().nextInt(3) - 1) * random * new Random().nextFloat(), target.posZ + (new Random().nextInt(3) - 1) * random * new Random().nextFloat());
    }

    public static double getRotationDifference(final Rotations a, final Rotations b) {
        return Math.hypot(getAngleDifference(a.getYaw(), b.getYaw()), getAngleDifference(a.getPitch(), b.getPitch()));
    }
    public static double getYawDiff(final Rotations a, final Rotations b) {
        return getAngleDifference(a.getYaw(), b.getYaw());
    }
    public static double getPitchDiff(final Rotations a, final Rotations b) {
        return getAngleDifference(a.getPitch(), b.getPitch());
    }

    public static Rotations getRotations(final Vec3 vec3) {
        return getRotations(vec3.xCoord, vec3.yCoord, vec3.zCoord);
    }

    public static Rotations getRotations(final double posX, final double posY, final double posZ) {
        final double x = posX - mc.thePlayer.posX;
        final double y = posY - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        final double z = posZ - mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(x * x + z * z);
        final float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / 3.141592653589793));
        return new Rotations(yaw, pitch);
    }
    public static Rotations getRotations(final double posX, final double posY, final double posZ, float random) {
        final double x = posX+(new Random().nextInt(3) - 1) * random * new Random().nextFloat();
        final double y = posY+(new Random().nextInt(3) - 1) * random * new Random().nextFloat();
        final double z = posZ+(new Random().nextInt(3) - 1) * random * new Random().nextFloat();
        return getRotations(x,y,z);
    }

    public static Rotations getSmoothRotation(final Rotations current, final Rotations target, final float smooth) {
        return new Rotations(current.getYaw() + (target.getYaw() - current.getYaw()) / smooth, current.getPitch() + (target.getPitch() - current.getPitch()) / smooth);
    }

    public static Rotations getPlayerRotation() {
        return new Rotations(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
    }

    public static Rotations getLimitedRotation(final Rotations currentRotation, final Rotations targetRotation, final float turnSpeed) {
        return new Rotations(currentRotation.getYaw() + MathHelper.clamp_float(getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw()), -turnSpeed, turnSpeed), currentRotation.getPitch() + MathHelper.clamp_float(getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch()), -turnSpeed, turnSpeed));
    }

    public static float getAngleDifference(final float a, final float b) {
        return ((a - b) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }

    public static Vec3 getClosestPointInAABB(final Vec3 vec3, final AxisAlignedBB aabb) {
        return new Vec3(clamp(aabb.minX, aabb.maxX, vec3.xCoord), clamp(aabb.minY, aabb.maxY, vec3.yCoord), clamp(aabb.minZ, aabb.maxZ, vec3.zCoord));
    }

    public static boolean isBlockVisible(final BlockPos pos) {
        return getRandomVisibilityLine(pos) != null;
    }


    public static int accuracy = 10;
    public static Vec3 getRandomVisibilityLine(final BlockPos pos) {
        final List<Vec3> lines = new ArrayList<Vec3>();
        for (int x = 0; x < accuracy; ++x) {
            for (int y = 0; y < accuracy; ++y) {
                for (int z = 0; z < accuracy; ++z) {
                    final Vec3 target = new Vec3(pos.getX() + (double) x / accuracy, pos.getY() + (double) y / accuracy, pos.getZ() + (double) z / accuracy);
                    BlockPos test = new BlockPos(target.xCoord, target.yCoord, target.zCoord);
                    final MovingObjectPosition movingObjectPosition = Minecraft.getMinecraft().theWorld.rayTraceBlocks(Minecraft.getMinecraft().thePlayer.getPositionEyes(0.0f), target, true, false, true);
                    if (movingObjectPosition != null) {
                        final BlockPos obj = movingObjectPosition.getBlockPos();
                        if (obj.equals(test) && Minecraft.getMinecraft().thePlayer.getDistance(target.xCoord, target.yCoord - Minecraft.getMinecraft().thePlayer.getEyeHeight(), target.zCoord) < 4.5 && Math.abs(Minecraft.getMinecraft().thePlayer.posY - target.yCoord) < 1) {
                            lines.add(target);
                        }
                    }
                }
            }
        }
        return lines.isEmpty() ? null : lines.get(new Random().nextInt(lines.size()));
    }

    public static Rotations getLastReportedRotation() {
        return new Rotations(((PlayerSPAccessor)mc.thePlayer).getLastReportedYaw(), ((PlayerSPAccessor)mc.thePlayer).getLastReportedPitch());
    }

    private static double clamp(final double min, final double max, final double value) {
        return Math.max(min, Math.min(max, value));
    }
}