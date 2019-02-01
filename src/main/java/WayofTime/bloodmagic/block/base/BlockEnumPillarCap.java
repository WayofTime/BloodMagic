package WayofTime.bloodmagic.block.base;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

public class BlockEnumPillarCap<E extends Enum<E> & IStringSerializable> extends BlockEnum<E> {
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public BlockEnumPillarCap(Material material, Class<E> enumClass, String propName) {
        super(material, enumClass, propName);
    }

    public BlockEnumPillarCap(Material material, Class<E> enumClass) {
        this(material, enumClass, "type");
    }

    @Override
    protected BlockStateContainer createStateContainer() {
        return new BlockStateContainer.Builder(this).add(getProperty(), FACING).build();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = getBlockState().getBaseState().withProperty(this.getProperty(), getTypes()[meta % 2]);
        return state.withProperty(FACING, EnumFacing.byIndex(meta / 2));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = ArrayUtils.indexOf(getTypes(), state.getValue(getProperty()));
        return i + 2 * state.getValue(FACING).getIndex();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, damageDropped(state));
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(this, 1, damageDropped(state));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, facing);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return super.getMetaFromState(state);
    }
}
