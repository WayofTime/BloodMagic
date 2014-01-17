package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;

import com.google.common.collect.Multimap;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DaggerOfSacrifice extends EnergyItems
{
	public DaggerOfSacrifice(int id)
	{
		super(id);
		maxStackSize = 1;
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
		setEnergyUsed(100);
		setFull3D();
		setMaxDamage(100);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:DaggerOfSacrifice");
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
	{
		if (par3EntityLivingBase == null || par2EntityLivingBase == null || par3EntityLivingBase.worldObj.isRemote || !par3EntityLivingBase.getClass().equals(EntityPlayerMP.class))
		{
			return false;
		}

		//EntityWither d;
		if (par2EntityLivingBase.isChild() || par2EntityLivingBase instanceof EntityWither || par2EntityLivingBase instanceof EntityDragon || par2EntityLivingBase instanceof EntityPlayer || par2EntityLivingBase instanceof IBossDisplayData)
		{
			return false;
		}

		if (par2EntityLivingBase.isDead || par2EntityLivingBase.getHealth() < 0.5f)
		{
			return false;
		}

		if (par2EntityLivingBase instanceof EntityVillager && !par2EntityLivingBase.isChild())
		{
			if (findAndFillAltar(par2EntityLivingBase.worldObj, par2EntityLivingBase, 2000))
			{
				double posX = par2EntityLivingBase.posX;
				double posY = par2EntityLivingBase.posY;
				double posZ = par2EntityLivingBase.posZ;

				for (int i = 0; i < 8; i++)
				{
					PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(posX, posY, posZ, (short)1));
				}

				par2EntityLivingBase.setHealth(-1);
				par2EntityLivingBase.onDeath(DamageSource.generic);
				return false;
			}
		}

		if (par2EntityLivingBase instanceof EntitySlime && !par2EntityLivingBase.isChild())
		{
			if (findAndFillAltar(par2EntityLivingBase.worldObj, par2EntityLivingBase, 150))
			{
				double posX = par2EntityLivingBase.posX;
				double posY = par2EntityLivingBase.posY;
				double posZ = par2EntityLivingBase.posZ;

				for (int i = 0; i < 8; i++)
				{
					PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(posX, posY, posZ, (short)1));
				}

				par2EntityLivingBase.setHealth(-1);
				par2EntityLivingBase.onDeath(DamageSource.generic);
				return false;
			}
		}

		if (par2EntityLivingBase instanceof EntityEnderman && !par2EntityLivingBase.isChild())
		{
			if (findAndFillAltar(par2EntityLivingBase.worldObj, par2EntityLivingBase, 200))
			{
				double posX = par2EntityLivingBase.posX;
				double posY = par2EntityLivingBase.posY;
				double posZ = par2EntityLivingBase.posZ;

				for (int i = 0; i < 8; i++)
				{
					PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(posX, posY, posZ, (short)1));
				}

				par2EntityLivingBase.setHealth(-1);
				par2EntityLivingBase.onDeath(DamageSource.generic);
				return false;
			}
		}

		if (par2EntityLivingBase instanceof EntityAnimal && !par2EntityLivingBase.isChild())
		{
			if (findAndFillAltar(par2EntityLivingBase.worldObj, par2EntityLivingBase, 250))
			{
				double posX = par2EntityLivingBase.posX;
				double posY = par2EntityLivingBase.posY;
				double posZ = par2EntityLivingBase.posZ;

				for (int i = 0; i < 8; i++)
				{
					PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(posX, posY, posZ, (short)1));
				}

				par2EntityLivingBase.setHealth(-1);
				par2EntityLivingBase.onDeath(DamageSource.generic);
				return false;
			}
		}

		if (findAndFillAltar(par2EntityLivingBase.worldObj, par2EntityLivingBase, 500))
		{
			double posX = par2EntityLivingBase.posX;
			double posY = par2EntityLivingBase.posY;
			double posZ = par2EntityLivingBase.posZ;

			for (int i = 0; i < 8; i++)
			{
				PacketDispatcher.sendPacketToAllPlayers(TEAltar.getParticlePacket(posX, posY, posZ, (short)1));
			}

			par2EntityLivingBase.setHealth(-1);
			par2EntityLivingBase.onDeath(DamageSource.generic);
			return false;
		}

		return false;
	}

	public float func_82803_g()
	{
		return 4.0F;
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Caution: may cause");
		par3List.add("a bad day...");
	}

	@Override
	public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block)
	{
		if (par2Block.blockID == Block.web.blockID)
		{
			return 15.0F;
		}
		else
		{
			Material material = par2Block.blockMaterial;
			return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.pumpkin ? 1.0F : 1.5F;
		}
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
	{
		return false;
	}

	@Override

	public Multimap getItemAttributeModifiers()
	{
		Multimap multimap = super.getItemAttributeModifiers();
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Tool modifier", 1.0d, 0));
		return multimap;
	}

	public boolean findAndFillAltar(World world, EntityLivingBase sacrifice, int amount)
	{
		int posX = (int)Math.round(sacrifice.posX - 0.5f);
		int posY = (int)sacrifice.posY;
		int posZ = (int)Math.round(sacrifice.posZ - 0.5f);
		TEAltar altarEntity = getAltar(world, posX, posY, posZ);

		if (altarEntity == null)
		{
			return false;
		}

		altarEntity.sacrificialDaggerCall(amount, true);
		altarEntity.startCycle();
		return true;
	}

	public TEAltar getAltar(World world, int x, int y, int z)
	{
		TileEntity tileEntity = null;

		for (int i = -2; i <= 2; i++)
		{
			for (int j = -2; j <= 2; j++)
			{
				for (int k = -2; k <= 1; k++)
				{
					tileEntity = world.getBlockTileEntity(i + x, k + y, j + z);

					if (tileEntity instanceof TEAltar)
					{
						return (TEAltar)tileEntity;
					}
				}

				if (tileEntity instanceof TEAltar)
				{
					return (TEAltar)tileEntity;
				}
			}

			if (tileEntity instanceof TEAltar)
			{
				return (TEAltar)tileEntity;
			}
		}

		if (tileEntity instanceof TEAltar)
		{
			return (TEAltar)tileEntity;
		}

		return null;
	}
}
