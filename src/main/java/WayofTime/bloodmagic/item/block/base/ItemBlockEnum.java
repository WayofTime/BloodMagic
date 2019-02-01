package WayofTime.bloodmagic.item.block.base;

import WayofTime.bloodmagic.block.base.BlockEnum;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;

public class ItemBlockEnum<E extends Enum<E> & IStringSerializable> extends ItemBlock {

    public ItemBlockEnum(BlockEnum<E> block) {
        super(block);

        if (block.getTypes().length > 1)
            setHasSubtypes(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public BlockEnum<E> getBlock() {
        return (BlockEnum<E>) super.getBlock();
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return getBlock().getTranslationKey() + getBlock().getTypes()[MathHelper.clamp(stack.getItemDamage(), 0, getBlock().getTypes().length - 1)].getName();
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}
