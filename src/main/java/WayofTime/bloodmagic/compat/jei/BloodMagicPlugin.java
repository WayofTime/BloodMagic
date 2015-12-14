package WayofTime.bloodmagic.compat.jei;

import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeCategory;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeHandler;
import WayofTime.bloodmagic.compat.jei.altar.AltarRecipeMaker;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeCategory;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeHandler;
import WayofTime.bloodmagic.compat.jei.binding.BindingRecipeMaker;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class BloodMagicPlugin implements IModPlugin {

    @Override
    public boolean isModLoaded() {
        return true;
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCategories(
                new AltarRecipeCategory(),
                new BindingRecipeCategory()
        );

        registry.addRecipeHandlers(
                new AltarRecipeHandler(),
                new BindingRecipeHandler()
        );

        registry.addRecipes(AltarRecipeMaker.getRecipes());
        registry.addRecipes(BindingRecipeMaker.getRecipes());
    }
}
