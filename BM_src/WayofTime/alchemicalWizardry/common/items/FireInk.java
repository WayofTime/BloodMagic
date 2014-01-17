package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FireInk extends Item
{
	public FireInk(int id)
	{
		super(id);
		maxStackSize = 1;
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:ElementalInkFire");
	}
}
