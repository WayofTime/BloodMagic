package WayofTime.alchemicalWizardry.common.entity.mob;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class EntityAirElemental extends EntityElemental implements IMob
{
    public EntityAirElemental(World world)
    {
        super(world, AlchemicalWizardry.entityAirElementalID);
    }

    public void inflictEffectOnEntity(Entity target)
    {
        if (target instanceof EntityPlayer)
        {
            SpellHelper.setPlayerSpeedFromServer((EntityPlayer) target, target.motionX, target.motionY + 3, target.motionZ);
            ((EntityLivingBase) target).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionInhibit.id, 150, 0));
        } else if (target instanceof EntityLivingBase)
        {
            ((EntityLivingBase) target).motionY += 3.0D;
            ((EntityLivingBase) target).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionInhibit.id, 150, 0));
        }
    }
}
