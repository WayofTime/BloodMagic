package WayofTime.bloodmagic.block.base;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.BlockStairs.EnumShape;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.List;

public class BlockEnumStairs<E extends Enum<E> & IStringSerializable> extends BlockEnum<E> {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    protected static final AxisAlignedBB AABB_SLAB_TOP = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_TOP_WEST = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_TOP_EAST = new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_TOP_NORTH = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_QTR_TOP_SOUTH = new AxisAlignedBB(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_OCT_TOP_NW = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_OCT_TOP_NE = new AxisAlignedBB(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_OCT_TOP_SW = new AxisAlignedBB(0.0D, 0.5D, 0.5D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_OCT_TOP_SE = new AxisAlignedBB(0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_SLAB_BOTTOM = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_BOT_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_BOT_EAST = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_QTR_BOT_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
    protected static final AxisAlignedBB AABB_QTR_BOT_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_OCT_BOT_NW = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
    protected static final AxisAlignedBB AABB_OCT_BOT_NE = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
    protected static final AxisAlignedBB AABB_OCT_BOT_SW = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_OCT_BOT_SE = new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);

    public BlockEnumStairs(Material material, Class<E> enumClass, String propName) {
        super(material, enumClass, propName);
    }

    public BlockEnumStairs(Material material, Class<E> enumClass) {
        this(material, enumClass, "type");
    }

    @Override
    protected BlockStateContainer createStateContainer() {
        return new BlockStateContainer.Builder(this).add(getProperty(), FACING, BlockStairs.HALF, BlockStairs.SHAPE).build();
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean bool) {
        state = this.getActualState(state, worldIn, pos);

        for (AxisAlignedBB axisalignedbb : getCollisionBoxList(state)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, axisalignedbb);
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
        state = state.withProperty(FACING, placer.getHorizontalFacing()).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT);
        return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double) hitY <= 0.5D) ? state.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.BOTTOM) : state.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP);
    }

    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        List<RayTraceResult> list = Lists.newArrayList();

        for (AxisAlignedBB axisalignedbb : getCollisionBoxList(this.getActualState(blockState, worldIn, pos))) {
            list.add(this.rayTrace(pos, start, end, axisalignedbb));
        }

        RayTraceResult rayTrace = null;
        double d1 = 0.0D;

        for (RayTraceResult raytraceresult : list) {
            if (raytraceresult != null) {
                double d0 = raytraceresult.hitVec.squareDistanceTo(end);

                if (d0 > d1) {
                    rayTrace = raytraceresult;
                    d1 = d0;
                }
            }
        }

        return rayTrace;
    }

    // Meta looks like: {1|11|1} = {HALF|FACING|TYPE}
    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = getBlockState().getBaseState().withProperty(BlockStairs.HALF, (meta & 8) > 0 ? BlockStairs.EnumHalf.TOP : BlockStairs.EnumHalf.BOTTOM);
        state = state.withProperty(FACING, EnumFacing.byIndex(5 - (meta & 6) / 2)).withProperty(this.getProperty(), getTypes()[meta % 2]);
        return state;
    }

    // Meta looks like: {1|11|1} = {HALF|FACING|TYPE}
    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;

        if (state.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP) {
            i |= 4;
        }

        i = i | 5 - state.getValue(FACING).getIndex();
        return i * 2 + ArrayUtils.indexOf(getTypes(), state.getValue(getProperty()));
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(BlockStairs.SHAPE, getStairsShape(state, worldIn, pos));
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        EnumFacing facing = state.getValue(FACING);
        BlockStairs.EnumShape stairShape = state.getValue(BlockStairs.SHAPE);

        switch (mirrorIn) {
            case LEFT_RIGHT:

                if (facing.getAxis() == EnumFacing.Axis.Z) {
                    switch (stairShape) {
                        case OUTER_LEFT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.OUTER_LEFT);
                        case INNER_RIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);
                        case INNER_LEFT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
                        default:
                            return state.withRotation(Rotation.CLOCKWISE_180);
                    }
                }

                break;
            case FRONT_BACK:

                if (facing.getAxis() == EnumFacing.Axis.X) {
                    switch (stairShape) {
                        case OUTER_LEFT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.OUTER_LEFT);
                        case INNER_RIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT);
                        case INNER_LEFT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT);
                        case STRAIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180);
                    }
                }
        }

        return super.withMirror(state, mirrorIn);
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(this, 1, damageDropped(state));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return super.getMetaFromState(state);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, damageDropped(state));
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        if (ForgeModContainer.disableStairSlabCulling)
            return super.doesSideBlockRendering(state, world, pos, face);

        if (state.isOpaqueCube())
            return true;

        state = this.getActualState(state, world, pos);

        EnumHalf half = state.getValue(BlockStairs.HALF);
        EnumFacing side = state.getValue(FACING);
        EnumShape shape = state.getValue(BlockStairs.SHAPE);
        if (face == EnumFacing.UP)
            return half == EnumHalf.TOP;
        if (face == EnumFacing.DOWN)
            return half == EnumHalf.BOTTOM;
        if (shape == EnumShape.OUTER_LEFT || shape == EnumShape.OUTER_RIGHT)
            return false;
        if (face == side)
            return true;
        if (shape == EnumShape.INNER_LEFT && face.rotateY() == side)
            return true;
        if (shape == EnumShape.INNER_RIGHT && face.rotateYCCW() == side)
            return true;
        return false;
    }

    private static List<AxisAlignedBB> getCollisionBoxList(IBlockState state) {
        List<AxisAlignedBB> list = Lists.newArrayList();
        boolean flag = state.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP;
        list.add(flag ? AABB_SLAB_TOP : AABB_SLAB_BOTTOM);
        BlockStairs.EnumShape stairShape = state.getValue(BlockStairs.SHAPE);

        if (stairShape == BlockStairs.EnumShape.STRAIGHT || stairShape == BlockStairs.EnumShape.INNER_LEFT || stairShape == BlockStairs.EnumShape.INNER_RIGHT) {
            list.add(getCollQuarterBlock(state));
        }

        if (stairShape != BlockStairs.EnumShape.STRAIGHT) {
            list.add(getCollEighthBlock(state));
        }

        return list;
    }

    private static AxisAlignedBB getCollQuarterBlock(IBlockState state) {
        boolean flag = state.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP;

        switch (state.getValue(FACING)) {
            case NORTH:
            default:
                return flag ? AABB_QTR_BOT_NORTH : AABB_QTR_TOP_NORTH;
            case SOUTH:
                return flag ? AABB_QTR_BOT_SOUTH : AABB_QTR_TOP_SOUTH;
            case WEST:
                return flag ? AABB_QTR_BOT_WEST : AABB_QTR_TOP_WEST;
            case EAST:
                return flag ? AABB_QTR_BOT_EAST : AABB_QTR_TOP_EAST;
        }
    }

    private static AxisAlignedBB getCollEighthBlock(IBlockState state) {
        EnumFacing facing = state.getValue(FACING);
        EnumFacing newFacing;

        switch (state.getValue(BlockStairs.SHAPE)) {
            case OUTER_LEFT:
            default:
                newFacing = facing;
                break;
            case OUTER_RIGHT:
                newFacing = facing.rotateY();
                break;
            case INNER_RIGHT:
                newFacing = facing.getOpposite();
                break;
            case INNER_LEFT:
                newFacing = facing.rotateYCCW();
        }

        boolean isTop = state.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP;

        switch (newFacing) {
            case NORTH:
            default:
                return isTop ? AABB_OCT_BOT_NW : AABB_OCT_TOP_NW;
            case SOUTH:
                return isTop ? AABB_OCT_BOT_SE : AABB_OCT_TOP_SE;
            case WEST:
                return isTop ? AABB_OCT_BOT_SW : AABB_OCT_TOP_SW;
            case EAST:
                return isTop ? AABB_OCT_BOT_NE : AABB_OCT_TOP_NE;
        }
    }

    private static BlockStairs.EnumShape getStairsShape(IBlockState state, IBlockAccess world, BlockPos pos) {
        EnumFacing facing = state.getValue(FACING);
        IBlockState offsetState = world.getBlockState(pos.offset(facing));

        if (isBlockStairs(offsetState) && state.getValue(BlockStairs.HALF) == offsetState.getValue(BlockStairs.HALF)) {
            EnumFacing offsetFacing = offsetState.getValue(FACING);

            if (offsetFacing.getAxis() != state.getValue(FACING).getAxis() && isDifferentStairs(state, world, pos, offsetFacing.getOpposite())) {
                if (offsetFacing == facing.rotateYCCW()) {
                    return BlockStairs.EnumShape.OUTER_LEFT;
                }

                return BlockStairs.EnumShape.OUTER_RIGHT;
            }
        }

        IBlockState oppositeOffsetState = world.getBlockState(pos.offset(facing.getOpposite()));

        if (isBlockStairs(oppositeOffsetState) && state.getValue(BlockStairs.HALF) == oppositeOffsetState.getValue(BlockStairs.HALF)) {
            EnumFacing oppositeOffsetFacing = oppositeOffsetState.getValue(FACING);

            if (oppositeOffsetFacing.getAxis() != (state.getValue(FACING)).getAxis() && isDifferentStairs(state, world, pos, oppositeOffsetFacing)) {
                if (oppositeOffsetFacing == facing.rotateYCCW()) {
                    return BlockStairs.EnumShape.INNER_LEFT;
                }

                return BlockStairs.EnumShape.INNER_RIGHT;
            }
        }

        return BlockStairs.EnumShape.STRAIGHT;
    }

    private static boolean isDifferentStairs(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing) {
        IBlockState offsetState = world.getBlockState(pos.offset(facing));
        return !isBlockStairs(offsetState) || offsetState.getValue(FACING) != state.getValue(FACING) || offsetState.getValue(BlockStairs.HALF) != state.getValue(BlockStairs.HALF);
    }

    public static boolean isBlockStairs(IBlockState state) {
        return state.getBlock() instanceof BlockStairs || state.getBlock() instanceof BlockEnumStairs;
    }
}