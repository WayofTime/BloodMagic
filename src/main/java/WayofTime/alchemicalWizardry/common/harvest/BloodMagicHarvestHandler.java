package WayofTime.alchemicalWizardry.common.harvest;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import WayofTime.alchemicalWizardry.api.harvest.IHarvestHandler;

public class BloodMagicHarvestHandler implements IHarvestHandler
{
    public boolean canHandleBlock(Block block)
    {
        return block == Blocks.wheat || block == Blocks.carrots || block == Blocks.potatoes || block == Blocks.nether_wart;
    }

    public int getHarvestMeta(Block block)
    {
        if (block == Blocks.wheat)
        {
            return 7;
        }
        if (block == Blocks.carrots)
        {
            return 7;
        }
        if (block == Blocks.potatoes)
        {
            return 7;
        }
        if (block == Blocks.nether_wart)
        {
            return 3;
        }
        return 7;
    }

    @Override
    public boolean harvestAndPlant(World world, BlockPos pos, Block block, IBlockState state)
    {
        if (!this.canHandleBlock(block) || block.getMetaFromState(state) != this.getHarvestMeta(block))
        {
            return false;
        }

        IPlantable seed = this.getSeedItem(block);

        if (seed == null)
        {
            return false;
        }

        int fortune = 0;

        List<ItemStack> list = block.getDrops(world, pos, state, fortune);
        boolean foundAndRemovedSeed = false;

        for (ItemStack stack : list)
        {
            if (stack == null)
            {
                continue;
            }

            Item item = stack.getItem();
            if (item == seed)
            {
                int itemSize = stack.stackSize;
                if (itemSize > 1)
                {
                    stack.stackSize--;
                    foundAndRemovedSeed = true;
                    break;
                } else if (itemSize == 1)
                {
                    list.remove(stack);
                    foundAndRemovedSeed = true;
                    break;
                }
            }
        }

        if (foundAndRemovedSeed)
        {
            IBlockState plantState = seed.getPlant(world, pos);

            world.destroyBlock(pos, false);

            world.setBlockState(pos, plantState, 3);

            for (ItemStack stack : list)
            {
                EntityItem itemEnt = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);

                world.spawnEntityInWorld(itemEnt);
            }
        }

        return false;
    }

    public IPlantable getSeedItem(Block block)
    {
        if (block == Blocks.wheat)
        {
            return (IPlantable) Items.wheat_seeds;
        }
        if (block == Blocks.carrots)
        {
            return (IPlantable) Items.carrot;
        }
        if (block == Blocks.potatoes)
        {
            return (IPlantable) Items.potato;
        }
        if (block == Blocks.nether_wart)
        {
            return (IPlantable) Items.nether_wart;
        }

        return null;
    }
}