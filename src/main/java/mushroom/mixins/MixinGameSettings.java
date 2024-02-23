package mushroom.mixins;

import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GameSettings.class})
public class MixinGameSettings {
    @Inject(method = {"setOptionKeyBinding"}, at = {@At("RETURN")})
    public void setOptionKeyBinding(CallbackInfo var1) {
        // pretend i made keybinds
    }
}
