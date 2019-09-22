package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.tile.TileSoulForge;
import WayofTime.bloodmagic.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSoulForge extends Block implements IVariantProvider, IBMBlock {
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.06F, 0.0F, 0.06F, 0.94F, 0.75F, 0.94F);

    public BlockSoulForge() {
        super(Material.IRON);

        setTranslationKey(BloodMagic.MODID + ".soulForge");
        setHardness(2.0F);
        setResistance(5.0F);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(BloodMagic.TAB_BM);
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
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
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction side, float hitX, float hitY, float hitZ) {
        if (world.getTileEntity(pos) instanceof TileSoulForge)
            player.openGui(BloodMagic.instance, Constants.Gui.SOUL_FORGE_GUI, world, pos.getX(), pos.getY(), pos.getZ());

        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, BlockState blockState) {
        TileSoulForge tileSoulForge = (TileSoulForge) world.getTileEntity(blockPos);
        if (tileSoulForge != null)
            tileSoulForge.dropItems();

        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, BlockState state) {
        return new TileSoulForge();
    }

    @Override
    public BlockItem getItem() {
        return new BlockItem(this);
    }
}
