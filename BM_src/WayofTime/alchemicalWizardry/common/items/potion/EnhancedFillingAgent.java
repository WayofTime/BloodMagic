package WayofTime.alchemicalWizardry.common.items.potion;

import java.util.Random;

import net.minecraft.client.renderer.texture.IconRegister;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EnhancedFillingAgent extends WeakFillingAgent
{
	public EnhancedFillingAgent(int par1)
	{
		super(par1);
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}

	@Override
	public int getFilledAmountForPotionNumber(int potionEffects)
	{
		new Random();

		if (potionEffects == 0)
		{
			return 8;
		}

		//if(potionEffects >=1 && potionEffects<=5)
		{
			switch (potionEffects)
			{
			case 1:
				return 6;

			case 2:
				return 4;

			case 3:
				return 3;

			case 4:
				return 2;

			case 5:
				return 2;
			}
		}
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:EnhancedFillingAgent");
	}
}
