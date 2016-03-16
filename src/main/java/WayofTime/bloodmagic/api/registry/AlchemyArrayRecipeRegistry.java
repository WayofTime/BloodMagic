package WayofTime.bloodmagic.api.registry;

import java.util.Map.Entry;

import javax.annotation.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffect;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffectCrafting;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyCircleRenderer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class AlchemyArrayRecipeRegistry
{
    public static final AlchemyCircleRenderer defaultRenderer = new AlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/BaseArray.png"));

    private static BiMap<ItemStackWrapper, AlchemyArrayRecipe> recipes = HashBiMap.create();

    /**
     * General case for creating an AlchemyArrayEffect for a given input.
     * 
     * @param inputStack
     *        - Input item that is used to change the Alchemy Circle into the
     *        circle that you are making
     * @param catalystStack
     *        - Catalyst item that, when right-clicked onto the array, will
     *        cause an effect
     * @param arrayEffect
     *        - The effect that will be activated once the array is activated
     * @param circleRenderer
     *        - Circle rendered when the array is passive - can be substituted
     *        for a special renderer
     */
    public static void registerRecipe(ItemStack inputStack, @Nullable ItemStack catalystStack, AlchemyArrayEffect arrayEffect, AlchemyCircleRenderer circleRenderer)
    {
        for (Entry<ItemStackWrapper, AlchemyArrayRecipe> entry : recipes.entrySet())
        {
            AlchemyArrayRecipe arrayRecipe = entry.getValue();
            if (arrayRecipe.doesInputMatchRecipe(inputStack))
            {
                AlchemyArrayEffect eff = arrayRecipe.getAlchemyArrayEffectForCatalyst(catalystStack);
                if (eff != null)
                {
                    return; // Recipe already exists!
                } else
                {
                    arrayRecipe.catalystMap.put(ItemStackWrapper.getHolder(catalystStack), arrayEffect);
                    if (circleRenderer != null)
                        arrayRecipe.circleRenderer = circleRenderer;
                    return;
                }
            }
        }

        if (circleRenderer == null)
        {
            recipes.put(ItemStackWrapper.getHolder(inputStack), new AlchemyArrayRecipe(inputStack, catalystStack, arrayEffect, defaultRenderer));
        } else
        {
            recipes.put(ItemStackWrapper.getHolder(inputStack), new AlchemyArrayRecipe(inputStack, catalystStack, arrayEffect, circleRenderer));
        }

        recipes.put(ItemStackWrapper.getHolder(inputStack), new AlchemyArrayRecipe(inputStack, catalystStack, arrayEffect, circleRenderer));
    }

    public static void registerCraftingRecipe(ItemStack inputStack, ItemStack catalystStack, ItemStack outputStack, AlchemyCircleRenderer circleRenderer)
    {
        registerRecipe(inputStack, catalystStack, new AlchemyArrayEffectCrafting(outputStack), circleRenderer);
    }

    public static void registerCraftingRecipe(ItemStack inputStack, ItemStack catalystStack, ItemStack outputStack, ResourceLocation arrayResource)
    {
        registerRecipe(inputStack, catalystStack, new AlchemyArrayEffectCrafting(outputStack), arrayResource);
    }

    public static void registerCraftingRecipe(ItemStack inputStack, ItemStack catalystStack, ItemStack outputStack)
    {
        registerRecipe(inputStack, catalystStack, new AlchemyArrayEffectCrafting(outputStack));
    }

    public static void registerRecipe(ItemStack inputStack, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, ResourceLocation arrayResource)
    {
        AlchemyCircleRenderer circleRenderer = arrayResource == null ? defaultRenderer : new AlchemyCircleRenderer(arrayResource);
        registerRecipe(inputStack, catalystStack, arrayEffect, circleRenderer);
    }

    public static void registerRecipe(ItemStack inputStack, ItemStack catalystStack, AlchemyArrayEffect arrayEffect)
    {
        registerRecipe(inputStack, catalystStack, arrayEffect, (AlchemyCircleRenderer) null);
    }

    public static void replaceAlchemyCircle(ItemStack inputStack, AlchemyCircleRenderer circleRenderer)
    {
        if (circleRenderer == null)
            return;

        for (Entry<ItemStackWrapper, AlchemyArrayRecipe> entry : recipes.entrySet())
        {
            AlchemyArrayRecipe arrayRecipe = entry.getValue();
            if (arrayRecipe.doesInputMatchRecipe(inputStack))
                arrayRecipe.circleRenderer = circleRenderer;
        }
    }

    public static AlchemyArrayRecipe getRecipeForInput(ItemStack input)
    {
        return recipes.get(ItemStackWrapper.getHolder(input));
    }

    public static AlchemyArrayEffect getAlchemyArrayEffect(ItemStack inputStack, @Nullable ItemStack catalystStack)
    {
        for (Entry<ItemStackWrapper, AlchemyArrayRecipe> entry : recipes.entrySet())
        {
            AlchemyArrayRecipe arrayRecipe = entry.getValue();
            if (ItemStackWrapper.getHolder(arrayRecipe.getInputStack()).equals(ItemStackWrapper.getHolder(inputStack)))
                return arrayRecipe.getAlchemyArrayEffectForCatalyst(catalystStack); // TODO: Decide if a copy should be returned.
        }

        return null;
    }

    public static AlchemyCircleRenderer getAlchemyCircleRenderer(ItemStack inputStack)
    {
        for (Entry<ItemStackWrapper, AlchemyArrayRecipe> entry : recipes.entrySet())
        {
            AlchemyArrayRecipe arrayRecipe = entry.getValue();
            if (arrayRecipe.doesInputMatchRecipe(inputStack))
                return arrayRecipe.circleRenderer;
        }

        return defaultRenderer;
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    public static class AlchemyArrayRecipe
    {

        public AlchemyCircleRenderer circleRenderer;
        public final ItemStack inputStack;
        public final BiMap<ItemStackWrapper, AlchemyArrayEffect> catalystMap = HashBiMap.create();

        public AlchemyArrayRecipe(ItemStack inputStack, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, AlchemyCircleRenderer circleRenderer)
        {
            this.inputStack = inputStack;

            catalystMap.put(ItemStackWrapper.getHolder(catalystStack), arrayEffect);

            this.circleRenderer = circleRenderer;
        }

        /**
         * Compares the inputed ItemStack to see if it matches with the recipe's
         * inputStack.
         * 
         * @param comparedStack
         *        - The stack to compare with
         * 
         * @return - True if the ItemStack is a compatible item
         */
        public boolean doesInputMatchRecipe(ItemStack comparedStack)
        {
            return !(comparedStack == null || this.inputStack == null) && this.inputStack.isItemEqual(comparedStack);
        }

        /**
         * Gets the actual AlchemyArrayEffect for the given catalyst.
         * 
         * @param comparedStack
         *        The catalyst that is being checked
         * 
         * @return - The effect
         */
        public AlchemyArrayEffect getAlchemyArrayEffectForCatalyst(@Nullable ItemStack comparedStack)
        {
            for (Entry<ItemStackWrapper, AlchemyArrayEffect> entry : catalystMap.entrySet())
            {
                ItemStack catalystStack = entry.getKey().toStack();

                if (comparedStack == null && catalystStack == null)
                    return entry.getValue();

                if (comparedStack == null || catalystStack == null)
                    continue;

                if (catalystStack.isItemEqual(comparedStack))
                    return entry.getValue();
            }

            return null;
        }
    }

    public static BiMap<ItemStackWrapper, AlchemyArrayRecipe> getRecipes()
    {
        return HashBiMap.create(recipes);
    }
}
