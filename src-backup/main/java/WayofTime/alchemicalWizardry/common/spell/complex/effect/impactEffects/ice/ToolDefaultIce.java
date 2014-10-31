package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.ice;


import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool.LeftClickEffect;


public class ToolDefaultIce extends LeftClickEffect
{
	public ToolDefaultIce(int power, int potency, int cost)
	{
		super(power, potency, cost);
	}


	@Override
	public int onLeftClickEntity(ItemStack stack, EntityLivingBase attacked, EntityLivingBase weilder) 
	{
		int duration = 200;


		attacked.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id,duration,this.powerUpgrades));


		return 0;
	}
}
