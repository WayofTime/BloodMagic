package WayofTime.bloodmagic.item.block;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.block.BlockDemonBase;

public class ItemBlockDemonBase extends ItemBlock
{
    public final BlockDemonBase demonBlock;

    public ItemBlockDemonBase(BlockDemonBase block)
    {
        super(block);
        setHasSubtypes(true);
        demonBlock = block;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + demonBlock.names[stack.getItemDamage()];
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }
}