package WayofTime.bloodmagic.ritual.harvest;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.iface.IHarvestHandler;
import WayofTime.bloodmagic.api.registry.HarvestRegistry;

/**
 * Harvest handler for standard plantable crops such as Wheat, Potatoes, and
 * Netherwart. <br>
 * Register a new crop for this handler with
 * {@link HarvestRegistry#registerStandardCrop(Block, int)}
 */
public class HarvestHandlerPlantable implements IHarvestHandler
{
    public HarvestHandlerPlantable()
    {
        HarvestRegistry.registerStandardCrop(Blocks.carrots, 7);
        HarvestRegistry.registerStandardCrop(Blocks.wheat, 7);
        HarvestRegistry.registerStandardCrop(Blocks.potatoes, 7);
        HarvestRegistry.registerStandardCrop(Blocks.nether_wart, 3);
    }

    @Override
    public boolean harvestAndPlant(World world, BlockPos pos, BlockStack blockStack)
    {
        if (!HarvestRegistry.getStandardCrops().containsKey(blockStack.getBlock()))
            return false;

        int matureMeta = HarvestRegistry.getStandardCrops().get(blockStack.getBlock());

        if (blockStack.getMeta() < matureMeta)
            return false;

        List<ItemStack> drops = blockStack.getBlock().getDrops(world, pos, blockStack.getState(), 0);
        boolean foundSeed = false;

        for (ItemStack stack : drops)
        {
            if (stack == null)
                continue;

            if (stack.getItem() instanceof IPlantable)
            {
                if (stack.stackSize > 1)
                    stack.stackSize--;
                else
                    drops.remove(stack);

                foundSeed = true;
                break;
            }
        }

        if (foundSeed)
        {
            world.setBlockState(pos, blockStack.getBlock().getDefaultState());
            world.playAuxSFX(2001, pos, Block.getIdFromBlock(blockStack.getBlock()) + (blockStack.getMeta() << 12));
            for (ItemStack stack : drops)
            {
                if (!world.isRemote)
                {
                    EntityItem toDrop = new EntityItem(world, pos.getX(), pos.getY() + 0.5, pos.getZ(), stack);
                    world.spawnEntityInWorld(toDrop);
                }
            }

            return true;
        }

        return false;
    }
}
