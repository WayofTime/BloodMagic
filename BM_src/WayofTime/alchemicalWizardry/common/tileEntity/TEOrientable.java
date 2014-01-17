package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.common.block.IOrientable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TEOrientable extends TileEntity implements IOrientable
{
    protected ForgeDirection inputFace;
    protected ForgeDirection outputFace;

    public TEOrientable()
    {
        this.inputFace = ForgeDirection.DOWN;
        this.outputFace = ForgeDirection.UP;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.setInputDirection(ForgeDirection.getOrientation(par1NBTTagCompound.getInteger("inputFace")));
        this.setOutputDirection(ForgeDirection.getOrientation(par1NBTTagCompound.getInteger("outputFace")));
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("inputFace", TEOrientable.getIntForForgeDirection(this.getInputDirection()));
        par1NBTTagCompound.setInteger("outputFace", TEOrientable.getIntForForgeDirection(this.getOutputDirection()));
    }

    @Override
    public ForgeDirection getInputDirection()
    {
        return this.inputFace;
    }

    @Override
    public ForgeDirection getOutputDirection()
    {
        return this.outputFace;
    }

    @Override
    public void setInputDirection(ForgeDirection direction)
    {
        this.inputFace = direction;
    }

    @Override
    public void setOutputDirection(ForgeDirection direction)
    {
        this.outputFace = direction;
    }

    public static int getIntForForgeDirection(ForgeDirection direction)
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
}
