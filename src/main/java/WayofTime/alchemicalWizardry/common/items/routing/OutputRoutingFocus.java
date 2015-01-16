package WayofTime.alchemicalWizardry.common.items.routing;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
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
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:HarvestGoddessSigil_deactivated");
    }
	
	public boolean doesItemMatch(ItemStack keyStack, ItemStack checkedStack)
	{
		return keyStack != null ? checkedStack != null && keyStack.areItemStacksEqual(keyStack, checkedStack) : false;
	}
}
