package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.FakePlayer;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.IBindable;
import WayofTime.alchemicalWizardry.common.tileEntity.TEAltar;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDiabloKey extends EnergyItems
{
	public ItemDiabloKey(int id)
	{
		super(id);
		setMaxStackSize(1);
		//setMaxDamage(damage);
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
		setEnergyUsed(1000);
		hasSubtypes = true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:DiabloKey");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Binds other items to the owner's network");

		//par3List.add("LP: " + (this.getMaxDamage() - this.getDamage(par1ItemStack)));
		if (!(par1ItemStack.stackTagCompound == null))
		{
			par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
		}

		//par3List.add("LP: " + par2EntityPlayer.getEntityData().getInteger("currentEssence"));
	}
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);
		World world = par3EntityPlayer.worldObj;

		if (par3EntityPlayer instanceof FakePlayer || par3EntityPlayer instanceof EntityPlayerMP)
		{
			return par1ItemStack;
		}

		if (world != null)
		{
			double posX = par3EntityPlayer.posX;
			double posY = par3EntityPlayer.posY;
			double posZ = par3EntityPlayer.posZ;
			world.playSoundEffect((float)posX + 0.5F, (float)posY + 0.5F, (float)posZ + 0.5F, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
			PacketDispatcher.sendPacketToAllAround(posX, posY, posZ, 20, world.provider.dimensionId, TEAltar.getParticlePacket(posX, posY, posZ, (short)4));
		}

		if (!par3EntityPlayer.worldObj.isRemote && !par3EntityPlayer.getClass().equals(EntityPlayerMP.class))
		{
			return par1ItemStack;
		}

		if (par3EntityPlayer.isSneaking())
		{
			return par1ItemStack;
		}

		NBTTagCompound itemTag = par1ItemStack.stackTagCompound;

		if (itemTag == null || itemTag.getString("ownerName").equals(""))
		{
			return par1ItemStack;
		}

		String ownerName = itemTag.getString("ownerName");
		ItemStack[] inv = par3EntityPlayer.inventory.mainInventory;

		for (ItemStack itemStack : inv)
		{
			if (itemStack == null)
			{
				continue;
			}

			Item item = itemStack.getItem();

			if (item instanceof ItemDiabloKey)
			{
				continue;
			}

			if (item instanceof IBindable)
			{
				EnergyItems.checkAndSetItemOwner(itemStack, ownerName);
			}
		}

		return par1ItemStack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs creativeTab, List list)
	{
		list.add(new ItemStack(AlchemicalWizardry.itemKeyOfDiablo));
		ItemStack boundKey = new ItemStack(AlchemicalWizardry.itemKeyOfDiablo);
		EnergyItems.checkAndSetItemOwner(boundKey, "Server-wide Soul Network");
		list.add(boundKey);
	}
}
