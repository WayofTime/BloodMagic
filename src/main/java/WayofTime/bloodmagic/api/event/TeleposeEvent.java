package WayofTime.bloodmagic.api.event;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import WayofTime.bloodmagic.api.BlockStack;

/**
 * Fired when a teleposer attempts to transpose two blocks. Use this to perform
 * special cleanup or compensation, or cancel it entirely to prevent the
 * transposition.
 */
@Cancelable
public class TeleposeEvent extends Event
{
    public final World initalWorld;
    public final BlockPos initialBlockPos;
    public final BlockStack initialStack;

    public final World finalWorld;
    public final BlockPos finalBlockPos;
    public final BlockStack finalStack;

    public TeleposeEvent(World initialWorld, BlockPos initialBlockPos, World finalWorld, BlockPos finalBlockPos)
    {
        this.initalWorld = initialWorld;
        this.initialBlockPos = initialBlockPos;
        this.initialStack = BlockStack.getStackFromPos(initialWorld, initialBlockPos);

        this.finalWorld = finalWorld;
        this.finalBlockPos = finalBlockPos;
        this.finalStack = BlockStack.getStackFromPos(finalWorld, finalBlockPos);
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
