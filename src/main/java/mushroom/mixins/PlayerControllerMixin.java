package mushroom.mixins;

import mushroom.GUI.Configs;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ PlayerControllerMP.class })
public class PlayerControllerMixin {
    @Redirect(method = { "onPlayerDamageBlock" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getPlayerRelativeBlockHardness(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;)F"))
    public float onPlayerDamageBlock(final Block instance, final EntityPlayer playerIn, final World worldIn, final BlockPos pos) {
        float hardness = instance.getPlayerRelativeBlockHardness(playerIn, worldIn, pos);
        if (Configs.fastbreak) {
            hardness *= Configs.breakSpeed;
        }
        return hardness;
    }
}