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
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import cpw.mods.fml.common.network.PacketDispatcher;

public class RitualEffectContainment extends RitualEffect
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
			int d0 = 5;
			AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1, y + 1, z + 1).expand(d0, d0, d0);
			List list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
			Iterator iterator = list.iterator();
			EntityLivingBase livingEntity;
			boolean flag = false;

			while (iterator.hasNext())
			{
				livingEntity = (EntityLivingBase)iterator.next();

				if (livingEntity instanceof EntityPlayer)
				{
					continue;
				}

				if (!livingEntity.getEntityName().equals(owner))
				{
					double xDif = livingEntity.posX - (x + 0.5);
					double yDif = livingEntity.posY - (y + 3);
					double zDif = livingEntity.posZ - (z + 0.5);
					livingEntity.motionX = -0.05 * xDif;
					livingEntity.motionY = -0.05 * yDif;
					livingEntity.motionZ = -0.05 * zDif;
					flag = true;
					//livingEntity.setVelocity(-0.05 * xDif, -0.05 * yDif, -0.05 * zDif);

					if (world.rand.nextInt(10) == 0)
					{
						PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(livingEntity.posX, livingEntity.posY, livingEntity.posZ, (short)1));
					}

					livingEntity.fallDistance = 0;
					//entityplayer.addPotionEffect(new PotionEffect(Potion.confusion.id, 80));
				}
			}

			if (world.getWorldTime() % 2 == 0 && flag)
			{
				data.currentEssence = currentEssence - getCostPerRefresh();
				data.markDirty();
			}
		}
	}

	@Override
	public int getCostPerRefresh()
	{
		return 1;
	}
}
