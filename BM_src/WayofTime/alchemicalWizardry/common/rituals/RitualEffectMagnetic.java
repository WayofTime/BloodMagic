package WayofTime.alchemicalWizardry.common.rituals;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.alchemicalWizardry.common.LifeEssenceNetwork;
import WayofTime.alchemicalWizardry.common.block.BlockTeleposer;
import WayofTime.alchemicalWizardry.common.tileEntity.TEMasterStone;

public class RitualEffectMagnetic extends RitualEffect
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

		if (world.getWorldTime() % 40 != 0)
		{
			return;
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
			int xRep = 0;
			int yRep = 0;
			int zRep = 0;
			boolean replace = false;

			for (int j = 1; j <= 3; j++)
			{
				for (int i = -1; i <= 1; i++)
				{
					for (int k = -1; k <= 1; k++)
					{
						if (!replace && world.isAirBlock(x + i, y + j, z + k))
						{
							xRep = x + i;
							yRep = y + j;
							zRep = z + k;
							replace = true;
						}
					}
				}
			}

			if (replace)
			{
				//boolean hasReplaced = false;
				for (int j = y - 1; j >= 0; j--)
				{
					for (int i = -3; i <= 3; i++)
					{
						for (int k = -3; k <= 3; k++)
						{
							Block block = Block.blocksList[world.getBlockId(x + i, j, z + k)];
							int meta = world.getBlockMetadata(x + i, j, z + k);

							if (block == null)
							{
								continue;
							}

							ItemStack itemStack = new ItemStack(block, 1, meta);
							int id = OreDictionary.getOreID(itemStack);

							if (id != -1)
							{
								String oreName = OreDictionary.getOreName(id);

								if (oreName.contains("ore"))
								{
									//TODO
									//Allow swapping code. This means the searched block is an ore.
									BlockTeleposer.swapBlocks(world, world, x + i, j, z + k, xRep, yRep, zRep);
									data.currentEssence = currentEssence - getCostPerRefresh();
									data.markDirty();
									return;
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public int getCostPerRefresh()
	{
		return 50;
	}
}
