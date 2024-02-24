package mushroom.mixins;

import mushroom.Libs.events.BlockChangeEvent;
import mushroom.Libs.events.ChunkLoadEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public abstract class ChunkMixin {
    @Shadow
    public abstract IBlockState getBlockState(BlockPos paramBlockPos);


    @Inject(method = "setBlockState", at = @At("HEAD"))
    public void onBlockSet(BlockPos pos, IBlockState state, CallbackInfoReturnable<IBlockState> cir) {
        IBlockState old = getBlockState(pos);
        if (state != old) MinecraftForge.EVENT_BUS.post(new BlockChangeEvent(pos, old, state));
    }

    @Inject(method = "fillChunk", at = @At("RETURN"))
    public void onFillChunk(byte[] p_177439_1_, int p_177439_2_, boolean p_177439_3_, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new ChunkLoadEvent((Chunk) (Object) this));
    }
}