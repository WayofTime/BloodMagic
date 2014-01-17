package WayofTime.alchemicalWizardry.common.rituals;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.entity.projectile.EntityMeteor;
import WayofTime.alchemicalWizardry.common.summoning.meteor.MeteorRegistry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;

public class RitualEffectSummonMeteor extends RitualEffect
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

		if (ritualStone.getCooldown() > 0)
		{
			ritualStone.setCooldown(0);
		}

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
			List<EntityItem> entities = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(x, y + 1, z, x + 1, y + 2, z + 1));

			if (entities == null)
			{
				return;
			}

			for (EntityItem entityItem : entities)
			{
				if (entityItem != null && MeteorRegistry.isValidParadigmItem(entityItem.getEntityItem()))
				{
					int meteorID = MeteorRegistry.getParadigmIDForItem(entityItem.getEntityItem());
					EntityMeteor meteor = new EntityMeteor(world, x + 0.5f, 257, z + 0.5f, meteorID);
					meteor.motionY = -1.0f;
					entityItem.setDead();
					world.spawnEntityInWorld(meteor);
					ritualStone.setActive(false);
					break;
				}
			}

			//        	EnergyBlastProjectile proj = new EnergyBlastProjectile(world, x, y+20, z);
			//        	proj.motionX = 0.0d;
			//        	proj.motionZ = 0.0d;
			//        	proj.motionY = -1.0d;
			//        	world.spawnEntityInWorld(proj);
			data.currentEssence = currentEssence - getCostPerRefresh();
			data.markDirty();
		}
	}

	@Override
	public int getCostPerRefresh()
	{
		return 0;
	}
}
