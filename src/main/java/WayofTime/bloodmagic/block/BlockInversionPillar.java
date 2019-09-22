package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.block.base.BlockEnum;
import WayofTime.bloodmagic.block.enums.EnumSubWillType;
import WayofTime.bloodmagic.tile.TileInversionPillar;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockInversionPillar extends BlockEnum<EnumSubWillType> {
    public BlockInversionPillar() {
        super(Material.ROCK, EnumSubWillType.class);

        setTranslationKey(BloodMagic.MODID + ".inversionpillar.");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 2);
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, BlockState blockState) {
        TileEntity tile = world.getTileEntity(blockPos);
        if (tile instanceof TileInversionPillar) {
            TileInversionPillar tilePillar = (TileInversionPillar) world.getTileEntity(blockPos);
            tilePillar.removePillarFromMap();
        }

        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public BlockState getActualState(BlockState state, IBlockAccess world, BlockPos pos) {
        return super.getActualState(state, world, pos).withProperty(Properties.StaticProperty, true);
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    public boolean causesSuffocation(BlockState state) {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void gatherVariants(Int2ObjectMap<String> variants) {
        for (int i = 0; i < this.getTypes().length; i++)
            variants.put(i, "static=false,type=" + this.getTypes()[i]);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, BlockState state) {
        return new TileInversionPillar(state.getValue(getProperty()).getType());
    }

    protected BlockStateContainer createStateContainer() {
        return new BlockStateContainer.Builder(this).add(getProperty(), Properties.StaticProperty).add(Properties.AnimationProperty).build();
    }
}
