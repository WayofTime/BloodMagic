package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.client.renderer.texture.IconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MasterBloodOrb extends EnergyBattery
{
	public MasterBloodOrb(int id, int damage)
	{
		super(id, damage);
		orbLevel = 4;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:MasterBloodOrb");
	}
}
