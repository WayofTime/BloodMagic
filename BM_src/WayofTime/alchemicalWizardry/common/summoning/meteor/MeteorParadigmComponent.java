package WayofTime.alchemicalWizardry.common.summoning.meteor;

import java.util.List;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MeteorParadigmComponent
{
	public String oreDictName;
	public int chance;

	public MeteorParadigmComponent(String dictName, int chance)
	{
		oreDictName = dictName;
		this.chance = chance;
	}

	public boolean isValidBlockParadigm()
	{
		if (getValidBlockParadigm() != null)
		{
			return true;
		}

		return false;
	}

	public String getOreDictName()
	{
		return oreDictName;
	}

	public int getChance()
	{
		return chance;
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
