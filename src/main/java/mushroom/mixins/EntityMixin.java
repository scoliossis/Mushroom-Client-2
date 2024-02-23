package mushroom.mixins;

import mushroom.Features.Combat.Killaura;
import mushroom.GUI.Configs;
import mushroom.Libs.RotationUtils;
import mushroom.Libs.events.MoveFlyingEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.DataWatcher;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Entity.class })
public class EntityMixin {
    @Shadow
    public float getCollisionBorderSize() {
        return 0;
    }

    @Shadow
    public double posX;
    @Shadow
    public double posY;
    @Shadow
    public double posZ;
    @Shadow
    public double motionX;
    @Shadow
    public double motionY;
    @Shadow
    public double motionZ;

    @Shadow
    protected DataWatcher dataWatcher;

    @Shadow
    public boolean isAirBorne;

    @Shadow
    public float rotationYaw;

    @Shadow
    protected boolean getFlag(int p_getFlag_1_) {
        return (this.dataWatcher.getWatchableObjectByte(0) & 1 << p_getFlag_1_) != 0;
    }



    @Overwrite
    public void moveFlying(float strafe, float forward, float friction) {
        final MoveFlyingEvent event = new MoveFlyingEvent(forward, strafe, friction, this.rotationYaw);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            return;
        }

        strafe = event.getStrafe();
        forward = event.getForward();
        friction = event.getFriction();

        float f = strafe * strafe + forward * forward;
        if (f >= 1.0E-4F) {
            f = MathHelper.sqrt_float(f);
            if (f < 1.0F) {
                f = 1.0F;
            }

            f = friction / f;
            strafe *= f;
            forward *= f;

            float f1 = MathHelper.sin(event.getYaw() * 3.1415927F / 180.0F);
            float f2 = MathHelper.cos(event.getYaw() * 3.1415927F / 180.0F);
            this.motionX += (strafe * f2 - forward * f1);
            this.motionZ += (forward * f2 + strafe * f1);
        }

    }


    @Overwrite
    public boolean isSprinting() {
        return this.getFlag(3);
    }
}