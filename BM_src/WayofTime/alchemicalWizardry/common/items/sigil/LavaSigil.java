package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.items.EnergyBattery;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LavaSigil extends ItemBucket implements ArmourUpgrade
{
	/** field for checking if the bucket has been filled. */
	private int isFull = Block.lavaMoving.blockID;
	private int energyUsed;

	public LavaSigil(int par1)
	{
		super(par1, Block.lavaMoving.blockID);
		maxStackSize = 1;
		//setMaxDamage(2000);
		setEnergyUsed(1000);
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:LavaSigil");
	}

	@Override
	public ItemStack getContainerItemStack(ItemStack itemStack)
	{
		ItemStack copiedStack = itemStack.copy();
		copiedStack.setItemDamage(copiedStack.getItemDamage() + getEnergyUsed());
		copiedStack.stackSize = 1;
		return copiedStack;
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Contact with liquid is");
		par3List.add("highly unrecommended.");

		if (!(par1ItemStack.stackTagCompound == null))
		{
			par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
		}
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

		if (par3EntityPlayer.isSneaking())
		{
			return par1ItemStack;
		}

		float f = 1.0F;
		double d0 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * f;
		double d1 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * f + 1.62D - par3EntityPlayer.yOffset;
		double d2 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * f;
		boolean flag = isFull == 0;
		MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, flag);

		if (movingobjectposition == null)
		{
			return par1ItemStack;
		}
		else
		{
			if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
			{
				int i = movingobjectposition.blockX;
				int j = movingobjectposition.blockY;
				int k = movingobjectposition.blockZ;

				if (!par2World.canMineBlock(par3EntityPlayer, i, j, k))
				{
					return par1ItemStack;
				}

				if (isFull == 0)
				{
					//Empty
				}
				else
				{
					if (movingobjectposition.sideHit == 0)
					{
						--j;
					}

					if (movingobjectposition.sideHit == 1)
					{
						++j;
					}

					if (movingobjectposition.sideHit == 2)
					{
						--k;
					}

					if (movingobjectposition.sideHit == 3)
					{
						++k;
					}

					if (movingobjectposition.sideHit == 4)
					{
						--i;
					}

					if (movingobjectposition.sideHit == 5)
					{
						++i;
					}

					if (!par3EntityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, par1ItemStack))
					{
						return par1ItemStack;
					}

					if (this.tryPlaceContainedLiquid(par2World, d0, d1, d2, i, j, k) && !par3EntityPlayer.capabilities.isCreativeMode)
					{
						if (!par3EntityPlayer.capabilities.isCreativeMode)
						{
							if (!EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
							{
							}
						}
						else
						{
							return par1ItemStack;
						}
					}
				}
			}

			return par1ItemStack;
		}
	}

	/**
	 * Attempts to place the liquid contained inside the bucket.
	 */
	public boolean tryPlaceContainedLiquid(World par1World, double par2, double par4, double par6, int par8, int par9, int par10)
	{
		if (isFull <= 0)
		{
			return false;
		}
		else if (!par1World.isAirBlock(par8, par9, par10) && par1World.getBlockMaterial(par8, par9, par10).isSolid())
		{
			return false;
		}
		else if ((par1World.getBlockId(par8, par9, par10) == Block.lavaMoving.blockID || par1World.getBlockId(par8, par9, par10) == Block.lavaStill.blockID) && par1World.getBlockMetadata(par8, par9, par10) == 0)
		{
			return false;
		}
		else
		{
			par1World.setBlock(par8, par9, par10, isFull, 0, 3);
			return true;
		}
	}

	protected void setEnergyUsed(int par1int)
	{
		energyUsed = par1int;
	}

	protected int getEnergyUsed()
	{
		return energyUsed;
	}
	//Heals the player using the item. If the player is at full health, or if the durability cannot be used any more,
	//the item is not used.

	//    protected void damagePlayer(World world, EntityPlayer player, int damage)
	//    {
	//        if (world != null)
	//        {
	//            double posX = player.posX;
	//            double posY = player.posY;
	//            double posZ = player.posZ;
	//            world.playSoundEffect((double)((float)posX + 0.5F), (double)((float)posY + 0.5F), (double)((float)posZ + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
	//            float f = (float)1.0F;
	//            float f1 = f * 0.6F + 0.4F;
	//            float f2 = f * f * 0.7F - 0.5F;
	//            float f3 = f * f * 0.6F - 0.7F;
	//
	//            for (int l = 0; l < 8; ++l)
	//            {
	//                world.spawnParticle("reddust", posX + Math.random() - Math.random(), posY + Math.random() - Math.random(), posZ + Math.random() - Math.random(), f1, f2, f3);
	//            }
	//        }
	//
	//        for (int i = 0; i < damage; i++)
	//        {
	//            //player.setEntityHealth((player.getHealth()-1));
	//            player.setEntityHealth(player.func_110143_aJ() - 1);
	//
	//            if (player.func_110143_aJ() <= 0)
	//            {
	//                player.inventory.dropAllItems();
	//            }
	//        }
	//    }
	protected boolean syphonBatteries(ItemStack ist, EntityPlayer player, int damageToBeDone)
	{
		if (!player.capabilities.isCreativeMode)
		{
			boolean usedBattery = false;
			IInventory inventory = player.inventory;

			for (int slot = 0; slot < inventory.getSizeInventory(); slot++)
			{
				ItemStack stack = inventory.getStackInSlot(slot);

				if (stack == null)
				{
					continue;
				}

				if (stack.getItem() instanceof EnergyBattery && !usedBattery)
				{
					if (stack.getItemDamage() <= stack.getMaxDamage() - damageToBeDone)
					{
						stack.setItemDamage(stack.getItemDamage() + damageToBeDone);
						usedBattery = true;
					}
				}
			}

			if (!usedBattery)
			{
				return false;
			}

			return true;
		}
		else
		{
			return true;
		}
	}

	@Override
	public void onArmourUpdate(World world, EntityPlayer player,
			ItemStack thisItemStack)
	{
		// TODO Auto-generated method stub
		player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2, 9));
		player.extinguish();
	}

	@Override
	public boolean isUpgrade()
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getEnergyForTenSeconds()
	{
		// TODO Auto-generated method stub
		return 100;
	}
}
