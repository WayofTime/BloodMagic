package WayofTime.alchemicalWizardry.common.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.tileEntity.TESocket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSocket extends BlockContainer
{
	@SideOnly(Side.CLIENT)
	private static Icon topIcon;
	@SideOnly(Side.CLIENT)
	private static Icon sideIcon1;
	@SideOnly(Side.CLIENT)
	private static Icon sideIcon2;
	@SideOnly(Side.CLIENT)
	private static Icon bottomIcon;
	public BlockSocket(int id)
	{
		super(id, Material.rock);
		setHardness(2.0F);
		setResistance(5.0F);
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
		setUnlocalizedName("bloodSocket");
		//func_111022_d("AlchemicalWizardry:blocks");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		topIcon = iconRegister.registerIcon("AlchemicalWizardry:BloodSocket");
		sideIcon1 = iconRegister.registerIcon("AlchemicalWizardry:BloodSocket");
		sideIcon2 = iconRegister.registerIcon("AlchemicalWizardry:BloodSocket");
		bottomIcon = iconRegister.registerIcon("AlchemicalWizardry:BloodSocket");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta)
	{
		switch (side)
		{
		case 0:
			return bottomIcon;

		case 1:
			return topIcon;

			//case 2: return sideIcon1;
			//case 3: return sideIcon1;
			//case 4: return sideIcon2;
			//case 5: return sideIcon2;
		default:
			return sideIcon2;
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int idk, float what, float these, float are)
	{
		TESocket tileEntity = (TESocket)world.getBlockTileEntity(x, y, z);

		if (tileEntity == null || player.isSneaking())
		{
			return false;
		}

		ItemStack playerItem = player.getCurrentEquippedItem();

		if (tileEntity.getStackInSlot(0) == null && playerItem != null)
		{
			if (playerItem.getItem() instanceof ArmourUpgrade)
			{
				ItemStack newItem = playerItem.copy();
				newItem.stackSize = 1;
				--playerItem.stackSize;
				tileEntity.setInventorySlotContents(0, newItem);
			}
		}
		else if (tileEntity.getStackInSlot(0) != null && playerItem == null)
		{
			/**stub method
			 * Add the item that is in the slot to the player's inventory, and
			 * then set the slot to null.
			 */
			 player.inventory.addItemStackToInventory(tileEntity.getStackInSlot(0));
			 tileEntity.setInventorySlotContents(0, null);
			 tileEntity.setActive();
		}

		world.markBlockForUpdate(x, y, z);
		//player.openGui(AlchemicalWizardry.instance, 0, world, x, y, z);
		//PacketDispatcher.sendPacketToServer(tileEntity.getDescriptionPacket());
		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}

	private void dropItems(World world, int x, int y, int z)
	{
		Random rand = new Random();
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (!(tileEntity instanceof IInventory))
		{
			return;
		}

		IInventory inventory = (IInventory) tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			ItemStack item = inventory.getStackInSlot(i);

			if (item != null && item.stackSize > 0)
			{
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;
				EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound())
				{
					entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
				}

				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TESocket();
	}
}
