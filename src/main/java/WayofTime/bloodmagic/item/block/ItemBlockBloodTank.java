package WayofTime.bloodmagic.item.block;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.block.BlockBloodTank;
import WayofTime.bloodmagic.tile.TileBloodTank;
import WayofTime.bloodmagic.util.helper.TextHelper;
import com.google.common.base.Strings;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBlockBloodTank extends ItemBlock implements IFluidContainerItem
{
    public ItemBlockBloodTank(Block block)
    {
        super(block);

        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(Constants.NBT.TANK) && !stack.getTagCompound().getCompoundTag(Constants.NBT.TANK).getString("FluidName").equals(""))
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(Constants.NBT.TANK);
            return super.getItemStackDisplayName(stack) + " " + TextHelper.localizeEffect("tooltip.BloodMagic.tier", stack.getItemDamage() + 1) + " (" + FluidStack.loadFluidStackFromNBT(tag).getLocalizedName() + ")";
        }
        else
        {
            return super.getItemStackDisplayName(stack) + " " + TextHelper.localizeEffect("tooltip.BloodMagic.tier", stack.getItemDamage() + 1);
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer entityPlayer, List<String> tooltip, boolean advanced)
    {
        tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.tier", stack.getItemDamage() + 1));
        tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.fluid.capacity") + ": " + getCapacity(stack) + "mB");
        if (stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(Constants.NBT.TANK);
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tag);
            if (!Strings.isNullOrEmpty(tag.getString("FluidName")) && fluidStack != null)
            {
                tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.fluid.type") + ": " + fluidStack.getLocalizedName());
                tooltip.add(TextHelper.localizeEffect("tooltip.BloodMagic.fluid.amount") + ": " + tag.getInteger("Amount") + "/" + getCapacity(stack) + "mB");
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, List<ItemStack> list)
    {
        for (int i = 0; i < TileBloodTank.capacities.length; i++)
            list.add(new ItemStack(id, 1, i));
    }

    @Override
    public FluidStack getFluid(ItemStack stack)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(Constants.NBT.TANK) && !stack.getTagCompound().getCompoundTag(Constants.NBT.TANK).getString("FluidName").equals(""))
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(Constants.NBT.TANK);
            return FluidStack.loadFluidStackFromNBT(tag);
        }

        return null;
    }

    @Override
    public int getCapacity(ItemStack container)
    {
        return container != null && Block.getBlockFromItem(container.getItem()) instanceof BlockBloodTank ? TileBloodTank.capacities[container.getMetadata()] * Fluid.BUCKET_VOLUME : 0;
    }

    @Override
    public int fill(ItemStack stack, FluidStack resource, boolean doFill)
    {
        if (resource == null || stack.stackSize != 1)
            return 0;

        int fillAmount = 0, capacity = getCapacity(stack);
        NBTTagCompound tag = stack.getTagCompound(), fluidTag = null;
        FluidStack fluid = null;

        if (tag == null || !tag.hasKey(Constants.NBT.TANK) || (fluidTag = tag.getCompoundTag(Constants.NBT.TANK)) == null || (fluid = FluidStack.loadFluidStackFromNBT(fluidTag)) == null)
            fillAmount = Math.min(capacity, resource.amount);

        if (fluid == null)
        {
            if (doFill)
            {
                fluid = resource.copy();
                fluid.amount = 0;
            }
        }
        else if (!fluid.isFluidEqual(resource))
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
            tag.setTag(Constants.NBT.TANK, fluid.writeToNBT(fluidTag == null ? new NBTTagCompound() : fluidTag));
        }

        return fillAmount;
    }

    @Override
    public FluidStack drain(ItemStack stack, int maxDrain, boolean doDrain)
    {
        NBTTagCompound tag = stack.getTagCompound(), fluidTag = null;
        FluidStack fluid;

        if (tag == null || !tag.hasKey(Constants.NBT.TANK) || (fluidTag = tag.getCompoundTag(Constants.NBT.TANK)) == null || (fluid = FluidStack.loadFluidStackFromNBT(fluidTag)) == null)
        {
            if (fluidTag != null)
                tag.removeTag(Constants.NBT.TANK);
            return null;
        }

        int drainAmount = Math.min(maxDrain, fluid.amount);

        if (doDrain)
        {
            tag.removeTag(Constants.NBT.TANK);
            fluid.amount -= drainAmount;
            if (fluid.amount > 0)
                fill(stack, fluid, true);
        }

        fluid.amount = drainAmount;
        return fluid;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
    {
        return new FluidHandlerItemStack(stack, getCapacity(stack));
    }
}
