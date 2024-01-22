package wayoftime.bloodmagic.common.data.recipe.builder;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeBuilder;
import wayoftime.bloodmagic.recipe.helper.SerializerHelper;
import wayoftime.bloodmagic.util.Constants;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TartaricForgeRecipeBuilder extends BloodMagicRecipeBuilder<TartaricForgeRecipeBuilder>
{
	private final List<Ingredient> input;
	private final ItemStack output;
	private final double minimumSouls;
	private final double soulDrain;

	protected TartaricForgeRecipeBuilder(List<Ingredient> input, ItemStack output, double minimumSouls, double soulDrain)
	{
		super(bmSerializer("soulforge"));
		this.input = input;
		this.output = output;
		this.minimumSouls = minimumSouls;
		this.soulDrain = soulDrain;
	}

	public static TartaricForgeRecipeBuilder tartaricForge(List<Ingredient> input, ItemStack output, double minimumSouls, double soulDrain)
	{
		return new TartaricForgeRecipeBuilder(input, output, minimumSouls, soulDrain);
	}

	public static TartaricForgeRecipeBuilder tartaricForge(ItemStack output, double minimumSouls, double soulDrain, Ingredient... inputArray)
	{
		List<Ingredient> inputList = new ArrayList<Ingredient>();
		for (int i = 0; i < inputArray.length; i++)
		{
			inputList.add(inputArray[i]);
		}
		return new TartaricForgeRecipeBuilder(inputList, output, minimumSouls, soulDrain);
	}

	@Override
	protected TartaricForgeRecipeResult getResult(ResourceLocation id)
	{
		return new TartaricForgeRecipeResult(id);
	}

	public class TartaricForgeRecipeResult extends RecipeResult
	{
		protected TartaricForgeRecipeResult(ResourceLocation id)
		{
			super(id);
		}

		@Override
		public void serializeRecipeData(@Nonnull JsonObject json)
		{
			for (int i = 0; i < Math.min(input.size(), 4); i++)
			{
				json.add(Constants.JSON.INPUT + i, input.get(i).toJson());
			}

			json.add(Constants.JSON.OUTPUT, SerializerHelper.serializeItemStack(output));
			json.addProperty(Constants.JSON.TARTARIC_MINIMUM, (float) minimumSouls);
			json.addProperty(Constants.JSON.TARTARIC_DRAIN, (float) soulDrain);

		}
	}
}