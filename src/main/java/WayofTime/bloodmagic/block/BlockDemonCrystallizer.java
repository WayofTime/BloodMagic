package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.tile.TileDemonCrystallizer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockDemonCrystallizer extends ContainerBlock implements IVariantProvider, IBMBlock {
    public BlockDemonCrystallizer() {
        super(Material.ROCK);

        setTranslationKey(BloodMagic.MODID + ".demonCrystallizer");
        setCreativeTab(BloodMagic.TAB_BM);
        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("pickaxe", 0);

//        setBlockBounds(0.3F, 0F, 0.3F, 0.72F, 1F, 0.72F);
    }

    @Override
    public boolean isSideSolid(BlockState state, IBlockAccess world, BlockPos pos, Direction side) {
        return side == Direction.UP;
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
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileDemonCrystallizer();
    }

    @Override
    public void gatherVariants(@Nonnull Int2ObjectMap<String> variants) {
        variants.put(0, "normal");
    }

    @Override
    public BlockItem getItem() {
        return new BlockItem(this);
    }
}
