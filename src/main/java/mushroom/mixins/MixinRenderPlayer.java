package mushroom.mixins;

import mushroom.GUI.Configs;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static mushroom.Libs.PlayerLib.mc;

@Mixin({ RenderPlayer.class })
public abstract class MixinRenderPlayer extends MixinRenderLivingEntity {

    @Inject(method = { "preRenderCallback(Lnet/minecraft/client/entity/AbstractClientPlayer;F)V" }, at = { @At("HEAD") })
    public void onPreRenderCallback(final AbstractClientPlayer entitylivingbaseIn, final float partialTickTime, final CallbackInfo ci) {
        if (Configs.modelModifier) GlStateManager.scale(Configs.modelWidth, Configs.modelHeight, Configs.modelDepth);
    }

    private final ResourceLocation imposter = new ResourceLocation("mushroom/models/imposter.png");

    @Inject(method = {"getEntityTexture"}, at = {@At("HEAD")}, cancellable = true)
    public void getEntityTexture(AbstractClientPlayer entity, CallbackInfoReturnable<ResourceLocation> ci) {
        if (Configs.modelModifier && (Configs.newEveryoneModel || entity == mc.thePlayer)) {
            ci.setReturnValue(imposter);
        }
    }
}