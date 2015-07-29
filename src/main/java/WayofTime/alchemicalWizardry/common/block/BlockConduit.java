package WayofTime.alchemicalWizardry.common.block;

import WayofTime.alchemicalWizardry.common.tileEntity.TEConduit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockConduit extends BlockOrientable
{
    public BlockConduit()
    {
        super();
        setHardness(2.0F);
        setResistance(5.0F);
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState)
    {
        super.breakBlock(world, blockPos, blockState);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int noClue)
    {
        return new TEConduit();
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState blockState)
    {
        return true;
    }
}
