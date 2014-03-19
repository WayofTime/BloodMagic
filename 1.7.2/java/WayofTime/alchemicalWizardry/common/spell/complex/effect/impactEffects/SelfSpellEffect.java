package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class SelfSpellEffect implements ISelfSpellEffect
{
	protected int powerUpgrades;
	protected int potencyUpgrades;
	protected int costUpgrades;
	
	public SelfSpellEffect(int power, int potency, int cost)
	{
		this.powerUpgrades = power;
		this.potencyUpgrades = potency;
		this.costUpgrades = cost;
	}	
}
