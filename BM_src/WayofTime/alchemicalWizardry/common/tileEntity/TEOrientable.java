package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import WayofTime.alchemicalWizardry.common.block.IOrientable;

public class TEOrientable extends TileEntity implements IOrientable
{
	protected ForgeDirection inputFace;
	protected ForgeDirection outputFace;

	public TEOrientable()
	{
		inputFace = ForgeDirection.DOWN;
		outputFace = ForgeDirection.UP;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		setInputDirection(ForgeDirection.getOrientation(par1NBTTagCompound.getInteger("inputFace")));
		setOutputDirection(ForgeDirection.getOrientation(par1NBTTagCompound.getInteger("outputFace")));
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("inputFace", TEOrientable.getIntForForgeDirection(getInputDirection()));
		par1NBTTagCompound.setInteger("outputFace", TEOrientable.getIntForForgeDirection(getOutputDirection()));
	}

	@Override
	public ForgeDirection getInputDirection()
	{
		return inputFace;
	}

	@Override
	public ForgeDirection getOutputDirection()
	{
		return outputFace;
	}

	@Override
	public void setInputDirection(ForgeDirection direction)
	{
		inputFace = direction;
	}

	@Override
	public void setOutputDirection(ForgeDirection direction)
	{
		outputFace = direction;
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
