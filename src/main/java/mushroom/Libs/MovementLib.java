package mushroom.Libs;

import mushroom.Features.Combat.Killaura;
import mushroom.GUI.Configs;
import mushroom.mixins.PlayerSPAccessor;
import net.minecraft.entity.Entity;

import static mushroom.Libs.PlayerLib.mc;

public class MovementLib {
    public static MillisTimer strafeTimer;

    public static float getSpeed() {
        return (float)Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }

    public static float getSpeed(final double x, final double z) {
        return (float)Math.sqrt(x * x + z * z);
    }

    public static void strafe() {
        strafe(getSpeed());
    }

    public static boolean isMoving() {
        return mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f;
    }

    public static boolean hasMotion() {
        return mc.thePlayer.motionX != 0.0 && mc.thePlayer.motionZ != 0.0 && mc.thePlayer.motionY != 0.0;
    }

    public static boolean isOnGround(final double height) {
        return !mc.theWorld.getCollidingBoundingBoxes((Entity)mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }

    public static void strafe(final double speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getDirection();
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
        strafeTimer.reset();
    }

    public static void strafe(final float speed, final float yaw) {
        if (!isMoving() || !strafeTimer.hasTimePassed(150L)) {
            return;
        }
        mc.thePlayer.motionX = -Math.sin(Math.toRadians(yaw)) * speed;
        mc.thePlayer.motionZ = Math.cos(Math.toRadians(yaw)) * speed;
        strafeTimer.reset();
    }

    public static void forward(final double length) {
        final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
        mc.thePlayer.setPosition(mc.thePlayer.posX + -Math.sin(yaw) * length, mc.thePlayer.posY, mc.thePlayer.posZ + Math.cos(yaw) * length);
    }

    public static double getDirection() {
        return Math.toRadians(getYaw());
    }

    public static void setMotion(double speed) {
        if (PlayerLib.inGame()) setMotion(speed, 10000);
    }

    public static void setMotion(double speed, double yaw) {
        if (PlayerLib.inGame() && mc.thePlayer != null) {
            double forward = mc.thePlayer.movementInput.moveForward;
            double strafe = mc.thePlayer.movementInput.moveStrafe;

            if (yaw == 10000) yaw = mc.thePlayer.rotationYaw;

            if (!mc.thePlayer.onGround) speed *= 1.4;

            if (forward == 0.0 && strafe == 0.0) {
                mc.thePlayer.motionX = 0.0;
                mc.thePlayer.motionZ = 0.0;
            } else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += ((forward > 0.0) ? -45 : 45);
                    } else if (strafe < 0.0) {
                        yaw += ((forward > 0.0) ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    } else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }
                final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
                final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
                mc.thePlayer.motionX = forward * speed * cos + strafe * speed * sin;
                mc.thePlayer.motionZ = forward * speed * sin - strafe * speed * cos;
            }
        }
    }

    public static void setMotion(double speed, boolean jumpSprint, double jumpMulti) {
        if (PlayerLib.inGame() && mc.thePlayer != null) {
            double forward = mc.thePlayer.movementInput.moveForward;
            double strafe = mc.thePlayer.movementInput.moveStrafe;

            double yaw = mc.thePlayer.rotationYaw;

            if (!mc.thePlayer.onGround) {
                try {if (!mc.thePlayer.isSprinting()) mc.thePlayer.setSprinting(jumpSprint);}
                catch (Exception e) {e.printStackTrace();}

                speed *= jumpMulti;
            }

            if (forward == 0.0 && strafe == 0.0) {
                mc.thePlayer.motionX = 0.0;
                mc.thePlayer.motionZ = 0.0;
            } else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += ((forward > 0.0) ? -45 : 45);
                    } else if (strafe < 0.0) {
                        yaw += ((forward > 0.0) ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    } else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }
                final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
                final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
                mc.thePlayer.motionX = forward * speed * cos + strafe * speed * sin;
                mc.thePlayer.motionZ = forward * speed * sin - strafe * speed * cos;
            }
        }
    }

    public static float getYaw() {
        float yaw = (Killaura.target != null && Configs.moveFixAura) ? RotationUtils.getRotations(Killaura.target).getYaw() : mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else if (mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (mc.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (mc.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw;
    }
}