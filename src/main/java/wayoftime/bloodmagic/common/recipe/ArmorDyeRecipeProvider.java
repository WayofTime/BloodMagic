package wayoftime.bloodmagic.common.recipe;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import wayoftime.bloodmagic.BloodMagic;
import wayoftime.bloodmagic.common.data.recipe.builder.ArmorDyeRecipeBuilder;
import wayoftime.bloodmagic.common.item.BloodMagicItems;

import java.util.function.Consumer;

public class ArmorDyeRecipeProvider implements ISubRecipeProvider{
    @Override
    public void addRecipes(Consumer<FinishedRecipe> consumer) {

        String basePath = "living/";

        ArmorDyeRecipeBuilder.armorDye(new ItemStack(BloodMagicItems.LIVING_PLATE.get()) ,Ingredient.of(BloodMagicItems.LIVING_PLATE.get()), Ingredient.of(Tags.Items.DYES)).build(consumer, new ResourceLocation(BloodMagic.MODID, basePath + "living_plate"));
    }
}
