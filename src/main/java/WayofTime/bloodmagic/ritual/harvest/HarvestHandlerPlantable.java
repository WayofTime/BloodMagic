package WayofTime.bloodmagic.ritual.harvest;

import WayofTime.bloodmagic.api.BlockStack;
import WayofTime.bloodmagic.api.iface.IHarvestHandler;
import WayofTime.bloodmagic.api.registry.HarvestRegistry;
import net.minecraft.block.BlockCrops;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.List;

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
        if (!HarvestRegistry.getValidBlocks().containsKey(blockStack.getBlock()))
            return false;

        int matureMeta = HarvestRegistry.getValidBlocks().get(blockStack.getBlock());

        if(blockStack.getMeta() < matureMeta)
            return false;

        List<ItemStack> drops = blockStack.getBlock().getDrops(world, pos, blockStack.getState(), 0);
        boolean foundSeed = false;

        for (ItemStack stack : drops) {
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
            world.setBlockState(pos, blockStack.getState().withProperty(BlockCrops.AGE, 0));
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
