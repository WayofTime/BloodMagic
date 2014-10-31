package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemSpellEffectBlock extends ItemBlock

{
    public ItemSpellEffectBlock(Block par1)
    {
        super(par1);
        setHasSubtypes(true);
//        this.setUnlocalizedName("itemSpellEffectBlock");
//        setCreativeTab(AlchemicalWizardry.tabBloodMagic);

    }

    public String getUnlocalizedName(ItemStack itemstack)

    {
        String name = "";

        switch (itemstack.getItemDamage())
        {
            case 0:
            {
                name = "fire";
                break;
            }

            case 1:
            {
                name = "ice";
                break;
            }

            case 2:
                name = "wind";
                break;

            case 3:
                name = "earth";
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
