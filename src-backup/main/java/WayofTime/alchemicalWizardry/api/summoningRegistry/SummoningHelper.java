package WayofTime.alchemicalWizardry.api.summoningRegistry;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public abstract class SummoningHelper
{
	protected int id;

    public SummoningHelper(int id)
    {
        this.id = id;
    }

    public abstract EntityLivingBase getEntity(World worldObj);

    public int getSummoningHelperID()
    {
        return id;
    }
}
