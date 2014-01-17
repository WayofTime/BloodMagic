package WayofTime.alchemicalWizardry.common.rituals;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class RitualEffectJumping extends RitualEffect
{
	@Override
	public void performEffect(TEMasterStone ritualStone)
	{
		String owner = ritualStone.getOwner();
		World worldSave = MinecraftServer.getServer().worldServers[0];
		LifeEssenceNetwork data = (LifeEssenceNetwork)worldSave.loadItemData(LifeEssenceNetwork.class, owner);

		if (data == null)
		{
			data = new LifeEssenceNetwork(owner);
			worldSave.setItemData(owner, data);
		}

		int currentEssence = data.currentEssence;
		World world = ritualStone.worldObj;
		int x = ritualStone.xCoord;
		int y = ritualStone.yCoord;
		int z = ritualStone.zCoord;

		if (currentEssence < getCostPerRefresh())
		{
			EntityPlayer entityOwner = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(owner);

			if (entityOwner == null)
			{
				return;
			}

			entityOwner.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
		}
		else
		{
			int d0 = 0;
			AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB(x, (double)y + 1, z, x + 1, y + 2, z + 1).expand(d0, d0, d0);
			List list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
			Iterator iterator = list.iterator();
			EntityLivingBase entityplayer;
			boolean flag = false;

			while (iterator.hasNext())
			{
				entityplayer = (EntityLivingBase)iterator.next();

				if (entityplayer instanceof EntityPlayer)
				{
					PacketDispatcher.sendPacketToPlayer(PacketHandler.getPlayerVelocitySettingPacket(entityplayer.motionX, 1.5, entityplayer.motionZ), (Player)entityplayer);
					entityplayer.motionY = 1.5;
					entityplayer.fallDistance = 0;
					flag = true;
				}
				else
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
				data.currentEssence = currentEssence - getCostPerRefresh();
				data.markDirty();
			}
		}
	}

	@Override
	public int getCostPerRefresh()
	{
		return 5;
	}
}
