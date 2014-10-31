package WayofTime.alchemicalWizardry.common.entity.mob;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class EntityShadeElemental extends EntityElemental implements IMob
{
    public EntityShadeElemental(World world)
    {
        super(world, AlchemicalWizardry.entityShadeElementalID);
    }

    public void inflictEffectOnEntity(Entity target)
    {
        if (target instanceof EntityLivingBase)
        {
            ((EntityLivingBase) target).addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 1));
            ((EntityLivingBase) target).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionInhibit.id, 150, 0));
            ((EntityLivingBase) target).addPotionEffect(new PotionEffect(Potion.nightVision.id, 100, 0));
        }
    }
}