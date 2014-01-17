package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.items.EnergyBattery;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class VoidSigil extends ItemBucket implements ArmourUpgrade
{
	private int isFull;
	private int energyUsed;
	public VoidSigil(int id)
	{
		super(id, 0);
		maxStackSize = 1;
		//setMaxDamage(1000);
		setEnergyUsed(50);
		isFull = 0;
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:VoidSigil");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Better than a Swiffer!");

		if (!(par1ItemStack.stackTagCompound == null))
		{
			par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
		}
	}

	@Override
	public ItemStack getContainerItemStack(ItemStack itemStack)
	{
		ItemStack copiedStack = itemStack.copy();
		copiedStack.setItemDamage(copiedStack.getItemDamage() + getEnergyUsed());
		copiedStack.stackSize = 1;
		return copiedStack;
	}

	protected void setEnergyUsed(int par1int)
	{
		energyUsed = par1int;
	}

	protected int getEnergyUsed()
	{
		return energyUsed;
	}

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
	//        }
	//
	//        if (player.func_110143_aJ() <= 0)
	//        {
	//            player.inventory.dropAllItems();
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
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

		if (par3EntityPlayer.isSneaking())
		{
			return par1ItemStack;
		}

		boolean flag = isFull == 0;
		MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, flag);

		if (movingobjectposition == null)
		{
			return par1ItemStack;
		}
		else
		{
			FillBucketEvent event = new FillBucketEvent(par3EntityPlayer, par1ItemStack, par2World, movingobjectposition);

			if (MinecraftForge.EVENT_BUS.post(event))
			{
				return par1ItemStack;
			}

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
					if (!par3EntityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, par1ItemStack))
					{
						return par1ItemStack;
					}

					if (par2World.getBlockMaterial(i, j, k) instanceof MaterialLiquid)
					{
						par2World.setBlockToAir(i, j, k);

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
		return false;
	}

	@Override
	public void onArmourUpdate(World world, EntityPlayer player,
			ItemStack thisItemStack)
	{
		// TODO Auto-generated method stub
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
		return 25;
	}
}
