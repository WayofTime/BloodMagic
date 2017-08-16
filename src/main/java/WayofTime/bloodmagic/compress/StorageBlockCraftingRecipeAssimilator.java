package WayofTime.bloodmagic.compress;

public class StorageBlockCraftingRecipeAssimilator {
//    public static final List<Class> ignore = new ArrayList<Class>();
//
//    public List<IRecipe> getPackingRecipes()
//    {
//        // grab all recipes potentially suitable for packing or unpacking
//
//        List<PackingRecipe> packingRecipes = new LinkedList<PackingRecipe>();
//        List<IRecipe> unpackingRecipes = new ArrayList<IRecipe>();
//
//        for (IRecipe recipe : getCraftingRecipes())
//        {
//            if (ignore.contains(recipe.getClass()))
//                continue;
//
//            ItemStack output = recipe.getRecipeOutput();
//            if (output.isEmpty())
//                continue;
//
//            if (output.getCount() == 1)
//            {
//                PackingRecipe packingRecipe = getPackingRecipe(recipe);
//
//                if (packingRecipe != null)
//                {
//                    packingRecipes.add(packingRecipe);
//                }
//            } else if ((output.getCount() == 4 || output.getCount() == 9) && recipe.canFit(1, 1))
//            {
//                unpackingRecipes.add(recipe);
//            }
//        }
//
//        // grab all packing recipes which accept the output of any of the
//        // unpacking recipes
//
//        Container container = makeDummyContainer();
//        InventoryCrafting inventoryUnpack = new InventoryCrafting(container, 2, 2);
//        InventoryCrafting inventory2x2 = new InventoryCrafting(container, 2, 2);
//        InventoryCrafting inventory3x3 = new InventoryCrafting(container, 3, 3);
//        World world = null; // TODO: use a proper dummy world?
//
//        List<IRecipe> ret = new ArrayList<IRecipe>();
//
//        for (IRecipe recipeUnpack : unpackingRecipes)
//        {
//            if (ignore.contains(recipeUnpack.getClass()))
//                continue;
//
//            ItemStack unpacked = recipeUnpack.getRecipeOutput();
//            InventoryCrafting inventory = null;
//
//            for (Iterator<PackingRecipe> it = packingRecipes.iterator(); it.hasNext();)
//            {
//                PackingRecipe recipePack = it.next();
//
//                // check if the packing recipe accepts the unpacking recipe's output
//
//                boolean matched = false;
//
//                if (recipePack.possibleInputs != null)
//                {
//                    // the recipe could be parsed, use its inputs directly since that's faster verify recipe size
//
//                    if (recipePack.inputCount != unpacked.getCount())
//                        continue;
//
//                    // check if any of the input options matches the unpacked
//                    // item stack
//
//                    for (ItemStack stack : recipePack.possibleInputs)
//                    {
//                        if (areInputsIdentical(unpacked, stack))
//                        {
//                            matched = true;
//                            break;
//                        }
//                    }
//                } else
//                {
//                    // unknown IRecipe, check through the recipe conventionally verify recipe size for 3x3 to skip anything smaller quickly
//
//                    if (unpacked.getCount() == 9 && recipePack.recipe.getIngredients().size() < 9)
//                        continue;
//
//                    // initialize inventory late, but only once per unpack recipe
//
//                    if (inventory == null)
//                    {
//                        if (unpacked.getCount() == 4)
//                        {
//                            inventory = inventory2x2;
//                        } else
//                        {
//                            inventory = inventory3x3;
//                        }
//
//                        for (int i = 0; i < unpacked.getCount(); i++)
//                        {
//                            inventory.setInventorySlotContents(i, unpacked.copy());
//                        }
//                    }
//
//                    // check if the packing recipe accepts the unpacked item
//                    // stack
//
//                    matched = recipePack.recipe.matches(inventory, world);
//                }
//
//                if (matched)
//                {
//                    // check if the unpacking recipe accepts the packing
//                    // recipe's output
//
//                    ItemStack packOutput = recipePack.recipe.getRecipeOutput();
//                    inventoryUnpack.setInventorySlotContents(0, packOutput.copy());
//
//                    if (recipeUnpack.matches(inventoryUnpack, world))
//                    {
//                        ret.add(recipePack.recipe);
//                        it.remove();
//                    }
//                }
//            }
//        }
//
//        return ret;
//    }
//
//    @SuppressWarnings("unchecked")
//    private List<IRecipe> getCraftingRecipes()
//    {
//        return ForgeRegistries.RECIPES.getValues();
//    }
//
//    private Container makeDummyContainer()
//    {
//        return new Container()
//        {
//            @Override
//            public boolean canInteractWith(EntityPlayer player)
//            {
//                return true;
//            }
//        };
//    }
//
//    private PackingRecipe getPackingRecipe(IRecipe recipe)
//    {
//        if (recipe.getIngredients().size() < 4)
//            return null;
//
//        List<Ingredient> inputs = recipe.getIngredients();
//
//        // check if the recipe inputs are size 4 or 9
//
//        int count = 0;
//
//        for (Object o : inputs)
//        {
//            if (o != null)
//                count++;
//        }
//
//        if (count != 4 && count != 9)
//            return null;
//
//        // grab identical inputs
//
//        List<Ingredient> identicalInputs = getIdenticalInputs(inputs);
//        if (identicalInputs == null)
//            return null;
//
//        return new PackingRecipe(recipe, identicalInputs, count);
//    }
//
//    /**
//     * Determine the item stacks from the provided inputs which are suitable for
//     * every input element.
//     *
//     * @param inputs
//     *        List of all inputs, null elements are being ignored.
//     * @return List List of all options.
//     */
//    @SuppressWarnings("unchecked")
//    private List<Ingredient> getIdenticalInputs(List<Ingredient> inputs)
//    {
//        List<Ingredient> options = null;
//
//        for (Ingredient input : inputs)
//        {
//            if (input == null)
//                continue;
//
//            List<ItemStack> offers;
//
//            if (input. instanceof ItemStack)
//            {
//                offers = Collections.singletonList((ItemStack) input);
//            } else if (input instanceof List)
//            {
//                offers = (List<ItemStack>) input;
//
//                if (offers.isEmpty())
//                    return null;
//            } else
//            {
//                throw new RuntimeException("invalid input: " + input.getClass());
//            }
//
//            if (options == null)
//            {
//                options = new ArrayList<ItemStack>(offers);
//                continue;
//            }
//
//            for (Iterator<ItemStack> it = options.iterator(); it.hasNext();)
//            {
//                ItemStack stackReq = it.next();
//                boolean found = false;
//
//                for (ItemStack stackCmp : offers)
//                {
//                    if (areInputsIdentical(stackReq, stackCmp))
//                    {
//                        found = true;
//                        break;
//                    }
//                }
//
//                if (!found)
//                {
//                    it.remove();
//
//                    if (options.isEmpty())
//                        return null;
//                }
//            }
//        }
//
//        return options;
//    }
//
//    private boolean areInputsIdentical(ItemStack a, ItemStack b)
//    {
//
//        try
//        {
//            if (a.getItem() != b.getItem())
//                return false;
//
//            int dmgA = a.getItemDamage();
//            int dmgB = b.getItemDamage();
//
//            return dmgA == dmgB || dmgA == OreDictionary.WILDCARD_VALUE || dmgB == OreDictionary.WILDCARD_VALUE;
//        } catch (NullPointerException e)
//        {
//
//            BloodMagic.instance.getLogger().error("A mod in this instance has registered an item with a null input. Known problem mods are:");
//
//            // String err = "";
//            // for (String problem : problemMods)
//            // err += (err.length() > 0 ? ", " : "") + problem;
//            // BloodMagic.instance.getLogger().error(err);
//
//            return false;
//        }
//    }
//
//    private static class PackingRecipe
//    {
//        PackingRecipe(IRecipe recipe, List<ItemStack> possibleInputs, int inputCount)
//        {
//            this.recipe = recipe;
//            this.possibleInputs = possibleInputs;
//            this.inputCount = inputCount;
//        }
//
//        final IRecipe recipe;
//        final List<ItemStack> possibleInputs;
//        final int inputCount;
//    }
}
