package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.Random;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.PacketHandler;
import WayofTime.alchemicalWizardry.common.entity.projectile.LightningBoltProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.common.network.PacketDispatcher;

public class SpellLightningBolt extends HomSpell
{
	Random itemRand = new Random();

	public SpellLightningBolt()
	{
		super();
		setEnergies(75, 200, 700, 700);
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

		par2World.playSoundAtEntity(par3EntityPlayer, "random.fizz", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!par2World.isRemote)
		{
			//par2World.spawnEntityInWorld(new EnergyBlastProjectile(par2World, par3EntityPlayer, damage));
			par2World.spawnEntityInWorld(new LightningBoltProjectile(par2World, par3EntityPlayer, 8, false));
		}

		return par1ItemStack;
	}

	@Override
	public ItemStack onOffensiveMeleeRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		//TODO Make it work better...?
		EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

		if (par3EntityPlayer.isSneaking())
		{
			return par1ItemStack;
		}

		if (!par3EntityPlayer.capabilities.isCreativeMode)
		{
			EnergyItems.syphonAndDamageWhileInContainer(par1ItemStack, par3EntityPlayer, getOffensiveMeleeEnergy());
		}

		double xCoord = par3EntityPlayer.posX;
		double yCoord = par3EntityPlayer.posY;
		double zCoord = par3EntityPlayer.posZ;
		par2World.getWorldInfo().setRaining(true);
		//par2World.setRainStrength(1.0F);
		par2World.rainingStrength = 1.0F;
		par2World.thunderingStrength = 1.0F;
		par2World.getWorldInfo().setThunderTime(0);
		par2World.getWorldInfo().setThundering(true);

		for (int i = 0; i < 5; i++)
		{
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, PacketHandler.getCustomParticlePacket("mobSpell", xCoord + itemRand.nextFloat() - itemRand.nextFloat(), yCoord + itemRand.nextFloat() - itemRand.nextFloat(), zCoord + itemRand.nextFloat() - itemRand.nextFloat(), 1.0F, 1.0F, 1.0F));
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

		for (int i = 0; i < 5; i++)
		{
			par2World.addWeatherEffect(new EntityLightningBolt(par2World, xCoord + itemRand.nextInt(64) - 32, yCoord + itemRand.nextInt(8) - 8, zCoord + itemRand.nextInt(64) - 32));
		}

		for (int i = 0; i < 8; i++)
		{
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 30, par2World.provider.dimensionId, PacketHandler.getCustomParticlePacket("mobSpell", xCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, yCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, zCoord + (itemRand.nextFloat() - itemRand.nextFloat()) * 3, 1.0F, 1.0F, 1.0F));
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

		if (!par2World.isRemote)
		{
			//par2World.spawnEntityInWorld(new EnergyBlastProjectile(par2World, par3EntityPlayer, damage));
			par2World.spawnEntityInWorld(new LightningBoltProjectile(par2World, par3EntityPlayer, 8, true));
		}

		return par1ItemStack;
	}
}