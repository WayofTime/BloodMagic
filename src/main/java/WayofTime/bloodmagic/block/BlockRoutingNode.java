package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.tile.routing.TileMasterRoutingNode;
import WayofTime.bloodmagic.tile.routing.TileRoutingNode;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
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
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(UP, this.shouldConnect(state, worldIn, pos.up(), EnumFacing.DOWN)).withProperty(DOWN, this.shouldConnect(state, worldIn, pos.down(), EnumFacing.UP)).withProperty(NORTH, this.shouldConnect(state, worldIn, pos.north(), EnumFacing.SOUTH)).withProperty(EAST, this.shouldConnect(state, worldIn, pos.east(), EnumFacing.WEST)).withProperty(SOUTH, this.shouldConnect(state, worldIn, pos.south(), EnumFacing.NORTH)).withProperty(WEST, this.shouldConnect(state, worldIn, pos.west(), EnumFacing.EAST));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UP, DOWN, NORTH, EAST, WEST, SOUTH);
    }

    public boolean shouldConnect(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing attachedSide) {
        IBlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        return block.getMaterial(blockState).isOpaque() && blockState.isFullCube();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockState) {
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
    public ItemBlock getItem() {
        return new ItemBlock(this);
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        variants.put(0, "inventory");
    }
}
