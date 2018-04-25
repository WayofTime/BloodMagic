package WayofTime.bloodmagic.item.block;

import WayofTime.bloodmagic.block.BlockBloodTank;
import WayofTime.bloodmagic.tile.TileBloodTank;
import WayofTime.bloodmagic.util.helper.TextHelper;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemBlockBloodTank extends ItemBlock {
    public ItemBlockBloodTank(Block block) {
        super(block);

        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
        if (fluidStack != null) {
            return super.getItemStackDisplayName(stack) + " " + TextHelper.localizeEffect("tooltip.bloodmagic.tier", stack.getItemDamage() + 1) + " (" + fluidStack.getLocalizedName() + ")";
        } else {
            return super.getItemStackDisplayName(stack) + " " + TextHelper.localizeEffect("tooltip.bloodmagic.tier", stack.getItemDamage() + 1);
        }
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.tier", stack.getItemDamage() + 1));
        tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.fluid.capacity", getCapacity(stack)));
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Fluid")) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag("Fluid"));
            if (fluidStack != null) {
                tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.fluid.type", fluidStack.getLocalizedName()));
                tooltip.add(TextHelper.localizeEffect("tooltip.bloodmagic.fluid.amount", fluidStack.amount, getCapacity(stack)));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(creativeTab))
            return;

        for (int i = 0; i < TileBloodTank.CAPACITIES.length; i++)
            list.add(new ItemStack(this, 1, i));
    }

    public int getCapacity(ItemStack container) {
        int meta = MathHelper.clamp(container.getItemDamage(), 0, TileBloodTank.CAPACITIES.length - 1);
        return !container.isEmpty() && Block.getBlockFromItem(container.getItem()) instanceof BlockBloodTank ? TileBloodTank.CAPACITIES[meta] * Fluid.BUCKET_VOLUME : 0;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new FluidHandlerItemStack(stack, getCapacity(stack));
    }
}
