package WayofTime.alchemicalWizardry.common.rituals;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;
import cpw.mods.fml.common.network.PacketDispatcher;

public class RitualEffectGrowth extends RitualEffect
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
			if (world.getWorldTime() % 20 != 0)
			{
				return;
			}

			boolean flag = false;

			for (int i = -1; i <= 1; i++)
			{
				for (int j = -1; j <= 1; j++)
				{
					int id = world.getBlockId(x + i, y + 2, z + j);
					Block block = Block.blocksList[id];

					if (block instanceof IPlantable)
					{
						{
							PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(x + i, y + 2, z + j, (short)3));
							block.updateTick(world, x + i, y + 2, z + j, world.rand);
							flag = true;
						}
					}
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
		return 100;
	}
}
