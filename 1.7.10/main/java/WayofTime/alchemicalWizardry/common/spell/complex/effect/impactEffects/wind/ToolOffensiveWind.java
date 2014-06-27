package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool.LeftClickEffect;

public class ToolOffensiveWind extends LeftClickEffect
{
	public ToolOffensiveWind(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public int onLeftClickEntity(ItemStack stack, EntityLivingBase attacked, EntityLivingBase weilder)
	{
		attacked.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionHeavyHeart.id,(int)(100*(2*this.powerUpgrades+1)*(1/(this.potencyUpgrades+1))),this.potencyUpgrades));
		
		return (int)(100*(0.5*this.potencyUpgrades+1)*(this.powerUpgrades+1)*Math.pow(0.85, costUpgrades));
	}
}
