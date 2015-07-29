package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import WayofTime.alchemicalWizardry.common.NewPacketHandler;
import WayofTime.alchemicalWizardry.common.block.IOrientable;

public class TEOrientable extends TileEntity implements IOrientable
{
    protected EnumFacing inputFace;
    protected EnumFacing outputFace;

    public TEOrientable()
    {
        this.inputFace = EnumFacing.DOWN;
        this.outputFace = EnumFacing.UP;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.setInputDirection(EnumFacing.getFront(par1NBTTagCompound.getInteger("inputFace")));
        this.setOutputDirection(EnumFacing.getFront(par1NBTTagCompound.getInteger("outputFace")));
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("inputFace", TEOrientable.getIntForEnumFacing(this.getInputDirection()));
        par1NBTTagCompound.setInteger("outputFace", TEOrientable.getIntForEnumFacing(this.getOutputDirection()));
    }

    @Override
    public EnumFacing getInputDirection()
    {
        return this.inputFace;
    }

    @Override
    public EnumFacing getOutputDirection()
    {
        return this.outputFace;
    }

    @Override
    public void setInputDirection(EnumFacing direction)
    {
        this.inputFace = direction;
    }

    @Override
    public void setOutputDirection(EnumFacing direction)
    {
        this.outputFace = direction;
    }

    public static int getIntForEnumFacing(EnumFacing direction)
    {
        switch (direction)
        {
            case DOWN:
                return 0;

            case UP:
                return 1;

            case NORTH:
                return 2;

            case SOUTH:
                return 3;

            case WEST:
                return 4;

            case EAST:
                return 5;

            default:
                return 0;
        }
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return NewPacketHandler.getPacket(this);
    }


    public boolean isSideRendered(EnumFacing side)
    {
        if (side.equals(this.getInputDirection()) || side.equals(this.getOutputDirection()))
        {
            return true;
        }
        return false;
    }

    public String getResourceLocationForMeta(int meta)
    {
        return "";
    }
}
