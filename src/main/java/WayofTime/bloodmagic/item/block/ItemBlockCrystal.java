package WayofTime.bloodmagic.item.block;

import WayofTime.bloodmagic.block.BlockCrystal;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockCrystal extends ItemBlock {

    public ItemBlockCrystal(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + BlockCrystal.names[stack.getItemDamage()];
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }
}