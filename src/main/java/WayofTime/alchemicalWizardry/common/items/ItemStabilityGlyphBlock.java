package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemStabilityGlyphBlock extends ItemBlock
{
    public ItemStabilityGlyphBlock(Block block)
    {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        String name = "";

        switch (itemstack.getItemDamage())
        {
        case 0:
            name = "stability1";
            break;

        default:
            name = "broken";
        }

        return getUnlocalizedName() + "." + name;
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }
}
