package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.ModBlocks;

public class TESpectralBlock extends TileEntity implements IUpdatePlayerListBox
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
    public void update()
    {
        if (worldObj.isRemote)
        {
            return;
        }

        this.ticksRemaining--;

        if (this.ticksRemaining <= 0)
        {
            worldObj.setBlockToAir(pos);
        }
    }

    public static boolean createSpectralBlockAtLocation(World world, BlockPos pos, int duration)
    {
        if (!world.isAirBlock(pos))
        {
            return false;
        }
        
        world.setBlockState(pos, ModBlocks.spectralBlock.getDefaultState());
        TileEntity tile = world.getTileEntity(pos);
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
