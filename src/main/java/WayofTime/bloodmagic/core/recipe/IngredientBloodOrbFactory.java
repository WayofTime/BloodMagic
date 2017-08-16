package WayofTime.bloodmagic.core.recipe;

import WayofTime.bloodmagic.core.RegistrarBloodMagic;
import com.google.gson.JsonObject;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;

public class IngredientBloodOrbFactory implements IIngredientFactory {

    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
        ResourceLocation orb = new ResourceLocation(JsonUtils.getString(json, "orb"));
        return new IngredientBloodOrb(RegistrarBloodMagic.BLOOD_ORBS.getValue(orb));
    }
}
