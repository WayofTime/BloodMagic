package WayofTime.alchemicalWizardry.common.rituals;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;

public class RitualEffectFeatheredKnife extends RitualEffect
{
	public final int timeDelay = 20;
	public final int amount = 100;

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

		if (world.getWorldTime() % timeDelay != 0)
		{
			return;
		}

		//		if(!(world.getBlockTileEntity(x, y-1, z) instanceof TEAltar))
			//		{
			//			return;
			//		}
		TEAltar tileAltar = null;
		boolean testFlag = false;

		for (int i = -5; i <= 5; i++)
		{
			for (int j = -5; j <= 5; j++)
			{
				for (int k = -10; k <= 10; k++)
				{
					if (world.getBlockTileEntity(x + i, y + k, z + j) instanceof TEAltar)
					{
						tileAltar = (TEAltar)world.getBlockTileEntity(x + i, y + k, z + j);
						testFlag = true;
					}
				}
			}
		}

		if (!testFlag)
		{
			return;
		}

		//tileAltar = (TEAltar)world.getBlockTileEntity(x,y-1,z);
		int d0 = 15;
		int vertRange = 20;
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1, y + 1, z + 1).expand(d0, vertRange, d0);
		List list = world.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
		Iterator iterator1 = list.iterator();
		EntityPlayer entity;
		int entityCount = 0;
		while (iterator1.hasNext())
		{
			entity = (EntityPlayer)iterator1.next();

			if (entity.getClass().equals(EntityPlayerMP.class) || entity.getClass().equals(EntityPlayer.class))
			{
				entityCount++;
			}
		}

		if (currentEssence < getCostPerRefresh() * entityCount)
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
			Iterator iterator2 = list.iterator();
			entityCount = 0;

			while (iterator2.hasNext())
			{
				entity = (EntityPlayer)iterator2.next();

				//entity = (EntityPlayer)iterator1.next();
				if (entity.getClass().equals(EntityPlayerMP.class) || entity.getClass().equals(EntityPlayer.class))
				{
					if (entity.getHealth() > 6.2f)
					{
						entity.setHealth(entity.getHealth() - 1);
						entityCount++;
						tileAltar.sacrificialDaggerCall(amount, false);
					}
				}

				//entity.setHealth(entity.getHealth()-1);
				//                if(entity.getHealth()<=0.2f)
				//                {
				//                	entity.onDeath(DamageSource.inFire);
				//                }
			}

			data.currentEssence = currentEssence - getCostPerRefresh() * entityCount;
			data.markDirty();
		}
	}

	@Override
	public int getCostPerRefresh()
	{
		// TODO Auto-generated method stub
		return 20;
	}
}
