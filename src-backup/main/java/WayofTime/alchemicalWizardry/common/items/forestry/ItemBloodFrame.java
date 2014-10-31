package WayofTime.alchemicalWizardry.common.items.forestry;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBloodFrame extends EnergyItems //implements IHiveFrame
{
    public ItemBloodFrame()
    {
        super();
        this.maxStackSize = 1;
        this.setMaxDamage(10);
        //setMaxDamage(1000);
        setEnergyUsed(3000);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Stirs bees into a frenzy.");

        if (!(par1ItemStack.stackTagCompound == null))
        {
            par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:BloodFrame");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);
        
        if(par1ItemStack.getItemDamage()>0)
        {
        	EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed());
        	par1ItemStack.setItemDamage(par1ItemStack.getItemDamage()-1);
        }
        
        return par1ItemStack;
    }

    /**TODO Bee Stuff
	@Override
	public float getTerritoryModifier(IBeeGenome genome, float currentModifier) 
	{
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier) 
	{
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public float getLifespanModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier) 
	{
		// TODO Auto-generated method stub
		return 0.0001f;
	}

	@Override
	public float getProductionModifier(IBeeGenome genome, float currentModifier) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloweringModifier(IBeeGenome genome, float currentModifier) 
	{
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public float getGeneticDecay(IBeeGenome genome, float currentModifier) 
	{
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean isSealed() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSelfLighted() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSunlightSimulated() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHellish() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack frameUsed(IBeeHousing housing, ItemStack frame, IBee queen, int wear) 
	{
		// TODO Auto-generated method stub
		if(EnergyItems.canSyphonInContainer(frame, getEnergyUsed()*wear))
		{
			EnergyItems.syphonWhileInContainer(frame, getEnergyUsed()*wear);
			return frame;
		}else
		{
			frame.setItemDamage(frame.getItemDamage() + wear);
			if(frame.getItemDamage()>=frame.getMaxDamage())
			{
				return null;
			}
			return frame;
		}

	}
	
	*/

}
