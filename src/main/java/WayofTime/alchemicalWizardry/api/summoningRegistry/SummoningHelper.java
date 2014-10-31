package WayofTime.alchemicalWizardry.api.summoningRegistry;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public abstract class SummoningHelper
{
    protected String id;

    public SummoningHelper(String id)
    {
        this.id = id;
    }

    public abstract EntityLivingBase getEntity(World worldObj);

    public String getSummoningHelperID()
    {
        return id;
    }
}
