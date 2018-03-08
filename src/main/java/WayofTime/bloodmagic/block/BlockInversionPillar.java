package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.block.enums.EnumSubWillType;
import WayofTime.bloodmagic.tile.TileInversionPillar;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
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

public class BlockInversionPillar extends BlockEnum<EnumSubWillType> {
    public BlockInversionPillar() {
        super(Material.ROCK, EnumSubWillType.class);

        setUnlocalizedName(BloodMagic.MODID + ".inversionpillar.");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState) {
        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof TileInversionPillar) {
            TileInversionPillar tilePillar = (TileInversionPillar) world.getTileEntity(blockPos);
            tilePillar.removePillarFromMap();
        }

        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return super.getActualState(state, world, pos).withProperty(Properties.StaticProperty, true);
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
    public void gatherVariants(Int2ObjectMap<String> variants) {
        for (int i = 0; i < this.getTypes().length; i++)
            variants.put(i, "static=false,type=" + this.getTypes()[i]);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileInversionPillar(state.getValue(getProperty()).getType());
    }

    protected BlockStateContainer createStateContainer() {
        return new BlockStateContainer.Builder(this).add(getProperty(), Properties.StaticProperty).add(Properties.AnimationProperty).build();
    }
}
