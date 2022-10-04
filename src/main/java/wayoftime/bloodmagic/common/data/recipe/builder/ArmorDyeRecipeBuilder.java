package wayoftime.bloodmagic.common.data.recipe.builder;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeBuilder;
import wayoftime.bloodmagic.recipe.helper.SerializerHelper;
import wayoftime.bloodmagic.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class ArmorDyeRecipeBuilder extends BloodMagicRecipeBuilder<ArmorDyeRecipeBuilder> {
    private final List<Ingredient> input;
    private final ItemStack output;

    protected ArmorDyeRecipeBuilder(List<Ingredient> input, ItemStack output){
        super(bmSerializer("armor_dye"));
        this.input = input;
        this.output = output;
    }

    public static ArmorDyeRecipeBuilder armorDye(ItemStack output, Ingredient... inputArray){
        List<Ingredient> inputList = new ArrayList<Ingredient>();
        for (int i = 0; i < inputArray.length; i++)
        {
            inputList.add(inputArray[i]);
        }
        return new ArmorDyeRecipeBuilder(inputList,output);
    }


    @Override
    protected ArmorDyeRecipeResult getResult(ResourceLocation id) {
        return new ArmorDyeRecipeResult(id);
    }

    public class ArmorDyeRecipeResult extends RecipeResult {
        protected ArmorDyeRecipeResult (ResourceLocation id) {super(id);}


        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            for (int i = 0; i < Math.min(input.size(), 4); i++)
            {
                json.add(Constants.JSON.INPUT + i, input.get(i).toJson());
            }
            json.add(Constants.JSON.OUTPUT, SerializerHelper.serializeItemStack(output));
        }
    }
}
