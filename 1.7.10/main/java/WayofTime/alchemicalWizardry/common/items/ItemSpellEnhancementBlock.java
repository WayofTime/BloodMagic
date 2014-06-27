package WayofTime.alchemicalWizardry.common.items;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemSpellEnhancementBlock extends ItemBlock

{
    public ItemSpellEnhancementBlock(Block par1)
    {
        super(par1);
        setHasSubtypes(true);
//        this.setUnlocalizedName("itemSpellEnhancementBlock");
//        setCreativeTab(AlchemicalWizardry.tabBloodMagic);

    }

    public String getUnlocalizedName(ItemStack itemstack)

    {
        String name = "";

        switch (itemstack.getItemDamage())
        {
            case 0:
                name = "power1";
                break;

            case 1:
                name = "power2";
                break;         

            case 2:
                name = "power3";
                break;

            case 3:
                name = "power4";
                break;
                
            case 4:
                name = "power5";
                break;
                
            case 5:
                name = "cost1";
                break;
                
            case 6:
                name = "cost2";
                break;
                
            case 7:
                name = "cost3";
                break;
                
            case 8:
                name = "cost4";
                break;
                
            case 9:
                name = "cost5";
                break;
                
            case 10:
                name = "potency1";
                break;
                
            case 11:
                name = "potency2";
                break;
                
            case 12:
                name = "potency3";
                break;
                
            case 13:
                name = "potency4";
                break;
                
            case 14:
                name = "potency5";
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
