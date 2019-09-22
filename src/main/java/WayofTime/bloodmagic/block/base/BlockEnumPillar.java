package WayofTime.bloodmagic.block.base;

import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

public class BlockEnumPillar<E extends Enum<E> & IStringSerializable> extends BlockEnum<E> {
    public BlockEnumPillar(Material material, Class<E> enumClass, String propName) {
        super(material, enumClass, propName);
    }

    public BlockEnumPillar(Material material, Class<E> enumClass) {
        this(material, enumClass, "type");
    }

    @Override
    protected BlockStateContainer createStateContainer() {
        return new BlockStateContainer.Builder(this).add(getProperty(), RotatedPillarBlock.AXIS).build();
    }

    @Override
    public BlockState getStateFromMeta(int meta) {
        BlockState state = getBlockState().getBaseState().withProperty(this.getProperty(), getTypes()[meta % 5]);

        switch (meta / 5) {
            case 0:
                state = state.withProperty(RotatedPillarBlock.AXIS, Direction.Axis.Y);
                break;
            case 1:
                state = state.withProperty(RotatedPillarBlock.AXIS, Direction.Axis.X);
                break;
            case 2:
                state = state.withProperty(RotatedPillarBlock.AXIS, Direction.Axis.Z);
                break;
            default:
                state.withProperty(RotatedPillarBlock.AXIS, Direction.Axis.Y);
                break;
        }

        return state;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, World world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(this, 1, damageDropped(state));
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public int getMetaFromState(BlockState state) {
        int i = ArrayUtils.indexOf(getTypes(), state.getValue(getProperty()));

        switch (state.getValue(RotatedPillarBlock.AXIS)) {
            case X:
                i = i + 5;
                break;
            case Z:
                i = i + 10;
                break;
        }

        return i;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, Direction axis) {
        BlockState state = world.getBlockState(pos);
        for (IProperty<?> prop : state.getProperties().keySet()) {
            if (prop == RotatedPillarBlock.AXIS) {
                world.setBlockState(pos, state.cycleProperty(prop));
                return true;
            }
        }
        return false;
    }

    @Override
    public BlockState withRotation(BlockState state, Rotation rot) {
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch (state.getValue(RotatedPillarBlock.AXIS)) {
                    case X:
                        return state.withProperty(RotatedPillarBlock.AXIS, Direction.Axis.Z);
                    case Z:
                        return state.withProperty(RotatedPillarBlock.AXIS, Direction.Axis.X);
                    default:
                        return state;
                }

            default:
                return state;
        }
    }

    @Override
    protected ItemStack getSilkTouchDrop(BlockState state) {
        return new ItemStack(this, 1, damageDropped(state));
    }


    @Override
    public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(RotatedPillarBlock.AXIS, facing.getAxis());
    }

    @Override
    public int damageDropped(BlockState state) {
        return super.getMetaFromState(state);
    }
}
