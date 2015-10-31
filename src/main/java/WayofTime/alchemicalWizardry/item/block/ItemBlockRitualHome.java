package WayofTime.alchemicalWizardry.item.block;

import WayofTime.alchemicalWizardry.block.BlockRitualController;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockRitualHome extends ItemBlock {

    public ItemBlockRitualHome(Block block) {
        super(block);

        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + BlockRitualController.names[stack.getItemDamage()];
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }
}
