package WayofTime.alchemicalWizardry.common.spell.simple;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.entity.projectile.ExplosionProjectile;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;

public class SpellExplosions extends HomSpell
{
	Random itemRand = new Random();

	public SpellExplosions()
	{
		super();
		setEnergies(400, 500, 1900, 1500);
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
			par2World.spawnEntityInWorld(new ExplosionProjectile(par2World, par3EntityPlayer, 6, true));
		}

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

		int distance = 4;
		double yaw = par3EntityPlayer.rotationYaw / 180 * Math.PI;
		double pitch = par3EntityPlayer.rotationPitch / 180 * Math.PI;
		par2World.createExplosion(par3EntityPlayer, par3EntityPlayer.posX + Math.sin(yaw) * Math.cos(pitch) * -distance, par3EntityPlayer.posY + par3EntityPlayer.getEyeHeight() + Math.sin(-pitch) * distance, par3EntityPlayer.posZ + Math.cos(yaw) * Math.cos(pitch) * distance, 3, true);
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

		int distance = 4;
		//		double yaw = par3EntityPlayer.rotationYaw/180*Math.PI;
		//		double pitch = par3EntityPlayer.rotationPitch/180*Math.PI;
		par2World.createExplosion(par3EntityPlayer, par3EntityPlayer.posX, par3EntityPlayer.posY + par3EntityPlayer.getEyeHeight(), par3EntityPlayer.posZ, distance, false);
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

		int radius = 3;

		for (int i = 0; i < 360; i += 36)
		{
			par2World.createExplosion(par3EntityPlayer, par3EntityPlayer.posX + Math.cos(i) * radius, par3EntityPlayer.posY, par3EntityPlayer.posZ + Math.sin(i) * radius, 2, true);
		}

		return null;
	}
}
