package wayoftime.bloodmagic.common.data.recipe;

import net.minecraft.advancements.ICriterionInstance;

public class RecipeCriterion
{
	public final String name;
	public final ICriterionInstance criterion;

	private RecipeCriterion(String name, ICriterionInstance criterion)
	{
		this.name = name;
		this.criterion = criterion;
	}

	public static RecipeCriterion of(String name, ICriterionInstance criterion)
	{
		return new RecipeCriterion(name, criterion);
	}
}