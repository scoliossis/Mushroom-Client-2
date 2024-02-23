package mushroom.mixins;

import mushroom.GUI.Configs;
import mushroom.Libs.PlayerLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;


@Mixin({ AbstractClientPlayer.class })
public class AbstractClientPlayerMixin {


    private static ResourceLocation getCape() {
        String[] capes = {"2011 Minecon", "2012 Minecon", "2013 Minecon", "2015 Minecon", "2016 Minecon", "Silly Cape", "women kissing", "wtf", "mushroom", "anime girl"};
        return new ResourceLocation("mushroom/capes/"+ capes[Configs.caperNum] + ".png");
    }

    @Inject(method = { "getLocationCape" }, at = { @At("RETURN") }, cancellable = true)
    public void getLocationCape(final CallbackInfoReturnable<ResourceLocation> cir) {
        if (Configs.capes) {
            cir.setReturnValue(getCape());
        }
    }
}