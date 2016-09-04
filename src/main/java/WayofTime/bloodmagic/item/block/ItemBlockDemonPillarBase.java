package WayofTime.bloodmagic.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import WayofTime.bloodmagic.block.BlockDemonPillar;

public class ItemBlockDemonPillarBase extends ItemBlock
{
//    public final BlockDemonBase demonBlock;

    public ItemBlockDemonPillarBase(Block block)
    {
        super(block);
        setHasSubtypes(true);
//        demonBlock = block;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + BlockDemonPillar.names[stack.getItemDamage() % BlockDemonPillar.names.length];
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta % BlockDemonPillar.names.length;
    }
}