package mushroom.mixins;

import java.awt.Color;

import mushroom.Features.Visual.CoolMainMenu;
import mushroom.GUI.ClickGUI;
import mushroom.GUI.Configs;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiMainMenu.class})
public abstract class MixinGuiMainMenu extends GuiScreen {
    @Inject(method="initGui", at={@At(value="RETURN")})
    private void initGui(CallbackInfo callbackInfo) {
        if (Configs.sillymainmenu) {
            this.mc.displayGuiScreen(new CoolMainMenu());
        }
    }
}
