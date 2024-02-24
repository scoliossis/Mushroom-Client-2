package mushroom.Libs.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;

public class BlockChangeEvent extends Event {
    public BlockPos pos;
    public IBlockState old;
    public IBlockState update;

    public BlockChangeEvent(@Nonnull BlockPos pos, @Nonnull IBlockState old, @Nonnull IBlockState update) {
        this.pos = pos;
        this.old = old;
        this.update = update;
    }
}