package WayofTime.alchemicalWizardry.common.items.potion;

import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MundaneLengtheningCatalyst extends LengtheningCatalyst
{
	public MundaneLengtheningCatalyst(int id)
	{
		super(id, 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:MundaneLengtheningCatalyst");
	}
}
