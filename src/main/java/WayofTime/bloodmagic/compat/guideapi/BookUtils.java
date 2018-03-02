package WayofTime.bloodmagic.compat.guideapi;

import WayofTime.bloodmagic.alchemyArray.AlchemyCircleRenderer;
import WayofTime.bloodmagic.core.registry.AlchemyArrayRecipeRegistry;
import WayofTime.bloodmagic.client.render.alchemyArray.DualAlchemyCircleRenderer;
import WayofTime.bloodmagic.compat.guideapi.page.PageAlchemyArray;
import amerifrance.guideapi.page.PageIRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class BookUtils {

    public static PageAlchemyArray getAlchemyPage(String key) {
        ItemStack[] recipe = AlchemyArrayRecipeRegistry.getRecipeForArrayEffect(key);
        if (recipe[0] != null) {
            ItemStack inputStack = recipe[0];
            ItemStack catalystStack = recipe[1];

            AlchemyCircleRenderer renderer = AlchemyArrayRecipeRegistry.getAlchemyCircleRenderer(inputStack, catalystStack);
            if (renderer instanceof DualAlchemyCircleRenderer) {
                List<ResourceLocation> resources = new ArrayList<>();
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
                    List<ResourceLocation> resources = new ArrayList<>();
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

    public static PageIRecipe getPageForRecipe(IRecipe recipe) {
        return new PageIRecipe(recipe);
    }
}
