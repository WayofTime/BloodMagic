package WayofTime.alchemicalWizardry.common.summoning;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.summoningRegistry.SummoningHelper;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGrunt;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntEarth;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntFire;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntIce;
import WayofTime.alchemicalWizardry.common.demonVillage.demonHoard.demon.EntityMinorDemonGruntWind;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityAirElemental;
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

        if(this.id.equals(AlchemicalWizardry.entityMinorDemonGruntID))
        {
        	return new EntityMinorDemonGrunt(worldObj);
        }
        
        if(this.id.equals(AlchemicalWizardry.entityMinorDemonGruntFireID))
        {
        	return new EntityMinorDemonGruntFire(worldObj);
        }
        
        if(this.id.equals(AlchemicalWizardry.entityMinorDemonGruntEarthID))
        {
        	return new EntityMinorDemonGruntEarth(worldObj);
        }
        
        if(this.id.equals(AlchemicalWizardry.entityMinorDemonGruntWindID))
        {
        	return new EntityMinorDemonGruntWind(worldObj);
        }
        
        if(this.id.equals(AlchemicalWizardry.entityMinorDemonGruntIceID))
        {
        	return new EntityMinorDemonGruntIce(worldObj);
        }
        
        return new EntityPig(worldObj);
    }

    public String getSummoningHelperID()
    {
        return id;
    }
}
