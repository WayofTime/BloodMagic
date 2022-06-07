package wayoftime.bloodmagic.common.recipe.serializer;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;

public class TestSpecialRecipe extends SpecialRecipe implements IShapedRecipe<CraftingInventory>
{

	public TestSpecialRecipe(ResourceLocation idIn)
	{
		super(idIn);
		System.out.println("XYZ");
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canFit(int width, int height)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRecipeType<?> getType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRecipeWidth()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRecipeHeight()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
