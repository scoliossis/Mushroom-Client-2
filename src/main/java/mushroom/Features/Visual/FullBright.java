package mushroom.Features.Visual;

import mushroom.GUI.Configs;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static mushroom.Libs.PlayerLib.mc;

public class FullBright {
    @SubscribeEvent
    public void RenderTickEvent(TickEvent.RenderTickEvent tick) {
        if (Configs.fullbright) {
            mc.gameSettings.gammaSetting = 100000.0f;
        }
    }
}
