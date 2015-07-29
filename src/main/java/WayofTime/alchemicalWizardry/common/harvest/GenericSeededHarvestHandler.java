//package WayofTime.alchemicalWizardry.common.harvest;
//
//import java.util.List;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.entity.item.EntityItem;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.BlockPos;
//import net.minecraft.world.World;
//import net.minecraftforge.common.IPlantable;
//import WayofTime.alchemicalWizardry.api.harvest.IHarvestHandler;
//import cpw.mods.fml.common.registry.GameRegistry;
//
//public class GenericSeededHarvestHandler implements IHarvestHandler
//{
//    public Block harvestBlock;
//    public int harvestMeta;
//    public IPlantable harvestSeed;
//
//    public GenericSeededHarvestHandler(String block, int meta, String seed)
//    {
//        harvestBlock = getBlockForString(block);
//        harvestMeta = meta;
//        Item testSeed = getItemForString(seed);
//        if (testSeed instanceof IPlantable)
//        {
//            harvestSeed = (IPlantable) testSeed;
//        } else
//        {
//            harvestSeed = null;
//        }
//    }
//
//    public boolean isHarvesterValid()
//    {
//        return harvestBlock != null && harvestSeed != null;
//    }
//
//    public static Block getBlockForString(String str)
//    {
//        String[] parts = str.split(":");
//        String modId = parts[0];
//        String name = parts[1];
//        return GameRegistry.findBlock(modId, name);
//    }
//
//    public static Item getItemForString(String str)
//    {
//        String[] parts = str.split(":");
//        String modId = parts[0];
//        String name = parts[1];
//        return GameRegistry.findItem(modId, name);
//    }
//
//    public boolean canHandleBlock(Block block)
//    {
//        return block == harvestBlock;
//    }
//
//    public int getHarvestMeta()
//    {
//        return harvestMeta;
//    }
//
//    @Override
//    public boolean harvestAndPlant(World world, BlockPos pos, Block block, IBlockState state)
//    {
//        if (!this.canHandleBlock(block) || block.getMetaFromState(state) != this.getHarvestMeta())
//        {
//            return false;
//        }
//
//        IPlantable seed = this.getSeedItem(block);
//
//        if (seed == null)
//        {
//            world.destroyBlock(pos, true);
//
//            return true;
//        } else
//        {
//            int fortune = 0;
//
//            List<ItemStack> list = block.getDrops(world, pos, state, fortune);
//            boolean foundAndRemovedSeed = false;
//
//            for (ItemStack stack : list)
//            {
//                if (stack == null)
//                {
//                    continue;
//                }
//
//                Item item = stack.getItem();
//                if (item == seed)
//                {
//                    int itemSize = stack.stackSize;
//                    if (itemSize > 1)
//                    {
//                        stack.stackSize--;
//                        foundAndRemovedSeed = true;
//                        break;
//                    } else if (itemSize == 1)
//                    {
//                        list.remove(stack);
//                        foundAndRemovedSeed = true;
//                        break;
//                    }
//                }
//            }
//
//            if (foundAndRemovedSeed)
//            {
//            	IBlockState plantState = seed.getPlant(world, pos);
//
//                world.destroyBlock(pos, false);
//
//                world.setBlockState(pos, plantState, 3);
//
//                for (ItemStack stack : list)
//                {
//                    EntityItem itemEnt = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
//
//                    world.spawnEntityInWorld(itemEnt);
//                }
//            }
//
//            return false;
//        }
//    }
//
//    public IPlantable getSeedItem(Block block)
//    {
//        return harvestSeed;
//    }
//}
