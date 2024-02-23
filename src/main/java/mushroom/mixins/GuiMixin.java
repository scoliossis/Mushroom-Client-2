package mushroom.mixins;

import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Gui.class })
public abstract class GuiMixin {
    @Shadow
    public static void drawRect(final int left, final int top, final int right, final int bottom, final int color) {
    }

    @Shadow
    protected abstract void drawGradientRect(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);

    @Shadow
    public abstract void drawTexturedModalRect(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5);
}