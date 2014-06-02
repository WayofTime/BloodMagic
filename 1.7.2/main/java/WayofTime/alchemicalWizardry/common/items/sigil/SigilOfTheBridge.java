package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import javax.swing.Icon;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.ModBlocks;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SigilOfTheBridge extends EnergyItems implements ArmourUpgrade
{
    private static IIcon activeIcon;
    private static IIcon passiveIcon;
    private int tickDelay = 200;

    public SigilOfTheBridge()
    {
        super();
        this.maxStackSize = 1;
        //setMaxDamage(1000);
        setEnergyUsed(100);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Activate to create a bridge");
        par3List.add("beneath your feet.");

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
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:BridgeSigil_deactivated");
        this.activeIcon = iconRegister.registerIcon("AlchemicalWizardry:BridgeSigil_activated");
        this.passiveIcon = iconRegister.registerIcon("AlchemicalWizardry:BridgeSigil_deactivated");
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
                EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed());
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
                EnergyItems.syphonBatteries(par1ItemStack, (EntityPlayer) par3Entity, this.getLPUsed(par1ItemStack));
                this.setLPUsed(par1ItemStack, 0);
            }

//        	if(par2World.isRemote)
//            {
//        		return;
//            }
            if (!par3EntityPlayer.onGround && !par3EntityPlayer.isSneaking())
            {
                return;
            }

            int range = 2;
            int verticalOffset = -1;

            if (par3EntityPlayer.isSneaking())
            {
                verticalOffset--;
            }

            if (par2World.isRemote)
            {
                verticalOffset--;
            }

            int posX = (int) Math.round(par3Entity.posX - 0.5f);
            int posY = (int) par3Entity.posY;
            int posZ = (int) Math.round(par3Entity.posZ - 0.5f);
            int incremented = 0;

            for (int ix = posX - range; ix <= posX + range; ix++)
            {
                for (int iz = posZ - range; iz <= posZ + range; iz++)
                {
                    //for(int iy=posY-verticalRange;iy<=posY+verticalRange;iy++)
                    {
                        Block block = par2World.getBlock(ix, posY + verticalOffset, iz);
                        

                        if (par2World.isAirBlock(ix, posY + verticalOffset, iz))
                        {
                            par2World.setBlock(ix, posY + verticalOffset, iz, ModBlocks.spectralBlock, 0, 3);

                            if (par2World.rand.nextInt(2) == 0)
                            {
                                incremented++;
                            }
                        }
                    }
                }
            }

            this.incrimentLPUSed(par1ItemStack, incremented);
        }

        return;
    }

    public int getLPUsed(ItemStack par1ItemStack)
    {
        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        return par1ItemStack.stackTagCompound.getInteger("LPUsed");
    }

    public void incrimentLPUSed(ItemStack par1ItemStack, int addedLP)
    {
        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        par1ItemStack.stackTagCompound.setInteger("LPUsed", par1ItemStack.stackTagCompound.getInteger("LPUsed") + addedLP);
    }

    public void setLPUsed(ItemStack par1ItemStack, int newLP)
    {
        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        par1ItemStack.stackTagCompound.setInteger("LPUsed", newLP);
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
        if (!player.onGround && !player.isSneaking())
        {
            return;
        }

        int range = 2;
        int verticalOffset = -1;

        if (player.isSneaking())
        {
            verticalOffset--;
        }

        int posX = (int) Math.round(player.posX - 0.5f);
        int posY = (int) player.posY;
        int posZ = (int) Math.round(player.posZ - 0.5f);

        //int incremented = 0;

        for (int ix = posX - range; ix <= posX + range; ix++)
        {
            for (int iz = posZ - range; iz <= posZ + range; iz++)
            {
                //for(int iy=posY-verticalRange;iy<=posY+verticalRange;iy++)
                {
                    Block block = world.getBlock(ix, posY + verticalOffset, iz);
                    

                    if (world.isAirBlock(ix, posY + verticalOffset, iz))
                    {
                        world.setBlock(ix, posY + verticalOffset, iz, ModBlocks.spectralBlock, 0, 3);
                    }
                }
            }
        }
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
