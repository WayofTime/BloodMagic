package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import thaumcraft.api.IGoggles;
import thaumcraft.api.nodes.IRevealer;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.IBindable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BoundArmour extends ItemArmor implements ISpecialArmor, IRevealer, IGoggles, IBindable
{
	private static int invSize = 9;
	private static Icon helmetIcon;
	private static Icon plateIcon;
	private static Icon leggingsIcon;
	private static Icon bootsIcon;

	public BoundArmour(int par1, int armorType)
	{
		super(par1, EnumArmorMaterial.GOLD, 0, armorType);
		setMaxDamage(1000);
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
		helmetIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundHelmet");
		plateIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundPlate");
		leggingsIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundLeggings");
		bootsIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundBoots");
	}

	@Override
	@SideOnly(Side.CLIENT)

	public Icon getIconFromDamage(int par1)
	{
		if (itemID == AlchemicalWizardry.boundHelmet.itemID)
		{
			return helmetIcon;
		}

		if (itemID == AlchemicalWizardry.boundPlate.itemID)
		{
			return plateIcon;
		}

		if (itemID == AlchemicalWizardry.boundLeggings.itemID)
		{
			return leggingsIcon;
		}

		if (itemID == AlchemicalWizardry.boundBoots.itemID)
		{
			return bootsIcon;
		}

		return itemIcon;
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
	{
		return false;
	}
	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot)
	{
		if (source.equals(DamageSource.drown))
		{
			return new ArmorProperties(-1, 0, 0);
		}

		if (source.equals(DamageSource.outOfWorld))
		{
			if (isImmuneToVoid(armor))
			{
				return new ArmorProperties(-1, 3, 100000);
			}
			else
			{
				return new ArmorProperties(-1, 0, 0);
			}
		}

		ItemStack helmet = player.getCurrentItemOrArmor(4);
		ItemStack plate = player.getCurrentItemOrArmor(3);
		ItemStack leggings = player.getCurrentItemOrArmor(2);
		ItemStack boots = player.getCurrentItemOrArmor(1);

		if (helmet == null || plate == null || leggings == null || boots == null)
		{
			return new ArmorProperties(-1, 0, 0);
		}

		if (helmet.itemID == AlchemicalWizardry.boundHelmet.itemID || plate.itemID == AlchemicalWizardry.boundPlate.itemID || leggings.itemID == AlchemicalWizardry.boundLeggings.itemID || boots.itemID == AlchemicalWizardry.boundBoots.itemID)
		{
			if (source.isUnblockable())
			{
				return new ArmorProperties(-1, 3, 3);
			}

			return new ArmorProperties(-1, 3, 100000);
		}

		return new ArmorProperties(-1, 0, 0);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot)
	{
		if (armor.itemID == AlchemicalWizardry.boundHelmet.itemID)
		{
			return 3;
		}

		if (armor.itemID == AlchemicalWizardry.boundPlate.itemID)
		{
			return 8;
		}

		if (armor.itemID == AlchemicalWizardry.boundLeggings.itemID)
		{
			return 6;
		}

		if (armor.itemID == AlchemicalWizardry.boundBoots.itemID)
		{
			return 3;
		}

		return 5;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot)
	{
		if (entity instanceof EntityPlayer)
		{
			EnergyItems.checkAndSetItemOwner(stack, (EntityPlayer)entity);

			if (((EntityPlayer)entity).capabilities.isCreativeMode)
			{
				return;
			}

			//EnergyItems.syphonBatteries(stack, (EntityPlayer)entity, 200);
		}

		stack.setItemDamage(stack.getItemDamage() + damage);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Devilish Protection");

		if (!(par1ItemStack.stackTagCompound == null))
		{
			if (!par1ItemStack.stackTagCompound.getString("ownerName").equals(""))
			{
				par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
			}

			ItemStack[] inv = getInternalInventory(par1ItemStack);

			if (inv == null)
			{
				return;
			}

			for (int i = 0; i < invSize; i++)
			{
				if (inv[i] != null)
				{
					par3List.add("Item in slot " + i + ": " + inv[i].getDisplayName());
				}
			}
		}
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
	{
		//TODO Make the armour invisible when the player has Invisibility on.
		if (entity instanceof EntityLivingBase)
		{
			if (((EntityLivingBase)entity).isPotionActive(Potion.invisibility.id))
			{
				if (itemID == AlchemicalWizardry.boundHelmet.itemID || itemID == AlchemicalWizardry.boundPlate.itemID || itemID == AlchemicalWizardry.boundBoots.itemID)
				{
					return "alchemicalwizardry:models/armor/boundArmour_invisible_layer_1.png";
				}

				if (itemID == AlchemicalWizardry.boundLeggings.itemID)
				{
					return "alchemicalwizardry:models/armor/boundArmour_invisible_layer_2.png";
				}
			}
		}

		if (itemID == AlchemicalWizardry.boundHelmet.itemID || itemID == AlchemicalWizardry.boundPlate.itemID || itemID == AlchemicalWizardry.boundBoots.itemID)
		{
			return "alchemicalwizardry:models/armor/boundArmour_layer_1.png";
		}

		if (itemID == AlchemicalWizardry.boundLeggings.itemID)
		{
			return "alchemicalwizardry:models/armor/boundArmour_layer_2.png";
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);
		return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack)
	{
		if (itemStack.stackTagCompound == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		getMaxBloodShardLevel(itemStack);
		ItemStack[] inv = getInternalInventory(itemStack);

		if (inv != null)
		{
		}

		if (!player.isPotionActive(AlchemicalWizardry.customPotionInhibit))
		{
			tickInternalInventory(itemStack, world, player, 0, false);
		}

		if (itemStack.getItemDamage() > 0)
		{
			EnergyItems.checkAndSetItemOwner(itemStack, player);

			if (!player.capabilities.isCreativeMode)
			{
				EnergyItems.syphonBatteries(itemStack, player, itemStack.getItemDamage() * 75);
				itemStack.setItemDamage(0);
			}
		}

		return;
	}

	public void tickInternalInventory(ItemStack par1ItemStack, World par2World, EntityPlayer par3Entity, int par4, boolean par5)
	{
		ItemStack[] inv = getInternalInventory(par1ItemStack);

		if (inv == null)
		{
			return;
		}

		int blood = getMaxBloodShardLevel(par1ItemStack);

		//int blood = 1;
		for (int i = 0; i < invSize; i++)
		{
			if (inv[i] == null)
			{
				continue;
			}

			if (inv[i].getItem() instanceof ArmourUpgrade && blood > 0)
			{
				if (((ArmourUpgrade)inv[i].getItem()).isUpgrade())
				{
					((ArmourUpgrade)inv[i].getItem()).onArmourUpdate(par2World, par3Entity, inv[i]);
					blood--;
				}

				if (par2World.getWorldTime() % 200 == 0)
				{
					if (getUpgradeCostMultiplier(par1ItemStack) > 0.02f)
					{
						EnergyItems.syphonBatteries(par1ItemStack, par3Entity, (int)(((ArmourUpgrade)inv[i].getItem()).getEnergyForTenSeconds() * getUpgradeCostMultiplier(par1ItemStack)));
					}
				}
			}
		}
	}

	public int getMaxBloodShardLevel(ItemStack armourStack)
	{
		ItemStack[] inv = getInternalInventory(armourStack);

		if (inv == null)
		{
			return 0;
		}

		int max = 0;

		for (int i = 0; i < invSize; i++)
		{
			ItemStack itemStack = inv[i];

			if (itemStack != null)
			{
				if (itemStack.itemID == AlchemicalWizardry.weakBloodShard.itemID)
				{
					max = Math.max(max, 1);
				}

				if (itemStack.itemID == AlchemicalWizardry.demonBloodShard.itemID)
				{
					max = Math.max(max, 2);
				}
			}
		}

		return max;
	}

	public boolean hasAddedToInventory(ItemStack sigilItemStack, ItemStack addedItemStack)
	{
		ItemStack[] inv = getInternalInventory(sigilItemStack);

		if (inv == null)
		{
			return false;
		}

		if (addedItemStack == null)
		{
			return false;
		}

		int candidateSlot = -1;

		for (int i = invSize - 1; i >= 0; i--)
		{
			ItemStack nextItem = inv[i];

			if (nextItem == null)
			{
				candidateSlot = i;
				continue;
			}
		}

		if (candidateSlot == -1)
		{
			return false;
		}

		if (addedItemStack.getItem() instanceof ArmourUpgrade)
		{
			inv[candidateSlot] = addedItemStack;
			saveInternalInventory(sigilItemStack, inv);
			return true;
		}

		return false;
	}

	public ItemStack[] getInternalInventory(ItemStack itemStack)
	{
		NBTTagCompound itemTag = itemStack.stackTagCompound;

		if (itemTag == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
			return null;
		}

		ItemStack[] inv = new ItemStack[9];
		NBTTagList tagList = itemTag.getTagList("Inventory");

		if (tagList == null)
		{
			return null;
		}

		for (int i = 0; i < tagList.tagCount(); i++)
		{
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			int slot = tag.getByte("Slot");

			if (slot >= 0 && slot < invSize)
			{
				inv[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}

		return inv;
	}

	public void saveInternalInventory(ItemStack itemStack, ItemStack[] inventory)
	{
		NBTTagCompound itemTag = itemStack.stackTagCompound;

		if (itemTag == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		NBTTagList itemList = new NBTTagList();

		for (int i = 0; i < invSize; i++)
		{
			if (inventory[i] != null)
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}

		itemTag.setTag("Inventory", itemList);
	}

	public boolean isImmuneToVoid(ItemStack itemStack)
	{
		ItemStack[] inv = getInternalInventory(itemStack);

		if (inv == null)
		{
			return false;
		}

		for (ItemStack item : inv)
		{
			if (item == null)
			{
				continue;
			}

			if (item.itemID == AlchemicalWizardry.voidSigil.itemID)
			{
				return true;
			}
		}

		return false;
	}

	public boolean hasIRevealer(ItemStack itemStack)
	{
		ItemStack[] inv = getInternalInventory(itemStack);

		if (inv == null)
		{
			return false;
		}

		for (ItemStack item : inv)
		{
			if (item == null)
			{
				continue;
			}

			if (item.getItem() instanceof IRevealer)
			{
				return true;
			}
		}

		return false;
	}

	public boolean hasIGoggles(ItemStack itemStack)
	{
		ItemStack[] inv = getInternalInventory(itemStack);

		if (inv == null)
		{
			return false;
		}

		for (ItemStack item : inv)
		{
			if (item == null)
			{
				continue;
			}

			if (item.getItem() instanceof IGoggles)
			{
				return true;
			}
		}

		return false;
	}

	public float getUpgradeCostMultiplier(ItemStack itemStack)
	{
		ItemStack[] inv = getInternalInventory(itemStack);

		if (inv == null)
		{
			return 1.0f;
		}

		for (ItemStack item : inv)
		{
			if (item == null)
			{
				continue;
			}

			if (item.itemID == AlchemicalWizardry.weakBloodOrb.itemID)
			{
				return 0.75f;
			}

			if (item.itemID == AlchemicalWizardry.apprenticeBloodOrb.itemID)
			{
				return 0.50f;
			}

			if (item.itemID == AlchemicalWizardry.magicianBloodOrb.itemID)
			{
				return 0.25f;
			}

			if (item.itemID == AlchemicalWizardry.masterBloodOrb.itemID)
			{
				return 0.0f;
			}

			if (item.itemID == AlchemicalWizardry.archmageBloodOrb.itemID)
			{
				return 0.0f;
			}
		}

		return 1.0f;
	}

	@Override
	public int getItemEnchantability()
	{
		return 0;
	}

	@Override
	public boolean showNodes(ItemStack itemstack, EntityLivingBase player)
	{
		return hasIRevealer(itemstack);
	}

	@Override
	public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player)
	{
		return hasIGoggles(itemstack);
	}
}
