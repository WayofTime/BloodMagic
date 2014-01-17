package WayofTime.alchemicalWizardry.common.block;

import net.minecraft.block.BlockStationary;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LifeEssenceStill extends BlockStationary
{
	protected LifeEssenceStill(int par1)
	{
		super(par1, Material.water);
		blockHardness = 100.0F;
		setLightOpacity(3);
		//this.setLightValue(10);
		disableStats();
		//setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		theIcon = new Icon[]
				{
				iconRegister.registerIcon("AlchemicalWizardry:lifeEssenceStill"),
				iconRegister.registerIcon("AlchemicalWizardry:lifeEssenceFlowing")
				};
	}
}