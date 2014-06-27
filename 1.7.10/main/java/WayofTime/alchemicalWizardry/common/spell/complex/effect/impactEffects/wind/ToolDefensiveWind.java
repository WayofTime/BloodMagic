package WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.wind;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.impactEffects.tool.LeftClickEffect;

public class ToolDefensiveWind extends LeftClickEffect
{

	public ToolDefensiveWind(int power, int potency, int cost) 
	{
		super(power, potency, cost);
	}

	@Override
	public int onLeftClickEntity(ItemStack stack, EntityLivingBase attacked, EntityLivingBase weilder) 
	{
		Vec3 vec = weilder.getLookVec();
		vec.yCoord = 0;
		vec.normalize();
		
		float velocity = 0.5f*(1+this.powerUpgrades*0.8f);
		float ratio = 0.1f + 0.3f*this.potencyUpgrades;
		
		attacked.motionX += vec.xCoord*velocity;
		attacked.motionY += velocity*ratio;
		attacked.motionZ += vec.zCoord*velocity;
		
		return 0;
	}
}
