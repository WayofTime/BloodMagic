package WayofTime.bloodmagic.api.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import WayofTime.bloodmagic.api.ItemStackWrapper;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffect;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyArrayEffectCrafting;
import WayofTime.bloodmagic.api.alchemyCrafting.AlchemyCircleRenderer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class AlchemyArrayRecipeRegistry
{
    public static final AlchemyCircleRenderer defaultRenderer = new AlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/BaseArray.png"));

    private static BiMap<List<ItemStack>, AlchemyArrayRecipe> recipes = HashBiMap.create();
    private static HashMap<String, AlchemyArrayEffect> effectMap = new HashMap<String, AlchemyArrayEffect>();

    /**
     * General case for creating an AlchemyArrayEffect for a given input.
     * 
     * @param input
     *        - Input item(s) that is used to change the Alchemy Circle into the
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
    public static void registerRecipe(List<ItemStack> input, @Nullable ItemStack catalystStack, AlchemyArrayEffect arrayEffect, AlchemyCircleRenderer circleRenderer)
    {
        effectMap.put(arrayEffect.getKey(), arrayEffect);

        for (Entry<List<ItemStack>, AlchemyArrayRecipe> entry : recipes.entrySet())
        {
            AlchemyArrayRecipe arrayRecipe = entry.getValue();
            if (arrayRecipe.doesInputMatchRecipe(input))
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
            recipes.put(input, new AlchemyArrayRecipe(input, catalystStack, arrayEffect, defaultRenderer));
        } else
        {
            recipes.put(input, new AlchemyArrayRecipe(input, catalystStack, arrayEffect, circleRenderer));
        }
    }

    public static AlchemyArrayEffect getAlchemyArrayEffect(String key)
    {
        return effectMap.get(key);
    }

    public static void registerCraftingRecipe(ItemStack input, ItemStack catalystStack, ItemStack outputStack, ResourceLocation arrayResource)
    {
        registerRecipe(input, catalystStack, new AlchemyArrayEffectCrafting(outputStack), arrayResource);
    }

    public static void registerCraftingRecipe(List<ItemStack> inputStacks, ItemStack catalystStack, ItemStack outputStack, ResourceLocation arrayResource)
    {
        registerRecipe(inputStacks, catalystStack, new AlchemyArrayEffectCrafting(outputStack), arrayResource);
    }

    public static void registerCraftingRecipe(String inputOreDict, ItemStack catalystStack, ItemStack outputStack, ResourceLocation arrayResource)
    {
        registerRecipe(OreDictionary.doesOreNameExist(inputOreDict) && OreDictionary.getOres(inputOreDict).size() > 0 ? OreDictionary.getOres(inputOreDict) : Collections.<ItemStack>emptyList(), catalystStack, new AlchemyArrayEffectCrafting(outputStack), arrayResource);
    }

    public static void registerCraftingRecipe(ItemStack input, ItemStack catalystStack, ItemStack outputStack)
    {
        registerRecipe(input, catalystStack, new AlchemyArrayEffectCrafting(outputStack));
    }

    public static void registerCraftingRecipe(List<ItemStack> inputStacks, ItemStack catalystStack, ItemStack outputStack)
    {
        registerRecipe(inputStacks, catalystStack, new AlchemyArrayEffectCrafting(outputStack));
    }

    public static void registerCraftingRecipe(String inputOreDict, ItemStack catalystStack, ItemStack outputStack)
    {
        registerRecipe(OreDictionary.doesOreNameExist(inputOreDict) && OreDictionary.getOres(inputOreDict).size() > 0 ? OreDictionary.getOres(inputOreDict) : Collections.<ItemStack>emptyList(), catalystStack, new AlchemyArrayEffectCrafting(outputStack));
    }

    public static void registerRecipe(ItemStack inputStacks, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, ResourceLocation arrayResource)
    {
        AlchemyCircleRenderer circleRenderer = arrayResource == null ? defaultRenderer : new AlchemyCircleRenderer(arrayResource);
        registerRecipe(Collections.singletonList(inputStacks), catalystStack, arrayEffect, circleRenderer);
    }

    public static void registerRecipe(ItemStack inputStacks, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, AlchemyCircleRenderer circleRenderer)
    {
        registerRecipe(Collections.singletonList(inputStacks), catalystStack, arrayEffect, circleRenderer);
    }

    public static void registerRecipe(List<ItemStack> inputStacks, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, ResourceLocation arrayResource)
    {
        AlchemyCircleRenderer circleRenderer = arrayResource == null ? defaultRenderer : new AlchemyCircleRenderer(arrayResource);
        registerRecipe(inputStacks, catalystStack, arrayEffect, circleRenderer);
    }

    public static void registerRecipe(String inputOreDict, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, ResourceLocation arrayResource)
    {
        AlchemyCircleRenderer circleRenderer = arrayResource == null ? defaultRenderer : new AlchemyCircleRenderer(arrayResource);
        registerRecipe(OreDictionary.doesOreNameExist(inputOreDict) && OreDictionary.getOres(inputOreDict).size() > 0 ? OreDictionary.getOres(inputOreDict) : Collections.<ItemStack>emptyList(), catalystStack, arrayEffect, circleRenderer);
    }

    public static void registerRecipe(ItemStack input, ItemStack catalystStack, AlchemyArrayEffect arrayEffect)
    {
        registerRecipe(Collections.singletonList(input), catalystStack, arrayEffect, (AlchemyCircleRenderer) null);
    }

    public static void registerRecipe(List<ItemStack> inputStacks, ItemStack catalystStack, AlchemyArrayEffect arrayEffect)
    {
        registerRecipe(inputStacks, catalystStack, arrayEffect, (AlchemyCircleRenderer) null);
    }

    public static void registerRecipe(String inputOreDict, ItemStack catalystStack, AlchemyArrayEffect arrayEffect)
    {
        registerRecipe(OreDictionary.doesOreNameExist(inputOreDict) && OreDictionary.getOres(inputOreDict).size() > 0 ? OreDictionary.getOres(inputOreDict) : Collections.<ItemStack>emptyList(), catalystStack, arrayEffect, (AlchemyCircleRenderer) null);
    }

    public static void replaceAlchemyCircle(List<ItemStack> input, AlchemyCircleRenderer circleRenderer)
    {
        if (circleRenderer == null)
            return;

        for (Entry<List<ItemStack>, AlchemyArrayRecipe> entry : recipes.entrySet())
        {
            AlchemyArrayRecipe arrayRecipe = entry.getValue();
            if (arrayRecipe.doesInputMatchRecipe(input))
                arrayRecipe.circleRenderer = circleRenderer;
        }
    }

    public static AlchemyArrayRecipe getRecipeForInput(List<ItemStack> input)
    {
        return recipes.get(input);
    }

    public static AlchemyArrayEffect getAlchemyArrayEffect(List<ItemStack> input, @Nullable ItemStack catalystStack)
    {
        for (Entry<List<ItemStack>, AlchemyArrayRecipe> entry : recipes.entrySet())
        {
            AlchemyArrayRecipe arrayRecipe = entry.getValue();
            if (input.size() == 1 && arrayRecipe.getInput().size() == 1)
            {
                if (ItemStackWrapper.getHolder(arrayRecipe.getInput().get(0)).equals(ItemStackWrapper.getHolder(input.get(0))))
                    return arrayRecipe.getAlchemyArrayEffectForCatalyst(catalystStack); // TODO: Decide if a copy should be returned.
            } else
            {
                if (input.equals(arrayRecipe.getInput()))
                    return arrayRecipe.getAlchemyArrayEffectForCatalyst(catalystStack);
            }
        }

        return null;
    }

    public static AlchemyArrayEffect getAlchemyArrayEffect(ItemStack input, @Nullable ItemStack catalystStack)
    {
        return getAlchemyArrayEffect(Collections.singletonList(input), catalystStack);
    }

    public static AlchemyCircleRenderer getAlchemyCircleRenderer(List<ItemStack> input)
    {
        for (Entry<List<ItemStack>, AlchemyArrayRecipe> entry : recipes.entrySet())
        {
            AlchemyArrayRecipe arrayRecipe = entry.getValue();
            if (arrayRecipe.doesInputMatchRecipe(input))
                return arrayRecipe.circleRenderer;
        }

        return defaultRenderer;
    }

    public static AlchemyCircleRenderer getAlchemyCircleRenderer(ItemStack itemStack)
    {
        return getAlchemyCircleRenderer(Collections.singletonList(itemStack));
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    public static class AlchemyArrayRecipe
    {
        public AlchemyCircleRenderer circleRenderer;
        public final List<ItemStack> input;
        public final BiMap<ItemStackWrapper, AlchemyArrayEffect> catalystMap = HashBiMap.create();

        private AlchemyArrayRecipe(List<ItemStack> input, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, AlchemyCircleRenderer circleRenderer, boolean useless)
        {
            this.input = input;

            catalystMap.put(ItemStackWrapper.getHolder(catalystStack), arrayEffect);

            this.circleRenderer = circleRenderer;
        }

        public AlchemyArrayRecipe(ItemStack inputStack, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, AlchemyCircleRenderer circleRenderer)
        {
            this(Collections.singletonList(inputStack), catalystStack, arrayEffect, circleRenderer, false);
        }

        public AlchemyArrayRecipe(String inputOreDict, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, AlchemyCircleRenderer circleRenderer)
        {
            this(OreDictionary.doesOreNameExist(inputOreDict) && OreDictionary.getOres(inputOreDict).size() > 0 ? OreDictionary.getOres(inputOreDict) : Collections.<ItemStack>emptyList(), catalystStack, arrayEffect, circleRenderer, false);
        }

        public AlchemyArrayRecipe(List<ItemStack> inputStacks, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, AlchemyCircleRenderer circleRenderer)
        {
            this(inputStacks, catalystStack, arrayEffect, circleRenderer, false);
        }

        /**
         * Compares the inputed list of ItemStacks to see if it matches with the
         * recipe's list.
         * 
         * @param comparedList
         *        - The list to compare with
         * 
         * @return - True if the ItemStack(s) is a compatible item
         */
        public boolean doesInputMatchRecipe(List<ItemStack> comparedList)
        {
            return !(comparedList == null || this.input == null) && (this.input.size() == 1 && comparedList.size() == 1 ? this.input.get(0).isItemEqual(comparedList.get(0)) : this.input.equals(comparedList));
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

    public static BiMap<List<ItemStack>, AlchemyArrayRecipe> getRecipes()
    {
        return HashBiMap.create(recipes);
    }
}
