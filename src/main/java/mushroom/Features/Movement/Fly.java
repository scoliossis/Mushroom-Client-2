package mushroom.Features.Movement;

import mushroom.GUI.Configs;
import mushroom.Libs.MovementLib;
import mushroom.Libs.PlayerLib;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static mushroom.Libs.PlayerLib.mc;

public class Fly {

    boolean flyon = false;
    private boolean placed;
    private boolean isFlying;
    private int ticks;
    private int stage;
    private int flyingTicks;
    private int distance;

    int hurttime = 0;
    double xmot = 0;
    double ymot = 0;
    double zmot = 0;
    boolean kb = false;
    @SubscribeEvent
    public void onPlayertick(TickEvent.ClientTickEvent e) {
        if (Configs.fly && PlayerLib.inGame()) {
            if (Configs.flymode == 0) {
                mc.thePlayer.motionY = 0;
                if (Configs.spoofground) {
                    mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(true));
                    mc.thePlayer.fallDistance = 0;
                }
                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.thePlayer.motionY += (double) Configs.flyverticlespeed;
                }
                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.thePlayer.motionY -= (double) Configs.flyverticlespeed;
                }
                MovementLib.setMotion((double) Configs.flyhorizonstalspeed);

                if (flyon != Configs.fly && Configs.toggletimeronfly) {
                    if (Configs.fly) {
                        Configs.timer = true;
                    }
                    else {
                        Configs.timer = false;
                    }
                }
            }

            else if (Configs.flymode == 1) {
                if (mc.thePlayer.hurtTime > hurttime && !kb) {
                    mc.thePlayer.motionX = xmot * Configs.flyhorizonstalspeed * 10;
                    mc.thePlayer.motionY = (double) Configs.flyverticlespeed;
                    mc.thePlayer.motionZ = zmot * Configs.flyhorizonstalspeed * 10;
                    kb = true;
                } else if (mc.thePlayer.hurtTime == 2 && kb) {
                    kb = false;
                    mc.thePlayer.motionY = -0.4;
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionZ = 0;
                }
                xmot = mc.thePlayer.motionX;
                ymot = mc.thePlayer.motionY;
                zmot = mc.thePlayer.motionZ;
                hurttime = mc.thePlayer.hurtTime;
            }
        }
        flyon = Configs.fly;
    }
}
