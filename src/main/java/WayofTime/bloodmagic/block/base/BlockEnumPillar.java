package WayofTime.bloodmagic.block.base;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
        return new BlockStateContainer.Builder(this).add(getProperty(), BlockRotatedPillar.AXIS).build();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = getBlockState().getBaseState().withProperty(this.getProperty(), getTypes()[meta % 5]);

        switch (meta / 5) {
            case 0:
                state = state.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y);
                break;
            case 1:
                state = state.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X);
                break;
            case 2:
                state = state.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Z);
                break;
            default:
                state.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y);
                break;
        }

        return state;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, damageDropped(state));
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public int getMetaFromState(IBlockState state) {
        int i = ArrayUtils.indexOf(getTypes(), state.getValue(getProperty()));

        switch (state.getValue(BlockRotatedPillar.AXIS)) {
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
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        IBlockState state = world.getBlockState(pos);
        for (IProperty<?> prop : state.getProperties().keySet()) {
            if (prop == BlockRotatedPillar.AXIS) {
                world.setBlockState(pos, state.cycleProperty(prop));
                return true;
            }
        }
        return false;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch (state.getValue(BlockRotatedPillar.AXIS)) {
                    case X:
                        return state.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Z);
                    case Z:
                        return state.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X);
                    default:
                        return state;
                }

            default:
                return state;
        }
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(this, 1, damageDropped(state));
    }


    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(BlockRotatedPillar.AXIS, facing.getAxis());
    }

    @Override
    public int damageDropped(IBlockState state) {
        return super.getMetaFromState(state);
    }
}
