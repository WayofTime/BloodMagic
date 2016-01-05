package WayofTime.bloodmagic.api;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

@RequiredArgsConstructor
@EqualsAndHashCode
public class ItemStackWrapper
{
    public final Item item;
    public final int stackSize;
    public final int meta;
    @Setter
    public NBTTagCompound nbtTag;

    public ItemStackWrapper(Item item, int stackSize)
    {
        this(item, stackSize, 0);
    }

    public ItemStackWrapper(Item item)
    {
        this(item, 1, 0);
    }

    public ItemStackWrapper(Block block, int stackSize, int meta)
    {
        this(Item.getItemFromBlock(block), stackSize, meta);
    }

    public ItemStackWrapper(Block block, int stackSize)
    {
        this(block, stackSize, 0);
    }

    public ItemStackWrapper(Block block)
    {
        this(block, 1, 0);
    }

    public ItemStackWrapper(BlockStack blockStack)
    {
        this(blockStack.getBlock(), 1, blockStack.getMeta());
    }

    public static ItemStackWrapper getHolder(ItemStack stack)
    {
        return new ItemStackWrapper(stack.getItem(), stack.stackSize, stack.getItemDamage());
    }

    public ItemStack toStack()
    {
        return new ItemStack(item, stackSize, meta);
    }

    public String getDisplayName()
    {
        return toStack().getDisplayName();
    }

    @Override
    public String toString()
    {
        return stackSize + "x" + item.getUnlocalizedName() + "@" + this.meta;
    }

    public ItemStack toStack(int count)
    {
        ItemStack result = new ItemStack(item, count, meta);
        result.setTagCompound(nbtTag);
        return result;
    }
}
