package wayoftime.bloodmagic.common.data.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeBuilder;

public class ArmorDyeRecipeBuilder extends BloodMagicRecipeBuilder<ArmorDyeRecipeBuilder> {
    private final Ingredient input;
    private final Ingredient dye;
    private final ItemStack output;

    protected ArmorDyeRecipeBuilder( Ingredient input, Ingredient dye,ItemStack output){
        super(bmSerializer("armordye"));
        this.input = input;
        this.dye = dye;
        this.output = output;
    }

    public static ArmorDyeRecipeBuilder armorDye(Ingredient input, Ingredient dye, ItemStack output){
        return new ArmorDyeRecipeBuilder(input, dye,output);
    }


    @Override
    protected ArmorDyeRecipeResult getResult(ResourceLocation id) {
        return new ArmorDyeRecipeResult(id);
    }

    public class ArmorDyeRecipeResult extends RecipeResult {
        protected ArmorDyeRecipeResult (ResourceLocation id) {super(id);}


        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            JsonArray ingredients = new JsonArray();
            ingredients.add(dye.toJson());
            ingredients.add(input.toJson());
            json.add("ingredients", ingredients);
            JsonObject itemObject = new JsonObject();
            itemObject.addProperty("item", output.getItem().getRegistryName().toString());
            json.add("result", itemObject);
        }
    }
}
