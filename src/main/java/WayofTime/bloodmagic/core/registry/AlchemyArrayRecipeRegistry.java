package WayofTime.bloodmagic.core.registry;

import WayofTime.bloodmagic.util.ItemStackWrapper;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffect;
import WayofTime.bloodmagic.alchemyArray.AlchemyArrayEffectCrafting;
import WayofTime.bloodmagic.alchemyArray.AlchemyCircleRenderer;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class AlchemyArrayRecipeRegistry {
    public static final AlchemyCircleRenderer DEFAULT_RENDERER = new AlchemyCircleRenderer(new ResourceLocation("bloodmagic", "textures/models/AlchemyArrays/BaseArray.png"));

    private static BiMap<List<ItemStack>, AlchemyArrayRecipe> recipes = HashBiMap.create();
    private static HashMap<String, AlchemyArrayEffect> effectMap = new HashMap<>();

    /**
     * General case for creating an AlchemyArrayEffect for a given input.
     *
     * @param input          - Input item(s) that is used to change the Alchemy Circle into the
     *                       circle that you are making
     * @param catalystStack  - Catalyst item that, when right-clicked onto the array, will
     *                       cause an effect
     * @param arrayEffect    - The effect that will be activated once the array is activated
     * @param circleRenderer - Circle rendered when the array is passive - can be substituted
     *                       for a special renderer
     */
    public static void registerRecipe(List<ItemStack> input, @Nullable ItemStack catalystStack, AlchemyArrayEffect arrayEffect, AlchemyCircleRenderer circleRenderer) {
        effectMap.put(arrayEffect.getKey(), arrayEffect);

        for (Entry<List<ItemStack>, AlchemyArrayRecipe> entry : recipes.entrySet()) {
            AlchemyArrayRecipe arrayRecipe = entry.getValue();
            if (arrayRecipe.doesInputMatchRecipe(input)) {
                AlchemyArrayEffect eff = arrayRecipe.getAlchemyArrayEffectForCatalyst(catalystStack);
                if (eff != null) {
                    return; // Recipe already exists!
                } else {
                    arrayRecipe.catalystMap.put(ItemStackWrapper.getHolder(catalystStack), arrayEffect);
                    if (circleRenderer != null) {
                        if (arrayRecipe.defaultCircleRenderer == null) {
                            arrayRecipe.defaultCircleRenderer = circleRenderer;
                        } else {
                            arrayRecipe.circleMap.put(ItemStackWrapper.getHolder(catalystStack), circleRenderer);
                        }
                    }
                    return;
                }
            }
        }

        recipes.put(input, new AlchemyArrayRecipe(input, catalystStack, arrayEffect, circleRenderer == null ? DEFAULT_RENDERER : circleRenderer));
    }

    public static AlchemyArrayEffect getAlchemyArrayEffect(String key) {
        return effectMap.get(key);
    }

    /**
     * @param key
     * @return an array of two ItemStacks - first index is the input stack,
     * second is the catalyst stack. Returns {null, null} if no recipe
     * is valid.
     */
    public static ItemStack[] getRecipeForArrayEffect(String key) {
        for (Entry<List<ItemStack>, AlchemyArrayRecipe> entry : recipes.entrySet()) {
            AlchemyArrayRecipe recipe = entry.getValue();
            if (recipe != null && entry.getKey().size() > 0) {
                for (Entry<ItemStackWrapper, AlchemyArrayEffect> effectEntry : recipe.catalystMap.entrySet()) {
                    if (effectEntry.getValue() != null && effectEntry.getValue().key.equals(key)) {
                        return new ItemStack[]{entry.getKey().get(0), effectEntry.getKey().toStack()};
                    }
                }
            }
        }

        return new ItemStack[]{null, null};
    }

    /**
     * @param stack of the recipe
     * @return an array of two ItemStacks - first index is the input stack,
     * second is the catalyst stack. Returns {null, null} if no recipe
     * is valid.
     */
    public static ItemStack[] getRecipeForOutputStack(ItemStack stack) {
        for (Entry<List<ItemStack>, AlchemyArrayRecipe> entry : recipes.entrySet()) {
            AlchemyArrayRecipe recipe = entry.getValue();
            if (recipe != null && entry.getKey().size() > 0) {
                for (Entry<ItemStackWrapper, AlchemyArrayEffect> effectEntry : recipe.catalystMap.entrySet()) {
                    if (effectEntry.getValue() instanceof AlchemyArrayEffectCrafting) {
                        AlchemyArrayEffectCrafting craftingEffect = (AlchemyArrayEffectCrafting) effectEntry.getValue();
                        ItemStack resultStack = craftingEffect.outputStack;
                        if (!resultStack.isEmpty()) {
                            if (resultStack.getItem() == stack.getItem() && resultStack.getItemDamage() == stack.getItemDamage()) {
                                return new ItemStack[]{entry.getKey().get(0), effectEntry.getKey().toStack()};
                            }
                        }
                    }
                }
            }
        }

        return new ItemStack[]{null, null};
    }

    public static void registerCraftingRecipe(ItemStack input, ItemStack catalystStack, ItemStack outputStack, ResourceLocation arrayResource) {
        registerRecipe(input, catalystStack, new AlchemyArrayEffectCrafting(outputStack), arrayResource);
    }

    public static void registerCraftingRecipe(List<ItemStack> inputStacks, ItemStack catalystStack, ItemStack outputStack, ResourceLocation arrayResource) {
        registerRecipe(inputStacks, catalystStack, new AlchemyArrayEffectCrafting(outputStack), arrayResource);
    }

    public static void registerCraftingRecipe(String inputOreDict, ItemStack catalystStack, ItemStack outputStack, ResourceLocation arrayResource) {
        registerRecipe(OreDictionary.doesOreNameExist(inputOreDict) && OreDictionary.getOres(inputOreDict).size() > 0 ? OreDictionary.getOres(inputOreDict) : Collections.emptyList(), catalystStack, new AlchemyArrayEffectCrafting(outputStack), arrayResource);
    }

    public static void registerCraftingRecipe(ItemStack input, ItemStack catalystStack, ItemStack outputStack) {
        registerRecipe(input, catalystStack, new AlchemyArrayEffectCrafting(outputStack));
    }

    public static void registerCraftingRecipe(List<ItemStack> inputStacks, ItemStack catalystStack, ItemStack outputStack) {
        registerRecipe(inputStacks, catalystStack, new AlchemyArrayEffectCrafting(outputStack));
    }

    public static void registerCraftingRecipe(String inputOreDict, ItemStack catalystStack, ItemStack outputStack) {
        registerRecipe(OreDictionary.doesOreNameExist(inputOreDict) && OreDictionary.getOres(inputOreDict).size() > 0 ? OreDictionary.getOres(inputOreDict) : Collections.emptyList(), catalystStack, new AlchemyArrayEffectCrafting(outputStack));
    }

    public static void registerRecipe(ItemStack inputStacks, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, ResourceLocation arrayResource) {
        AlchemyCircleRenderer circleRenderer = arrayResource == null ? DEFAULT_RENDERER : new AlchemyCircleRenderer(arrayResource);
        registerRecipe(Collections.singletonList(inputStacks), catalystStack, arrayEffect, circleRenderer);
    }

    public static void registerRecipe(ItemStack inputStacks, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, AlchemyCircleRenderer circleRenderer) {
        registerRecipe(Collections.singletonList(inputStacks), catalystStack, arrayEffect, circleRenderer);
    }

    public static void registerRecipe(List<ItemStack> inputStacks, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, ResourceLocation arrayResource) {
        AlchemyCircleRenderer circleRenderer = arrayResource == null ? DEFAULT_RENDERER : new AlchemyCircleRenderer(arrayResource);
        registerRecipe(inputStacks, catalystStack, arrayEffect, circleRenderer);
    }

    public static void registerRecipe(String inputOreDict, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, ResourceLocation arrayResource) {
        AlchemyCircleRenderer circleRenderer = arrayResource == null ? DEFAULT_RENDERER : new AlchemyCircleRenderer(arrayResource);
        registerRecipe(OreDictionary.doesOreNameExist(inputOreDict) && OreDictionary.getOres(inputOreDict).size() > 0 ? OreDictionary.getOres(inputOreDict) : Collections.emptyList(), catalystStack, arrayEffect, circleRenderer);
    }

    public static void registerRecipe(ItemStack input, ItemStack catalystStack, AlchemyArrayEffect arrayEffect) {
        registerRecipe(Collections.singletonList(input), catalystStack, arrayEffect, (AlchemyCircleRenderer) null);
    }

    public static void registerRecipe(List<ItemStack> inputStacks, ItemStack catalystStack, AlchemyArrayEffect arrayEffect) {
        registerRecipe(inputStacks, catalystStack, arrayEffect, (AlchemyCircleRenderer) null);
    }

    public static void registerRecipe(String inputOreDict, ItemStack catalystStack, AlchemyArrayEffect arrayEffect) {
        registerRecipe(OreDictionary.doesOreNameExist(inputOreDict) && OreDictionary.getOres(inputOreDict).size() > 0 ? OreDictionary.getOres(inputOreDict) : Collections.emptyList(), catalystStack, arrayEffect, (AlchemyCircleRenderer) null);
    }

    public static void replaceAlchemyCircle(List<ItemStack> input, AlchemyCircleRenderer circleRenderer) {
        if (circleRenderer == null)
            return;

        for (Entry<List<ItemStack>, AlchemyArrayRecipe> entry : recipes.entrySet()) {
            AlchemyArrayRecipe arrayRecipe = entry.getValue();
            if (arrayRecipe.doesInputMatchRecipe(input))
                arrayRecipe.defaultCircleRenderer = circleRenderer;
        }
    }

    public static AlchemyArrayRecipe getRecipeForInput(List<ItemStack> input) {
        return recipes.get(input);
    }

    public static AlchemyArrayEffect getAlchemyArrayEffect(List<ItemStack> input, @Nullable ItemStack catalystStack) {
        for (Entry<List<ItemStack>, AlchemyArrayRecipe> entry : recipes.entrySet()) {
            AlchemyArrayRecipe arrayRecipe = entry.getValue();
            if (input.size() == 1 && arrayRecipe.getInput().size() == 1) {
                if (ItemStack.areItemsEqual(input.get(0), arrayRecipe.input.get(0))) {
                    AlchemyArrayEffect effect = arrayRecipe.getAlchemyArrayEffectForCatalyst(catalystStack);
                    if (effect != null) {
                        return effect.getNewCopy();
                    } else {
                        return null;
                    }
                }
            } else {
                if (input.equals(arrayRecipe.getInput())) {
                    AlchemyArrayEffect effect = arrayRecipe.getAlchemyArrayEffectForCatalyst(catalystStack);
                    if (effect != null) {
                        return effect.getNewCopy();
                    } else {
                        return null;
                    }
                }
            }
        }

        return null;
    }

    public static AlchemyArrayEffect getAlchemyArrayEffect(ItemStack input, @Nullable ItemStack catalystStack) {
        return getAlchemyArrayEffect(Collections.singletonList(input), catalystStack);
    }

    public static AlchemyCircleRenderer getAlchemyCircleRenderer(List<ItemStack> input, @Nullable ItemStack catalystStack) {
        for (Entry<List<ItemStack>, AlchemyArrayRecipe> entry : recipes.entrySet()) {
            AlchemyArrayRecipe arrayRecipe = entry.getValue();
            if (arrayRecipe.doesInputMatchRecipe(input)) {
                return arrayRecipe.getAlchemyArrayRendererForCatalyst(catalystStack);
            }
        }

        return DEFAULT_RENDERER;
    }

    public static AlchemyCircleRenderer getAlchemyCircleRenderer(ItemStack itemStack, @Nullable ItemStack catalystStack) {
        return getAlchemyCircleRenderer(Collections.singletonList(itemStack), catalystStack);
    }

    public static BiMap<List<ItemStack>, AlchemyArrayRecipe> getRecipes() {
        return HashBiMap.create(recipes);
    }

    public static class AlchemyArrayRecipe {
        public final List<ItemStack> input;
        public final BiMap<ItemStackWrapper, AlchemyArrayEffect> catalystMap = HashBiMap.create();
        public final BiMap<ItemStackWrapper, AlchemyCircleRenderer> circleMap = HashBiMap.create();
        public AlchemyCircleRenderer defaultCircleRenderer;

        private AlchemyArrayRecipe(List<ItemStack> input, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, AlchemyCircleRenderer circleRenderer, boolean useless) {
            this.input = input;

            catalystMap.put(ItemStackWrapper.getHolder(catalystStack), arrayEffect);

            this.defaultCircleRenderer = circleRenderer;
        }

        public AlchemyArrayRecipe(ItemStack inputStack, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, AlchemyCircleRenderer circleRenderer) {
            this(Collections.singletonList(inputStack), catalystStack, arrayEffect, circleRenderer, false);
        }

        public AlchemyArrayRecipe(String inputOreDict, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, AlchemyCircleRenderer circleRenderer) {
            this(OreDictionary.doesOreNameExist(inputOreDict) && OreDictionary.getOres(inputOreDict).size() > 0 ? OreDictionary.getOres(inputOreDict) : Collections.emptyList(), catalystStack, arrayEffect, circleRenderer, false);
        }

        public AlchemyArrayRecipe(List<ItemStack> inputStacks, ItemStack catalystStack, AlchemyArrayEffect arrayEffect, AlchemyCircleRenderer circleRenderer) {
            this(inputStacks, catalystStack, arrayEffect, circleRenderer, false);
        }

        /**
         * Compares the inputed list of ItemStacks to see if it matches with the
         * recipe's list.
         *
         * @param comparedList - The list to compare with
         * @return - True if the ItemStack(s) is a compatible item
         */
        public boolean doesInputMatchRecipe(List<ItemStack> comparedList) {
            return !(comparedList == null || this.input == null) && (this.input.size() == 1 && comparedList.size() == 1 ? this.input.get(0).isItemEqual(comparedList.get(0)) : this.input.equals(comparedList));
        }

        /**
         * Gets the actual AlchemyArrayEffect for the given catalyst.
         *
         * @param comparedStack The catalyst that is being checked
         * @return - The effect
         */
        public AlchemyArrayEffect getAlchemyArrayEffectForCatalyst(@Nullable ItemStack comparedStack) {
            for (Entry<ItemStackWrapper, AlchemyArrayEffect> entry : catalystMap.entrySet()) {
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

        public AlchemyCircleRenderer getAlchemyArrayRendererForCatalyst(@Nullable ItemStack comparedStack) {
            for (Entry<ItemStackWrapper, AlchemyCircleRenderer> entry : circleMap.entrySet()) {
                ItemStack catalystStack = entry.getKey().toStack();

                if (comparedStack == null && catalystStack == null)
                    return entry.getValue();

                if (comparedStack == null || catalystStack == null)
                    continue;

                if (catalystStack.isItemEqual(comparedStack))
                    return entry.getValue();
            }

            return defaultCircleRenderer;
        }

        public AlchemyCircleRenderer getDefaultCircleRenderer() {
            return defaultCircleRenderer;
        }

        public List<ItemStack> getInput() {
            return input;
        }

        public BiMap<ItemStackWrapper, AlchemyArrayEffect> getCatalystMap() {
            return catalystMap;
        }

        public BiMap<ItemStackWrapper, AlchemyCircleRenderer> getCircleMap() {
            return circleMap;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AlchemyArrayRecipe)) return false;

            AlchemyArrayRecipe that = (AlchemyArrayRecipe) o;

            if (defaultCircleRenderer != null ? !defaultCircleRenderer.equals(that.defaultCircleRenderer) : that.defaultCircleRenderer != null)
                return false;
            if (input != null ? !input.equals(that.input) : that.input != null) return false;
            if (catalystMap != null ? !catalystMap.equals(that.catalystMap) : that.catalystMap != null) return false;
            return circleMap != null ? circleMap.equals(that.circleMap) : that.circleMap == null;
        }

        @Override
        public int hashCode() {
            int result = defaultCircleRenderer != null ? defaultCircleRenderer.hashCode() : 0;
            result = 31 * result + (input != null ? input.hashCode() : 0);
            result = 31 * result + (catalystMap != null ? catalystMap.hashCode() : 0);
            result = 31 * result + (circleMap != null ? circleMap.hashCode() : 0);
            return result;
        }
    }
}
