package WayofTime.alchemicalWizardry.common.harvest;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import WayofTime.alchemicalWizardry.api.harvest.IHarvestHandler;

public class GenericItemStackHarvestHandler implements IHarvestHandler
{
    public Block harvestBlock;
    public int harvestMeta;
    public ItemStack harvestItem;
    public IPlantable harvestSeed;

    public GenericItemStackHarvestHandler(Block block, int meta, ItemStack seed)
    {
        harvestBlock = block;
        harvestMeta = meta;
        harvestItem = seed;
        if (seed.getItem() instanceof IPlantable) harvestSeed = (IPlantable) seed.getItem();
    }

    public boolean canHandleBlock(Block block)
    {
        return block == harvestBlock;
    }

    public int getHarvestMeta()
    {
        return harvestMeta;
    }

    @Override
    public boolean harvestAndPlant(World world, BlockPos pos, Block block, IBlockState state)
    {
        if (!this.canHandleBlock(block) || block.getMetaFromState(state) != this.getHarvestMeta())
        {
            return false;
        }

        IPlantable seed = this.getSeedItem(block);

        if (seed == null)
        {
            world.destroyBlock(pos, true);

            return true;
        } else
        {
            int fortune = 0;

            List<ItemStack> list = block.getDrops(world, pos, state, fortune);
            boolean foundAndRemovedSeed = false;

            for (ItemStack stack : list)
            {
                if (stack == null)
                {
                    continue;
                }
                if (harvestItem.isItemEqual(stack))
                {
                    int itemSize = stack.stackSize;
                    if (itemSize<1)
                    {
                        continue;
                    }
                    else if (itemSize==1)
                    {
                        list.remove(stack);
                    }
                    else
                    {
                        stack.stackSize--;
                    }
                    foundAndRemovedSeed = true;
                    break;
                }
            }

            if (foundAndRemovedSeed)
            {
                IBlockState plantState = seed.getPlant(world, pos);

                world.destroyBlock(pos, false);

                world.setBlockState(pos, plantState, 3);

                for (ItemStack stack : list)
                {
                    EntityItem itemEnt = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);

                    world.spawnEntityInWorld(itemEnt);
                }
            }

            return false;
        }
    }

    public IPlantable getSeedItem(Block block)
    {
        return harvestSeed;
    }
}
