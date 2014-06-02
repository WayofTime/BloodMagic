package WayofTime.alchemicalWizardry.common.summoning;

import WayofTime.alchemicalWizardry.api.summoningRegistry.SummoningHelper;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityFallenAngel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class SummoningFallenAngel extends SummoningHelper
{
    public SummoningFallenAngel(int id)
    {
        super(id);
        // TODO Auto-generated constructor stub
    }

    public EntityLivingBase getEntity(World worldObj)
    {
        return new EntityFallenAngel(worldObj);
    }
}
