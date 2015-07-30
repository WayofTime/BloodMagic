package WayofTime.alchemicalWizardry.common.items.routing;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import WayofTime.alchemicalWizardry.api.RoutingFocusLogic;
import WayofTime.alchemicalWizardry.common.routing.RoutingFocusLogicLimitDefault;
import WayofTime.alchemicalWizardry.common.routing.RoutingFocusLogicLimitGlobal;
import WayofTime.alchemicalWizardry.common.routing.RoutingFocusLogicLimitIgnMeta;
import WayofTime.alchemicalWizardry.common.routing.RoutingFocusLogicLimitMatchNBT;
import WayofTime.alchemicalWizardry.common.routing.RoutingFocusLogicLimitModItems;

public class OutputRoutingFocus extends RoutingFocus implements ILimitedRoutingFocus
{	
	public OutputRoutingFocus()
	{
		super();
	}
	
	@Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);

        if (!(par1ItemStack.getTagCompound() == null))
        {
        	int limit = this.getRoutingFocusLimit(par1ItemStack);
        	if(limit > 0)
        	{
            	par3List.add(StatCollector.translateToLocal("tooltip.routingFocus.limit") + " " + limit);
        	}
        }
    }
	
	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		String addedString = "";
		
		switch(itemStack.getItemDamage())
		{
		case 0:
			addedString = "default";
			break;
		case 1:
			addedString = "modItem";
			break;
		case 2:
			addedString = "ignMeta";
			break;
		case 3:
			addedString = "matchNBT";
			break;
		case 4:
			addedString = "global";
			break;
		}
		
		return super.getUnlocalizedName() + "." + addedString;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List list)
    {
        for (int meta = 0; meta < 5; ++meta)
        {
            list.add(new ItemStack(id, 1, meta));
        }
    }
	
	@Override
	public RoutingFocusLogic getLogic(ItemStack itemStack)
	{
		if(itemStack != null)
		{
			switch(itemStack.getItemDamage())
			{
			case 0:
				return new RoutingFocusLogicLimitDefault(itemStack);
			case 1:
				return new RoutingFocusLogicLimitModItems(itemStack);
			case 2:
				return new RoutingFocusLogicLimitIgnMeta(itemStack);
			case 3:
				return new RoutingFocusLogicLimitMatchNBT(itemStack);
			case 4:
				return new RoutingFocusLogicLimitGlobal(itemStack);
			}
		}
		
		return new RoutingFocusLogic();
	}	
    
    public int getDefaultStackLimit(int damage)
    {
    	return 0;
    }
    
    public int getRoutingFocusLimit(ItemStack itemStack)
    {
    	if (!(itemStack.getTagCompound() == null))
        {
            return itemStack.getTagCompound().getInteger("stackLimit");
        } else
        {
            return getDefaultStackLimit(itemStack.getItemDamage());
        }
    }
    
    public void setRoutingFocusLimit(ItemStack itemStack, int amt)
    {
    	if ((itemStack.getTagCompound() == null))
        {
            itemStack.setTagCompound(new NBTTagCompound());   
        }
    	
    	itemStack.getTagCompound().setInteger("stackLimit", amt);
    }
}
