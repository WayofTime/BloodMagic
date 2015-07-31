package WayofTime.alchemicalWizardry.common.items.sigil;

import java.util.List;

import WayofTime.alchemicalWizardry.api.items.interfaces.ISigil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import WayofTime.alchemicalWizardry.api.items.interfaces.ArmourUpgrade;
import WayofTime.alchemicalWizardry.common.items.BindableItems;
import WayofTime.alchemicalWizardry.common.spell.complex.effect.SpellHelper;

public class SigilVoid extends ItemBucket implements ArmourUpgrade, ISigil
{
    private int energyUsed;

    public SigilVoid()
    {
        super(null);
        setEnergyUsed(50);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add(StatCollector.translateToLocal("tooltip.voidsigil.desc"));

        if (!(stack.getTagCompound() == null))
        {
            par3List.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + stack.getTagCompound().getString("ownerName"));
        }
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        ItemStack copiedStack = itemStack.copy();
        copiedStack.setItemDamage(copiedStack.getItemDamage() + getEnergyUsed());
        copiedStack.stackSize = 1;
        return copiedStack;
    }

    protected void setEnergyUsed(int par1int)
    {
        this.energyUsed = par1int;
    }

    protected int getEnergyUsed()
    {
        return this.energyUsed;
    }

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
            FluidStack amount = ((IFluidHandler) tile).drain(side, 1000, false);

            if (amount != null && amount.amount > 0 && BindableItems.syphonBatteries(stack, player, getEnergyUsed()))
            {
                ((IFluidHandler) tile).drain(side, 1000, true);
                return true;
            }

            return false;
        }

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
        
        if (!player.func_175151_a(new BlockPos(x, y, z), side, stack)) //was canPlayerEdit
        {
            return false;
        }

        if (SpellHelper.isBlockFluid(world.getBlockState(new BlockPos(x, y, z)).getBlock()) && BindableItems.syphonBatteries(stack, player, getEnergyUsed()))
        {
            world.setBlockToAir(new BlockPos(x, y, z));
            return true;
        }
        

        return false;
    }

    @Override
    /**
     * Attempts to place the liquid contained inside the bucket.
     */
    public boolean func_180616_a(World world, BlockPos blockPos) //was tryPlaceContainedLiquid
    {
        return false;
    }

    @Override
    public void onArmourUpdate(World world, EntityPlayer player, ItemStack thisItemStack) {}

    @Override
    public boolean isUpgrade()
    {
        return true;
    }

    @Override
    public int getEnergyForTenSeconds()
    {
        return 25;
    }
}
