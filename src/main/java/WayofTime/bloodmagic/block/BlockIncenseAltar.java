package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.tile.TileIncenseAltar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockIncenseAltar extends Block implements IVariantProvider, IBMBlock {
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(5 / 16F, 0F, 5 / 16F, 12 / 16F, 1F, 11 / 16F);

    public BlockIncenseAltar() {
        super(Material.ROCK);

        setTranslationKey(BloodMagic.MODID + ".incenseAltar");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
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
    public void breakBlock(World world, BlockPos blockPos, BlockState blockState) {
        TileIncenseAltar TileIncenseAltar = (TileIncenseAltar) world.getTileEntity(blockPos);
        if (TileIncenseAltar != null)
            TileIncenseAltar.dropItems();

        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, BlockState state) {
        return new TileIncenseAltar();
    }

    @Override
    public BlockItem getItem() {
        return new BlockItem(this);
    }
}
