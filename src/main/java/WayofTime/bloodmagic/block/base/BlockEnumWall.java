package WayofTime.bloodmagic.block.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEnumWall<E extends Enum<E> & IStringSerializable> extends BlockEnum<E> {
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    protected static final AxisAlignedBB[] AABB_BY_INDEX = new AxisAlignedBB[]{new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 0.875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D),
            new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 1.0D, 0.875D, 0.6875D), new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    protected static final AxisAlignedBB[] CLIP_AABB_BY_INDEX = new AxisAlignedBB[]{AABB_BY_INDEX[0].setMaxY(1.5D), AABB_BY_INDEX[1].setMaxY(1.5D), AABB_BY_INDEX[2].setMaxY(1.5D), AABB_BY_INDEX[3].setMaxY(1.5D), AABB_BY_INDEX[4].setMaxY(1.5D), AABB_BY_INDEX[5].setMaxY(1.5D), AABB_BY_INDEX[6].setMaxY(1.5D), AABB_BY_INDEX[7].setMaxY(1.5D), AABB_BY_INDEX[8].setMaxY(1.5D), AABB_BY_INDEX[9].setMaxY(1.5D), AABB_BY_INDEX[10].setMaxY(1.5D), AABB_BY_INDEX[11].setMaxY(1.5D), AABB_BY_INDEX[12].setMaxY(1.5D), AABB_BY_INDEX[13].setMaxY(1.5D), AABB_BY_INDEX[14].setMaxY(1.5D),
            AABB_BY_INDEX[15].setMaxY(1.5D)};

    // Most of this is copied from BlockWall - if there is an issue when porting, look there first.
    public BlockEnumWall(Material material, Class<E> enumClass, String propName) {
        super(material, enumClass, propName);
    }

    public BlockEnumWall(Material material, Class<E> enumClass) {
        this(material, enumClass, "type");
    }

    @Override
    protected BlockStateContainer createStateContainer() {
        return new BlockStateContainer.Builder(this).add(getProperty(), UP, NORTH, EAST, SOUTH, WEST).build();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        state = state.getActualState(source, pos);
        return AABB_BY_INDEX[getAABBIndex(state)];
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        blockState = blockState.getActualState(worldIn, pos);
        return CLIP_AABB_BY_INDEX[getAABBIndex(blockState)];
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    private boolean canConnectTo(IBlockAccess worldIn, BlockPos pos) {
        IBlockState worldState = worldIn.getBlockState(pos);
        Block block = worldState.getBlock();
        return block != Blocks.BARRIER && (!(block != this && !(block instanceof BlockFenceGate)) || ((worldState.getMaterial().isOpaque() && worldState.isFullCube()) && worldState.getMaterial() != Material.GOURD));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return side != EnumFacing.DOWN || super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        boolean canNorth = this.canConnectTo(worldIn, pos.north());
        boolean canEast = this.canConnectTo(worldIn, pos.east());
        boolean canSouth = this.canConnectTo(worldIn, pos.south());
        boolean canWest = this.canConnectTo(worldIn, pos.west());
        boolean flag4 = canNorth && !canEast && canSouth && !canWest || !canNorth && canEast && !canSouth && canWest;
        return state.withProperty(UP, !flag4 || !worldIn.isAirBlock(pos.up())).withProperty(NORTH, canNorth).withProperty(EAST, canEast).withProperty(SOUTH, canSouth).withProperty(WEST, canWest);
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(this, 1, damageDropped(state));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return super.getMetaFromState(state);
    }

    private static int getAABBIndex(IBlockState state) {
        int i = 0;

        if (state.getValue(NORTH)) {
            i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }

        if (state.getValue(EAST)) {
            i |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }

        if (state.getValue(SOUTH)) {
            i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }

        if (state.getValue(WEST)) {
            i |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }

        return i;
    }
}
