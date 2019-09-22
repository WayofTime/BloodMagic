package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.tile.routing.TileMasterRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileRoutingNode;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockRoutingNode extends Block implements IBMBlock, IVariantProvider {
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.378F, 0.378F, 0.378F, 0.625F, 0.625F, 0.625F);

    public BlockRoutingNode() {
        super(Material.ROCK);

        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 2);

        this.setDefaultState(this.blockState.getBaseState().withProperty(DOWN, false).withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false));
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockAccess world, BlockPos pos, Direction side) {
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    public boolean causesSuffocation(BlockState state) {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(BlockState state) {
        return 0;
    }

    @Override
    public BlockState getActualState(BlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(UP, this.shouldConnect(state, worldIn, pos.up(), Direction.DOWN)).withProperty(DOWN, this.shouldConnect(state, worldIn, pos.down(), Direction.UP)).withProperty(NORTH, this.shouldConnect(state, worldIn, pos.north(), Direction.SOUTH)).withProperty(EAST, this.shouldConnect(state, worldIn, pos.east(), Direction.WEST)).withProperty(SOUTH, this.shouldConnect(state, worldIn, pos.south(), Direction.NORTH)).withProperty(WEST, this.shouldConnect(state, worldIn, pos.west(), Direction.EAST));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UP, DOWN, NORTH, EAST, WEST, SOUTH);
    }

    public boolean shouldConnect(BlockState state, IBlockAccess world, BlockPos pos, Direction attachedSide) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        return block.getMaterial(blockState).isOpaque() && blockState.isFullCube();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, BlockState blockState) {
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileRoutingNode) {
                ((TileRoutingNode) tile).removeAllConnections();
            } else if (tile instanceof TileMasterRoutingNode) {
                ((TileMasterRoutingNode) tile).removeAllConnections();
            }
        }

        super.breakBlock(world, pos, blockState);
    }

    @Override
    public BlockItem getItem() {
        return new BlockItem(this);
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        variants.put(0, "inventory");
    }
}
