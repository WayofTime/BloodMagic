package WayofTime.bloodmagic.compat.guideapi;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyCircleRenderer;
import WayofTime.bloodmagic.api.recipe.ShapedBloodOrbRecipe;
import WayofTime.bloodmagic.api.recipe.ShapelessBloodOrbRecipe;
import WayofTime.bloodmagic.api.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.client.render.alchemyArray.DualAlchemyCircleRenderer;
import WayofTime.bloodmagic.compat.guideapi.page.PageAlchemyArray;
import WayofTime.bloodmagic.compat.guideapi.page.recipeRenderer.ShapedBloodOrbRecipeRenderer;
import WayofTime.bloodmagic.compat.guideapi.page.recipeRenderer.ShapelessBloodOrbRecipeRenderer;
import amerifrance.guideapi.page.PageIRecipe;

public class BookUtils
{

    public static PageAlchemyArray getAlchemyPage(String key)
    {
        ItemStack[] recipe = AlchemyArrayRecipeRegistry.getRecipeForArrayEffect(key);
        if (recipe[0] != null)
        {
            ItemStack inputStack = recipe[0];
            ItemStack catalystStack = recipe[1];

            AlchemyCircleRenderer renderer = AlchemyArrayRecipeRegistry.getAlchemyCircleRenderer(inputStack, catalystStack);
            if (renderer instanceof DualAlchemyCircleRenderer)
            {
                List<ResourceLocation> resources = new ArrayList<ResourceLocation>();
                resources.add(((DualAlchemyCircleRenderer) renderer).arrayResource);
                resources.add(((DualAlchemyCircleRenderer) renderer).secondaryArrayResource);
                return new PageAlchemyArray(resources, inputStack, catalystStack);
            } else
            {
                return new PageAlchemyArray(renderer.arrayResource, inputStack, catalystStack);
            }
        }

        return null;
    }

    public static PageAlchemyArray getAlchemyPage(ItemStack outputStack)
    {
        ItemStack[] recipe = AlchemyArrayRecipeRegistry.getRecipeForOutputStack(outputStack);
        if (recipe[0] != null)
        {
            ItemStack inputStack = recipe[0];
            ItemStack catalystStack = recipe[1];

            AlchemyCircleRenderer renderer = AlchemyArrayRecipeRegistry.getAlchemyCircleRenderer(inputStack, catalystStack);
            if (renderer != null)
            {
                if (renderer instanceof DualAlchemyCircleRenderer)
                {
                    List<ResourceLocation> resources = new ArrayList<ResourceLocation>();
                    resources.add(((DualAlchemyCircleRenderer) renderer).arrayResource);
                    resources.add(((DualAlchemyCircleRenderer) renderer).secondaryArrayResource);
                    return new PageAlchemyArray(resources, inputStack, catalystStack, outputStack);
                } else
                {
                    return new PageAlchemyArray(renderer.arrayResource, inputStack, catalystStack, outputStack);
                }
            }
        }

        return null;
    }

    public static PageIRecipe getPageForRecipe(IRecipe recipe)
    {
        if (recipe instanceof ShapedBloodOrbRecipe)
        {
            return new PageIRecipe(recipe, new ShapedBloodOrbRecipeRenderer((ShapedBloodOrbRecipe) recipe));
        } else if (recipe instanceof ShapelessBloodOrbRecipe)
        {
            return new PageIRecipe(recipe, new ShapelessBloodOrbRecipeRenderer((ShapelessBloodOrbRecipe) recipe));
        }

        return new PageIRecipe(recipe);
    }
}
