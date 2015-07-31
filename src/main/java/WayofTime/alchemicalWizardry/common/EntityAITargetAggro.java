package WayofTime.alchemicalWizardry.common;

import WayofTime.alchemicalWizardry.common.entity.mob.EntityDemon;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

public class EntityAITargetAggro extends EntityAINearestAttackableTarget
{
    private EntityDemon theCreature;

    public EntityAITargetAggro(EntityDemon par1EntityDemon, Class par2Class, int par3, boolean par4)
    {
        super(par1EntityDemon, par2Class, par3, par4);
        this.theCreature = par1EntityDemon;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        return theCreature.isAggro() && super.shouldExecute();
    }
}
