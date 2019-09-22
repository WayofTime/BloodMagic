package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.BloodMagic;
import WayofTime.bloodmagic.ConfigHandler;
import WayofTime.bloodmagic.tile.TileSpectralBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockSpectral extends Block {
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

    public BlockSpectral() {
        super(Material.CLOTH);

        setTranslationKey(BloodMagic.MODID + ".spectral");
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
    public boolean isOpaqueCube(BlockState state) {
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

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return ConfigHandler.client.invisibleSpectralBlocks ? BlockRenderType.INVISIBLE : BlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(BlockState state, IBlockAccess world, BlockPos pos, Direction side) {
        return world.getBlockState(pos.offset(side)) != state || state.getBlock() != this && super.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity, boolean bool) {
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isAir(BlockState state, IBlockAccess world, BlockPos blockPos) {
        return true;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, BlockState state) {
        return new TileSpectralBlock();
    }
}
