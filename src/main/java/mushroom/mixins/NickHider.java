package mushroom.mixins;

import mushroom.GUI.Configs;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ FontRenderer.class })
public abstract class NickHider
{
    @Shadow
    protected abstract void renderStringAtPos(final String p0, final boolean p1);

    @Shadow
    public abstract int getStringWidth(final String p0);

    @Shadow
    public abstract int getCharWidth(final char p0);

    @Inject(method = { "renderStringAtPos" }, at = { @At("HEAD") }, cancellable = true)
    private void renderString(final String text, final boolean shadow, final CallbackInfo ci) {
        if (Configs.nickhider && text.contains(Minecraft.getMinecraft().getSession().getUsername())) {
            ci.cancel();
            this.renderStringAtPos(text.replaceAll(Minecraft.getMinecraft().getSession().getUsername(), Configs.fakename), shadow);
        }
    }

    @Inject(method = { "getStringWidth" }, at = { @At("RETURN") }, cancellable = true)
    private void getStringWidth(final String text, final CallbackInfoReturnable<Integer> cir) {
        if (Configs.nickhider && text != null && text.contains(Minecraft.getMinecraft().getSession().getUsername())) {
            cir.setReturnValue(this.getStringWidth(text.replaceAll(Minecraft.getMinecraft().getSession().getUsername(), Configs.fakename)));
        }
    }
}