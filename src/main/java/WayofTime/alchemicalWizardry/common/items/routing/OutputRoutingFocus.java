package WayofTime.alchemicalWizardry.common.items.routing;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import WayofTime.alchemicalWizardry.api.RoutingFocusLogic;
import WayofTime.alchemicalWizardry.api.RoutingFocusLogicIgnMeta;
import WayofTime.alchemicalWizardry.api.RoutingFocusLogicMatchNBT;
import WayofTime.alchemicalWizardry.api.RoutingFocusLogicModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OutputRoutingFocus extends RoutingFocus
{
	IIcon modItemIcon;
	public OutputRoutingFocus()
	{
		super();
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:OutputRoutingFocus");
        this.modItemIcon = iconRegister.registerIcon("AlchemicalWizardry:OutputRoutingFocusModItems");
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
	public RoutingFocusLogic getLogic(int damage)
	{
		switch(damage)
		{
		case 0:
			return new RoutingFocusLogic();
		case 1:
			return new RoutingFocusLogicModItems();
		case 2:
			return new RoutingFocusLogicIgnMeta();
		case 3:
			return new RoutingFocusLogicMatchNBT();
		}
		return new RoutingFocusLogic();
	}
}
