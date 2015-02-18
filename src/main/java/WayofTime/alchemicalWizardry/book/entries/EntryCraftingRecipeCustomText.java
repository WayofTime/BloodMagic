package WayofTime.alchemicalWizardry.book.entries;

import net.minecraft.item.crafting.IRecipe;

public class EntryCraftingRecipeCustomText extends EntryCraftingRecipe implements IEntryCustomText
{

	public EntryCraftingRecipeCustomText(IRecipe recipes) 
	{
		super(recipes);
	}

	@Override
	public String getText()
	{
		return "";
	}

	@Override
	public void setText(String str){}
}
