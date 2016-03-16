package WayofTime.bloodmagic.item.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import WayofTime.bloodmagic.tile.TileBloodTank;

public class ItemBlockBloodTank extends ItemBlock implements IFluidContainerItem
{
    public ItemBlockBloodTank(Block block)
    {
        super(block);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("tank") && !stack.getTagCompound().getCompoundTag("tank").getString("FluidName").equals(""))
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag("tank");
            return super.getItemStackDisplayName(stack) + " (" + tag.getString("FluidName") + ")";
        } else
        {
            return super.getItemStackDisplayName(stack);
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer entityPlayer, List<String> tooltip, boolean advanced)
    {
        tooltip.add(StatCollector.translateToLocal("tooltip.BloodMagic.fluid.capacity") + ": " + String.valueOf(getCapacity(stack)) + "mB");
        if (stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag("tank");
            if (!tag.getString("FluidName").equals(""))
            {
                tooltip.add(" ");
                tooltip.add(StatCollector.translateToLocal("tooltip.BloodMagic.fluid.type") + ": " + tag.getString("FluidName"));
                tooltip.add(StatCollector.translateToLocal("tooltip.BloodMagic.fluid.amount") + ": " + tag.getInteger("Amount") + "/" + getCapacity(stack) + "mB");
            }
        }
    }

    @Override
    public FluidStack getFluid(ItemStack stack)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("tank") && !stack.getTagCompound().getCompoundTag("tank").getString("FluidName").equals(""))
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag("tank");
            return FluidStack.loadFluidStackFromNBT(tag);
        }
        return null;
    }

    @Override
    public int getCapacity(ItemStack container)
    {
        return TileBloodTank.capacity;
    }

    @Override
    public int fill(ItemStack stack, FluidStack resource, boolean doFill)
    {
        if (resource == null || stack.stackSize != 1)
            return 0;
        int fillAmount = 0, capacity = getCapacity(stack);
        NBTTagCompound tag = stack.getTagCompound(), fluidTag = null;
        FluidStack fluid = null;
        if (tag == null || !tag.hasKey("tank") || (fluidTag = tag.getCompoundTag("tank")) == null || (fluid = FluidStack.loadFluidStackFromNBT(fluidTag)) == null)
            fillAmount = Math.min(capacity, resource.amount);
        if (fluid == null)
        {
            if (doFill)
            {
                fluid = resource.copy();
                fluid.amount = 0;
            }
        } else if (!fluid.isFluidEqual(resource))
            return 0;
        else
            fillAmount = Math.min(capacity - fluid.amount, resource.amount);
        fillAmount = Math.max(fillAmount, 0);
        if (doFill)
        {
            if (tag == null)
                stack.setTagCompound(new NBTTagCompound());
            tag = stack.getTagCompound();
            fluid.amount += fillAmount;
            tag.setTag("tank", fluid.writeToNBT(fluidTag == null ? new NBTTagCompound() : fluidTag));
        }
        return fillAmount;
    }

    @Override
    public FluidStack drain(ItemStack stack, int maxDrain, boolean doDrain)
    {
        NBTTagCompound tag = stack.getTagCompound(), fluidTag = null;
        FluidStack fluid = null;
        if (tag == null || !tag.hasKey("tank") || (fluidTag = tag.getCompoundTag("tank")) == null || (fluid = FluidStack.loadFluidStackFromNBT(fluidTag)) == null)
        {
            if (fluidTag != null)
                tag.removeTag("tank");
            return null;
        }
        int drainAmount = Math.min(maxDrain, fluid.amount);
        if (doDrain)
        {
            tag.removeTag("tank");
            fluid.amount -= drainAmount;
            if (fluid.amount > 0)
                fill(stack, fluid, true);
        }
        fluid.amount = drainAmount;
        return fluid;
    }
}
