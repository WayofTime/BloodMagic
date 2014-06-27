package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.items.EnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WaterSigil extends ItemBucket implements ArmourUpgrade
{
    /**
     * field for checking if the bucket has been filled.
     */
    private Block isFull = Blocks.water;
    private int energyUsed;

    public WaterSigil()
    {
        super(Blocks.water);
        this.maxStackSize = 1;
        //setMaxDamage(1000);
        setEnergyUsed(100);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par1ItemStack.stackTagCompound == null)
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon("AlchemicalWizardry:WaterSigil");
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        ItemStack copiedStack = itemStack.copy();
        copiedStack.setItemDamage(copiedStack.getItemDamage() + 1);
        copiedStack.stackSize = 1;
        return copiedStack;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Infinite water, anyone?");

        if (!(par1ItemStack.stackTagCompound == null))
        {
            par3List.add("Current owner: " + par1ItemStack.stackTagCompound.getString("ownerName"));
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        EnergyItems.checkAndSetItemOwner(par1ItemStack, par3EntityPlayer);

        if (par3EntityPlayer.isSneaking())
        {
            return par1ItemStack;
        }

        float f = 1.0F;
        double d0 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double) f;
        double d1 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double) f + 1.62D - (double) par3EntityPlayer.yOffset;
        double d2 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double) f;
        //boolean flag = this.isFull == 0;
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, false);

        if (movingobjectposition == null)
        {
            return par1ItemStack;
        } else
        {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                if (!par2World.canMineBlock(par3EntityPlayer, i, j, k))
                {
                    return par1ItemStack;
                }
                
                TileEntity tile = par2World.getTileEntity(i, j, k);
                if(tile instanceof IFluidHandler)
                {
                	FluidStack fluid = new FluidStack(FluidRegistry.WATER,1000);
                	int amount = ((IFluidHandler) tile).fill(ForgeDirection.getOrientation(movingobjectposition.sideHit), fluid, false);
                	
                	if(amount>0)
                	{
                		((IFluidHandler) tile).fill(ForgeDirection.getOrientation(movingobjectposition.sideHit), fluid, true);
                		if (!par3EntityPlayer.capabilities.isCreativeMode)
                        {
                            if (!EnergyItems.syphonBatteries(par1ItemStack, par3EntityPlayer, getEnergyUsed()))
                            {
                            }
                        }
                	}
                	
                	return par1ItemStack;
                }
                
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
                        } else
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
        if (!par1World.isAirBlock(par8, par9, par10) && par1World.getBlock(par8, par9, par10).getMaterial().isSolid())
        {
            return false;
        } else if ((par1World.getBlock(par8, par9, par10) == Blocks.water || par1World.getBlock(par8, par9, par10) == Blocks.flowing_water) && par1World.getBlockMetadata(par8, par9, par10) == 0)
        {
            return false;
        } else
        {
            if (par1World.provider.isHellWorld)
            {
                par1World.playSoundEffect(par2 + 0.5D, par4 + 0.5D, par6 + 0.5D, "random.fizz", 0.5F, 2.6F + (par1World.rand.nextFloat() - par1World.rand.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l)
                {
                    par1World.spawnParticle("largesmoke", (double) par8 + Math.random(), (double) par9 + Math.random(), (double) par10 + Math.random(), 0.0D, 0.0D, 0.0D);
                }
            } else
            {
                par1World.setBlock(par8, par9, par10, this.isFull, 0, 3);
            }

            return true;
        }
    }

    protected void setEnergyUsed(int par1int)
    {
        this.energyUsed = par1int;
    }

    protected int getEnergyUsed()
    {
        return this.energyUsed;
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
//        }
//
//        if (player.func_110143_aJ() <= 0)
//        {
//            player.inventory.dropAllItems();
//        }
//    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player,
                               ItemStack thisItemStack)
    {
        // TODO Auto-generated method stub
        //PotionEffect effect = new PotionEffect(Potion.waterBreathing.id, 2,9);
        player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 2, 9,true));
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
