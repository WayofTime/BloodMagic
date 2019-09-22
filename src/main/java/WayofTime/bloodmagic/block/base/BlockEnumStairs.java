package WayofTime.bloodmagic.block.base;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.StairsBlock.EnumHalf;
import net.minecraft.block.StairsBlock.EnumShape;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
    public static final PropertyDirection FACING = HorizontalBlock.FACING;

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
        return new BlockStateContainer.Builder(this).add(getProperty(), FACING, StairsBlock.HALF, StairsBlock.SHAPE).build();
    }

    @Override
    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean bool) {
        state = this.getActualState(state, worldIn, pos);

        for (AxisAlignedBB axisalignedbb : getCollisionBoxList(state)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, axisalignedbb);
        }
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand) {
        BlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
        state = state.withProperty(FACING, placer.getHorizontalFacing()).withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.STRAIGHT);
        return facing != Direction.DOWN && (facing == Direction.UP || (double) hitY <= 0.5D) ? state.withProperty(StairsBlock.HALF, StairsBlock.EnumHalf.BOTTOM) : state.withProperty(StairsBlock.HALF, StairsBlock.EnumHalf.TOP);
    }

    @Override
    public RayTraceResult collisionRayTrace(BlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
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
    public BlockState getStateFromMeta(int meta) {
        BlockState state = getBlockState().getBaseState().withProperty(StairsBlock.HALF, (meta & 8) > 0 ? StairsBlock.EnumHalf.TOP : StairsBlock.EnumHalf.BOTTOM);
        state = state.withProperty(FACING, Direction.byIndex(5 - (meta & 6) / 2)).withProperty(this.getProperty(), getTypes()[meta % 2]);
        return state;
    }

    // Meta looks like: {1|11|1} = {HALF|FACING|TYPE}
    @Override
    public int getMetaFromState(BlockState state) {
        int i = 0;

        if (state.getValue(StairsBlock.HALF) == StairsBlock.EnumHalf.TOP) {
            i |= 4;
        }

        i = i | 5 - state.getValue(FACING).getIndex();
        return i * 2 + ArrayUtils.indexOf(getTypes(), state.getValue(getProperty()));
    }

    @Override
    public BlockState getActualState(BlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(StairsBlock.SHAPE, getStairsShape(state, worldIn, pos));
    }

    @Override
    public BlockState withRotation(BlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public BlockState withMirror(BlockState state, Mirror mirrorIn) {
        Direction facing = state.getValue(FACING);
        StairsBlock.EnumShape stairShape = state.getValue(StairsBlock.SHAPE);

        switch (mirrorIn) {
            case LEFT_RIGHT:

                if (facing.getAxis() == Direction.Axis.Z) {
                    switch (stairShape) {
                        case OUTER_LEFT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.OUTER_LEFT);
                        case INNER_RIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.INNER_LEFT);
                        case INNER_LEFT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.INNER_RIGHT);
                        default:
                            return state.withRotation(Rotation.CLOCKWISE_180);
                    }
                }

                break;
            case FRONT_BACK:

                if (facing.getAxis() == Direction.Axis.X) {
                    switch (stairShape) {
                        case OUTER_LEFT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.OUTER_LEFT);
                        case INNER_RIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.INNER_RIGHT);
                        case INNER_LEFT:
                            return state.withRotation(Rotation.CLOCKWISE_180).withProperty(StairsBlock.SHAPE, StairsBlock.EnumShape.INNER_LEFT);
                        case STRAIGHT:
                            return state.withRotation(Rotation.CLOCKWISE_180);
                    }
                }
        }

        return super.withMirror(state, mirrorIn);
    }

    @Override
    protected ItemStack getSilkTouchDrop(BlockState state) {
        return new ItemStack(this, 1, damageDropped(state));
    }

    @Override
    public int damageDropped(BlockState state) {
        return super.getMetaFromState(state);
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, World world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(this, 1, damageDropped(state));
    }

    @Override
    public boolean doesSideBlockRendering(BlockState state, IBlockAccess world, BlockPos pos, Direction face) {
        if (ForgeModContainer.disableStairSlabCulling)
            return super.doesSideBlockRendering(state, world, pos, face);

        if (state.isOpaqueCube())
            return true;

        state = this.getActualState(state, world, pos);

        EnumHalf half = state.getValue(StairsBlock.HALF);
        Direction side = state.getValue(FACING);
        EnumShape shape = state.getValue(StairsBlock.SHAPE);
        if (face == Direction.UP)
            return half == EnumHalf.TOP;
        if (face == Direction.DOWN)
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

    private static List<AxisAlignedBB> getCollisionBoxList(BlockState state) {
        List<AxisAlignedBB> list = Lists.newArrayList();
        boolean flag = state.getValue(StairsBlock.HALF) == StairsBlock.EnumHalf.TOP;
        list.add(flag ? AABB_SLAB_TOP : AABB_SLAB_BOTTOM);
        StairsBlock.EnumShape stairShape = state.getValue(StairsBlock.SHAPE);

        if (stairShape == StairsBlock.EnumShape.STRAIGHT || stairShape == StairsBlock.EnumShape.INNER_LEFT || stairShape == StairsBlock.EnumShape.INNER_RIGHT) {
            list.add(getCollQuarterBlock(state));
        }

        if (stairShape != StairsBlock.EnumShape.STRAIGHT) {
            list.add(getCollEighthBlock(state));
        }

        return list;
    }

    private static AxisAlignedBB getCollQuarterBlock(BlockState state) {
        boolean flag = state.getValue(StairsBlock.HALF) == StairsBlock.EnumHalf.TOP;

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

    private static AxisAlignedBB getCollEighthBlock(BlockState state) {
        Direction facing = state.getValue(FACING);
        Direction newFacing;

        switch (state.getValue(StairsBlock.SHAPE)) {
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

        boolean isTop = state.getValue(StairsBlock.HALF) == StairsBlock.EnumHalf.TOP;

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

    private static StairsBlock.EnumShape getStairsShape(BlockState state, IBlockAccess world, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockState offsetState = world.getBlockState(pos.offset(facing));

        if (isBlockStairs(offsetState) && state.getValue(StairsBlock.HALF) == offsetState.getValue(StairsBlock.HALF)) {
            Direction offsetFacing = offsetState.getValue(FACING);

            if (offsetFacing.getAxis() != state.getValue(FACING).getAxis() && isDifferentStairs(state, world, pos, offsetFacing.getOpposite())) {
                if (offsetFacing == facing.rotateYCCW()) {
                    return StairsBlock.EnumShape.OUTER_LEFT;
                }

                return StairsBlock.EnumShape.OUTER_RIGHT;
            }
        }

        BlockState oppositeOffsetState = world.getBlockState(pos.offset(facing.getOpposite()));

        if (isBlockStairs(oppositeOffsetState) && state.getValue(StairsBlock.HALF) == oppositeOffsetState.getValue(StairsBlock.HALF)) {
            Direction oppositeOffsetFacing = oppositeOffsetState.getValue(FACING);

            if (oppositeOffsetFacing.getAxis() != (state.getValue(FACING)).getAxis() && isDifferentStairs(state, world, pos, oppositeOffsetFacing)) {
                if (oppositeOffsetFacing == facing.rotateYCCW()) {
                    return StairsBlock.EnumShape.INNER_LEFT;
                }

                return StairsBlock.EnumShape.INNER_RIGHT;
            }
        }

        return StairsBlock.EnumShape.STRAIGHT;
    }

    private static boolean isDifferentStairs(BlockState state, IBlockAccess world, BlockPos pos, Direction facing) {
        BlockState offsetState = world.getBlockState(pos.offset(facing));
        return !isBlockStairs(offsetState) || offsetState.getValue(FACING) != state.getValue(FACING) || offsetState.getValue(StairsBlock.HALF) != state.getValue(StairsBlock.HALF);
    }

    public static boolean isBlockStairs(BlockState state) {
        return state.getBlock() instanceof StairsBlock || state.getBlock() instanceof BlockEnumStairs;
    }
}