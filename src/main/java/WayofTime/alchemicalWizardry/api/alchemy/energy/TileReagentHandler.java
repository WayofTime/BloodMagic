
package WayofTime.alchemicalWizardry.api.alchemy.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileReagentHandler extends TileEntity implements IReagentHandler
{
    protected ReagentContainer tank = new ReagentContainer(4000);

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        tank.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tank.writeToNBT(tag);
    }

    /* IReagentHandler */
    @Override
    public int fill(EnumFacing from, ReagentStack resource, boolean doFill)
    {
        return tank.fill(resource, doFill);
    }

    @Override
    public ReagentStack drain(EnumFacing from, ReagentStack resource, boolean doDrain)
    {
        if (resource == null || !resource.isReagentEqual(tank.getReagent()))
        {
            return null;
        }
        return tank.drain(resource.amount, doDrain);
    }

    @Override
    public ReagentStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Reagent reagent)
    {
        return true;
    }

    @Override
    public boolean canDrain(EnumFacing from, Reagent reagent)
    {
        return true;
    }

    @Override
    public ReagentContainerInfo[] getContainerInfo(EnumFacing from)
    {
        return new ReagentContainerInfo[]{tank.getInfo()};
    }
}