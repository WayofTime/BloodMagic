package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public abstract class LeftClickEffect implements ILeftClickEffect
{
	protected int powerUpgrades;
	protected int potencyUpgrades;
	protected int costUpgrades;
	
	public LeftClickEffect(int power, int potency, int cost)
	{
		this.powerUpgrades = power;
		this.potencyUpgrades = potency;
		this.costUpgrades = cost;
	}
	
	@Override
	public abstract int onLeftClickEntity(ItemStack stack, EntityLivingBase attacked, EntityLivingBase weilder);
}
