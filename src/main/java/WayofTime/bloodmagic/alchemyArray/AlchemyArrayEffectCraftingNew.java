package WayofTime.bloodmagic.alchemyArray;

import WayofTime.bloodmagic.api.impl.recipe.RecipeAlchemyArray;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class AlchemyArrayEffectCraftingNew extends AlchemyArrayEffect {

    private final RecipeAlchemyArray recipe;

    public AlchemyArrayEffectCraftingNew(RecipeAlchemyArray recipe) {
        this(recipe.getOutput().toString() + 200, recipe);
    }

    public AlchemyArrayEffectCraftingNew(String key, RecipeAlchemyArray recipeAlchemyArray) {
        super(key);

        this.recipe = recipeAlchemyArray;
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive) {
        if (tile.getWorld().isRemote)
            return false;

        if (ticksActive >= 200) {
            BlockPos pos = tile.getPos();

            ItemStack output = recipe.getOutput().copy();

            EntityItem outputEntity = new EntityItem(tile.getWorld(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, output);

            tile.getWorld().spawnEntity(outputEntity);

            return true;
        }

        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }

    @Override
    public AlchemyArrayEffect getNewCopy() {
        return new AlchemyArrayEffectCraftingNew(key, recipe);
    }
}
