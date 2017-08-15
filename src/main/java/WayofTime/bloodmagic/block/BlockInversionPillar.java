package WayofTime.bloodmagic.block;

import java.util.ArrayList;
import java.util.List;

import WayofTime.bloodmagic.block.base.BlockEnum;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.enums.EnumSubWillType;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.tile.TileInversionPillar;

public class BlockInversionPillar extends BlockEnum<EnumSubWillType>
{
    public BlockInversionPillar()
    {
        super(Material.ROCK, EnumSubWillType.class);

        setUnlocalizedName(BloodMagic.MODID + ".inversionpillar.");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState)
    {
        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof TileInversionPillar)
        {
            TileInversionPillar tilePillar = (TileInversionPillar) world.getTileEntity(blockPos);
            tilePillar.removePillarFromMap();
        }

        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return super.getActualState(state, world, pos).withProperty(Properties.StaticProperty, true);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean causesSuffocation(IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (int i = 0; i < this.getTypes().length; i++)
            ret.add(Pair.of(i, "static=false,type=" + this.getTypes()[i]));
        return ret;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileInversionPillar(state.getValue(getProperty()).getType());
    }

    protected BlockStateContainer createStateContainer()
    {
        return new BlockStateContainer.Builder(this).add(getProperty(), Properties.StaticProperty).add(Properties.AnimationProperty).build();
    }
}
