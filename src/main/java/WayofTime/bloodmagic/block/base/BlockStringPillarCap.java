package WayofTime.bloodmagic.block.base;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockStringPillarCap extends BlockString
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public BlockStringPillarCap(Material material, String[] values, String propName)
    {
        super(material, values, propName);
    }

    public BlockStringPillarCap(Material material, String[] values)
    {
        this(material, values, "type");
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = getBlockState().getBaseState().withProperty(this.getStringProp(), this.getValues().get(meta % 2));

        int index = meta / 2;
        EnumFacing facing = EnumFacing.getFront(index);
        iblockstate = iblockstate.withProperty(FACING, facing);

        return iblockstate;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = this.getValues().indexOf(String.valueOf(state.getValue(this.getStringProp())));

        EnumFacing facing = (EnumFacing) state.getValue(FACING);
        int index = facing.getIndex();
        i = i + 2 * index;

        return i;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
    }

    @Override
    protected void setupStates()
    {
        this.setDefaultState(getExtendedBlockState().withProperty(this.getUnlistedStringProp(), this.getValues().get(0)).withProperty(this.getStringProp(), this.getValues().get(0)).withProperty(FACING, EnumFacing.UP));
    }

    @Override
    protected BlockStateContainer createRealBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[] { this.getStringProp(), FACING }, new IUnlistedProperty[] { this.getUnlistedStringProp() });
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.getValues().indexOf(String.valueOf(state.getValue(this.getStringProp()))));
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        System.out.println("Facing: " + facing);
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, facing);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return this.getValues().indexOf(String.valueOf(state.getValue(this.getStringProp())));
    }
}
