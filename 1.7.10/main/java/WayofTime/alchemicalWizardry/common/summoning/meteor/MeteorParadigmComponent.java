package WayofTime.alchemicalWizardry.common.summoning.meteor;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class MeteorParadigmComponent
{
    public String oreDictName;
    public int chance;

    public MeteorParadigmComponent(String dictName, int chance)
    {
        this.oreDictName = dictName;
        this.chance = chance;
    }

    public boolean isValidBlockParadigm()
    {
        if (this.getValidBlockParadigm() != null)
        {
            return true;
        }

        return false;
    }

    public String getOreDictName()
    {
        return this.oreDictName;
    }

    public int getChance()
    {
        return this.chance;
    }

    public ItemStack getValidBlockParadigm()
    {
        List<ItemStack> list = OreDictionary.getOres(getOreDictName());

        for (ItemStack stack : list)
        {
            if (stack != null && stack.getItem() instanceof ItemBlock)
            {
                return stack;
            }
        }

        return null;
    }
}
