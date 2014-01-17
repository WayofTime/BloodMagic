package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.FakePlayer;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SacrificialDagger extends Item
{
	public SacrificialDagger(int id)
	{
		super(id);
		maxStackSize = 1;
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
		setFull3D();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SacrificialDagger");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Just a prick of the");
		par3List.add("finger will suffice...");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (!par3EntityPlayer.capabilities.isCreativeMode)
		{
			par3EntityPlayer.setHealth(par3EntityPlayer.getHealth() - 2);
		}

		if (par3EntityPlayer instanceof FakePlayer)
		{
			return par1ItemStack;
		}

		double posX = par3EntityPlayer.posX;
		double posY = par3EntityPlayer.posY;
		double posZ = par3EntityPlayer.posZ;
		par2World.playSoundEffect((float)posX + 0.5F, (float)posY + 0.5F, (float)posZ + 0.5F, "random.fizz", 0.5F, 2.6F + (par2World.rand.nextFloat() - par2World.rand.nextFloat()) * 0.8F);
		float f = 1.0F;
		float f1 = f * 0.6F + 0.4F;
		float f2 = f * f * 0.7F - 0.5F;
		float f3 = f * f * 0.6F - 0.7F;

		for (int l = 0; l < 8; ++l)
		{
			par2World.spawnParticle("reddust", posX + Math.random() - Math.random(), posY + Math.random() - Math.random(), posZ + Math.random() - Math.random(), f1, f2, f3);
		}

		if (!par2World.isRemote && !par3EntityPlayer.getClass().equals(EntityPlayerMP.class))
		{
			return par1ItemStack;
		}

		findAndFillAltar(par2World, par3EntityPlayer, 200);

		if (par3EntityPlayer.getHealth() <= 0.5f)
		{
			//par3EntityPlayer.inventory.dropAllItems();
			par3EntityPlayer.onDeath(DamageSource.generic);
		}

		return par1ItemStack;
	}

	public void findAndFillAltar(World world, EntityPlayer player, int amount)
	{
		int posX = (int)Math.round(player.posX - 0.5f);
		int posY = (int)player.posY;
		int posZ = (int)Math.round(player.posZ - 0.5f);
		TEAltar altarEntity = getAltar(world, posX, posY, posZ);

		if (altarEntity == null)
		{
			return;
		}

		altarEntity.sacrificialDaggerCall(amount, false);
		altarEntity.startCycle();
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