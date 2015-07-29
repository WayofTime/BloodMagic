package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.common.tileEntity.TESpectralContainer;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockSpectralContainer extends BlockContainer
{
    public BlockSpectralContainer()
    {
        super(Material.cloth);
        this.setBlockBounds(0, 0, 0, 0, 0, 0);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {}

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    public boolean isReplaceable(World world, BlockPos blockPos)
    {
        return true;
    }

    @Override
    public boolean isAir(IBlockAccess world, BlockPos blockPos)
    {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TESpectralContainer();
    }
}
