package WayofTime.bloodmagic.compat.guideapi;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyCircleRenderer;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.api_impl.BloodMagicAPI;
import WayofTime.bloodmagic.api_impl.recipe.RecipeBloodAltar;
import WayofTime.bloodmagic.api_impl.recipe.RecipeTartaricForge;
import WayofTime.bloodmagic.client.render.alchemyArray.DualAlchemyCircleRenderer;
import WayofTime.bloodmagic.compat.guideapi.page.PageAlchemyArray;
import WayofTime.bloodmagic.compat.guideapi.page.PageAltarRecipe;
import WayofTime.bloodmagic.compat.guideapi.page.PageTartaricForgeRecipe;
import amerifrance.guideapi.page.PageJsonRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BookUtils {

    @Nullable
    public static PageAltarRecipe getAltarPage(ItemStack output) {
        for (RecipeBloodAltar recipe : BloodMagicAPI.INSTANCE.getRecipeRegistrar().getAltarRecipes().values())
            if (ItemStack.areItemStacksEqualUsingNBTShareTag(output, recipe.getOutput()))
                return new PageAltarRecipe(recipe);

        return null;
    }

    @Nullable
    public static PageTartaricForgeRecipe getForgePage(ItemStack output) {
        for (RecipeTartaricForge recipe : BloodMagicAPI.INSTANCE.getRecipeRegistrar().getTartaricForgeRecipes().values())
            if (ItemStack.areItemStacksEqualUsingNBTShareTag(output, recipe.getOutput()))
                return new PageTartaricForgeRecipe(recipe);

        return null;
    }

    public static PageJsonRecipe getCraftingPage(String name) {
        return new PageJsonRecipe(new ResourceLocation(BloodMagic.MODID, name));
    }

    public static PageAlchemyArray getAlchemyPage(String key) {
        ItemStack[] recipe = AlchemyArrayRecipeRegistry.getRecipeForArrayEffect(key);
        if (recipe[0] != null) {
            ItemStack inputStack = recipe[0];
            ItemStack catalystStack = recipe[1];

            AlchemyCircleRenderer renderer = AlchemyArrayRecipeRegistry.getAlchemyCircleRenderer(inputStack, catalystStack);
            if (renderer instanceof DualAlchemyCircleRenderer) {
                List<ResourceLocation> resources = new ArrayList<ResourceLocation>();
                resources.add(((DualAlchemyCircleRenderer) renderer).arrayResource);
                resources.add(((DualAlchemyCircleRenderer) renderer).secondaryArrayResource);
                return new PageAlchemyArray(resources, inputStack, catalystStack);
            } else {
                return new PageAlchemyArray(renderer.arrayResource, inputStack, catalystStack);
            }
        }

        return null;
    }

    public static PageAlchemyArray getAlchemyPage(ItemStack outputStack) {
        ItemStack[] recipe = AlchemyArrayRecipeRegistry.getRecipeForOutputStack(outputStack);
        if (recipe[0] != null) {
            ItemStack inputStack = recipe[0];
            ItemStack catalystStack = recipe[1];

            AlchemyCircleRenderer renderer = AlchemyArrayRecipeRegistry.getAlchemyCircleRenderer(inputStack, catalystStack);
            if (renderer != null) {
                if (renderer instanceof DualAlchemyCircleRenderer) {
                    List<ResourceLocation> resources = new ArrayList<ResourceLocation>();
                    resources.add(((DualAlchemyCircleRenderer) renderer).arrayResource);
                    resources.add(((DualAlchemyCircleRenderer) renderer).secondaryArrayResource);
                    return new PageAlchemyArray(resources, inputStack, catalystStack, outputStack);
                } else {
                    return new PageAlchemyArray(renderer.arrayResource, inputStack, catalystStack, outputStack);
                }
            }
        }

        return null;
    }
}
