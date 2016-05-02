package WayofTime.bloodmagic.tile;

import lombok.Getter;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import WayofTime.bloodmagic.api.Constants;

@Getter
public class TileAlchemyTable extends TileInventory implements ISidedInventory, ITickable
{
    public EnumFacing direction = EnumFacing.NORTH;
    public boolean isSlave = false;

    public BlockPos connectedPos = BlockPos.ORIGIN;

    public TileAlchemyTable()
    {
        super(1, "alchemyTable");
    }

    public void setInitialTableParameters(EnumFacing direction, boolean isSlave, BlockPos connectedPos)
    {
        this.isSlave = isSlave;
        this.connectedPos = connectedPos;

        if (!isSlave)
        {
            this.direction = direction;
        }
    }

    public boolean isInvisible()
    {
        return isSlave();
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        isSlave = tag.getBoolean("isSlave");
        direction = EnumFacing.getFront(tag.getInteger(Constants.NBT.DIRECTION));
        connectedPos = new BlockPos(tag.getInteger(Constants.NBT.X_COORD), tag.getInteger(Constants.NBT.Y_COORD), tag.getInteger(Constants.NBT.Z_COORD));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setBoolean("isSlave", isSlave);
        tag.setInteger(Constants.NBT.DIRECTION, direction.getIndex());
        tag.setInteger(Constants.NBT.X_COORD, pos.getX());
        tag.setInteger(Constants.NBT.Y_COORD, pos.getY());
        tag.setInteger(Constants.NBT.Z_COORD, pos.getZ());
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return false;
    }

    @Override
    public void update()
    {
        //TODO: Stuff and things
    }
}
