package WayofTime.bloodmagic.incense;

import WayofTime.bloodmagic.api.incense.EnumTranquilityType;
import WayofTime.bloodmagic.api.incense.ITranquilityHandler;
import WayofTime.bloodmagic.api.incense.TranquilityStack;
import WayofTime.bloodmagic.registry.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TranquilityHandlers
{

    public static class Plant implements ITranquilityHandler
    {
        @Override
        public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state)
        {
            if (block instanceof BlockLeaves)
            {
                return new TranquilityStack(EnumTranquilityType.PLANT, 1);
            }

            return null;
        }
    }

    public static class Lava implements ITranquilityHandler
    {
        @Override
        public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state)
        {
            if (block == Blocks.lava || block == Blocks.flowing_lava)
            {
                return new TranquilityStack(EnumTranquilityType.LAVA, 1.2);
            }

            return null;
        }
    }

    public static class Fire implements ITranquilityHandler
    {
        @Override
        public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state)
        {
            if (block instanceof BlockFire)
            {
                return new TranquilityStack(EnumTranquilityType.FIRE, 1);
            }

            if (block == Blocks.netherrack)
            {
                return new TranquilityStack(EnumTranquilityType.FIRE, 0.5);
            }

            return null;
        }
    }

    public static class Earth implements ITranquilityHandler
    {
        @Override
        public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state)
        {
            if (block == Blocks.dirt)
            {
                return new TranquilityStack(EnumTranquilityType.EARTHEN, 0.25);
            }

            if (block instanceof BlockGrass)
            {
                return new TranquilityStack(EnumTranquilityType.EARTHEN, 0.5);
            }

            if (block == Blocks.farmland)
            {
                return new TranquilityStack(EnumTranquilityType.EARTHEN, 1);
            }

            return null;
        }
    }

    public static class Crop implements ITranquilityHandler
    {
        @Override
        public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state)
        {
            if (block == Blocks.potatoes || block == Blocks.carrots || block == Blocks.wheat || block == Blocks.nether_wart)
            {
                return new TranquilityStack(EnumTranquilityType.CROP, 1);
            }

            return null;
        }
    }

    public static class Tree implements ITranquilityHandler
    {
        @Override
        public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state)
        {
            if (block instanceof BlockLog)
            {
                return new TranquilityStack(EnumTranquilityType.TREE, 1);
            }

            return null;
        }
    }

    public static class Water implements ITranquilityHandler
    {
        @Override
        public TranquilityStack getTranquilityOfBlock(World world, BlockPos pos, Block block, IBlockState state)
        {
            if (block == Blocks.water || block == Blocks.flowing_water)
            {
                return new TranquilityStack(EnumTranquilityType.WATER, 1);
            }

            if (block == ModBlocks.lifeEssence)
            {
                return new TranquilityStack(EnumTranquilityType.WATER, 1.5);
            }

            return null;
        }
    }
}
