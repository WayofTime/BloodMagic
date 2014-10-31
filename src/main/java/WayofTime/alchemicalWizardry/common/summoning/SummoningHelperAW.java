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
    public SummoningHelperAW(String id)
    {
        super(id);
    }

    public EntityLivingBase getEntity(World worldObj)
    {
        if (this.id.equals(AlchemicalWizardry.entityFallenAngelID))
        {
            return new EntityFallenAngel(worldObj);
        }

        if (this.id.equals(AlchemicalWizardry.entityLowerGuardianID))
        {
            return new EntityLowerGuardian(worldObj);
        }

        if (this.id.equals(AlchemicalWizardry.entityBileDemonID))
        {
            return new EntityBileDemon(worldObj);
        }

        if (this.id.equals(AlchemicalWizardry.entityWingedFireDemonID))
        {
            return new EntityWingedFireDemon(worldObj);
        }

        if (this.id.equals(AlchemicalWizardry.entitySmallEarthGolemID))
        {
            return new EntitySmallEarthGolem(worldObj);
        }

        if (this.id.equals(AlchemicalWizardry.entityIceDemonID))
        {
            return new EntityIceDemon(worldObj);
        }

        if (this.id.equals(AlchemicalWizardry.entityBoulderFistID))
        {
            return new EntityBoulderFist(worldObj);
        }

        if (this.id.equals(AlchemicalWizardry.entityShadeID))
        {
            return new EntityShade(worldObj);
        }

        if (this.id.equals(AlchemicalWizardry.entityAirElementalID))
        {
            return new EntityAirElemental(worldObj);
        }

        if (this.id.equals(AlchemicalWizardry.entityWaterElementalID))
        {
            return new EntityWaterElemental(worldObj);
        }

        if (this.id.equals(AlchemicalWizardry.entityEarthElementalID))
        {
            return new EntityEarthElemental(worldObj);
        }

        if (this.id.equals(AlchemicalWizardry.entityFireElementalID))
        {
            return new EntityFireElemental(worldObj);
        }

        if (this.id.equals(AlchemicalWizardry.entityShadeElementalID))
        {
            return new EntityShadeElemental(worldObj);
        }

        if (this.id.equals(AlchemicalWizardry.entityHolyElementalID))
        {
            return new EntityHolyElemental(worldObj);
        }

        return new EntityPig(worldObj);
    }

    public String getSummoningHelperID()
    {
        return id;
    }
}
