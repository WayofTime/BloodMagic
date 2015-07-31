package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.ModBlocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TESpectralBlock extends TileEntity
{
    private int ticksRemaining;

    public TESpectralBlock()
    {
        ticksRemaining = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        ticksRemaining = par1NBTTagCompound.getInteger("ticksRemaining");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);

        par1NBTTagCompound.setInteger("ticksRemaining", ticksRemaining);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

//        if (worldObj.isRemote)
        {
//            return;
        }

        this.ticksRemaining--;

        if (this.ticksRemaining <= 0)
        {
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        }
    }

    public static boolean createSpectralBlockAtLocation(World world, int x, int y, int z, int duration)
    {
        if (!world.isAirBlock(x, y, z))
        {
            return false;
        }
        world.setBlock(x, y, z, ModBlocks.spectralBlock);
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TESpectralBlock)
        {
            ((TESpectralBlock) tile).setDuration(duration);
            return true;
        }

        return false;
    }

    public void setDuration(int dur)
    {
        this.ticksRemaining = dur;
    }

    public void resetDuration(int dur)
    {
        if (this.ticksRemaining < dur)
        {
            this.ticksRemaining = dur;
        }
    }
}
