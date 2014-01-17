package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.common.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.common.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AirSigil extends EnergyItems implements ArmourUpgrade
{
	public AirSigil(int id)
	{
		super(id);
		maxStackSize = 1;
		//setMaxDamage(1000);
		setEnergyUsed(50);
		setCreativeTab(AlchemicalWizardry.tabBloodMagic);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("I feel lighter already...");

		if (!(par1ItemStack.stackTagCompound == null))
		{
			par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon("AlchemicalWizardry:AirSigil");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

		if (par3EntityPlayer.isSneaking())
		{
			return par1ItemStack;
		}

		Vec3 vec = par3EntityPlayer.getLookVec();
		double wantedVelocity = 1.7;

		if (par3EntityPlayer.isPotionActive(AlchemicalWizardry.customPotionBoost))
		{
			int i = par3EntityPlayer.getActivePotionEffect(AlchemicalWizardry.customPotionBoost).getAmplifier();
			wantedVelocity += (1 + i) * 0.35;
		}

		par3EntityPlayer.motionX = vec.xCoord * wantedVelocity;
		par3EntityPlayer.motionY = vec.yCoord * wantedVelocity;
		par3EntityPlayer.motionZ = vec.zCoord * wantedVelocity;
		par2World.playSoundEffect((float)par3EntityPlayer.posX + 0.5F, (float)par3EntityPlayer.posY + 0.5F, (float)par3EntityPlayer.posZ + 0.5F, "random.fizz", 0.5F, 2.6F + (par2World.rand.nextFloat() - par2World.rand.nextFloat()) * 0.8F);
		par3EntityPlayer.fallDistance = 0;

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

		return par1ItemStack;
	}

	@Override
	public void onArmourUpdate(World world, EntityPlayer player,
			ItemStack thisItemStack)
	{
		// TODO Auto-generated method stub
		player.fallDistance = 0;
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
		return 50;
	}
}
