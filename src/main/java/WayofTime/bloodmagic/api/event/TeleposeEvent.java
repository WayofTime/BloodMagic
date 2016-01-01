package WayofTime.bloodmagic.api.event;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/** Fired when a teleposer attempts to transpose two blocks. Use this to perform special cleanup or compensation,
 or cancel it entirely to prevent the transposition. */
@Cancelable
public class TeleposeEvent extends Event
{
    public final World initalWorld;
    public final BlockPos initialBlockPos;

    public final Block initialBlock;
    public final int initialMetadata;

    public final World finalWorld;
    public final BlockPos finalBlockPos;

    public final Block finalBlock;
    public final int finalMetadata;

    public TeleposeEvent(World initialWorld, BlockPos initialBlockPos, Block initialBlock, int initialMetadata, World finalWorld, BlockPos finalBlockPos, Block finalBlock, int finalMetadata)
    {
        this.initalWorld = initialWorld;
        this.initialBlockPos = initialBlockPos;
        this.initialBlock = initialBlock;
        this.initialMetadata = initialMetadata;

        this.finalWorld = finalWorld;
        this.finalBlockPos = finalBlockPos;
        this.finalBlock = finalBlock;
        this.finalMetadata = finalMetadata;
    }

    public TileEntity getInitialTile()
    {
        return initalWorld.getTileEntity(initialBlockPos);
    }

    public TileEntity getFinalTile()
    {
        return finalWorld.getTileEntity(finalBlockPos);
    }
}
