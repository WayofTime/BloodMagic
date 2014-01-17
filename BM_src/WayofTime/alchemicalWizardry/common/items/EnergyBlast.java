package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.entity.projectile.EnergyBlastProjectile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EnergyBlast extends EnergyItems
{
	private static Icon activeIcon;
	private static Icon passiveIcon;
	private static int damage;
	//private static int delay;
	private static final int maxDelay = 15;
	public EnergyBlast(int id)
	{
		super(id);
		setMaxStackSize(1);
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
		setUnlocalizedName("energyBlaster");
		setFull3D();
		setMaxDamage(250);
		setEnergyUsed(150);
		damage = 12;
		//delay = 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:EnergyBlaster_activated");
		activeIcon = iconRegister.registerIcon("AlchemicalWizardry:EnergyBlaster_activated");
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

		if (getDelay(par1ItemStack) > 0)
		{
			return par1ItemStack;
		}

		if (!par3EntityPlayer.capabilities.isCreativeMode)
		{
			syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed());
		}

		par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!par2World.isRemote)
		{
			//par2World.spawnEntityInWorld(new EnergyBlastProjectile(par2World, par3EntityPlayer, damage));
			par2World.spawnEntityInWorld(new EnergyBlastProjectile(par2World, par3EntityPlayer, damage));
			setDelay(par1ItemStack, maxDelay);
		}

		return par1ItemStack;
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
		int delay = getDelay(par1ItemStack);

		if (!par2World.isRemote && delay > 0)
		{
			setDelay(par1ItemStack, delay - 1);
		}

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

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Used to fire devastating");
		par3List.add("projectiles.");
		par3List.add("Damage: " + damage);

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

	public void setDelay(ItemStack par1ItemStack, int newDelay)
	{
		NBTTagCompound itemTag = par1ItemStack.stackTagCompound;

		if (itemTag == null)
		{
			par1ItemStack.setTagCompound(new NBTTagCompound());
		}

		itemTag.setInteger("delay", newDelay);
	}

	public int getDelay(ItemStack par1ItemStack)
	{
		NBTTagCompound itemTag = par1ItemStack.stackTagCompound;

		if (itemTag == null)
		{
			par1ItemStack.setTagCompound(new NBTTagCompound());
		}

		return itemTag.getInteger("delay");
	}
}
