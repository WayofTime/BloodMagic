package WayofTime.alchemicalWizardry.common.rituals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.api.rituals.IMasterRitualStone;
import WayofTime.alchemicalWizardry.api.rituals.RitualComponent;
import WayofTime.alchemicalWizardry.api.rituals.RitualEffect;
import WayofTime.alchemicalWizardry.api.soulNetwork.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class RitualEffectJumping extends RitualEffect
{
    @Override
    public void performEffect(IMasterRitualStone ritualStone)
    {
        String owner = ritualStone.getOwner();
        World worldSave = MinecraftServer.getServer().worldServers[0];
        LifeEssenceNetwork data = (LifeEssenceNetwork) worldSave.loadItemData(LifeEssenceNetwork.class, owner);

        if (data == null)
        {
            data = new LifeEssenceNetwork(owner);
            worldSave.setItemData(owner, data);
        }

        int currentEssence = data.currentEssence;
        World world = ritualStone.getWorld();
        int x = ritualStone.getXCoord();
        int y = ritualStone.getYCoord();
        int z = ritualStone.getZCoord();

        if (currentEssence < this.getCostPerRefresh())
        {
            EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

            if (entityOwner == null)
            {
                return;
            }

            entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
        } else
        {
            int d0 = 0;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double) x, (double) y + 1, (double) z, (double) (x + 1), (double) (y + 2), (double) (z + 1)).expand(d0, d0, d0);
            List list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
            Iterator iterator = list.iterator();
            EntityLivingBase entityplayer;
            boolean flag = false;

            while (iterator.hasNext())
            {
                entityplayer = (EntityLivingBase) iterator.next();

                if (entityplayer instanceof EntityPlayer)
                {
                    //PacketDispatcher.sendPacketToPlayer(PacketHandler.getPlayerVelocitySettingPacket(entityplayer.motionX, 1.5, entityplayer.motionZ), (Player) entityplayer);
                    SpellHelper.setPlayerSpeedFromServer((EntityPlayer)entityplayer, entityplayer.motionX, 1.5, entityplayer.motionZ);
                    entityplayer.motionY = 1.5;
                    entityplayer.fallDistance = 0;
                    flag = true;
                } else
                //if (!(entityplayer.getEntityName().equals(owner)))
                {
//                    double xDif = entityplayer.posX - xCoord;
//                    double yDif = entityplayer.posY - (yCoord + 1);
//                    double zDif = entityplayer.posZ - zCoord;
                    //entityplayer.motionX=0.1*xDif;
                    entityplayer.motionY = 1.5;
                    //entityplayer.motionZ=0.1*zDif;
                    entityplayer.fallDistance = 0;
                    flag = true;
                    //entityplayer.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
                }
            }

            if (flag)
            {
                data.currentEssence = currentEssence - this.getCostPerRefresh();
                data.markDirty();
            }
        }
    }

    @Override
    public int getCostPerRefresh()
    {
        return 5;
    }

    @Override
	public List<RitualComponent> getRitualComponentList() 
	{
		ArrayList<RitualComponent> jumpingRitual = new ArrayList();

        for (int i = -1; i <= 1; i++)
        {
            jumpingRitual.add(new RitualComponent(1, i, 1, RitualComponent.AIR));
            jumpingRitual.add(new RitualComponent(-1, i, 1, RitualComponent.AIR));
            jumpingRitual.add(new RitualComponent(-1, i, -1, RitualComponent.AIR));
            jumpingRitual.add(new RitualComponent(1, i, -1, RitualComponent.AIR));
        }
        return jumpingRitual;
	}
}
