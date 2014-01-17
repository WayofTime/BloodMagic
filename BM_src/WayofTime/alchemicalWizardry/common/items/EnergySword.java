package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EnergySword extends ItemSword
{
	private static Icon activeIcon;
	private static Icon passiveIcon;

	private int energyUsed;

	public EnergySword(int id)
	{
		super(id, AlchemicalWizardry.bloodBoundToolMaterial);
		maxStackSize = 1;
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
		setEnergyUsed(50);
		setFull3D();
		setMaxDamage(100);
		//weaponDamaged = 12.0F;
	}

	public void setEnergyUsed(int i)
	{
		energyUsed = i;
	}

	public int getEnergyUsed()
	{
		return energyUsed;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundSword_activated");
		activeIcon = iconRegister.registerIcon("AlchemicalWizardry:BoundSword_activated");
		passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:SheathedItem");
	}

	@Override
	public Icon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
	{
		if (stack.stackTagCompound == null)
		{
			stack.setTagCompound(new NBTTagCompound());
		}

		NBTTagCompound tag = stack.stackTagCompound;

		if (tag.getBoolean("isActive"))
		{
			return activeIcon;
		}
		else
		{
			return passiveIcon;
		}
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		return !getActivated(stack);
	}

	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
	{
		if (par3EntityLivingBase instanceof EntityPlayer)
		{
			EnergyItems.checkAndSetItemOwner(par1ItemStack, (EntityPlayer)par3EntityLivingBase);

			if (!EnergyItems.syphonBatteries(par1ItemStack, (EntityPlayer)par3EntityLivingBase, getEnergyUsed()))
			{
				//this.damagePlayer(null, (EntityPlayer)par3EntityLivingBase, (this.getEnergyUsed() + 99) / 100);
			}
		}

		par2EntityLivingBase.addPotionEffect(new PotionEffect(Potion.weakness.id, 60, 2));
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

		if (par3EntityPlayer.isSneaking())
		{
			setActivated(par1ItemStack, !getActivated(par1ItemStack));
			par1ItemStack.stackTagCompound.setInteger("worldTimeDelay", (int)(par2World.getWorldTime() - 1) % 100);
			return par1ItemStack;
		}

		if (!getActivated(par1ItemStack))
		{
			return par1ItemStack;
		}

		return par1ItemStack;
	}

	@Override
	public int getItemEnchantability()
	{
		return 30;
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
	{
		if (!(par3Entity instanceof EntityPlayer))
		{
			return;
		}

		EntityPlayer par3EntityPlayer = (EntityPlayer)par3Entity;

		if (par1ItemStack.stackTagCompound == null)
		{
			par1ItemStack.setTagCompound(new NBTTagCompound());
		}

		//        if(par1ItemStack.stackTagCompound.getBoolean("isActive"))
		//        {
		//        	EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, 1);
		//        }

		if (par2World.getWorldTime() % 100 == par1ItemStack.stackTagCompound.getInteger("worldTimeDelay") && par1ItemStack.stackTagCompound.getBoolean("isActive"))
		{
			if (!par3EntityPlayer.capabilities.isCreativeMode)
			{
				EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, 50);
			}
		}

		par1ItemStack.setItemDamage(0);
		return;
	}

	public void setActivated(ItemStack par1ItemStack, boolean newActivated)
	{
		NBTTagCompound itemTag = par1ItemStack.stackTagCompound;

		if (itemTag == null)
		{
			par1ItemStack.setTagCompound(new NBTTagCompound());
		}

		itemTag.setBoolean("isActive", newActivated);
	}

	public boolean getActivated(ItemStack par1ItemStack)
	{
		NBTTagCompound itemTag = par1ItemStack.stackTagCompound;

		if (itemTag == null)
		{
			par1ItemStack.setTagCompound(new NBTTagCompound());
		}

		return itemTag.getBoolean("isActive");
	}

	//    public int getDamageVsEntity(Entity par1Entity)
	//    {
	//        return (int) this.weaponDamage;
	//    }

	@Override
	public float func_82803_g()
	{
		return 4.0F;
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Caution: may cause");
		par3List.add("a bad day...");

		if (!(par1ItemStack.stackTagCompound == null))
		{
			if (par1ItemStack.stackTagCompound.getBoolean("isActive"))
			{
				par3List.add("Activated");
			}
			else
			{
				par3List.add("Deactivated");
			}

			if (!par1ItemStack.stackTagCompound.getString("ownerName").equals(""))
			{
				par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
			}
		}
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

	//    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
	//    {
	//        return false;
	//    }
}
