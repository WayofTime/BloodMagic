package WayofTime.bloodmagic.item.block;

import WayofTime.bloodmagic.block.BlockRitualStone;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockRitualStone extends ItemBlock {

    public ItemBlockRitualStone(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + BlockRitualStone.names[stack.getItemDamage()];
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }
}
