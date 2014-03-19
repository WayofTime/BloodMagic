package WayofTime.alchemicalWizardry.common.items;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemSpellParadigmBlock extends ItemBlock

{
    public ItemSpellParadigmBlock(int par1)
    {
        super(par1);
        setHasSubtypes(true);
        this.setUnlocalizedName("itemSpellParadigmBlock");
    }

    public String getUnlocalizedName(ItemStack itemstack)

    {
        String name = "";

        switch (itemstack.getItemDamage())
        {
            case 0:
            {
                name = "projectile";
                break;
            }

            case 1:
            {
                name = "self";
                break;
            }

            case 2:
                name = "melee";
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
