package wayoftime.bloodmagic.common.data.recipe.builder;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import wayoftime.bloodmagic.common.data.recipe.BloodMagicRecipeBuilder;
import wayoftime.bloodmagic.recipe.helper.SerializerHelper;
import wayoftime.bloodmagic.util.Constants;

public class ArmorDyeRecipeBuilder extends BloodMagicRecipeBuilder<ArmorDyeRecipeBuilder> {
    private final Ingredient armor_piece;
    private final Ingredient dye;
    private final ItemStack output;

    protected ArmorDyeRecipeBuilder( Ingredient armor_piece, Ingredient dye,ItemStack output){
        super(bmSerializer("armordye"));
        this.armor_piece = armor_piece;
        this.dye = dye;
        this.output = output;
    }

    public static ArmorDyeRecipeBuilder armorDye(Ingredient armor_piece, Ingredient dye, ItemStack output){
        return new ArmorDyeRecipeBuilder(armor_piece, dye,output);
    }


    @Override
    protected ArmorDyeRecipeResult getResult(ResourceLocation id) {
        return new ArmorDyeRecipeResult(id);
    }

    public class ArmorDyeRecipeResult extends RecipeResult {
        protected ArmorDyeRecipeResult (ResourceLocation id) {super(id);}


        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            json.add(Constants.JSON.BASEINPUT, armor_piece.toJson());
            json.add(Constants.JSON.ADDEDINPUT, dye.toJson());
            json.add(Constants.JSON.OUTPUT, SerializerHelper.serializeItemStack(output));
        }
    }
}
