package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import WayofTime.alchemicalWizardry.common.tileEntity.TESocket;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.items.Orb;
import WayofTime.alchemicalWizardry.common.items.BindableItems;

public class SigilLava extends ItemBucket implements ArmourUpgrade, ISigil
{
    /**
     * field for checking if the bucket has been filled.
     */
    private Block isFull = Blocks.lava;
    private int energyUsed;

    public SigilLava()
    {
        super(Blocks.lava);
        setEnergyUsed(1000);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        ItemStack copiedStack = itemStack.copy();
        copiedStack.setItemDamage(copiedStack.getItemDamage() + getEnergyUsed());
        copiedStack.stackSize = 1;
        return copiedStack;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.lavasigil.desc1"));
        par3List.add(StatCollector.translateToLocal("tooltip.lavasigil.desc2"));

        if (!(par1ItemStack.getTagCompound() == null))
        {
            par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + par1ItemStack.getTagCompound().getString("ownerName"));
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
            FluidStack fluid = new FluidStack(FluidRegistry.LAVA, 1000);
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

        {
            int x = blockPos.getX();
            int y = blockPos.getY();
            int z = blockPos.getZ();

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

            if (!player.func_175151_a(new BlockPos(x, y, z), side, stack))
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

    @Override
    /**
     * Attempts to place the liquid contained inside the bucket.
     */
    public boolean func_180616_a(World par1World, BlockPos blockPos)
    {
        if (!par1World.isAirBlock(blockPos) && par1World.getBlockState(blockPos).getBlock().getMaterial().isSolid())
        {
            return false;
        } else if ((par1World.getBlockState(blockPos).getBlock() == Blocks.lava || par1World.getBlockState(blockPos).getBlock() == Blocks.flowing_lava) && par1World.getBlockState(blockPos).getBlock().getMetaFromState(par1World.getBlockState(blockPos)) == 0)
        {
            return false;
        } else
        {
            par1World.setBlockState(blockPos, this.isFull.getBlockState().getBaseState());
            return true;
        }
    }
    
    public boolean canPlaceContainedLiquid(World par1World, BlockPos blockPos)
    {
        if (!par1World.isAirBlock(blockPos) && par1World.getBlockState(blockPos).getBlock().getMaterial().isSolid())
        {
            return false;
        } else if ((par1World.getBlockState(blockPos).getBlock() == Blocks.lava || par1World.getBlockState(blockPos).getBlock() == Blocks.flowing_lava) && par1World.getBlockState(blockPos).getBlock().getMetaFromState(par1World.getBlockState(blockPos)) == 0)
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

    protected boolean syphonBatteries(ItemStack ist, EntityPlayer player, int damageToBeDone)
    {
        if (!player.capabilities.isCreativeMode)
        {
            boolean usedBattery = false;
            IInventory inventory = player.inventory;

            for (int slot = 0; slot < inventory.getSizeInventory(); slot++)
            {
                ItemStack stack = inventory.getStackInSlot(slot);

                if (stack == null)
                {
                    continue;
                }
                if (stack.getItem() instanceof Orb && !usedBattery)
                {
                    if (stack.getItemDamage() <= stack.getMaxDamage() - damageToBeDone)
                    {
                        stack.setItemDamage(stack.getItemDamage() + damageToBeDone);
                        usedBattery = true;
                    }
                }
            }

            return usedBattery;
        } else
        {
            return true;
        }
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack)
    {
        player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 2, 9, true, false));
        player.extinguish();
    }

    @Override
    public boolean isUpgrade()
    {
        return true;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        return 100;
    }
}
