package WayofTime.bloodmagic.block.base;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockStringPillar extends BlockString
{
    public BlockStringPillar(Material material, String[] values, String propName)
    {
        super(material, values, propName);
    }

    public BlockStringPillar(Material material, String[] values)
    {
        this(material, values, "type");
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = getBlockState().getBaseState().withProperty(this.getStringProp(), this.getValues().get(meta % 5));

        switch (meta / 5)
        {
        case 0:
            iblockstate = iblockstate.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y);
            break;
        case 1:
            iblockstate = iblockstate.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X);
            break;
        case 2:
            iblockstate = iblockstate.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Z);
            break;
        default:
            iblockstate.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y);
            break;
        }

        return iblockstate;
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = this.getValues().indexOf(String.valueOf(state.getValue(this.getStringProp())));

        switch ((EnumFacing.Axis) state.getValue(BlockRotatedPillar.AXIS))
        {
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
    public boolean rotateBlock(net.minecraft.world.World world, BlockPos pos, EnumFacing axis)
    {
        IBlockState state = world.getBlockState(pos);
        for (IProperty<?> prop : state.getProperties().keySet())
        {
            if (prop == BlockRotatedPillar.AXIS)
            {
                world.setBlockState(pos, state.cycleProperty(prop));
                return true;
            }
        }
        return false;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        switch (rot)
        {
        case COUNTERCLOCKWISE_90:
        case CLOCKWISE_90:
            switch (state.getValue(BlockRotatedPillar.AXIS))
            {
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
    protected void setupStates()
    {
        this.setDefaultState(getExtendedBlockState().withProperty(this.getUnlistedStringProp(), this.getValues().get(0)).withProperty(this.getStringProp(), this.getValues().get(0)).withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y));
    }

//    @Override
//    protected BlockStateContainer createBlockState()
//    {
//        return new BlockStateContainer(this, new IProperty[] { this.getStringProp(), BlockRotatedPillar.AXIS });
//    }

    @Override
    protected BlockStateContainer createRealBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[] { this.getStringProp(), BlockRotatedPillar.AXIS }, new IUnlistedProperty[] { this.getUnlistedStringProp() });
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.getValues().indexOf(String.valueOf(state.getValue(this.getStringProp()))));
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(BlockRotatedPillar.AXIS, facing.getAxis());
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return this.getValues().indexOf(String.valueOf(state.getValue(this.getStringProp())));
    }
}
