package WayofTime.bloodmagic.recipe.alchemyTable;

import WayofTime.bloodmagic.util.Constants;
import WayofTime.bloodmagic.util.Utils;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class AlchemyTableDyeableRecipe extends AlchemyTableRecipe {
    private ItemStack inputItem;

    public AlchemyTableDyeableRecipe(int lpDrained, int ticksRequired, int tierRequired, ItemStack inputItem) {
        super(inputItem, lpDrained, ticksRequired, tierRequired);

        ArrayList<ItemStack> validDyes = new ArrayList<>();
        validDyes.add(new ItemStack(Items.NAME_TAG));
        validDyes.add(new ItemStack(Items.DYE, 1, OreDictionary.WILDCARD_VALUE));

        ArrayList<Object> recipe = new ArrayList<>();
        recipe.add(inputItem);
        recipe.add(validDyes);

        this.input = recipe;

        this.inputItem = inputItem;
    }

    @Override
    public ItemStack getRecipeOutput(List<ItemStack> inputList) {
        int nameTagOrDyeLocation = -1;
        int inputItemLocation = -1;
        for (int x = 0; x < inputList.size(); x++) {
            ItemStack slot = inputList.get(x);

            if (slot != null) {
                boolean match = OreDictionary.itemMatches(inputItem, slot, false);

                if (match) {
                    inputItemLocation = x;
                } else {
                    if (slot.getItem() == Items.NAME_TAG || slot.getItem() == Items.DYE) {
                        nameTagOrDyeLocation = x;
                    }
                }
            }
        }

        if (nameTagOrDyeLocation != -1 && inputItemLocation != -1) {
            ItemStack tagOrDyeStack = inputList.get(nameTagOrDyeLocation);
            ItemStack inputStack = inputList.get(inputItemLocation);

            if (inputStack.isEmpty() || tagOrDyeStack.isEmpty()) {
                return output.copy();
            }

            ItemStack outputStack = inputStack.copy();

            if (tagOrDyeStack.getItem() == Items.NAME_TAG) {
                if (!outputStack.hasTagCompound()) {
                    outputStack.setTagCompound(new NBTTagCompound());
                }

                outputStack.getTagCompound().setString(Constants.NBT.COLOR, tagOrDyeStack.getDisplayName());

                return outputStack;
            } else {
                EnumDyeColor dyeColor = ItemBanner.getBaseColor(tagOrDyeStack);
                if (!outputStack.hasTagCompound()) {
                    outputStack.setTagCompound(new NBTTagCompound());
                }

                outputStack.getTagCompound().setString(Constants.NBT.COLOR, String.valueOf(Utils.DYE_COLOR_VALUES.getOrDefault(dyeColor, 0)));

                return outputStack;
            }
        }

        return output.copy();
    }

    @Override
    public boolean matches(List<ItemStack> checkedList, World world, BlockPos pos) {
        boolean hasNameTagOrDye = false;
        boolean hasInputItem = false;

        for (ItemStack slot : checkedList) {
            if (!slot.isEmpty()) {
                boolean match = OreDictionary.itemMatches(inputItem, slot, false);

                if (match && hasInputItem) {
                    return false;
                } else if (match) {
                    hasInputItem = true;
                } else {
                    if (slot.getItem() == Items.NAME_TAG || slot.getItem() == Items.DYE) {
                        if (hasNameTagOrDye) {
                            return false;
                        } else {
                            hasNameTagOrDye = true;
                        }
                    }
                }
            }
        }

        return hasNameTagOrDye && hasInputItem;
    }
}
