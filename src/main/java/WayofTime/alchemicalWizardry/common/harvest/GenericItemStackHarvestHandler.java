package WayofTime.alchemicalWizardry.common.harvest;

import WayofTime.alchemicalWizardry.api.harvest.IHarvestHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.List;

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

    public int getHarvestMeta(Block block)
    {
        return harvestMeta;
    }

    @Override
    public boolean harvestAndPlant(World world, int xCoord, int yCoord, int zCoord, Block block, int meta)
    {
        if (!this.canHandleBlock(block) || meta != this.getHarvestMeta(block))
        {
            return false;
        }

        IPlantable seed = this.getSeedItem(block);

        if (seed == null)
        {
            world.func_147480_a(xCoord, yCoord, zCoord, true);

            return true;
        } else
        {
            int fortune = 0;

            List<ItemStack> list = block.getDrops(world, xCoord, yCoord, zCoord, meta, fortune);
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
                int plantMeta = seed.getPlantMetadata(world, xCoord, yCoord, zCoord);
                Block plantBlock = seed.getPlant(world, xCoord, yCoord, zCoord);

                world.func_147480_a(xCoord, yCoord, zCoord, false);

                world.setBlock(xCoord, yCoord, zCoord, plantBlock, plantMeta, 3);

                for (ItemStack stack : list)
                {
                    EntityItem itemEnt = new EntityItem(world, xCoord, yCoord, zCoord, stack);

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
