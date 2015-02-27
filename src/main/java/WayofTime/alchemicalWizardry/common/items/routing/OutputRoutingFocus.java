package WayofTime.alchemicalWizardry.common.items.routing;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import WayofTime.alchemicalWizardry.api.RoutingFocusLogic;
import WayofTime.alchemicalWizardry.common.routing.RoutingFocusLogicLimitDefault;
import WayofTime.alchemicalWizardry.common.routing.RoutingFocusLogicLimitIgnMeta;
import WayofTime.alchemicalWizardry.common.routing.RoutingFocusLogicLimitMatchNBT;
import WayofTime.alchemicalWizardry.common.routing.RoutingFocusLogicLimitModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OutputRoutingFocus extends RoutingFocus implements ILimitedRoutingFocus
{
	IIcon modItemIcon;
	IIcon ignMetaIcon;
	IIcon matchNBTIcon;
	
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
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:OutputRoutingFocus");
        this.modItemIcon = iconRegister.registerIcon("AlchemicalWizardry:OutputRoutingFocusModItems");
        this.ignMetaIcon = iconRegister.registerIcon("AlchemicalWizardry:OutputRoutingFocusIgnMeta");
        this.matchNBTIcon = iconRegister.registerIcon("AlchemicalWizardry:OutputRoutingFocusMatchNBT");
    }
	
	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
		switch(damage)
		{
		case 0:
			return this.itemIcon;
		case 1:
			return this.modItemIcon;
		case 2:
			return this.ignMetaIcon;
		case 3:
			return this.matchNBTIcon;
		}
        return this.itemIcon;
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
		}
		
		return super.getUnlocalizedName() + "." + addedString;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List list)
    {
        for (int meta = 0; meta < 4; ++meta)
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
