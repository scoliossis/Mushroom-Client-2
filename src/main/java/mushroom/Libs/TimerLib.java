package mushroom.Libs;

import mushroom.mixins.MinecraftAccessor;
import net.minecraft.util.Timer;

import static mushroom.Libs.PlayerLib.mc;

public class TimerLib {
    public static void setSpeed(final float speed) {
        getTimer().timerSpeed = speed;
    }

    public static void resetSpeed() {
        setSpeed(1.0f);
    }

    public static Timer getTimer() {
        return ((MinecraftAccessor)mc).getTimer();
    }
}