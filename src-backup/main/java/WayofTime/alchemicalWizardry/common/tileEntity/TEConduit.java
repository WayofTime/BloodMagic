package WayofTime.alchemicalWizardry.common.tileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.spell.complex.SpellParadigm;

public class TEConduit extends TESpellBlock
{
    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
    }

    //Logic for the actual block is under here
    @Override
    public void updateEntity()
    {

    }

	@Override
	protected void applySpellChange(SpellParadigm parad) 
	{
		return;		
	}
}
