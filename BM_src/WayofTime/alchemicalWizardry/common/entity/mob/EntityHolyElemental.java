package WayofTime.alchemicalWizardry.common.entity.mob;

import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class EntityHolyElemental extends EntityElemental implements IMob
{
    public EntityHolyElemental(World world)
    {
        super(world, AlchemicalWizardry.entityHolyElementalID);
    }

    public void inflictEffectOnEntity(Entity target)
    {
        if (target instanceof EntityLivingBase)
        {
            ((EntityLivingBase)target).attackEntityFrom(DamageSource.causeMobDamage(this), 15);
            ((EntityLivingBase)target).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionInhibit.id, 150, 0));
            ((EntityLivingBase)target).addPotionEffect(new PotionEffect(Potion.poison.id, 100, 1));
        }
    }
}