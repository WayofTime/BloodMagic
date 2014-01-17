package WayofTime.alchemicalWizardry.common.items.potion;

import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AverageLengtheningCatalyst extends LengtheningCatalyst
{
	public AverageLengtheningCatalyst(int id)
	{
		super(id, 2);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:AverageLengtheningCatalyst");
	}
}