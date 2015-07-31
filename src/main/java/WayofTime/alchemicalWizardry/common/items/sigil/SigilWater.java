package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.tileEntity.TESocket;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.items.BindableItems;

public class SigilWater extends ItemBucket implements ArmourUpgrade, ISigil
{
    private Block isFull = Blocks.water;
    private int energyUsed;

    public SigilWater()
    {
        super(Blocks.water);
        setEnergyUsed(100);
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

    @Override
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
    	return stack;
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote || !BindableItems.checkAndSetItemOwner(stack, player) || player.isSneaking())
        {
            return false;
        }
        
        if (!world.canMineBlockBody(player, blockPos))
        {
            return false;
        }

        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof IFluidHandler)
        {
            FluidStack fluid = new FluidStack(FluidRegistry.WATER, 1000);
            int amount = ((IFluidHandler) tile).fill(side, fluid, false);

            if (amount > 0 && BindableItems.syphonBatteries(stack, player, getEnergyUsed()))
            {
                ((IFluidHandler) tile).fill(side, fluid, true);
            }

            return false;
        }
        else if (tile instanceof TESocket)
        {
            return false;
        }

        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        {
            if (side.getIndex() == 0)
            {
                --y;
            }

            if (side.getIndex() == 1)
            {
                ++y;
            }

            if (side.getIndex() == 2)
            {
                --z;
            }

            if (side.getIndex() == 3)
            {
                ++z;
            }

            if (side.getIndex() == 4)
            {
                --x;
            }

            if (side.getIndex() == 5)
            {
                ++x;
            }

            if (!player.func_175151_a(new BlockPos(x, y, z), side, stack)) // was canPlayerEdit
            {
                return false;
            }

            if(this.canPlaceContainedLiquid(world, new BlockPos(x, y, z)) && BindableItems.syphonBatteries(stack, player, getEnergyUsed()))
            {
            	return this.func_180616_a(world, new BlockPos(x, y, z));
            }
        }

        return false;
        
    }

    /**
     * Attempts to place the liquid contained inside the bucket.
     */
    public boolean func_180616_a(World world, BlockPos blockPos) //was tryPlaceContainedLiquid
    {
        if (this.isFull == Blocks.air)
        {
            return false;
        }
        else
        {
            Material material = world.getBlockState(blockPos).getBlock().getMaterial();
            boolean flag = !material.isSolid();

            if (!world.isAirBlock(blockPos) && !flag)
            {
                return false;
            }
            else
            {
                if (world.provider.func_177500_n() && this.isFull == Blocks.flowing_water)
                {
                    int i = blockPos.getX();
                    int j = blockPos.getY();
                    int k = blockPos.getZ();
                    world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                    for (int l = 0; l < 8; ++l)
                    {
                        world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0D, 0.0D, 0.0D, 0);
                    }
                }
                else
                {
                    if (!world.isRemote && flag && !material.isLiquid())
                    {
                        world.destroyBlock(blockPos, true);
                    }

                    world.setBlockState(blockPos, this.isFull.getDefaultState(), 3);
                }

                return true;
            }
        }
    }
    
    public boolean canPlaceContainedLiquid(World world, BlockPos blockPos)
    {
        if (!world.isAirBlock(blockPos) && world.getBlockState(blockPos).getBlock().getMaterial().isSolid())
        {
            return false;
        } else if ((world.getBlockState(blockPos).getBlock() == Blocks.water || world.getBlockState(blockPos).getBlock() == Blocks.flowing_water) && world.getBlockState(blockPos).getBlock().getMetaFromState(world.getBlockState(blockPos)) == 0)
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
        player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 2, 9));
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
