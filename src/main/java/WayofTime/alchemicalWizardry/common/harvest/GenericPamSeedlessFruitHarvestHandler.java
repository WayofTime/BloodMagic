//package WayofTime.alchemicalWizardry.common.harvest;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.item.Item;
//import net.minecraft.util.BlockPos;
//import net.minecraft.world.World;
//import WayofTime.alchemicalWizardry.api.harvest.IHarvestHandler;
//import cpw.mods.fml.common.registry.GameRegistry;
//
//public class GenericPamSeedlessFruitHarvestHandler implements IHarvestHandler
//{
//    public Block harvestBlock;
//    public int harvestMeta;
//    public int resetMeta;
//
//    public GenericPamSeedlessFruitHarvestHandler(String block, int harvestMeta, int resetMeta)
//    {
//        this.harvestBlock = getBlockForString(block);
//        this.harvestMeta = harvestMeta;
//        this.resetMeta = resetMeta;
//    }
//
//    public boolean isHarvesterValid()
//    {
//        return harvestBlock != null;
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
//        world.destroyBlock(pos, true);
//
//        world.setBlockState(pos, harvestBlock.getStateFromMeta(resetMeta), 3);
//
//        return true;
//    }
//}
