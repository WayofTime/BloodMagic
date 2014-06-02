package WayofTime.alchemicalWizardry.common.entity.mob;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityEarthElemental extends EntityElemental implements IMob
{
    public EntityEarthElemental(World world)
    {
        super(world, AlchemicalWizardry.entityEarthElementalID);
    }

    public void inflictEffectOnEntity(Entity target)
    {
        if (target instanceof EntityLivingBase)
        {
            ((EntityLivingBase) target).attackEntityFrom(DamageSource.causeMobDamage(this), 10);
            ((EntityLivingBase) target).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 4));
            ((EntityLivingBase) target).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionInhibit.id, 150, 0));
        }
    }
}