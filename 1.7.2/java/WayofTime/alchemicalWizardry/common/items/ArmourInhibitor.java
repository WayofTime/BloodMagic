package WayofTime.alchemicalWizardry.common.items;

import java.util.List;

import javax.swing.Icon;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ArmourInhibitor extends EnergyItems
{
    private static IIcon activeIcon;
    private static IIcon passiveIcon;
    private int tickDelay = 200;

    public ArmourInhibitor()
    {
        super();
        this.maxStackSize = 1;
        //setMaxDamage(1000);
        setEnergyUsed(0);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Used to suppress a soul's");
        par3List.add("unnatural abilities.");

        if (!(par1ItemStack.stackTagCompound == null))
        {
            if (par1ItemStack.stackTagCompound.getBoolean("isActive"))
            {
                par3List.add("Activated");
            } else
            {
                par3List.add("Deactivated");
            }

            par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:ArmourInhibitor_deactivated");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:ArmourInhibitor_activated");
        this.passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:ArmourInhibitor_deactivated");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        if (stack.stackTagCompound == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = stack.stackTagCompound;

        if (tag.getBoolean("isActive"))
        {
            return this.activeIcon;
        } else
        {
            return this.passiveIcon;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1)
    {
        if (par1 == 1)
        {
            return this.activeIcon;
        } else
        {
            return this.passiveIcon;
        }
    }

//	@Override
//	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
//	{
//
//		if(applyBonemeal(par1ItemStack,par3World,par4,par5,par6,par2EntityPlayer))
//		{
//			if (par3World.isRemote)
//            {
//                par3World.playAuxSFX(2005, par4, par5, par6, 0);
//                EnergyItems.syphonBatteries(par1ItemStack, par2EntityPlayer, getEnergyUsed());
//                return true;
//            }
//			return true;
//		}
//		return false;
//	}

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound tag = par1ItemStack.stackTagCompound;
        tag.setBoolean("isActive", !(tag.getBoolean("isActive")));

        if (tag.getBoolean("isActive"))
        {
            par1ItemStack.setItemDamage(1);
            tag.setInteger("worldTimeDelay", (int) (par2World.getWorldTime() - 1) % tickDelay);

            if (!par3EntityPlayer.capabilities.isCreativeMode)
            {
                //EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed());
            }
        } else
        {
            par1ItemStack.setItemDamage(par1ItemStack.getMaxDamage());
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

        EntityPlayer par3EntityPlayer = (EntityPlayer) par3Entity;

        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        if (par1ItemStack.stackTagCompound.getBoolean("isActive"))
        {
            if (par2World.getWorldTime() % tickDelay == par1ItemStack.stackTagCompound.getInteger("worldTimeDelay") && par3Entity instanceof EntityPlayer)
            {
                //EnergyItems.syphonBatteries(par1ItemStack, (EntityPlayer)par3Entity, getEnergyUsed());
            }

            //TODO Do stuff
            par3EntityPlayer.addPotionEffect(new PotionEffect(AlchemicalWizardry.customPotionInhibit.id, 2, 0));
        }

        return;
    }

//	@Override
//	public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack) {
//
//		int range = 5;
//    	int verticalRange = 2;
//    	int posX = (int)Math.round(player.posX-0.5f);
//       	int posY = (int)player.posY;
//       	int posZ = (int)Math.round(player.posZ-0.5f);
//
//       	for(int ix=posX-range;ix<=posX+range;ix++)
//       	{
//       		for(int iz=posZ-range;iz<=posZ+range;iz++)
//       		{
//       			for(int iy=posY-verticalRange;iy<=posY+verticalRange;iy++)
//       			{
//       				int id = world.getBlockId(ix, iy, iz);
//       				Block block = Block.blocksList[id];
//       				if(block instanceof IPlantable)
//       				{
//       					if(world.rand.nextInt(10)==0)
//       						block.updateTick(world, ix, iy, iz, world.rand);
//       				}
//       			}
//       		}
//       	}
//
//	}

//	@Override
//	public boolean isUpgrade() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public int getEnergyForTenSeconds() {
//		// TODO Auto-generated method stub
//		return 50;
//	}
}
