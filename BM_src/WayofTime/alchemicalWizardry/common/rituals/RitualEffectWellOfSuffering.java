package WayofTime.alchemicalWizardry.common.rituals;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;

public class RitualEffectWellOfSuffering extends RitualEffect
{
	public final int timeDelay = 25;
	public final int amount = 10;

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
		int d0 = 10;
		int vertRange = 5;
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1, y + 1, z + 1).expand(d0, vertRange, d0);
		List list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
		Iterator iterator1 = list.iterator();
		EntityLivingBase entity;
		int entityCount = 0;
		while (iterator1.hasNext())
		{
			entity = (EntityLivingBase)iterator1.next();
			entityCount++;
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
				entity = (EntityLivingBase)iterator2.next();

				if (entity instanceof EntityPlayer)
				{
					continue;
				}

				//entity.setHealth(entity.getHealth()-1);
				entity.attackEntityFrom(DamageSource.outOfWorld, 1);
				entityCount++;
				//                if(entity.getHealth()<=0.2f)
					//                {
					//                	entity.onDeath(DamageSource.inFire);
				//                }
				tileAltar.sacrificialDaggerCall(amount, true);
			}

			data.currentEssence = currentEssence - getCostPerRefresh() * entityCount;
			data.markDirty();
		}
	}

	@Override
	public int getCostPerRefresh()
	{
		// TODO Auto-generated method stub
		return 2;
	}
}
