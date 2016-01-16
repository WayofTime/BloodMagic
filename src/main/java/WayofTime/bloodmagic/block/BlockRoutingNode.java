package WayofTime.bloodmagic.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import WayofTime.bloodmagic.BloodMagic;

public abstract class BlockRoutingNode extends BlockContainer
{
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");

    public BlockRoutingNode()
    {
        super(Material.rock);

        setCreativeTab(BloodMagic.tabBloodMagic);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);

        this.setDefaultState(this.blockState.getBaseState().withProperty(DOWN, Boolean.valueOf(false)).withProperty(UP, Boolean.valueOf(false)).withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)));
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public boolean isVisuallyOpaque()
    {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(UP, Boolean.valueOf(this.shouldConnect(worldIn, pos.up()))).withProperty(DOWN, Boolean.valueOf(this.shouldConnect(worldIn, pos.down()))).withProperty(NORTH, Boolean.valueOf(this.shouldConnect(worldIn, pos.north()))).withProperty(EAST, Boolean.valueOf(this.shouldConnect(worldIn, pos.east()))).withProperty(SOUTH, Boolean.valueOf(this.shouldConnect(worldIn, pos.south()))).withProperty(WEST, Boolean.valueOf(this.shouldConnect(worldIn, pos.west())));
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] { UP, DOWN, NORTH, EAST, WEST, SOUTH });
    }

    public boolean shouldConnect(IBlockAccess world, BlockPos pos)
    {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof BlockRoutingNode)
        {
            return false;
        }
        return block == Blocks.barrier ? false : (block != this && !(block instanceof BlockFenceGate) ? (block.getMaterial().isOpaque() && block.isFullCube() ? block.getMaterial() != Material.gourd : false) : true);
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }
}
