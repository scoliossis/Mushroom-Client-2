package mushroom.Features.Combat;

import mushroom.Features.Movement.AntiVoid;
import mushroom.GUI.Configs;
import mushroom.Libs.MovementLib;
import mushroom.Libs.PlayerLib;
import mushroom.Libs.TimerLib;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.lang.reflect.InvocationTargetException;

import static mushroom.Libs.PlayerLib.mc;

public class Velocity {

    int hurttime = 0;
    double xmot = 0;
    double ymot = 0;
    double zmot = 0;
    boolean kb = false;

    @SubscribeEvent
    public void onPlayertick(TickEvent.ClientTickEvent e) {
        if (Configs.velocity && PlayerLib.inGame() && !AntiVoid.isBlinking()) {
            double horzontalmultiplier = Configs.horizontalmodifier;
            double verticalmultiplier = Configs.verticalmodifier;

            if (mc.thePlayer.hurtTime > hurttime) {
                kb=true;
                switch (Configs.velocitymode) {

                    // vanilla
                    case 0:
                        mc.thePlayer.motionX = xmot - (mc.thePlayer.motionX * horzontalmultiplier);
                        mc.thePlayer.motionY *= verticalmultiplier;
                        mc.thePlayer.motionZ = zmot - (mc.thePlayer.motionZ * horzontalmultiplier);


                    // damage boost
                    case 2:
                        mc.thePlayer.motionX = (xmot - (mc.thePlayer.motionX * horzontalmultiplier)) * ((double) Configs.velocityboostspeed / 100);
                        mc.thePlayer.motionY *= verticalmultiplier;
                        mc.thePlayer.motionZ = (zmot - (mc.thePlayer.motionZ * horzontalmultiplier)) * ((double) Configs.velocityboostspeed / 100);

                    case 3:
                        mc.thePlayer.motionX = xmot - (mc.thePlayer.motionX * horzontalmultiplier);
                        mc.thePlayer.motionY *= verticalmultiplier;
                        mc.thePlayer.motionZ = zmot - (mc.thePlayer.motionZ * horzontalmultiplier);

                    case 4:
                        mc.thePlayer.motionX = xmot - (mc.thePlayer.motionX * horzontalmultiplier);
                        mc.thePlayer.motionY *= verticalmultiplier;
                        mc.thePlayer.motionZ = zmot - (mc.thePlayer.motionZ * horzontalmultiplier);
                }
            } else if (Configs.velocitymode != 1) {
                xmot = mc.thePlayer.motionX;
                ymot = mc.thePlayer.motionY;
                zmot = mc.thePlayer.motionZ;
            }
            if (Configs.velocitymode == 1 && mc.thePlayer.hurtTime == 10-Configs.velocitydelay && kb) {
                kb=false;
                mc.thePlayer.motionX *= horzontalmultiplier;
                mc.thePlayer.motionZ *= horzontalmultiplier;
            }
            else if (Configs.velocitymode == 3 && mc.thePlayer.hurtTime == 10-Configs.velocityboostdelay && kb) {
                kb=false;
                mc.thePlayer.motionX *= (double) Configs.delayedboostspeed;
                mc.thePlayer.motionZ *= (double) Configs.delayedboostspeed;
            }
            else if (Configs.velocitymode == 4 && mc.thePlayer.hurtTime != 0) {
                MovementLib.setMotion((double) Configs.velobhopspeed);
                if (Configs.velotoggletimer) {
                    TimerLib.setSpeed((float) Configs.velotimerspeed);
                }
            }
            if (hurttime != mc.thePlayer.hurtTime && mc.thePlayer.hurtTime == 0 && Configs.velocitymode == 4 && Configs.velotoggletimer) {
                TimerLib.resetSpeed();
            }

            hurttime = mc.thePlayer.hurtTime;
        }
    }
}
