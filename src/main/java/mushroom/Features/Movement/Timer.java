package mushroom.Features.Movement;

import mushroom.GUI.Configs;
import mushroom.Libs.TimerLib;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.InvocationTargetException;

public class Timer {

    boolean timeron = false;
    float timertime = 1;
    @SubscribeEvent
    public void tick(TickEvent.PlayerTickEvent e) {
        if (timeron != Configs.timer || timertime != Configs.timerspeed) {
            if (Configs.timer) {
                TimerLib.setSpeed(Configs.timerspeed);
            }
            else {
                TimerLib.resetSpeed();
            }
        }
        timeron = Configs.timer;
        timertime = Configs.timerspeed;
    }
}
