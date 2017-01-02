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
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBlockBloodTank extends ItemBlock
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
            return super.getItemStackDisplayName(stack) + " " + TextHelper.localizeEffect("tooltip.bloodmagic.tier", stack.getItemDamage() + 1) + " (" + FluidStack.loadFluidStackFromNBT(tag).getLocalizedName() + ")";
        }
        else
        {
            return super.getItemStackDisplayName(stack) + " " + TextHelper.localizeEffect("tooltip.bloodmagic.tier", stack.getItemDamage() + 1);
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer entityPlayer, List<String> tooltip, boolean advanced)
    {
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.tier", stack.getItemDamage() + 1));
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.fluid.capacity") + ": " + getCapacity(stack) + "mB");
        if (stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(Constants.NBT.TANK);
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tag);
            if (!Strings.isNullOrEmpty(tag.getString("FluidName")) && fluidStack != null)
            {
                tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.fluid.type") + ": " + fluidStack.getLocalizedName());
                tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.fluid.amount") + ": " + tag.getInteger("Amount") + "/" + getCapacity(stack) + "mB");
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item id, CreativeTabs creativeTab, NonNullList<ItemStack> list)
    {
        for (int i = 0; i < TileBloodTank.CAPACITIES.length; i++)
            list.add(new ItemStack(id, 1, i));
    }

    public int getCapacity(ItemStack container)
    {
        return container != null && Block.getBlockFromItem(container.getItem()) instanceof BlockBloodTank ? TileBloodTank.CAPACITIES[container.getMetadata()] * Fluid.BUCKET_VOLUME : 0;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
    {
        return new FluidHandlerItemStack(stack, getCapacity(stack));
    }
}
