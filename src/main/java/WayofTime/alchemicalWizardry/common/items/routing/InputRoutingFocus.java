package WayofTime.alchemicalWizardry.common.items.routing;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import codechicken.lib.render.TextureUtils.IIconRegister;

public class InputRoutingFocus extends RoutingFocus
{
	public InputRoutingFocus()
	{
		super();
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:InputRoutingFocus");
    }
}
