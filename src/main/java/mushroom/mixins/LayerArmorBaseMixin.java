package mushroom.mixins;

import mushroom.GUI.Configs;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static mushroom.Libs.PlayerLib.mc;

@Mixin({LayerArmorBase.class})
public class LayerArmorBaseMixin {

    @Inject(method = {"doRenderLayer"}, at = {@At("HEAD")}, cancellable = true)
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final CallbackInfo ci) {
        if (Configs.modelModifier && (Configs.newEveryoneModel || entitylivingbaseIn == mc.thePlayer)) {
            ci.cancel();
        }
    }
}