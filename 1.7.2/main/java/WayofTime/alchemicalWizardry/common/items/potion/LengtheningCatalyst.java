package WayofTime.alchemicalWizardry.common.items.potion;

import WayofTime.alchemicalWizardry.AlchemicalWizardry;
import WayofTime.alchemicalWizardry.api.alchemy.AlchemyRecipeRegistry;
import WayofTime.alchemicalWizardry.common.ICatalyst;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

import java.util.List;

public class LengtheningCatalyst extends Item implements ICatalyst
{
    private int catalystStrength;

    public LengtheningCatalyst(int catalystStrength)
    {
        super();
        this.catalystStrength = catalystStrength;
        setCreativeTab(AlchemicalWizardry.tabBloodMagic);
    }

    @Override
    public int getCatalystLevel()
    {
        return catalystStrength;
    }

    @Override
    public boolean isConcentration()
    {
        return false;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        par3List.add("Used in alchemy");

        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
        {
            ItemStack[] recipe = AlchemyRecipeRegistry.getRecipeForItemStack(par1ItemStack);

            if (recipe != null)
            {
                par3List.add(EnumChatFormatting.BLUE + "Recipe:");

                for (ItemStack item : recipe)
                {
                    if (item != null)
                    {
                        par3List.add("" + item.getDisplayName());
                    }
                }
            }
        } else
        {
            par3List.add("-Press " + EnumChatFormatting.BLUE + "shift" + EnumChatFormatting.GRAY + " for Recipe-");
        }
    }
}
