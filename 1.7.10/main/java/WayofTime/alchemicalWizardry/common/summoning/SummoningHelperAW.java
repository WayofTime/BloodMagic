package WayofTime.alchemicalWizardry.common.summoning;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.summoningRegistry.SummoningHelper;
import WayofTime.alchemicalWizardry.common.EntityAirElemental;
import WayofTime.alchemicalWizardry.common.entity.mob.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.world.World;

public class SummoningHelperAW extends SummoningHelper
{
    public SummoningHelperAW(int id)
    {
        super(id);
    }

    public EntityLivingBase getEntity(World worldObj)
    {
        if (this.id == AlchemicalWizardry.entityFallenAngelID)
        {
            return new EntityFallenAngel(worldObj);
        }

        if (this.id == AlchemicalWizardry.entityLowerGuardianID)
        {
            return new EntityLowerGuardian(worldObj);
        }

        if (this.id == AlchemicalWizardry.entityBileDemonID)
        {
            return new EntityBileDemon(worldObj);
        }

        if (this.id == AlchemicalWizardry.entityWingedFireDemonID)
        {
            return new EntityWingedFireDemon(worldObj);
        }

        if (this.id == AlchemicalWizardry.entitySmallEarthGolemID)
        {
            return new EntitySmallEarthGolem(worldObj);
        }

        if (this.id == AlchemicalWizardry.entityIceDemonID)
        {
            return new EntityIceDemon(worldObj);
        }

        if (this.id == AlchemicalWizardry.entityBoulderFistID)
        {
            return new EntityBoulderFist(worldObj);
        }

        if (this.id == AlchemicalWizardry.entityShadeID)
        {
            return new EntityShade(worldObj);
        }

        if (this.id == AlchemicalWizardry.entityAirElementalID)
        {
            return new EntityAirElemental(worldObj);
        }

        if (this.id == AlchemicalWizardry.entityWaterElementalID)
        {
            return new EntityWaterElemental(worldObj);
        }

        if (this.id == AlchemicalWizardry.entityEarthElementalID)
        {
            return new EntityEarthElemental(worldObj);
        }

        if (this.id == AlchemicalWizardry.entityFireElementalID)
        {
            return new EntityFireElemental(worldObj);
        }

        if (this.id == AlchemicalWizardry.entityShadeElementalID)
        {
            return new EntityShadeElemental(worldObj);
        }

        if (this.id == AlchemicalWizardry.entityHolyElementalID)
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
