package wayoftime.bloodmagic.recipe;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;
import wayoftime.bloodmagic.common.registries.BloodMagicRecipeSerializers;
import wayoftime.bloodmagic.core.living.ILivingDyeable;


public class DyeRecipe extends ShapelessRecipe {


    public DyeRecipe(ResourceLocation idIn, String groupIn, ItemStack recipeOutputIn, NonNullList<Ingredient> recipeItemsIn) {
        super(idIn, groupIn, recipeOutputIn, recipeItemsIn);
    }

    @Override
    public ItemStack assemble(final CraftingContainer inv) {
        final ItemStack output = super.assemble(inv); // Get the default output

        if (!output.isEmpty()) {
            for (int i = 0; i < inv.getContainerSize(); i++) { // For each slot in the crafting inventory,
                final ItemStack ingredient = inv.getItem(i); // Get the ingredient in the slot
                if (!ingredient.isEmpty() && ingredient.getItem() instanceof ILivingDyeable) {
                    output.setTag(ingredient.getOrCreateTag().copy());
                }
            }
            for (int i = 0; i < inv.getContainerSize(); i++) { // For each slot in the crafting inventory,
                final ItemStack ingredient = inv.getItem(i); // Get the ingredient in the slot
                DyeColor color = DyeColor.getColor(ingredient);
                if (!ingredient.isEmpty() && color != null) {
                    if (output.getItem() instanceof ILivingDyeable dyeable) {
                        dyeable.onDye(output, color);
                    }
                }
            }
        }

        return output; // Return the modified output
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BloodMagicRecipeSerializers.DYE.get();
    }



    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<DyeRecipe> {

        @Override
        public DyeRecipe fromJson(final ResourceLocation recipeID, final JsonObject json) {
            final String group = GsonHelper.getAsString(json, "group", "");
            final NonNullList<Ingredient> ingredients = parseShapeless(json);
            final ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);

            return new DyeRecipe(recipeID, group, result, ingredients);
        }

        @Override
        public DyeRecipe fromNetwork(final ResourceLocation recipeID, final FriendlyByteBuf buffer) {
            final String group = buffer.readUtf(Short.MAX_VALUE);
            final int numIngredients = buffer.readVarInt();
            final NonNullList<Ingredient> ingredients = NonNullList.withSize(numIngredients, Ingredient.EMPTY);

            for (int j = 0; j < ingredients.size(); ++j) {
                ingredients.set(j, Ingredient.fromNetwork(buffer));
            }

            final ItemStack result = buffer.readItem();

            return new DyeRecipe(recipeID, group, result, ingredients);
        }

        @Override
        public void toNetwork(final FriendlyByteBuf buffer, final DyeRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeVarInt(recipe.getIngredients().size());

            for (final Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.getResultItem());
        }

        public static NonNullList<Ingredient> parseShapeless(final JsonObject json) {
            final NonNullList<Ingredient> ingredients = NonNullList.create();
            for (final JsonElement element : GsonHelper.getAsJsonArray(json, "ingredients"))
                ingredients.add(CraftingHelper.getIngredient(element));

            if (ingredients.isEmpty())
                throw new JsonParseException("No ingredients for shapeless recipe");

            return ingredients;
        }

    }

}






