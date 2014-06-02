package WayofTime.alchemicalWizardry.common.spell.simple;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public abstract class HomSpell implements ISimpleSpell
{
    private int offensiveRangedEnergy;
    private int offensiveMeleeEnergy;
    private int defensiveEnergy;
    private int environmentalEnergy;

    public HomSpell()
    {
        //super(id);
        //this.setMaxStackSize(1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public abstract ItemStack onOffensiveRangedRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer);

    ;

    @Override
    public abstract ItemStack onOffensiveMeleeRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer);

    @Override
    public abstract ItemStack onDefensiveRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer);

    @Override
    public abstract ItemStack onEnvironmentalRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer);

    public int getOffensiveRangedEnergy()
    {
        return offensiveRangedEnergy;
    }

    public int getOffensiveMeleeEnergy()
    {
        return offensiveMeleeEnergy;
    }

    public int getDefensiveEnergy()
    {
        return defensiveEnergy;
    }

    public int getEnvironmentalEnergy()
    {
        return environmentalEnergy;
    }

    public void setEnergies(int offensiveRanged, int offensiveMelee, int defensive, int environmental)
    {
        this.offensiveRangedEnergy = offensiveRanged;
        this.offensiveMeleeEnergy = offensiveMelee;
        this.defensiveEnergy = defensive;
        this.environmentalEnergy = environmental;
    }

    public void setSpellParadigm(ItemStack itemStack, int paradigm)
    {
        if (itemStack.stackTagCompound == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        itemStack.stackTagCompound.setInteger("paradigm", paradigm);
    }

    public int getSpellParadigm(ItemStack itemStack)
    {
        if (itemStack.stackTagCompound == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        return (itemStack.stackTagCompound.getInteger("paradigm"));
    }

    //@Override
    public ItemStack useSpell(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        int paradigm = getSpellParadigm(par1ItemStack);

        if (par3EntityPlayer.isSneaking())
        {
            if (paradigm < 3)
            {
                this.setSpellParadigm(par1ItemStack, paradigm + 1);
            } else
            {
                this.setSpellParadigm(par1ItemStack, 0);
            }

            return par1ItemStack;
        }

        switch (paradigm)
        {
            case 0:
                return this.onOffensiveRangedRightClick(par1ItemStack, par2World, par3EntityPlayer);

            case 1:
                return this.onOffensiveMeleeRightClick(par1ItemStack, par2World, par3EntityPlayer);

            case 2:
                return this.onDefensiveRightClick(par1ItemStack, par2World, par3EntityPlayer);

            case 3:
                return this.onEnvironmentalRightClick(par1ItemStack, par2World, par3EntityPlayer);
        }

        return par1ItemStack;
    }

//	@Override
//    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
//    {
//        if (!(par1ItemStack.stackTagCompound == null))
//        {
//            if (!par1ItemStack.stackTagCompound.getString("ownerName").equals(""))
//            {
//                par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
//            }
//
//            par3List.add("Current paradigm: " + this.getSpellParadigm(par1ItemStack));
//        }
//    }

    public int getDimensionID(ItemStack itemStack)
    {
        if (itemStack.stackTagCompound == null)
        {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        return itemStack.stackTagCompound.getInteger("dimensionId");
    }
}
