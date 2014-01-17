package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.entity.projectile.MudProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.common.network.PacketDispatcher;

public class SpellEarthBender extends HomSpell
{
	Random itemRand = new Random();

	public SpellEarthBender()
	{
		super();
		setEnergies(100, 150, 350, 200);
		//this.setCreativeTab(CreativeTabs.tabMisc);
	}
	@Override
	public ItemStack onOffensiveRangedRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

		if (par3EntityPlayer.isSneaking())
		{
			return par1ItemStack;
		}

		if (!par3EntityPlayer.capabilities.isCreativeMode)
		{
			EnergyItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, getOffensiveRangedEnergy());
		}

		par2World.spawnEntityInWorld(new MudProjectile(par2World, par3EntityPlayer, 8, false));
		par2World.playSoundAtEntity(par3EntityPlayer, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		return par1ItemStack;
	}

	@Override
	public ItemStack onOffensiveMeleeRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

		if (par3EntityPlayer.isSneaking())
		{
			return par1ItemStack;
		}

		if (!par3EntityPlayer.capabilities.isCreativeMode)
		{
			EnergyItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, getOffensiveMeleeEnergy());
		}

		if (!par2World.isRemote)
		{
			for (int i = -1; i <= 1; i++)
			{
				for (int j = -1; j <= 1; j++)
				{
					par2World.spawnEntityInWorld(new MudProjectile(par2World, par3EntityPlayer, 3, 3, par3EntityPlayer.posX, par3EntityPlayer.posY + par3EntityPlayer.getEyeHeight(), par3EntityPlayer.posZ, par3EntityPlayer.rotationYaw + i * 10F, par3EntityPlayer.rotationPitch + j * 5F, true));
				}
			}
		}

		return par1ItemStack;
	}

	@Override
	public ItemStack onDefensiveRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

		if (par3EntityPlayer.isSneaking())
		{
			return par1ItemStack;
		}

		if (!par3EntityPlayer.capabilities.isCreativeMode)
		{
			EnergyItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, getDefensiveEnergy());
		}

		double xCoord = par3EntityPlayer.posX;
		double yCoord = par3EntityPlayer.posY;
		double zCoord = par3EntityPlayer.posZ;
		int posX = (int)xCoord;
		int posY = (int)yCoord;
		int posZ = (int)zCoord;
		int blockID = Block.stone.blockID;

		if (par2World.isAirBlock(posX, posY + 3, posZ))
		{
			par2World.setBlock(posX, posY + 3, posZ, Block.glass.blockID);
		}

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				if (par2World.isAirBlock(posX + i - 1, posY + j, posZ - 2))
				{
					par2World.setBlock(posX + i - 1, posY + j, posZ - 2, blockID);
				}

				if (par2World.isAirBlock(posX + 2, posY + j, posZ - 1 + i))
				{
					par2World.setBlock(posX + 2, posY + j, posZ - 1 + i, blockID);
				}

				if (par2World.isAirBlock(posX - i + 1, posY + j, posZ + 2))
				{
					par2World.setBlock(posX - i + 1, posY + j, posZ + 2, blockID);
				}

				if (par2World.isAirBlock(posX - 2, posY + j, posZ + 1 - i))
				{
					par2World.setBlock(posX - 2, posY + j, posZ + 1 - i, blockID);
				}

				{
					if (par2World.isAirBlock(posX - 1 + i, posY + 3, posZ - 1 + j))
					{
						par2World.setBlock(posX - 1 + i, posY + 3, posZ - 1 + j, blockID);
					}
				}
			}
		}

		for (int i = 0; i < 20; i++)
		{
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, PacketHandler.getCustomParticlePacket("mobSpell", xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, 0.0F, 0.410F, 1.0F));
		}

		return par1ItemStack;
	}

	@Override
	public ItemStack onEnvironmentalRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

		if (par3EntityPlayer.isSneaking())
		{
			return par1ItemStack;
		}

		if (!par3EntityPlayer.capabilities.isCreativeMode)
		{
			EnergyItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, getEnvironmentalEnergy());
		}

		int range = 3;

		if (!par2World.isRemote)
		{
			for (int i = -range; i <= range; i++)
			{
				for (int j = -1; j <= 1; j++)
				{
					for (int k = -range; k <= range; k++)
					{
						if (par2World.getBlockId((int)par3EntityPlayer.posX + i, (int)par3EntityPlayer.posY + j, (int)par3EntityPlayer.posZ + k) == Block.waterStill.blockID || par2World.getBlockId((int)par3EntityPlayer.posX + i, (int)par3EntityPlayer.posY + j, (int)par3EntityPlayer.posZ + k) == Block.waterMoving.blockID)
						{
							int x = par2World.rand.nextInt(2);

							if (x == 0)
							{
								par2World.setBlock((int)par3EntityPlayer.posX + i, (int)par3EntityPlayer.posY + j, (int)par3EntityPlayer.posZ + k, Block.sand.blockID);
							}
							else
							{
								par2World.setBlock((int)par3EntityPlayer.posX + i, (int)par3EntityPlayer.posY + j, (int)par3EntityPlayer.posZ + k, Block.dirt.blockID);
							}
						}
					}
				}
			}
		}

		double xCoord = par3EntityPlayer.posX;
		double yCoord = par3EntityPlayer.posY;
		double zCoord = par3EntityPlayer.posZ;

		for (int i = 0; i < 16; i++)
		{
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, PacketHandler.getCustomParticlePacket("mobSpell", xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 2, 0.0F, 0.410F, 1.0F));
		}

		return par1ItemStack;
	}
}
