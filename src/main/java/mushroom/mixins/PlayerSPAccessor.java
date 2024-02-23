package mushroom.mixins;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityPlayerSP.class })
public interface PlayerSPAccessor {
    @Accessor
    double getLastReportedPosX();

    @Accessor
    void setLastReportedPosX(final double p0);

    @Accessor
    double getLastReportedPosY();

    @Accessor
    void setLastReportedPosY(final double p0);

    @Accessor
    double getLastReportedPosZ();

    @Accessor
    void setLastReportedPosZ(final double p0);

    @Accessor
    float getLastReportedYaw();

    @Accessor
    void setLastReportedYaw(final float p0);

    @Accessor
    float getLastReportedPitch();

    @Accessor
    void setLastReportedPitch(final float p0);

    @Accessor
    void setServerSprintState(final boolean p0);

    @Accessor
    boolean getServerSprintState();

    @Accessor
    void setServerSneakState(final boolean p0);

    @Accessor
    boolean getServerSneakState();
}