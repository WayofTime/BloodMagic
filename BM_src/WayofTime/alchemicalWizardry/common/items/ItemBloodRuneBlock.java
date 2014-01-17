package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBloodRuneBlock extends ItemBlock

{
    public ItemBloodRuneBlock(int par1)

    {
        super(par1);
        setHasSubtypes(true);
        this.setUnlocalizedName("itemBloodRuneBlock");
    }

    public String getUnlocalizedName(ItemStack itemstack)

    {
        String name = "";

        switch (itemstack.getItemDamage())
        {
            case 0:
            {
                name = "blank";
                break;
            }

            case 1:
            {
                name = "fill";
                break;
            }

            case 2:
                name = "empty";
                break;

            case 3:
                name = "test";
                break;

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
