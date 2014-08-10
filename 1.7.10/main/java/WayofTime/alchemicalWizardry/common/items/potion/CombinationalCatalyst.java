package WayofTime.alchemicalWizardry.common.items.potion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.alchemy.ICombinationalCatalyst;

public class CombinationalCatalyst extends Item implements ICombinationalCatalyst
{
	public CombinationalCatalyst()
	{
		super();
		this.setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:CombinationalCatalyst");
    }
}
