package pneumaticCraft.api.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AssemblyRecipe{
    public static List<AssemblyRecipe> drillRecipes = new ArrayList<AssemblyRecipe>();
    public static List<AssemblyRecipe> laserRecipes = new ArrayList<AssemblyRecipe>();
    public static List<AssemblyRecipe> drillLaserRecipes = new ArrayList<AssemblyRecipe>();

    private final ItemStack input;
    private final ItemStack output;

    public AssemblyRecipe(ItemStack input, ItemStack output){
        this.input = input;
        this.output = output;
    }

    public ItemStack getInput(){
        return input;
    }

    public ItemStack getOutput(){
        return output;
    }

    public static void addDrillRecipe(Object input, Object output){
        drillRecipes.add(new AssemblyRecipe(getStackFromObject(input), getStackFromObject(output)));
    }

    public static void addLaserRecipe(Object input, Object output){
        laserRecipes.add(new AssemblyRecipe(getStackFromObject(input), getStackFromObject(output)));
    }

    private static ItemStack getStackFromObject(Object object){
        if(object instanceof Block) {
            return new ItemStack((Block)object);
        } else if(object instanceof Item) {
            return new ItemStack((Item)object);
        } else {
            return (ItemStack)object;
        }
    }

}
