package WayofTime.bloodmagic.alchemyArray;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class AlchemyArrayEffectCrafting extends AlchemyArrayEffect {
    public final ItemStack outputStack;
    public int tickLimit;

    public AlchemyArrayEffectCrafting(ItemStack outputStack) {
        this(outputStack, 200);
    }

    public AlchemyArrayEffectCrafting(ItemStack outputStack, int tickLimit) {
        this(outputStack.toString() + tickLimit, outputStack, tickLimit);
    }

    public AlchemyArrayEffectCrafting(String key, ItemStack outputStack, int tickLimit) {
        super(key);
        this.outputStack = outputStack;
        this.tickLimit = tickLimit;
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive) {
        // TODO: Add recipe rechecking to verify nothing screwy is going on.
        if (tile.getWorld().isRemote) {
            return false;
        }

        if (ticksActive >= tickLimit) {
            BlockPos pos = tile.getPos();

            ItemStack output = outputStack.copy();

            ItemEntity outputEntity = new ItemEntity(tile.getWorld(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, output);

            tile.getWorld().spawnEntity(outputEntity);

            return true;
        }

        return false;
    }

    @Override
    public void writeToNBT(CompoundNBT tag) {

    }

    @Override
    public void readFromNBT(CompoundNBT tag) {

    }

    @Override
    public AlchemyArrayEffect getNewCopy() {
        return new AlchemyArrayEffectCrafting(key, outputStack, tickLimit);
    }
}
