package WayofTime.bloodmagic.block;

import WayofTime.bloodmagic.api.Constants;
import WayofTime.bloodmagic.tile.TileSpectralBlock;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockSpectral extends BlockContainer {

    public BlockSpectral() {
        super(Material.cloth);

        setUnlocalizedName(Constants.Mod.MODID + ".spectral");
        setBlockBounds(0, 0, 0, 0, 0, 0);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }

    @Override
    public boolean isReplaceable(World world, BlockPos blockPos) {
        return true;
    }

    @Override
    public boolean isAir(IBlockAccess world, BlockPos blockPos) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileSpectralBlock();
    }
}
