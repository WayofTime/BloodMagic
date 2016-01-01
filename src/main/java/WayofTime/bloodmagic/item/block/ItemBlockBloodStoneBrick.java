package WayofTime.bloodmagic.item.block;

import WayofTime.bloodmagic.block.BlockBloodStoneBrick;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBloodStoneBrick extends ItemBlock
{
    public ItemBlockBloodStoneBrick(Block block)
    {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + BlockBloodStoneBrick.names[stack.getItemDamage()];
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }
}