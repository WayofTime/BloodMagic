package WayofTime.alchemicalWizardry.common.items.routing;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import WayofTime.alchemicalWizardry.api.RoutingFocusLogic;
import WayofTime.alchemicalWizardry.api.RoutingFocusLogicModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OutputRoutingFocus extends RoutingFocus
{
	public OutputRoutingFocus()
	{
		super();
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:OutputRoutingFocus");
    }
	
	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
		switch(damage)
		{
		case 0:
			return this.itemIcon;
		}
        return this.itemIcon;
    }
	
	public RoutingFocusLogic getLogic(int damage)
	{
		switch(damage)
		{
		case 0:
			return new RoutingFocusLogic();
		case 1:
			return new RoutingFocusLogicModItems();
		}
		return new RoutingFocusLogic();
	}
}
