package WayofTime.alchemicalWizardry.common;

import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Represents an item together with metadata and NBT tag.
 * 
 * @author ljfa-ag
 */
public class ItemType
{
    public final Item item;
    public final int meta;
    public final NBTTagCompound nbtTag;
    
    public ItemType(Item item, int meta, NBTTagCompound nbtTag)
    {
        this.item = Objects.requireNonNull(item);
        this.meta = meta;
        this.nbtTag = nbtTag;
    }
    
    public ItemType(Item item, int meta)
    {
        this(item, meta, null);
    }
    
    public ItemType(Item item)
    {
        this(item, 0, null);
    }
    
    public ItemType(Block block, int meta, NBTTagCompound nbtTag)
    {
        this(Item.getItemFromBlock(block), meta, nbtTag);
    }
    
    public ItemType(Block block, int meta)
    {
        this(block, meta, null);
    }
    
    public ItemType(Block block)
    {
        this(block, 0, null);
    }
    
    public ItemStack createStack(int count)
    {
        ItemStack result = new ItemStack(item, count, meta);
        result.stackTagCompound = nbtTag;
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ItemType other = (ItemType) obj;
        return item == other.item && meta == other.meta && Objects.equals(nbtTag, other.nbtTag);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + item.hashCode();
        result = prime * result + meta;
        result = prime * result + ((nbtTag == null) ? 0 : nbtTag.hashCode());
        return result;
    }
    
    public static ItemType fromStack(ItemStack stack)
    {
        return new ItemType(stack.getItem(), stack.getItemDamage(), stack.stackTagCompound);
    }
    
}
