package mushroom.Libs;

import net.minecraft.util.MathHelper;

public class Rotations {
    private float yaw;
    private float pitch;

    public Rotations(final float yaw, final float pitch) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public Rotations setPitch(final float pitch) {
        this.pitch = pitch;
        return this;
    }

    public Rotations setYaw(final float yaw) {
        this.yaw = yaw;
        return this;
    }

    public Rotations wrap() {
        this.yaw = MathHelper.wrapAngleTo180_float(this.yaw);
        this.pitch = MathHelper.wrapAngleTo180_float(this.pitch);
        return this;
    }
}
