package WayofTime.alchemicalWizardry.common.tileEntity;

import WayofTime.alchemicalWizardry.api.spell.SpellParadigm;
import net.minecraft.nbt.NBTTagCompound;

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
