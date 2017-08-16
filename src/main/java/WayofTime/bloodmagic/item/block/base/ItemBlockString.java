package WayofTime.bloodmagic.item.block.base;

import WayofTime.bloodmagic.block.base.BlockString;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class ItemBlockString extends ItemBlock {

    public ItemBlockString(BlockString block) {
        super(block);

        if (block.getTypes().length > 1)
            setHasSubtypes(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public BlockString getBlock() {
        return (BlockString) super.getBlock();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getBlock().getUnlocalizedName() + "." + getBlock().getTypes()[MathHelper.clamp(stack.getItemDamage(), 0, 15)];
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}
