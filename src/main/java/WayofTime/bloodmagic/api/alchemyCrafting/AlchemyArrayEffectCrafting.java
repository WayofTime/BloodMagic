package WayofTime.bloodmagic.api.alchemyCrafting;

import lombok.Getter;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public class AlchemyArrayEffectCrafting extends AlchemyArrayEffect
{
    @Getter
    public final ItemStack outputStack;
    public int tickLimit;

    public AlchemyArrayEffectCrafting(ItemStack outputStack)
    {
        this(outputStack, 200);
    }

    public AlchemyArrayEffectCrafting(ItemStack outputStack, int tickLimit)
    {
        this.outputStack = outputStack;
        this.tickLimit = tickLimit;
    }

    @Override
    public boolean update(TileEntity tile, int ticksActive)
    {
        // TODO: Add recipe rechecking to verify nothing screwy is going on.
        if (tile.getWorld().isRemote)
        {
            return false;
        }

        if (ticksActive >= tickLimit)
        {
            BlockPos pos = tile.getPos();

            ItemStack output = outputStack.copy();
            EntityItem outputEntity = new EntityItem(tile.getWorld(), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, output);

            tile.getWorld().spawnEntityInWorld(outputEntity);

            return true;
        }

        return false;
    }
}
