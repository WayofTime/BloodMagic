package WayofTime.alchemicalWizardry.common.entity.mob;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class EntityWaterElemental extends EntityElemental implements IMob
{
    public EntityWaterElemental(World world)
    {
        super(world, AlchemicalWizardry.entityWaterElementalID);
    }

    public void inflictEffectOnEntity(Entity target)
    {
        if (target instanceof EntityLivingBase)
        {
            ((EntityLivingBase) target).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionDrowning.id, 100, 2));
            ((EntityLivingBase) target).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionInhibit.id, 150, 0));
        }
    }
}