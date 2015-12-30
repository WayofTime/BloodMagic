package WayofTime.bloodmagic.item.block;

import WayofTime.bloodmagic.block.BlockPedestal;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPedestal extends ItemBlock
{

    public ItemBlockPedestal(Block block)
    {
        super(block);

        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + BlockPedestal.names[stack.getItemDamage()];
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }
}
