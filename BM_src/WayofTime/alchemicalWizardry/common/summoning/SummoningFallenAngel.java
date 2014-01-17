package WayofTime.alchemicalWizardry.common.summoning;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityFallenAngel;

public class SummoningFallenAngel extends SummoningHelper
{
	public SummoningFallenAngel(int id)
	{
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	public EntityLivingBase getEntity(World worldObj)
	{
		return new EntityFallenAngel(worldObj);
	}
}
