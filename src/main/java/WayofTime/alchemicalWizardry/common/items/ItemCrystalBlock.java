package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemCrystalBlock extends ItemBlock

{
    public ItemCrystalBlock(Block par1)
    {
        super(par1);
        setHasSubtypes(true);

    }

    public String getUnlocalizedName(ItemStack itemstack)

    {
        String name = "";

        switch (itemstack.getItemDamage())
        {
            case 0:
            {
                name = "fullCrystal";
                break;
            }

            case 1:
            {
                name = "crystalBrick";
                break;
            }

            default:
                name = "broken";
        }

        return getUnlocalizedName() + "." + name;
    }

    public int getMetadata(int par1)

    {
        return par1;
    }
}
