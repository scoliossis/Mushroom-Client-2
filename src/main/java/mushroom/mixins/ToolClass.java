package mushroom.mixins;

import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.item.ItemTool;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ItemTool.class })
public interface ToolClass {
    @Accessor("toolClass")
    String getToolClass();
}