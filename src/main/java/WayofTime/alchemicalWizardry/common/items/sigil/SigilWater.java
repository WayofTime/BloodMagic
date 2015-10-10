package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.tileEntity.TESocket;
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
import net.minecraft.util.StatCollector;
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

public class SigilWater extends ItemBucket implements ArmourUpgrade, ISigil
{
    private Block isFull = Blocks.water;
    private int energyUsed;

    public SigilWater()
    {
        super(Blocks.water);
        this.maxStackSize = 1;
        setEnergyUsed(AlchemicalWizardry.sigilWaterCost);
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player)
    {
        if (stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
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
        return itemStack;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.watersigil.desc"));

        if (!(stack.getTagCompound() == null))
        {
            par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + stack.getTagCompound().getString("ownerName"));
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
    	return stack;
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote || !EnergyItems.checkAndSetItemOwner(stack, player) || player.isSneaking())
        {
            return false;
        }
        
        if (!world.canMineBlock(player, x, y, z))
        {
            return false;
        }

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IFluidHandler)
        {
            FluidStack fluid = new FluidStack(FluidRegistry.WATER, 1000);
            int amount = ((IFluidHandler) tile).fill(ForgeDirection.getOrientation(side), fluid, false);

            if (amount > 0 && EnergyItems.syphonBatteries(stack, player, getEnergyUsed()))
            {
                ((IFluidHandler) tile).fill(ForgeDirection.getOrientation(side), fluid, true);
            }

            return false;
        }
        else if (tile instanceof TESocket)
        {
            return false;
        }

        {
            if (side == 0)
            {
                --y;
            }

            if (side == 1)
            {
                ++y;
            }

            if (side == 2)
            {
                --z;
            }

            if (side == 3)
            {
                ++z;
            }

            if (side == 4)
            {
                --x;
            }

            if (side == 5)
            {
                ++x;
            }

            if (!player.canPlayerEdit(x, y, z, side, stack))
            {
                return false;
            }

            if(this.canPlaceContainedLiquid(world, x, y, z, x, y, z) && EnergyItems.syphonBatteries(stack, player, getEnergyUsed()))
            {
            	return this.tryPlaceContainedLiquid(world, x, y, z, x, y, z);
            }
        }
            

        return false;
        
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
                par1World.markBlockForUpdate(par8, par9, par10);
            }

            return true;
        }
    }
    
    public boolean canPlaceContainedLiquid(World par1World, double par2, double par4, double par6, int par8, int par9, int par10)
    {
        if (!par1World.isAirBlock(par8, par9, par10) && par1World.getBlock(par8, par9, par10).getMaterial().isSolid())
        {
            return false;
        } else if ((par1World.getBlock(par8, par9, par10) == Blocks.water || par1World.getBlock(par8, par9, par10) == Blocks.flowing_water) && par1World.getBlockMetadata(par8, par9, par10) == 0)
        {
            return false;
        } else
        {
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

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
        player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 2, 9, true));
    }

    @Override
    public boolean isUpgrade()
    {
        return true;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        return 50;
    }
}
