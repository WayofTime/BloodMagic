package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemSpellModifierBlock extends ItemBlock

{
    public ItemSpellModifierBlock(Block par1)
    {
        super(par1);
        setHasSubtypes(true);
//        this.setUnlocalizedName("itemSpellModifierBlock");
//        setCreativeTab(AlchemicalWizardry.tabBloodMagic);

    }

    public String getUnlocalizedName(ItemStack itemstack)

    {
        String name = "";

        switch (itemstack.getItemDamage())
        {
            case 0:
            {
                name = "power";
                break;
            }

            case 1:
            {
                name = "efficiency";
                break;
            }

            case 2:
                name = "potency";
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
