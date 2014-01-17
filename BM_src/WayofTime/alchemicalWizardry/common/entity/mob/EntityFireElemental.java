package WayofTime.alchemicalWizardry.common.entity.mob;

import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class EntityFireElemental extends EntityElemental implements IMob
{
    public EntityFireElemental(World world)
    {
        super(world, AlchemicalWizardry.entityFireElementalID);
        this.isImmuneToFire = true;
    }

    public void inflictEffectOnEntity(Entity target)
    {
        if (target instanceof EntityLivingBase)
        {
            ((EntityLivingBase)target).setFire(10);
            ((EntityLivingBase)target).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionInhibit.id, 150, 0));
        }
    }
}