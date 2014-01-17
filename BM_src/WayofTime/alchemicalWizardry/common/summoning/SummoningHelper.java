package WayofTime.alchemicalWizardry.common.summoning;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.EntityAirElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityBileDemon;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityBoulderFist;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityEarthElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityFallenAngel;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityFireElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityHolyElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityIceDemon;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityLowerGuardian;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityShade;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityShadeElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntitySmallEarthGolem;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityWaterElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityWingedFireDemon;

public class SummoningHelper
{
	private int id;

	public SummoningHelper(int id)
	{
		this.id = id;
	}
	public EntityLivingBase getEntity(World worldObj)
	{
		if (id == AlchemicalWizardry.entityFallenAngelID)
		{
			return new EntityFallenAngel(worldObj);
		}

		if (id == AlchemicalWizardry.entityLowerGuardianID)
		{
			return new EntityLowerGuardian(worldObj);
		}

		if (id == AlchemicalWizardry.entityBileDemonID)
		{
			return new EntityBileDemon(worldObj);
		}

		if (id == AlchemicalWizardry.entityWingedFireDemonID)
		{
			return new EntityWingedFireDemon(worldObj);
		}

		if (id == AlchemicalWizardry.entitySmallEarthGolemID)
		{
			return new EntitySmallEarthGolem(worldObj);
		}

		if (id == AlchemicalWizardry.entityIceDemonID)
		{
			return new EntityIceDemon(worldObj);
		}

		if (id == AlchemicalWizardry.entityBoulderFistID)
		{
			return new EntityBoulderFist(worldObj);
		}

		if (id == AlchemicalWizardry.entityShadeID)
		{
			return new EntityShade(worldObj);
		}

		if (id == AlchemicalWizardry.entityAirElementalID)
		{
			return new EntityAirElemental(worldObj);
		}

		if (id == AlchemicalWizardry.entityWaterElementalID)
		{
			return new EntityWaterElemental(worldObj);
		}

		if (id == AlchemicalWizardry.entityEarthElementalID)
		{
			return new EntityEarthElemental(worldObj);
		}

		if (id == AlchemicalWizardry.entityFireElementalID)
		{
			return new EntityFireElemental(worldObj);
		}

		if (id == AlchemicalWizardry.entityShadeElementalID)
		{
			return new EntityShadeElemental(worldObj);
		}

		if (id == AlchemicalWizardry.entityHolyElementalID)
		{
			return new EntityHolyElemental(worldObj);
		}

		return new EntityPig(worldObj);
	}

	public int getSummoningHelperID()
	{
		return id;
	}
}
