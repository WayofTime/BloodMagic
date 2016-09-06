package WayofTime.bloodmagic.item.block;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.block.BlockDemonStairsBase;

public class ItemDemonStairsBase extends ItemBlock
{
    public final BlockDemonStairsBase demonBlock;

    public ItemDemonStairsBase(BlockDemonStairsBase block)
    {
        super(block);
        setHasSubtypes(true);
        demonBlock = block;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + demonBlock.names[stack.getItemDamage() % demonBlock.names.length];
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta % demonBlock.names.length;
    }
}