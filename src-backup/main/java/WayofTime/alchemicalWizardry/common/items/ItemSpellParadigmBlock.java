package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemSpellParadigmBlock extends ItemBlock

{
    public ItemSpellParadigmBlock(Block par1)
    {
        super(par1);
        setHasSubtypes(true);
        //this.setUnlocalizedName("itemSpellParadigmBlock");
    }

    @Override
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
                
            case 3:
            	name = "tool";
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
