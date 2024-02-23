package mushroom.mixins;

import mushroom.GUI.Configs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ EntityPlayer.class })
public class PlayerMixin extends EntityMixin {

    @Override
    public float getCollisionBorderSize() {
        return Configs.hitboxes ? 0.1f * (Configs.hitboxesExpand) : 0.1f;
    }


}