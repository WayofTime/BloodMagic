package WayofTime.alchemicalWizardry.common;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.mob.EntityElemental;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
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
            PacketDispatcher.sendPacketToPlayer(PacketHandler.getPlayerVelocitySettingPacket(target.motionX, target.motionY + 3, target.motionZ), (Player) target);
            ((EntityLivingBase) target).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionInhibit.id, 150, 0));
        } else if (target instanceof EntityLivingBase)
        {
            ((EntityLivingBase) target).motionY += 3.0D;
            ((EntityLivingBase) target).addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionInhibit.id, 150, 0));
        }
    }
}
