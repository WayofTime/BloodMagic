package WayofTime.bloodmagic.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.block.BlockRitualController;

public class ItemBlockRitualController extends ItemBlock
{
    public ItemBlockRitualController(Block block)
    {
        super(block);

        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + BlockRitualController.names[stack.getItemDamage()];
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }
}
