package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.fire;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.MeleeSpellCenteredWorldEffect;

public class MeleeEnvironmentalFire extends MeleeSpellCenteredWorldEffect
{
	public MeleeEnvironmentalFire(int power, int potency, int cost) 
	{
		super(power, potency, cost);
		this.setRange(3*power + 2);
	}

	@Override
	public void onCenteredWorldEffect(EntityPlayer player, World world, int posX, int posY, int posZ) 
	{
		int radius = this.potencyUpgrades;
		
		for(int i=-radius; i<=radius; i++)
		{
			for(int j=-radius; j<=radius; j++)
			{
				for(int k=-radius; k<=radius; k++)
				{
					SpellHelper.evaporateWaterBlock(world, posX+i, posY+j, posZ+k);
				}
			}
		}
	}
}
